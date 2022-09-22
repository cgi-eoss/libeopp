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

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static com.google.common.truth.Truth.assertThat;

@RunWith(JUnit4.class)
public class DockerRegistryRuleTest {

    @ClassRule(order=0)
    public static DockerClientRule dockerClient = new DockerClientRule();

    @ClassRule(order=1)
    public static DockerRegistryRule dockerRegistry = new DockerRegistryRule(dockerClient);

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void test() throws Exception {
        Files.write(temporaryFolder.newFile("Dockerfile").toPath(), Arrays.asList("FROM alpine", "ARG foo"));

        String imageId = dockerClient.getDockerClient()
                .buildImageCmd(temporaryFolder.getRoot())
                .start()
                .awaitImageId();

        String imageName = UUID.randomUUID().toString();
        String imageTag = "latest";

        // We don't use this for pushing, as we have to use `localhost` to skip TLS
        String registryRepository = dockerRegistry.getImagePushRepository(imageName);
        assertThat(registryRepository).isEqualTo(dockerRegistry.getRegistryHost() + ":" + dockerRegistry.getRegistryPort() + "/" + imageName);

        String imageNameWithRegistry = String.format("localhost:%s/%s", dockerRegistry.getRegistryPort(), imageName);
        String imageWithRegistry = imageNameWithRegistry + ":" + imageTag;

        // Push the built image to the registry
        dockerClient.getDockerClient().tagImageCmd(imageId, imageNameWithRegistry, imageTag).exec();
        dockerClient.getDockerClient().pushImageCmd(imageNameWithRegistry)
                .withAuthConfig(dockerClient.getDockerClient().authConfig())
                .withTag(imageTag)
                .start()
                .awaitCompletion();
        List<String> pushedDigests = dockerClient.getDockerClient().inspectImageCmd(imageWithRegistry).exec().getRepoDigests();

        // Remove the image from the Docker host
        dockerClient.getDockerClient().removeImageCmd(imageId).withForce(true).exec();

        // Pull the image from the registry and check the digests
        dockerClient.getDockerClient().pullImageCmd(imageNameWithRegistry)
                .withTag(imageTag)
                .start()
                .awaitCompletion();
        List<String> pulledDigests = dockerClient.getDockerClient().inspectImageCmd(imageWithRegistry).exec().getRepoDigests();

        assertThat(pulledDigests).containsExactlyElementsIn(pushedDigests);
    }

}
