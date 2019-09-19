load("//tools:maven.bzl", "maven_library", "sonarqube_project")

maven_library(
    name = "resource",
    srcs = [
        "src/main/java/com/cgi/eoss/eopp/resource/EoppResource.java",
        "src/main/java/com/cgi/eoss/eopp/resource/WritableEoppResource.java",
    ],
    artifact_name = "libeopp :: resource",
    generate_sonarqube_project = False,
    deps = [
        "//file",
        "@org_springframework_spring_core",
    ],
)

maven_library(
    name = "path_resource",
    srcs = ["src/main/java/com/cgi/eoss/eopp/resource/EoppPathResource.java"],
    artifact_name = "libeopp :: resource :: path",
    generate_sonarqube_project = False,
    deps = [
        ":resource",
        "//file",
        "@org_springframework_spring_core",
    ],
)

sonarqube_project(
    name = "resource",
    srcs = glob(["src/main/**"]),
    artifact_name = "libeopp :: resource",
)

java_library(
    name = "resource_java_tests",
    testonly = True,
    srcs = glob(["src/test/java/**/*.java"]),
    deps = [
        ":path_resource",
        ":resource",
        "//file",
        "@com_google_guava_guava",
        "@com_google_truth_truth",
        "@junit_junit",
        "@org_mockito_mockito_core",
    ],
)

java_test(
    name = "EoppPathResourceTest",
    size = "small",
    test_class = "com.cgi.eoss.eopp.resource.EoppPathResourceTest",
    runtime_deps = [":resource_java_tests"],
)
