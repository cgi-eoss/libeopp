licenses(["notice"])

# Re-exports with Maven coordinates for POM generation
load(":protobuf_repositories.bzl", "PROTOBUF_VERSION")

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

java_proto_library(
    name = "timestamp_java_proto",
    tags = [
        "manual",
        "maven_coordinates=com.google.protobuf:protobuf-java:" + PROTOBUF_VERSION,
    ],
    visibility = ["//visibility:public"],
    deps = ["@com_google_protobuf//:timestamp_proto"],
)
