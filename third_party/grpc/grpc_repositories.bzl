load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

GRPC_JAVA_VERSION = "1.32.1"

REACTOR_GRPC_VERSION = "1.0.1"

def grpc_repositories():
    http_archive(
        name = "io_grpc_grpc_java",
        sha256 = "2705d274ce79b324f3520414202481a09640b4b14e58d3124841b3318d9b6e19",
        strip_prefix = "grpc-java-%s" % GRPC_JAVA_VERSION,
        urls = ["https://github.com/grpc/grpc-java/archive/v%s.zip" % GRPC_JAVA_VERSION],
    )

    http_archive(
        name = "com_salesforce_servicelibs_reactive_grpc",
        sha256 = "9cc3a2ad992d0f15a90f28dd974cff631ead35b8b8d2bcfacf23ea42b283cedf",
        strip_prefix = "reactive-grpc-%s" % REACTOR_GRPC_VERSION,
        urls = ["https://github.com/salesforce/reactive-grpc/archive/v%s.zip" % REACTOR_GRPC_VERSION],
    )

    # From https://github.com/grpc/grpc-java/blob/{GRPC_JAVA_VERSION}/repositories.bzl
    http_archive(
        name = "io_grpc_grpc_proto",
        sha256 = "5848a4e034126bece0c37c16554fb80625615aedf1acad4e2a3cdbaaa76944eb",
        strip_prefix = "grpc-proto-cf828d0e1155e5ea58b46d7184ee5596e03ddcb8",
        urls = ["https://github.com/grpc/grpc-proto/archive/cf828d0e1155e5ea58b46d7184ee5596e03ddcb8.zip"],
    )

IO_GRPC_GRPC_JAVA_OVERRIDE_TARGETS = {
    "io.grpc:grpc-api": "@com_cgi_eoss_eopp//third_party/grpc:api",
    "io.grpc:grpc-context": "@com_cgi_eoss_eopp//third_party/grpc:context",
    "io.grpc:grpc-core": "@com_cgi_eoss_eopp//third_party/grpc:all_core",
    "io.grpc:grpc-netty": "@com_cgi_eoss_eopp//third_party/grpc:netty",
    "io.grpc:grpc-protobuf": "@com_cgi_eoss_eopp//third_party/grpc:protobuf",
    "io.grpc:grpc-protobuf-lite": "@com_cgi_eoss_eopp//third_party/grpc:protobuf_lite",
    # TODO Override the bazel grpc-services target when they fix deprecation warnings (or we can suppress them)
    #"io.grpc:grpc-services": "@com_cgi_eoss_eopp//third_party/grpc:all_services",
    "io.grpc:grpc-stub": "@com_cgi_eoss_eopp//third_party/grpc:stub",
}
