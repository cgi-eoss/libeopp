workspace(name = "com_cgi_eoss_eopp")

load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

http_archive(
    name = "google_bazel_common",
    patch_args = ["-p1"],
    patches = ["//third_party:google_bazel_common.suppress-warning.patch"],
    sha256 = "12c4ffe760f7b867aeb105374e0f91be0d70c41ef076764139f46bbb804c7b2a",
    strip_prefix = "bazel-common-d0877c70dab70684355a93ec63b2b2150529c2df",
    urls = ["https://github.com/google/bazel-common/archive/d0877c70dab70684355a93ec63b2b2150529c2df.zip"],
)

http_archive(
    name = "bazel_skylib",
    sha256 = "c6966ec828da198c5d9adbaa94c05e3a1c7f21bd012a0b29ba8ddbccb2c93b0d",
    urls = [
        "https://github.com/bazelbuild/bazel-skylib/releases/download/1.1.1/bazel-skylib-1.1.1.tar.gz",
        "https://mirror.bazel.build/github.com/bazelbuild/bazel-skylib/releases/download/1.1.1/bazel-skylib-1.1.1.tar.gz",
    ],
)

load("@bazel_skylib//:workspace.bzl", "bazel_skylib_workspace")

bazel_skylib_workspace()

http_archive(
    name = "rules_pkg",
    sha256 = "a89e203d3cf264e564fcb96b6e06dd70bc0557356eb48400ce4b5d97c2c3720d",
    urls = [
        "https://mirror.bazel.build/github.com/bazelbuild/rules_pkg/releases/download/0.5.1/rules_pkg-0.5.1.tar.gz",
        "https://github.com/bazelbuild/rules_pkg/releases/download/0.5.1/rules_pkg-0.5.1.tar.gz",
    ],
)

load("@rules_pkg//:deps.bzl", "rules_pkg_dependencies")

rules_pkg_dependencies()

http_archive(
    name = "rules_java",
    sha256 = "34b41ec683e67253043ab1a3d1e8b7c61e4e8edefbcad485381328c934d072fe",
    urls = ["https://github.com/bazelbuild/rules_java/releases/download/4.0.0/rules_java-4.0.0.tar.gz"],
)

load("@rules_java//java:repositories.bzl", "rules_java_dependencies", "rules_java_toolchains")

rules_java_dependencies()

rules_java_toolchains()

http_archive(
    name = "rules_proto",
    sha256 = "20b240eba17a36be4b0b22635aca63053913d5c1ee36e16be36499d167a2f533",
    strip_prefix = "rules_proto-11bf7c25e666dd7ddacbcd4d4c4a9de7a25175f8",
    urls = ["https://github.com/bazelbuild/rules_proto/archive/11bf7c25e666dd7ddacbcd4d4c4a9de7a25175f8.tar.gz"],
)

http_archive(
    name = "rules_python",
    sha256 = "cd6730ed53a002c56ce4e2f396ba3b3be262fd7cb68339f0377a45e8227fe332",
    url = "https://github.com/bazelbuild/rules_python/releases/download/0.5.0/rules_python-0.5.0.tar.gz",
)

RULES_JVM_EXTERNAL_TAG = "4.1"

RULES_JVM_EXTERNAL_SHA = "f36441aa876c4f6427bfb2d1f2d723b48e9d930b62662bf723ddfb8fc80f0140"

http_archive(
    name = "rules_jvm_external",
    sha256 = RULES_JVM_EXTERNAL_SHA,
    strip_prefix = "rules_jvm_external-%s" % RULES_JVM_EXTERNAL_TAG,
    url = "https://github.com/bazelbuild/rules_jvm_external/archive/%s.zip" % RULES_JVM_EXTERNAL_TAG,
)

rules_kotlin_version = "v1.5.0-beta-4"

rules_kotlin_sha = "6cbd4e5768bdfae1598662e40272729ec9ece8b7bded8f0d2c81c8ff96dc139d"

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

load("//third_party/java:java_repositories.bzl", "ARTIFACTS", "REPOSITORIES")
load("//third_party/kotlin:kotlin_repositories.bzl", "kotlin_repositories", "kt_register_toolchains")
load("//third_party/protobuf:protobuf_repositories.bzl", "COM_GOOGLE_PROTOBUF_JAVA_OVERRIDE_TARGETS", "protobuf_repositories")
load("//third_party/grpc:grpc_repositories.bzl", "IO_GRPC_GRPC_JAVA_OVERRIDE_TARGETS", "grpc_repositories")
load("@rules_jvm_external//:defs.bzl", "maven_install")

maven_install(
    name = "maven",
    artifacts = ARTIFACTS,
    fetch_sources = True,
    generate_compat_repositories = True,
    maven_install_json = "//third_party/java:maven_install.json",
    override_targets = dict(
        COM_GOOGLE_PROTOBUF_JAVA_OVERRIDE_TARGETS.items() +
        IO_GRPC_GRPC_JAVA_OVERRIDE_TARGETS.items(),
    ),
    repositories = REPOSITORIES,
    strict_visibility = True,
    use_unsafe_shared_cache = True,
    version_conflict_policy = "pinned",
)

load("@maven//:defs.bzl", "pinned_maven_install")

pinned_maven_install()

load("@maven//:compat.bzl", "compat_repositories")

compat_repositories()

kotlin_repositories()

kt_register_toolchains()

protobuf_repositories()

grpc_repositories()

load("//third_party/grpc:grpc_dependency_repositories.bzl", "grpc_dependency_repositories")

grpc_dependency_repositories()

load("//third_party/java:jarjar_repositories.bzl", "jarjar_repositories")

jarjar_repositories()

load("@bazel_sonarqube//:repositories.bzl", "bazel_sonarqube_repositories")

bazel_sonarqube_repositories(
    sonar_scanner_cli_sha256 = "344bfeff44b09a11082b4a4646b1ed14f213feb00a5cd6d01c86f3767cb32471",
    sonar_scanner_cli_version = "4.6.2.2472",
)
