load("//tools:pom_file.bzl", "DEFAULT_POM_VERSION", "pom_file")
load("@rules_java//java:defs.bzl", "java_library")
load("@bazel_sonarqube//:defs.bzl", "sonarqube")
load("@rules_python//python:packaging.bzl", "py_wheel")

filegroup(
    name = "dummy",
    visibility = ["//visibility:public"],
)

filegroup(
    name = "git",
    srcs = glob(
        [".git/**"],
        exclude = [".git/**/*[*"],  # gitk creates temp files with []
    ),
    tags = ["manual"],
)

filegroup(
    name = "coverage_report",
    srcs = ["bazel-out/_coverage/_coverage_report.dat"],
    tags = ["manual"],
    visibility = ["//visibility:public"],
)

filegroup(
    name = "test_reports",
    srcs = glob(["bazel-testlogs/**/test.xml"]),
    tags = ["manual"],
    visibility = ["//visibility:public"],
)

# Create a library exporting all modules as dependencies

PROTO_MODULES = [
    "file",
    "identifier",
    "job",
    "workflow",
]

MODULES = [
    "file",
    "file-stream",
    "geojson",
    "identifier",
    "job-executor",
    "job-graph",
    "job",
    "resolver",
    "resource",
    "rpc",
    "util",
    "workflow",
]

NON_SQ_TARGETS = [
    "//geojson:geojson-schema",
    "//resolver:okhttp-resolver",
    "//resolver:path-resolver",
    "//resource:azure-blob-resource",
    "//resource:okhttp-resource",
    "//resource:path-resource",
    "//resource:s3-resource",
    "//rpc:grpc-discovery-client-name-resolver",
    "//testing:docker-junit-rules",
    "//util:eopp-headers",
    "//util:io",
    "//util:timestamps",
]

java_library(
    name = "eopp",
    tags = [
        "maven_artifact",
        "maven_coordinates=com.cgi.eoss.eopp:libeopp:" + DEFAULT_POM_VERSION,
    ],
    exports = ["//%s" % m for m in MODULES] + NON_SQ_TARGETS,
)

pom_file(
    name = "pom",
    artifact_name = "libeopp",
    target = ":eopp",
)

py_wheel(
    name = "eopp_py",
    distribution = "eopp",
    python_tag = "py3",
    version = "0.0.0",
    deps = ["//%s:%s_py_proto" % (m, m) for m in PROTO_MODULES],
)

sonarqube(
    name = "sq_libeopp",
    testonly = True,
    coverage_report = ":coverage_report",
    modules = {"//%s:sq_%s" % (m, m): m for m in MODULES},
    project_key = "com.cgi.eoss.eopp:libeopp",
    project_name = "libeopp",
    scm_info = [":git"],
    tags = ["manual"],
)

