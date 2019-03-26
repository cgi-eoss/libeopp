filegroup(
    name = "util_java_srcs",
    srcs = glob(["src/main/java/**/*.java"]),
)

java_library(
    name = "util",
    srcs = [":util_java_srcs"],
    visibility = ["//visibility:public"],
)

java_library(
    name = "util_java_tests",
    testonly = True,
    srcs = glob(["src/test/java/**/*.java"]),
    deps = [
        ":util",
        "@com_google_guava_guava",
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
