load("//tools:maven.bzl", "maven_library")
load("@google_bazel_common//testing:test_defs.bzl", "gen_java_tests")

maven_library(
    name = "util",
    srcs = [
        "src/main/java/com/cgi/eoss/eopp/util/AbortableCountDownLatch.java",
        "src/main/java/com/cgi/eoss/eopp/util/AbortedException.java",
        "src/main/java/com/cgi/eoss/eopp/util/Lazy.java",
        "src/main/java/com/cgi/eoss/eopp/util/package-info.java",
    ],
    analysis_srcs = glob(["src/main/java/**"]),
    analysis_targets = [
        ":util",
        ":anies",
        ":timestamps",
        ":eopp-headers",
        ":io",
    ],
    artifact_name = "libeopp :: util",
    generate_pitest_coverage_target = True,
    root_packages = ["com.cgi.eoss.eopp.util"],
    test_suite = [":util_tests"],
    deps = [
        "//third_party/java:org_springframework_spring_core_neverlink",
    ],
)

maven_library(
    name = "anies",
    srcs = ["src/main/java/com/cgi/eoss/eopp/util/Anies.kt"],
    artifact_name = "libeopp :: util :: anies",
    build_kt_jvm_library = True,
    generate_sonarqube_project = False,
    root_packages = ["com.cgi.eoss.eopp.util"],
    deps = [
        "//third_party/protobuf:any_kt_jvm_proto",
        "@maven//:com_google_protobuf_protobuf_kotlin",
    ],
)

maven_library(
    name = "timestamps",
    srcs = ["src/main/java/com/cgi/eoss/eopp/util/Timestamps.kt"],
    artifact_name = "libeopp :: util :: timestamps",
    build_kt_jvm_library = True,
    generate_sonarqube_project = False,
    root_packages = ["com.cgi.eoss.eopp.util"],
    deps = [
        "//third_party/protobuf:timestamp_kt_jvm_proto",
        "@maven//:com_google_protobuf_protobuf_kotlin",
    ],
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
        ":timestamps",
        ":util",
        "@maven//:com_google_protobuf_protobuf_java",
        "@maven//:com_google_protobuf_protobuf_kotlin",
        "@maven//:com_google_truth_truth",
        "@maven//:junit_junit",
    ],
)
