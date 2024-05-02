package com.cgi.eoss.eopp.testing.containers;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

/**
 * Testcontainers implementation for container registry.
 * <p>
 * Supported image: {@code registry:2}
 * <p>
 * Exposed ports:
 * <ul>
 *     <li>HTTP: 5000</li>
 * </ul>
 */
public class RegistryContainer extends GenericContainer<RegistryContainer> {

    private static final DockerImageName DEFAULT_IMAGE_NAME = DockerImageName.parse("docker.io/registry:2");

    private static final int REGISTRY_HTTP_PORT = 5000;

    /**
     * Constructs a registry container with the default image name
     */
    public RegistryContainer() {
        this(DEFAULT_IMAGE_NAME);
    }

    /**
     * Constructs a registry container from the dockerImageName
     *
     * @param dockerImageName the full image name to use
     */
    public RegistryContainer(final String dockerImageName) {
        this(DockerImageName.parse(dockerImageName));
    }

    /**
     * Constructs a registry container from the dockerImageName
     *
     * @param dockerImageName the full image name to use
     */
    public RegistryContainer(final DockerImageName dockerImageName) {
        super(dockerImageName);
        dockerImageName.assertCompatibleWith(DEFAULT_IMAGE_NAME);
        withExposedPorts(REGISTRY_HTTP_PORT);
        waitingFor(
                Wait
                        .forHttp("/")
                        .forPort(REGISTRY_HTTP_PORT)
                        .withStartupTimeout(Duration.of(60, ChronoUnit.SECONDS))
        );
    }

    /**
     * @return A Docker repository for the given Docker image name, i.e. a locator including this registry URL.
     */
    public String getImagePushRepository(String imageName) {
        return String.format("%s:%s/%s", getHost(), getMappedPort(REGISTRY_HTTP_PORT), imageName);
    }

    public int getHttpPort() {
        return getMappedPort(REGISTRY_HTTP_PORT);
    }

}
