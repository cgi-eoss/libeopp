load("@io_bazel_rules_kotlin//kotlin:jvm.bzl", "kt_jvm_binary", "kt_jvm_library")

kt_jvm_library(
    name = "pitest_coverage_test",
    srcs = ["src/main/kotlin/PitestCoverageTest.kt"],
    visibility = ["//visibility:public"],
    runtime_deps = ["@maven//:org_jetbrains_kotlin_kotlin_stdlib_jdk8"],
    deps = [
        "@maven//:com_google_guava_guava",
        "@maven//:com_google_truth_truth",
        "@maven//:junit_junit",
        "@maven//:org_pitest_pitest",
        "@maven//:org_pitest_pitest_command_line",
        "@maven//:org_pitest_pitest_entry",
    ],
)

kt_jvm_library(
    name = "_pitest_report_aggregator",
    srcs = ["src/main/kotlin/PitestReportAggregator.kt"],
    runtime_deps = [
        "@maven//:ch_qos_logback_logback_classic",
        "@maven//:org_jetbrains_kotlin_kotlin_stdlib_jdk8",
    ],
    deps = [
        "@maven//:org_pitest_pitest_aggregator",
    ],
)

java_binary(
    name = "pitest_report_aggregator",
    main_class = "com.cgi.eoss.eopp.testing.pitest.PitestReportAggregator",
    visibility = ["//visibility:public"],
    runtime_deps = [":_pitest_report_aggregator"],
)
