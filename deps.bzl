load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")
load("//third_party/protobuf:protobuf_repositories.bzl", "protobuf_repositories")
load("//third_party/grpc:grpc_repositories.bzl", "grpc_repositories")

def libeopp_dependencies():
    http_archive(
        name = "google_bazel_common",
        sha256 = "e30e092e50c47a38994334dbe42386675cf519a5e86b973e45034323bbdb70a3",
        strip_prefix = "bazel-common-a9e1d8efd54cbf27249695b23775b75ca65bb59d",
        urls = ["https://github.com/google/bazel-common/archive/a9e1d8efd54cbf27249695b23775b75ca65bb59d.zip"],
    )

    http_archive(
        name = "bazel_skylib",
        sha256 = "b8a1527901774180afc798aeb28c4634bdccf19c4d98e7bdd1ce79d1fe9aaad7",
        urls = [
            "https://mirror.bazel.build/github.com/bazelbuild/bazel-skylib/releases/download/1.4.1/bazel-skylib-1.4.1.tar.gz",
            "https://github.com/bazelbuild/bazel-skylib/releases/download/1.4.1/bazel-skylib-1.4.1.tar.gz",
        ],
    )

    protobuf_repositories()

    grpc_repositories()

    http_archive(
        name = "rules_cc",
        sha256 = "3d9e271e2876ba42e114c9b9bc51454e379cbf0ec9ef9d40e2ae4cec61a31b40",
        strip_prefix = "rules_cc-0.0.6",
        urls = ["https://github.com/bazelbuild/rules_cc/releases/download/0.0.6/rules_cc-0.0.6.tar.gz"],
    )

    http_archive(
        name = "rules_python",
        sha256 = "94750828b18044533e98a129003b6a68001204038dc4749f40b195b24c38f49f",
        strip_prefix = "rules_python-0.21.0",
        urls = ["https://github.com/bazelbuild/rules_python/releases/download/0.21.0/rules_python-0.21.0.tar.gz"],
    )

    http_archive(
        name = "rules_pkg",
        sha256 = "335632735e625d408870ec3e361e192e99ef7462315caa887417f4d88c4c8fb8",
        urls = [
            "https://mirror.bazel.build/github.com/bazelbuild/rules_pkg/releases/download/0.9.0/rules_pkg-0.9.0.tar.gz",
            "https://github.com/bazelbuild/rules_pkg/releases/download/0.9.0/rules_pkg-0.9.0.tar.gz",
        ],
    )

    http_archive(
        name = "rules_java",
        sha256 = "bcfabfb407cb0c8820141310faa102f7fb92cc806b0f0e26a625196101b0b57e",
        urls = ["https://github.com/bazelbuild/rules_java/releases/download/5.5.0/rules_java-5.5.0.tar.gz"],
    )

    http_archive(
        name = "rules_proto",
        sha256 = "dc3fb206a2cb3441b485eb1e423165b231235a1ea9b031b4433cf7bc1fa460dd",
        strip_prefix = "rules_proto-5.3.0-21.7",
        urls = ["https://github.com/bazelbuild/rules_proto/archive/refs/tags/5.3.0-21.7.tar.gz"],
    )

    RULES_JVM_EXTERNAL_TAG = "5.2"
    RULES_JVM_EXTERNAL_SHA = "f86fd42a809e1871ca0aabe89db0d440451219c3ce46c58da240c7dcdc00125f"

    http_archive(
        name = "rules_jvm_external",
        strip_prefix = "rules_jvm_external-%s" % RULES_JVM_EXTERNAL_TAG,
        sha256 = RULES_JVM_EXTERNAL_SHA,
        url = "https://github.com/bazelbuild/rules_jvm_external/releases/download/%s/rules_jvm_external-%s.tar.gz" % (RULES_JVM_EXTERNAL_TAG, RULES_JVM_EXTERNAL_TAG)
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
        sha256 = "2205ef64f2181e67b7dd8e91c4c7118d8163149225ec7a04b3e67216a9b74479",
        strip_prefix = "bazel-sonarqube-77044ecda2ba8d4c7aa4a4c3dfaa703d981b9b88",
        urls = ["https://github.com/Zetten/bazel-sonarqube/archive/77044ecda2ba8d4c7aa4a4c3dfaa703d981b9b88.zip"],
    )