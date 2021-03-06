load("@rules_java//java:defs.bzl", "java_library")
load("//tools:maven.bzl", "POM_VERSION", "pom_file")
load("@bazel_sonarqube//:defs.bzl", "sonarqube")

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

MODULES = [
    "file",
    "file-stream",
    "identifier",
    "geojson",
    "job",
    "job-executor",
    "job-graph",
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
    "//resource:okhttp-resource",
    "//resource:path-resource",
    "//resource:s3-resource",
    "//testing:docker-junit-rules",
    "//util:eopp-headers",
    "//util:timestamps",
    "//util:io",
]

java_library(
    name = "eopp",
    tags = [
        "maven_artifact",
        "maven_coordinates=com.cgi.eoss.eopp:libeopp:" + POM_VERSION,
    ],
    exports = ["//%s" % m for m in MODULES] + NON_SQ_TARGETS,
)

pom_file(
    name = "pom",
    artifact_id = "libeopp",
    artifact_name = "libeopp",
    tags = ["maven_coordinates=com.cgi.eoss.eopp:libeopp:" + POM_VERSION],
    targets = [":eopp"],
)

sonarqube(
    name = "sq_libeopp",
    coverage_report = ":coverage_report",
    modules = {"//%s:sq_%s" % (m, m): m for m in MODULES},
    project_key = "com.cgi.eoss.eopp:libeopp",
    project_name = "libeopp",
    scm_info = [":git"],
    tags = ["manual"],
)
