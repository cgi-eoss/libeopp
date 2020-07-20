load("//tools:maven.bzl", "maven_library", "sonarqube_project")

maven_library(
    name = "resource",
    srcs = [
        "src/main/java/com/cgi/eoss/eopp/resource/EoppResource.java",
        "src/main/java/com/cgi/eoss/eopp/resource/EoppResourceException.java",
        "src/main/java/com/cgi/eoss/eopp/resource/EoppZipCombiningResource.java",
        "src/main/java/com/cgi/eoss/eopp/resource/ResourceMetadataWrapper.java",
        "src/main/java/com/cgi/eoss/eopp/resource/WritableEoppResource.java",
        "src/main/java/com/cgi/eoss/eopp/resource/ZipCombiningResource.java",
    ],
    artifact_name = "libeopp :: resource",
    root_packages = ["com.cgi.eoss.eopp.resource"],
    sq_srcs = glob(["src/main/java/**"]),
    sq_targets = [
        ":okhttp-resource",
        ":path-resource",
        ":resource",
        ":s3-resource",
    ],
    deps = [
        "//file",
        "//util:io",
        "//util:timestamps",
        "@maven//:com_google_guava_guava",
        "@maven//:org_springframework_spring_core",
    ],
)

maven_library(
    name = "path-resource",
    srcs = ["src/main/java/com/cgi/eoss/eopp/resource/EoppPathResource.java"],
    artifact_name = "libeopp :: resource :: path",
    generate_sonarqube_project = False,
    root_packages = ["com.cgi.eoss.eopp.resource"],
    deps = [
        ":resource",
        "//file",
        "@maven//:org_springframework_spring_core",
    ],
)

maven_library(
    name = "okhttp-resource",
    srcs = ["src/main/java/com/cgi/eoss/eopp/resource/EoppOkHttpResource.java"],
    artifact_name = "libeopp :: resource :: okhttp",
    generate_sonarqube_project = False,
    root_packages = ["com.cgi.eoss.eopp.resource"],
    deps = [
        ":resource",
        "//file",
        "//util",
        "//util:eopp-headers",
        "//util:timestamps",
        "@maven//:com_google_guava_guava",
        "@maven//:com_squareup_okhttp3_okhttp",
        "@maven//:org_slf4j_slf4j_api",
        "@maven//:org_springframework_spring_core",
    ],
)

maven_library(
    name = "s3-resource",
    srcs = ["src/main/java/com/cgi/eoss/eopp/resource/EoppS3ObjectResource.java"],
    artifact_name = "libeopp :: resource :: s3",
    generate_sonarqube_project = False,
    root_packages = ["com.cgi.eoss.eopp.resource"],
    deps = [
        ":resource",
        "//file",
        "//util",
        "//util:eopp-headers",
        "//util:timestamps",
        "@maven//:io_projectreactor_reactor_core",
        "@maven//:org_slf4j_slf4j_api",
        "@maven//:org_springframework_spring_core",
        "@maven//:software_amazon_awssdk_s3",
        "@maven//:software_amazon_awssdk_sdk_core",
    ],
)

java_library(
    name = "resource_java_tests",
    testonly = True,
    srcs = glob(["src/test/java/**/*.java"]),
    runtime_deps = [
        "@maven//:ch_qos_logback_logback_classic",
    ],
    deps = [
        ":okhttp-resource",
        ":path-resource",
        ":resource",
        ":s3-resource",
        "//file",
        "//util:eopp-headers",
        "@com_google_protobuf//:protobuf_java",
        "@maven//:com_google_guava_guava",
        "@maven//:com_google_jimfs_jimfs",
        "@maven//:com_google_truth_extensions_truth_proto_extension",
        "@maven//:com_google_truth_truth",
        "@maven//:com_squareup_okhttp3_mockwebserver",
        "@maven//:com_squareup_okhttp3_okhttp",
        "@maven//:junit_junit",
        "@maven//:org_slf4j_slf4j_api",
        "@maven//:software_amazon_awssdk_auth",
        "@maven//:software_amazon_awssdk_regions",
        "@maven//:software_amazon_awssdk_s3",
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

java_test(
    name = "EoppS3ObjectResourceTest",
    size = "small",
    test_class = "com.cgi.eoss.eopp.resource.EoppS3ObjectResourceTest",
    runtime_deps = [":resource_java_tests"],
)

java_test(
    name = "EoppZipCombiningResourceTest",
    size = "small",
    test_class = "com.cgi.eoss.eopp.resource.EoppZipCombiningResourceTest",
    runtime_deps = [":resource_java_tests"],
)
