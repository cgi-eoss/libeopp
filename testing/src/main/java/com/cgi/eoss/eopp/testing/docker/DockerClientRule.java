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

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.InfoCmd;
import com.github.dockerjava.api.model.Info;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.core.RemoteApiVersion;
import com.github.dockerjava.transport.DockerHttpClient;
import com.github.dockerjava.zerodep.ZerodepDockerHttpClient;
import org.junit.Assume;
import org.junit.AssumptionViolatedException;
import org.junit.rules.ExternalResource;

import java.util.Objects;
import java.util.Optional;

/**
 * <p>A JUnit test rule to provision a Docker API client.</p>
 * <p>This includes a flag to automatically skip tests using this rule if the configured Docker Engine is unusable.</p>
 *
 * @see DockerClient
 */
public class DockerClientRule extends ExternalResource {
    //@VisibleForTesting
    static final String DEFAULT_DOCKER_HOST = Optional.ofNullable(System.getenv("DOCKER_HOST")).orElse("unix:///var/run/docker.sock");

    private final String dockerHostUrl;
    private final boolean requireProperDocker;
    private DockerClient dockerClient;

    /**
     * <p>Create a new Docker client, connecting to an Engine running on the local Docker socket, skipping tests if it
     * is unusable.</p>
     */
    public DockerClientRule() {
        this(DEFAULT_DOCKER_HOST);
    }

    /**
     * <p>Create a new Docker client, connecting to an Engine running at the specified URL, skipping tests if it is
     * unusable.</p>
     */
    public DockerClientRule(String dockerHostUrl) {
        this(dockerHostUrl, false);
    }

    /**
     * <p>Prepare a new Docker client connection to an Engine running at the specified URL, skipping tests if it is
     * unusable.</p>
     * <p>An additional (naive) check enables skipping tests in a non-standard environment, e.g. Kubedock.</p>
     */
    public DockerClientRule(boolean requireProperDocker) {
        this(DEFAULT_DOCKER_HOST, requireProperDocker);
    }

    /**
     * <p>Prepare a new Docker client connection to an Engine running at the specified URL, skipping tests if it is
     * unusable.</p>
     * <p>An additional (naive) check enables skipping tests in a non-standard environment, e.g. Kubedock.</p>
     */
    public DockerClientRule(String dockerHostUrl, boolean requireProperDocker) {
        this.dockerHostUrl = dockerHostUrl;
        this.requireProperDocker = requireProperDocker;
    }

    @Override
    protected void before() throws Throwable {
        try {
            if (dockerHostUrl.startsWith("unix://") && System.getProperty("os.name").toLowerCase().startsWith("windows")) {
                throw new UnsupportedOperationException("Docker client on Windows cannot connect to engine via unix socket");
            }
            DefaultDockerClientConfig dockerClientConfig = DefaultDockerClientConfig.createDefaultConfigBuilder()
                    .withApiVersion(RemoteApiVersion.VERSION_1_24)
                    .withDockerHost(dockerHostUrl)
                    .build();
            DockerHttpClient dockerHttpClient = new ZerodepDockerHttpClient.Builder()
                    .dockerHost(dockerClientConfig.getDockerHost())
                    .sslConfig(dockerClientConfig.getSSLConfig())
                    .build();
            this.dockerClient = DockerClientImpl.getInstance(dockerClientConfig, dockerHttpClient);
            try (InfoCmd infoCmd = dockerClient.infoCmd()) {
                Info info = infoCmd.exec();
                if (this.requireProperDocker) {
                    Assume.assumeTrue("Docker server indicates non-standard environment", isProperDocker(info));
                }
            }
        } catch (AssumptionViolatedException e) {
            throw e;
        } catch (Exception e) {
            Assume.assumeNoException("Docker client cannot connect to engine", e);
        }
    }

    /**
     * @return The configured Docker API client.
     */
    public DockerClient getDockerClient() {
        return dockerClient;
    }

    /**
     * @return The configured Docker Engine host URL.
     */
    public String getDockerHostUrl() {
        return dockerHostUrl;
    }

    private boolean isProperDocker(Info info) {
        // If the OS is 'kubernetes' this is kubedock, and therefore 'not proper'
        return !Objects.equals("kubernetes", info.getOperatingSystem());
    }
}
