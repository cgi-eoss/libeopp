load("//tools:maven.bzl", "maven_library", "sonarqube_project")

maven_library(
    name = "docker-junit-rules",
    srcs = [
        "src/main/java/com/cgi/eoss/eopp/testing/docker/DockerClientRule.java",
        "src/main/java/com/cgi/eoss/eopp/testing/docker/DockerRegistryRule.java",
    ],
    artifact_name = "libeopp :: testing :: docker-junit-rules",
    group_id = "com.cgi.eoss.eopp.testing",
    root_packages = ["com.cgi.eoss.eopp.testing"],
    deps = [
        "@maven//:com_github_docker_java_docker_java_api",
        "@maven//:com_github_docker_java_docker_java_core",
        "@maven//:com_github_docker_java_docker_java_transport",
        "@maven//:com_github_docker_java_docker_java_transport_zerodep",
        "@maven//:junit_junit",
        "@maven//:org_awaitility_awaitility",
    ],
)

java_library(
    name = "testing_tests",
    testonly = True,
    srcs = glob(["src/test/java/**/*.java"]),
    runtime_deps = [
        "@maven//:ch_qos_logback_logback_classic",
    ],
    deps = [
        ":docker-junit-rules",
        "@maven//:com_github_docker_java_docker_java_api",
        "@maven//:com_github_docker_java_docker_java_core",
        "@maven//:com_google_truth_truth",
        "@maven//:junit_junit",
        "@maven//:org_apache_commons_commons_compress",
    ],
)

java_test(
    name = "DockerClientRuleTest",
    size = "small",
    test_class = "com.cgi.eoss.eopp.testing.docker.DockerClientRuleTest",
    runtime_deps = [":testing_tests"],
)

java_test(
    name = "DockerRegistryRuleTest",
    size = "small",
    test_class = "com.cgi.eoss.eopp.testing.docker.DockerRegistryRuleTest",
    runtime_deps = [":testing_tests"],
)
