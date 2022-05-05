load("@rules_java//java:defs.bzl", "java_library", "java_proto_library")
load("@com_github_grpc_grpc_kotlin//:kt_jvm_grpc.bzl", "kt_jvm_grpc_library", "kt_jvm_proto_library")

# Re-exports with Maven coordinates for POM generation
load(":protobuf_repositories.bzl", "PROTOBUF_JAVA_VERSION")

licenses(["notice"])

java_library(
    name = "protobuf_java",
    tags = [
        "manual",
        "maven_coordinates=com.google.protobuf:protobuf-java:" + PROTOBUF_JAVA_VERSION,
    ],
    visibility = ["//visibility:public"],
    exports = ["@com_google_protobuf//:protobuf_java"],
)

java_library(
    name = "protobuf_java_util",
    tags = [
        "manual",
        "maven_coordinates=com.google.protobuf:protobuf-java-util:" + PROTOBUF_JAVA_VERSION,
    ],
    visibility = ["//visibility:public"],
    exports = ["@com_google_protobuf//:protobuf_java_util"],
)

java_library(
    name = "protobuf_javalite",
    tags = [
        "manual",
        "maven_coordinates=com.google.protobuf:protobuf-javalite:" + PROTOBUF_JAVA_VERSION,
    ],
    visibility = ["//visibility:public"],
    exports = ["@com_google_protobuf_javalite//:protobuf_javalite"],
)

java_library(
    name = "protobuf_kotlin",
    tags = [
        "manual",
        "maven_coordinates=com.google.protobuf:protobuf-kotlin:" + PROTOBUF_JAVA_VERSION,
    ],
    visibility = ["//visibility:public"],
    exports = ["@com_google_protobuf//java/kotlin:shared_runtime"],  # note: exports some additional non-visible targets as well
)

WELL_KNOWN_PROTOS = [
    "any",
    "duration",
    "empty",
    "field_mask",
    "struct",
    "timestamp",
    "wrappers",
]

[
    alias(
        name = "%s_proto" % proto,
        actual = "@com_google_protobuf//:%s_proto" % proto,
        visibility = ["//visibility:public"],
    )
    for proto in WELL_KNOWN_PROTOS
]

[
    java_proto_library(
        name = "%s_java_proto" % proto,
        tags = [
            "manual",
            "maven_coordinates=com.google.protobuf:protobuf-java:" + PROTOBUF_JAVA_VERSION,
        ],
        visibility = ["//visibility:public"],
        deps = [":%s_proto" % proto],
    )
    for proto in WELL_KNOWN_PROTOS
]

[
    kt_jvm_proto_library(
        name = "%s_kt_jvm_proto" % proto,
        java_proto_target = "%s_java_proto" % proto,
        tags = [
            "manual",
            "maven_coordinates=com.google.protobuf:protobuf-kotlin:" + PROTOBUF_JAVA_VERSION,
        ],
        visibility = ["//visibility:public"],
        deps = [":%s_proto" % proto],
    )
    for proto in WELL_KNOWN_PROTOS
]
