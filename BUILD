filegroup(
    name = "dummy",
    visibility = ["//visibility:public"],
)

# Create a library exporting all modules as dependencies

load("//tools:maven.bzl", "POM_VERSION", "pom_file")

java_library(
    name = "eopp",
    tags = ["maven_coordinates=com.cgi.eoss.eopp:libeopp:" + POM_VERSION],
    exports = [
        "//file",
        "//resolver",
        "//resource",
        "//rpc",
        "//util",
    ],
)

pom_file(
    name = "pom",
    artifact_id = "libeopp",
    artifact_name = "libeopp",
    targets = [":eopp"],
)
