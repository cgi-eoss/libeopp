load("@rules_java//java:defs.bzl", "java_library")

# Re-exports with Maven coordinates for POM generation
load(":protobuf_repositories.bzl", "PROTOBUF_VERSION")

licenses(["notice"])

java_library(
    name = "protobuf_java",
    tags = [
        "manual",
        "maven_coordinates=com.google.protobuf:protobuf-java:" + PROTOBUF_VERSION,
    ],
    visibility = ["//visibility:public"],
    exports = ["@com_google_protobuf//:protobuf_java"],
)

java_library(
    name = "protobuf_java_util",
    tags = [
        "manual",
        "maven_coordinates=com.google.protobuf:protobuf-java-util:" + PROTOBUF_VERSION,
    ],
    visibility = ["//visibility:public"],
    exports = ["@com_google_protobuf//:protobuf_java_util"],
)

java_library(
    name = "protobuf_javalite",
    tags = [
        "manual",
        "maven_coordinates=com.google.protobuf:protobuf-javalite:" + PROTOBUF_VERSION,
    ],
    visibility = ["//visibility:public"],
    exports = ["@com_google_protobuf_javalite//:protobuf_javalite"],
)
