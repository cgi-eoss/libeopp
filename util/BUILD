load("//tools:maven.bzl", "maven_library")
load("@google_bazel_common//testing:test_defs.bzl", "gen_java_tests")
load("@rules_java//java:defs.bzl", "java_library", "java_test")

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
    test_suite = [":util_tests"],
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

gen_java_tests(
    name = "util_tests",
    srcs = glob(["src/test/java/**/*.java"]),
    deps = [
        ":util",
        "@maven//:com_google_truth_truth",
        "@maven//:junit_junit",
    ],
)
