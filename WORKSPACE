workspace(name = "com_cgi_eoss_eopp")

load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

http_archive(
    name = "google_bazel_common",
    sha256 = "2f4ff4f09df4143cba49ca2c3750c1e8f544cf5a4a6164d37b6dc59d224d0c12",
    strip_prefix = "bazel-common-3ce0644a7d7da09218b96b3218f409ea8f8c84e6",
    urls = ["https://github.com/google/bazel-common/archive/3ce0644a7d7da09218b96b3218f409ea8f8c84e6.zip"],
)

http_archive(
    name = "bazel_skylib",
    sha256 = "2e351c3b4861b0c5de8db86fdd100869b544c759161008cd93949dddcbfaba53",
    strip_prefix = "bazel-skylib-0.8.0",
    urls = ["https://github.com/bazelbuild/bazel-skylib/archive/0.8.0.zip"],
)

load("//third_party/java:jarjar_repositories.bzl", "jarjar_repositories")

jarjar_repositories()

load("//third_party/java:java_repositories.bzl", "java_repositories")

java_repositories(
    fetch_sources = True,
    omit_com_google_guava_listenablefuture = True,
    omit_com_google_protobuf_protobuf_java = True,
    omit_io_grpc_grpc_context = True,
    omit_io_grpc_grpc_core = True,
    omit_io_grpc_grpc_stub = True,
    replacements = {
        "@com_google_protobuf_protobuf_java": ["@//third_party/protobuf:protobuf_java"],
        "@com_google_protobuf_protobuf_java_util": ["@//third_party/protobuf:protobuf_java_util"],
        "@com_google_guava_listenablefuture": [],
        "@io_grpc_grpc_context": ["@//third_party/grpc-java:context"],
        "@io_grpc_grpc_core": [
            # The published jar contains all these targets
            "@//third_party//grpc-java:core",
            "@//third_party//grpc-java:core_inprocess",
            "@//third_party//grpc-java:core_util",
        ],
        "@io_grpc_grpc_stub": ["@//third_party/grpc-java:stub"],
    },
)

# Protobuf expects an //external:python_headers label which would contain the
# Python headers if fast Python protos is enabled. Since we are not using fast
# Python protos, bind python_headers to a dummy target.
bind(
    name = "python_headers",
    actual = "//:dummy",
)

http_archive(
    name = "net_zlib",
    build_file = "@com_google_protobuf//:third_party/zlib.BUILD",
    sha256 = "c3e5e9fdd5004dcb542feda5ee4f0ff0744628baf8ed2dd5d66f8ca1197cb1a1",
    strip_prefix = "zlib-1.2.11",
    urls = ["https://zlib.net/zlib-1.2.11.tar.gz"],
)

# Protobuf and grpc expect these short names
bind(
    name = "guava",
    actual = "@com_google_guava_guava//jar",
)

bind(
    name = "gson",
    actual = "@com_google_code_gson_gson//jar",
)

bind(
    name = "zlib",
    actual = "@net_zlib//:zlib",
)

bind(
    name = "error_prone_annotations",
    actual = "@com_google_errorprone_error_prone_annotations//jar",
)

http_archive(
    name = "com_google_protobuf",
    sha256 = "f976a4cd3f1699b6d20c1e944ca1de6754777918320c719742e1674fcf247b7e",
    strip_prefix = "protobuf-3.7.1",
    urls = ["https://github.com/google/protobuf/archive/v3.7.1.zip"],
)

http_archive(
    name = "io_grpc_grpc_java",
    repo_mapping = {"@com_google_re2j": "@com_google_re2j_re2j"},
    sha256 = "9d23d9fec84e24bd3962f5ef9d1fd61ce939d3f649a22bcab0f19e8167fae8ef",
    strip_prefix = "grpc-java-1.20.0",
    urls = ["https://github.com/grpc/grpc-java/archive/v1.20.0.zip"],
)

http_archive(
    name = "bazel_sonarqube",
    sha256 = "a6dfa3ead0b4bd3781a844e7be63976aac91343cfe4bfd15c603be1c1a961f46",
    strip_prefix = "bazel-sonarqube-38d7e3822f95907d075e3c5252412e4d8068492e",
    urls = ["https://github.com/Zetten/bazel-sonarqube/archive/38d7e3822f95907d075e3c5252412e4d8068492e.zip"],
)
