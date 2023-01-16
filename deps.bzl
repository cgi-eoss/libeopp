load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")
load("//third_party/protobuf:protobuf_repositories.bzl", "protobuf_repositories")
load("//third_party/grpc:grpc_repositories.bzl", "grpc_repositories")

def libeopp_dependencies():
    http_archive(
        name = "google_bazel_common",
        sha256 = "bcb5de5a5b951434828ead94479d3e1ff6501c2c8fc490db6cf3fbf7c188684b",
        strip_prefix = "bazel-common-aaa4d801588f7744c6f4428e4f133f26b8518f42",
        urls = ["https://github.com/google/bazel-common/archive/aaa4d801588f7744c6f4428e4f133f26b8518f42.zip"],
    )

    http_archive(
        name = "bazel_skylib",
        sha256 = "74d544d96f4a5bb630d465ca8bbcfe231e3594e5aae57e1edbf17a6eb3ca2506",
        urls = [
            "https://mirror.bazel.build/github.com/bazelbuild/bazel-skylib/releases/download/1.3.0/bazel-skylib-1.3.0.tar.gz",
            "https://github.com/bazelbuild/bazel-skylib/releases/download/1.3.0/bazel-skylib-1.3.0.tar.gz",
        ],
    )

    protobuf_repositories()

    grpc_repositories()

    http_archive(
        name = "rules_cc",
        sha256 = "af6cc82d87db94585bceeda2561cb8a9d55ad435318ccb4ddfee18a43580fb5d",
        strip_prefix = "rules_cc-0.0.4",
        urls = ["https://github.com/bazelbuild/rules_cc/releases/download/0.0.4/rules_cc-0.0.4.tar.gz"],
    )

    http_archive(
        name = "rules_python",
        sha256 = "48a838a6e1983e4884b26812b2c748a35ad284fd339eb8e2a6f3adf95307fbcd",
        strip_prefix = "rules_python-0.16.2",
        url = "https://github.com/bazelbuild/rules_python/archive/refs/tags/0.16.2.tar.gz",
    )

    http_archive(
        name = "rules_pkg",
        sha256 = "eea0f59c28a9241156a47d7a8e32db9122f3d50b505fae0f33de6ce4d9b61834",
        urls = [
            "https://mirror.bazel.build/github.com/bazelbuild/rules_pkg/releases/download/0.8.0/rules_pkg-0.8.0.tar.gz",
            "https://github.com/bazelbuild/rules_pkg/releases/download/0.8.0/rules_pkg-0.8.0.tar.gz",
        ],
    )

    http_archive(
        name = "rules_java",
        sha256 = "c73336802d0b4882e40770666ad055212df4ea62cfa6edf9cb0f9d29828a0934",
        url = "https://github.com/bazelbuild/rules_java/releases/download/5.3.5/rules_java-5.3.5.tar.gz",
    )

    http_archive(
        name = "rules_proto",
        sha256 = "dc3fb206a2cb3441b485eb1e423165b231235a1ea9b031b4433cf7bc1fa460dd",
        strip_prefix = "rules_proto-5.3.0-21.7",
        urls = ["https://github.com/bazelbuild/rules_proto/archive/refs/tags/5.3.0-21.7.tar.gz"],
    )

    RULES_JVM_EXTERNAL_TAG = "4.5"

    RULES_JVM_EXTERNAL_SHA = "b17d7388feb9bfa7f2fa09031b32707df529f26c91ab9e5d909eb1676badd9a6"

    http_archive(
        name = "rules_jvm_external",
        sha256 = RULES_JVM_EXTERNAL_SHA,
        strip_prefix = "rules_jvm_external-%s" % RULES_JVM_EXTERNAL_TAG,
        url = "https://github.com/bazelbuild/rules_jvm_external/archive/%s.zip" % RULES_JVM_EXTERNAL_TAG,
    )

    rules_kotlin_version = "v1.7.1"

    rules_kotlin_sha = "fd92a98bd8a8f0e1cdcb490b93f5acef1f1727ed992571232d33de42395ca9b3"

    http_archive(
        name = "io_bazel_rules_kotlin",
        sha256 = rules_kotlin_sha,
        urls = ["https://github.com/bazelbuild/rules_kotlin/releases/download/%s/rules_kotlin_release.tgz" % rules_kotlin_version],
    )

    http_archive(
        name = "bazel_sonarqube",
        sha256 = "b50d5faebc72fc1796ca010f0df795aa6943813e9770f0c0a2e950cf75bf3465",
        strip_prefix = "bazel-sonarqube-37261de24f80b661bbc4726e3382ef43e9d66a6e",
        urls = ["https://github.com/Zetten/bazel-sonarqube/archive/37261de24f80b661bbc4726e3382ef43e9d66a6e.zip"],
    )
