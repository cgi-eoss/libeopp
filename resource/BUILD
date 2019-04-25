filegroup(
    name = "resource_java_srcs",
    srcs = glob(["src/main/java/**/*.java"]),
)

load("//tools:maven.bzl", "maven_library")

maven_library(
    name = "resource",
    srcs = [":resource_java_srcs"],
    artifact_name = "libeopp :: resource",
    root_packages = ["com.cgi.eoss.eopp"],
    visibility = ["//visibility:public"],
    deps = [
        "//file",
        "@org_springframework_spring_core",
    ],
)
