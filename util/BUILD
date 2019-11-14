load("//tools:maven.bzl", "maven_library")

maven_library(
    name = "util",
    srcs = ["src/main/java/com/cgi/eoss/eopp/util/Lazy.java"],
    artifact_name = "libeopp :: util",
    sq_srcs = glob(["src/main/java/**"]),
    sq_targets = [
        ":util",
        ":timestamps",
    ],
)

maven_library(
    name = "timestamps",
    srcs = ["src/main/java/com/cgi/eoss/eopp/util/Timestamps.java"],
    artifact_name = "libeopp :: util :: timestamps",
    generate_sonarqube_project = False,
    deps = ["//third_party/protobuf:timestamp_java_proto"],
)

java_library(
    name = "util_java_tests",
    testonly = True,
    srcs = glob(["src/test/java/**/*.java"]),
    deps = [
        ":util",
        "@com_google_truth_truth",
        "@junit_junit",
    ],
)

java_test(
    name = "LazyTest",
    size = "small",
    test_class = "com.cgi.eoss.eopp.util.LazyTest",
    runtime_deps = [":util_java_tests"],
)
