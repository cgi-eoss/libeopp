load("//tools:maven.bzl", "maven_library", "sonarqube_project")

maven_library(
    name = "util",
    srcs = ["src/main/java/com/cgi/eoss/eopp/util/Lazy.java"],
    artifact_name = "libeopp :: util",
    root_packages = ["com.cgi.eoss.eopp.util"],
    sq_srcs = glob(["src/main/java/**"]),
    sq_targets = [
        ":util",
        ":timestamps",
        ":eopp-headers",
        ":io",
    ],
    test_targets = [
        ":LazyTest",
    ],
)

maven_library(
    name = "timestamps",
    srcs = ["src/main/java/com/cgi/eoss/eopp/util/Timestamps.java"],
    artifact_name = "libeopp :: util :: timestamps",
    generate_sonarqube_project = False,
    root_packages = ["com.cgi.eoss.eopp.util"],
    deps = ["//third_party/protobuf:timestamp_java_proto"],
)

maven_library(
    name = "eopp-headers",
    srcs = ["src/main/java/com/cgi/eoss/eopp/util/EoppHeaders.java"],
    artifact_name = "libeopp :: util :: eopp-headers",
    generate_sonarqube_project = False,
    root_packages = ["com.cgi.eoss.eopp.util"],
)

maven_library(
    name = "io",
    srcs = ["src/main/java/com/cgi/eoss/eopp/util/HashingCountingOutputStream.java"],
    artifact_name = "libeopp :: util :: io",
    generate_sonarqube_project = False,
    root_packages = ["com.cgi.eoss.eopp.util"],
    deps = [
        "//file",
        "@maven//:com_google_guava_guava",
    ],
)

java_library(
    name = "util_java_tests",
    testonly = True,
    srcs = glob(["src/test/java/**/*.java"]),
    deps = [
        ":util",
        "@maven//:com_google_truth_truth",
        "@maven//:junit_junit",
    ],
)

java_test(
    name = "LazyTest",
    size = "small",
    test_class = "com.cgi.eoss.eopp.util.LazyTest",
    runtime_deps = [":util_java_tests"],
)
