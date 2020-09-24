package com.cgi.eoss.eopp.testing.docker;

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
    public void testUnskippable() {
        DockerClientRule dockerClientRule = new DockerClientRule("tcp://non-existent-host:2375", false);
        try {
            dockerClientRule.before();
            fail("Expected exception");
        } catch (Throwable throwable) {
            assertThat(throwable).hasCauseThat().isInstanceOf(UnknownHostException.class);
        }
    }

}