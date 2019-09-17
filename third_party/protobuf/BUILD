licenses(["notice"])

# Re-exports with Maven coordinates for POM generation

VERSION = "3.9.1"

java_library(
    name = "protobuf_java",
    tags = [
        "manual",
        "maven_coordinates=com.google.protobuf:protobuf-java:" + VERSION,
    ],
    visibility = ["//visibility:public"],
    exports = ["@com_google_protobuf//:protobuf_java"],
)

java_library(
    name = "protobuf_java_util",
    tags = [
        "manual",
        "maven_coordinates=com.google.protobuf:protobuf-java-util:" + VERSION,
    ],
    visibility = ["//visibility:public"],
    exports = ["@com_google_protobuf//:protobuf_java_util"],
)
