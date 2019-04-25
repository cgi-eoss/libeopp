filegroup(
    name = "resolver_java_srcs",
    srcs = glob(["src/main/java/**/*.java"]),
)

load("//tools:maven.bzl", "maven_library")

maven_library(
    name = "resolver",
    srcs = [":resolver_java_srcs"],
    artifact_name = "libeopp :: resolver",
    root_packages = ["com.cgi.eoss.eopp"],
    visibility = ["//visibility:public"],
    deps = [
        "//resource",
        "@org_slf4j_slf4j_api",
    ],
)

java_library(
    name = "resolver_java_tests",
    testonly = True,
    srcs = glob(["src/test/java/**/*.java"]),
    deps = [
        ":resolver",
        "//resource",
        "//file",
        "@com_google_guava_guava",
        "@com_google_truth_truth",
        "@junit_junit",
        "@org_mockito_mockito_core",
    ],
)

java_test(
    name = "DefaultResolverFacadeTest",
    size = "small",
    test_class = "com.cgi.eoss.eopp.resolver.DefaultResolverFacadeTest",
    runtime_deps = [":resolver_java_tests"],
)
