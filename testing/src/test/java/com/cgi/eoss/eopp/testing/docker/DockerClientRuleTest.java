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

import org.junit.AssumptionViolatedException;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.net.UnknownHostException;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.fail;

@RunWith(JUnit4.class)
public class DockerClientRuleTest {

    @Test
    public void test() throws Throwable {
        DockerClientRule dockerClientRule = new DockerClientRule();
        dockerClientRule.before();
        assertThat(dockerClientRule.getDockerHostUrl()).isEqualTo(DockerClientRule.DEFAULT_DOCKER_HOST);
        assertThat(dockerClientRule.getDockerClient().infoCmd().exec().getId()).isNotEmpty();
    }

    @Test
    public void testSkippable() {
        DockerClientRule dockerClientRule = new DockerClientRule("tcp://non-existent-host:2375", false);
        try {
            dockerClientRule.before();
            fail("Expected exception");
        } catch (Throwable throwable) {
            assertThat(throwable).hasCauseThat().hasCauseThat().isInstanceOf(UnknownHostException.class);
        }
    }

    @Test
    @Ignore("Requires manually setting the Docker environment to a non-standard configuration")
    public void testNonStandard() {
        DockerClientRule dockerClientRule = new DockerClientRule(true);
        try {
            dockerClientRule.before();
            fail("Expected exception");
        } catch (Throwable throwable) {
            assertThat(throwable).isInstanceOf(AssumptionViolatedException.class);
            assertThat(throwable).hasMessageThat().isEqualTo("Docker server indicates non-standard environment");
        }
    }

}