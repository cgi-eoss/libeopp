load("//tools:maven.bzl", "maven_library")
load("@google_bazel_common//testing:test_defs.bzl", "gen_java_tests")

maven_library(
    name = "docker-junit-rules",
    srcs = [
        "src/main/java/com/cgi/eoss/eopp/testing/docker/DockerClientRule.java",
        "src/main/java/com/cgi/eoss/eopp/testing/docker/DockerRegistryRule.java",
        "src/main/java/com/cgi/eoss/eopp/testing/docker/package-info.java",
    ],
    artifact_name = "libeopp :: testing :: docker-junit-rules",
    generate_sonarqube_project = False,
    group_id = "com.cgi.eoss.eopp.testing",
    root_packages = ["com.cgi.eoss.eopp.testing"],
    test_suite = [":testing_tests"],
    deps = [
        "//third_party/java:org_springframework_spring_core_neverlink",
        "//util",
        "@maven//:com_github_docker_java_docker_java_api",
        "@maven//:com_github_docker_java_docker_java_core",
        "@maven//:com_github_docker_java_docker_java_transport",
        "@maven//:com_github_docker_java_docker_java_transport_zerodep",
        "@maven//:junit_junit",
        "@maven//:org_awaitility_awaitility",
    ],
)

maven_library(
    name = "testcontainers-modules",
    srcs = [
        "src/main/java/com/cgi/eoss/eopp/testing/containers/RegistryContainer.java",
        "src/main/java/com/cgi/eoss/eopp/testing/docker/package-info.java",
    ],
    artifact_name = "libeopp :: testing :: testcontainers-modules",
    generate_sonarqube_project = False,
    group_id = "com.cgi.eoss.eopp.testing",
    root_packages = ["com.cgi.eoss.eopp.testing"],
    test_suite = [":testing_tests"],
    deps = [
        "//third_party/java:org_springframework_spring_core_neverlink",
        "@maven//:org_testcontainers_testcontainers",
    ],
)

gen_java_tests(
    name = "testing_tests",
    srcs = glob(["src/test/java/**/*.java"]),
    deps = [
        ":docker-junit-rules",
        ":testcontainers-modules",
        "@maven//:com_github_docker_java_docker_java_api",
        "@maven//:com_github_docker_java_docker_java_core",
        "@maven//:com_google_truth_truth",
        "@maven//:junit_junit",
        "@maven//:org_apache_commons_commons_compress",
    ],
)
