load("//tools:maven.bzl", "maven_library", "sonarqube_project")

maven_library(
    name = "resource",
    srcs = [
        "src/main/java/com/cgi/eoss/eopp/resource/EoppResource.java",
        "src/main/java/com/cgi/eoss/eopp/resource/WritableEoppResource.java",
    ],
    artifact_name = "libeopp :: resource",
    sq_srcs = glob(["src/main/java/**"]),
    sq_targets = [
        ":okhttp-resource",
        ":path-resource",
        ":resource",
    ],
    deps = [
        "//file",
        "@org_springframework_spring_core",
    ],
)

maven_library(
    name = "path-resource",
    srcs = ["src/main/java/com/cgi/eoss/eopp/resource/EoppPathResource.java"],
    artifact_name = "libeopp :: resource :: path",
    generate_sonarqube_project = False,
    deps = [
        ":resource",
        "//file",
        "@org_springframework_spring_core",
    ],
)

maven_library(
    name = "okhttp-resource",
    srcs = ["src/main/java/com/cgi/eoss/eopp/resource/EoppOkHttpResource.java"],
    artifact_name = "libeopp :: resource :: okhttp",
    generate_sonarqube_project = False,
    deps = [
        ":resource",
        "//file",
        "//util",
        "//util:eopp-headers",
        "//util:timestamps",
        "@com_google_guava_guava",
        "@com_squareup_okhttp3_okhttp",
        "@org_slf4j_slf4j_api",
        "@org_springframework_spring_core",
    ],
)

java_library(
    name = "resource_java_tests",
    testonly = True,
    srcs = glob(["src/test/java/**/*.java"]),
    deps = [
        ":okhttp-resource",
        ":path-resource",
        ":resource",
        "//file",
        "//util:eopp-headers",
        "//util:timestamps",
        "@com_google_truth_extensions_truth_proto_extension",
        "@com_google_truth_truth",
        "@com_squareup_okhttp3_mockwebserver",
        "@com_squareup_okhttp3_okhttp",
        "@junit_junit",
    ],
)

java_test(
    name = "EoppPathResourceTest",
    size = "small",
    test_class = "com.cgi.eoss.eopp.resource.EoppPathResourceTest",
    runtime_deps = [":resource_java_tests"],
)

java_test(
    name = "EoppOkHttpResourceTest",
    size = "small",
    test_class = "com.cgi.eoss.eopp.resource.EoppOkHttpResourceTest",
    runtime_deps = [":resource_java_tests"],
)
