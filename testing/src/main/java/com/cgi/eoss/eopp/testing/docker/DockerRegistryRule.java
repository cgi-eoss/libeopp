/*
 * Copyright 2020 The libeopp Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cgi.eoss.eopp.testing.docker;

import com.cgi.eoss.eopp.util.Lazy;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.ContainerNetwork;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.Ports;
import org.awaitility.core.ConditionTimeoutException;
import org.junit.Assume;
import org.junit.rules.ExternalResource;

import java.net.ConnectException;
import java.net.Socket;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import static org.awaitility.Awaitility.await;

/**
 * <p>A JUnit test rule to configure access to a Docker image registry.</p>
 * <p>If configured with a {@link DockerClient} (e.g. from {@link DockerClientRule}), this rule will automatically start
 * a registry container in the configured Docker host, and tears it down after the test (or after all tests, if using
 * {@link org.junit.ClassRule}).</p>
 * <p>Alternatively, it may be configured to connect to an existing registry running on a specific host and port.</p>
 */
public class DockerRegistryRule extends ExternalResource {
    private static final String DEFAULT_REGISTRY_IMAGE = "registry:2";

    private final boolean startupRegistry;
    private Supplier<DockerClient> dockerClient;
    private String registryContainer;
    private String registryHost;
    private String registryPort;

    /**
     * <p>Start a new Docker Registry container using the Docker API client attached to the given
     * {@link DockerClientRule}.</p>
     *
     * <p>Note that the Rule ordering should be consistent to make this an 'inner' rule:</p>
     *
     * <pre>
     *     &#64;ClassRule(order = 0)
     *     public static DockerClientRule dockerClient = new DockerClientRule();
     *     &#64;ClassRule(order = 1)
     *     public static DockerRegistryRule dockerRegistry = new DockerRegistryRule(dockerClient);
     * </pre>
     */
    public DockerRegistryRule(DockerClientRule dockerClientRule) {
        this(Lazy.lazily(dockerClientRule::getDockerClient));
    }

    /**
     * <p>Start a new Docker Registry container using the given supplier of a <strong>non-null</strong> Docker API
     * client.</p>
     */
    public DockerRegistryRule(Supplier<DockerClient> dockerClient) {
        this.startupRegistry = true;
        this.dockerClient = dockerClient;
    }

    /**
     * <p>Connect to a running Docker Registry at the given address.</p>
     */
    public DockerRegistryRule(String registryHost, String registryPort) {
        this.startupRegistry = false;
        this.registryHost = registryHost;
        this.registryPort = registryPort;
    }

    @Override
    protected void before() {
        // Ensure correct ordering: dockerClient must be instantiated beforehand
        Objects.requireNonNull(dockerClient.get());

        if (startupRegistry) {
            try {
                dockerClient.get().infoCmd().exec();
            } catch (Exception e) {
                Assume.assumeNoException("Docker client cannot connect to engine", e);
            }
            try {
                startRegistryContainer();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException(e);
            }
        }
    }

    private void startRegistryContainer() throws InterruptedException {
        dockerClient.get().pullImageCmd(DEFAULT_REGISTRY_IMAGE).start().awaitCompletion();

        ExposedPort internalPort = ExposedPort.tcp(5000);

        try (CreateContainerCmd createContainerCmd = dockerClient.get().createContainerCmd(DEFAULT_REGISTRY_IMAGE)) {
            registryContainer = createContainerCmd
                    .withExposedPorts(internalPort)
                    .withHostConfig(HostConfig.newHostConfig().withPublishAllPorts(true))
                    .exec().getId();
        }

        dockerClient.get().startContainerCmd(registryContainer).exec();

        // Find the ephemeral Docker port
        InspectContainerResponse inspectResponse = dockerClient.get().inspectContainerCmd(registryContainer).exec();

        Map<String, ContainerNetwork> networks = inspectResponse.getNetworkSettings().getNetworks();
        if (networks.size() == 1) {
            registryHost = networks.values().iterator().next().getGateway();
        } else if (networks.containsKey("bridge")) {
            registryHost = networks.get("bridge").getGateway();
        } else {
            throw new UnsupportedOperationException("Unable to detect primary host address for Docker networking");
        }

        registryPort = Arrays.stream(inspectResponse.getNetworkSettings().getPorts().getBindings().get(internalPort))
                .findFirst().map(Ports.Binding::getHostPortSpec).orElseThrow(RuntimeException::new);

        try {
            await().atMost(30, TimeUnit.SECONDS)
                    .ignoreExceptions()
                    .untilAsserted(() -> new Socket(registryHost, Integer.parseInt(registryPort)));
        } catch (ConditionTimeoutException e) {
            after();
            if (e.getCause().getClass().equals(ConnectException.class)) {
                Assume.assumeNoException(e.getCause());
            } else {
                throw e;
            }
        }
    }

    @Override
    protected void after() {
        if (!startupRegistry) {
            return;
        }
        dockerClient.get().stopContainerCmd(registryContainer).exec();
        dockerClient.get().waitContainerCmd(registryContainer).start().awaitStatusCode();
        dockerClient.get().removeContainerCmd(registryContainer).withRemoveVolumes(true).exec();
    }

    /**
     * @return The running registry host.
     */
    public String getRegistryHost() {
        return registryHost;
    }

    /**
     * @return The running registry port.
     */
    public String getRegistryPort() {
        return registryPort;
    }

    /**
     * @return A Docker repository for the given Docker image name, i.e. a locator including this registry URL.
     */
    public String getImagePushRepository(String imageName) {
        return String.format("%s:%s/%s", registryHost, registryPort, imageName);
    }
}
