load("//tools:maven.bzl", "maven_library")
load("@google_bazel_common//testing:test_defs.bzl", "gen_java_tests")
load("@rules_java//java:defs.bzl", "java_library", "java_test")

maven_library(
    name = "docker-junit-rules",
    srcs = [
        "src/main/java/com/cgi/eoss/eopp/testing/docker/DockerClientRule.java",
        "src/main/java/com/cgi/eoss/eopp/testing/docker/DockerRegistryRule.java",
    ],
    artifact_name = "libeopp :: testing :: docker-junit-rules",
    generate_sonarqube_project = False,
    group_id = "com.cgi.eoss.eopp.testing",
    root_packages = ["com.cgi.eoss.eopp.testing"],
    test_suite = [":testing_tests"],
    deps = [
        "@maven//:com_github_docker_java_docker_java_api",
        "@maven//:com_github_docker_java_docker_java_core",
        "@maven//:com_github_docker_java_docker_java_transport",
        "@maven//:com_github_docker_java_docker_java_transport_zerodep",
        "@maven//:junit_junit",
        "@maven//:org_awaitility_awaitility",
    ],
)

gen_java_tests(
    name = "testing_tests",
    srcs = glob(["src/test/java/**/*.java"]),
    deps = [
        ":docker-junit-rules",
        "@maven//:com_github_docker_java_docker_java_api",
        "@maven//:com_github_docker_java_docker_java_core",
        "@maven//:com_google_truth_truth",
        "@maven//:junit_junit",
        "@maven//:org_apache_commons_commons_compress",
    ],
)
