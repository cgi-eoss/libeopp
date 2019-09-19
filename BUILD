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

# Create a library exporting all modules as dependencies

MODULES = [
    "file",
    "resolver",
    "resource",
    "rpc",
    "util",
]

load("//tools:maven.bzl", "POM_VERSION", "pom_file")

java_library(
    name = "eopp",
    tags = ["maven_artifact", "maven_coordinates=com.cgi.eoss.eopp:libeopp:" + POM_VERSION],
    exports = ["//%s" % m for m in MODULES],
)

pom_file(
    name = "pom",
    artifact_id = "libeopp",
    artifact_name = "libeopp",
    targets = [":eopp"],
    tags = ["maven_coordinates=com.cgi.eoss.eopp:libeopp:" + POM_VERSION]
)

load("@bazel_sonarqube//:defs.bzl", "sonarqube")

sonarqube(
    name = "sq_libeopp",
    coverage_report = ":coverage_report",
    modules = {"//%s:sq_%s" % (m, m): m for m in MODULES},
    project_key = "com.cgi.eoss.eopp:libeopp",
    project_name = "libeopp",
    scm_info = [":git"],
    tags = ["manual"],
)
