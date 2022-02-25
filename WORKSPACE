workspace(name = "com_cgi_eoss_eopp")

load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

http_archive(
    name = "google_bazel_common",
    patch_args = ["-p1"],
    patches = ["//third_party:google_bazel_common.suppress-warning.patch"],
    sha256 = "207317ec7f3acaa9e4e33f11eb45effdda2c3506edbe6fbdbffcff32be6fe16a",
    strip_prefix = "bazel-common-f538027ae7f9133d1796eb6b970fb78eff58e4b5",
    urls = ["https://github.com/google/bazel-common/archive/f538027ae7f9133d1796eb6b970fb78eff58e4b5.zip"],
)

http_archive(
    name = "bazel_skylib",
    sha256 = "f7be3474d42aae265405a592bb7da8e171919d74c16f082a5457840f06054728",
    urls = [
        "https://github.com/bazelbuild/bazel-skylib/releases/download/1.2.1/bazel-skylib-1.2.1.tar.gz",
        "https://mirror.bazel.build/github.com/bazelbuild/bazel-skylib/releases/download/1.2.1/bazel-skylib-1.2.1.tar.gz",
    ],
)

load("@bazel_skylib//:workspace.bzl", "bazel_skylib_workspace")

bazel_skylib_workspace()

http_archive(
    name = "rules_pkg",
    sha256 = "62eeb544ff1ef41d786e329e1536c1d541bb9bcad27ae984d57f18f314018e66",
    urls = [
        "https://github.com/bazelbuild/rules_pkg/releases/download/0.6.0/rules_pkg-0.6.0.tar.gz",
        "https://mirror.bazel.build/github.com/bazelbuild/rules_pkg/releases/download/0.6.0/rules_pkg-0.6.0.tar.gz",
    ],
)

load("@rules_pkg//:deps.bzl", "rules_pkg_dependencies")

rules_pkg_dependencies()

http_archive(
    name = "rules_java",
    sha256 = "ddc9e11f4836265fea905d2845ac1d04ebad12a255f791ef7fd648d1d2215a5b",
    strip_prefix = "rules_java-5.0.0",
    urls = ["https://github.com/bazelbuild/rules_java/archive/refs/tags/5.0.0.tar.gz"],
)

load("@rules_java//java:repositories.bzl", "rules_java_dependencies", "rules_java_toolchains")

rules_java_dependencies()

rules_java_toolchains()

http_archive(
    name = "rules_proto",
    sha256 = "c22cfcb3f22a0ae2e684801ea8dfed070ba5bed25e73f73580564f250475e72d",
    strip_prefix = "rules_proto-4.0.0-3.19.2",
    urls = ["https://github.com/bazelbuild/rules_proto/archive/refs/tags/4.0.0-3.19.2.tar.gz"],
)

http_archive(
    name = "rules_python",
    sha256 = "15f84594af9da06750ceb878abbf129241421e3abbd6e36893041188db67f2fb",
    strip_prefix = "rules_python-0.7.0",
    url = "https://github.com/bazelbuild/rules_python/archive/refs/tags/0.7.0.tar.gz",
)

RULES_JVM_EXTERNAL_TAG = "4.2"

RULES_JVM_EXTERNAL_SHA = "cd1a77b7b02e8e008439ca76fd34f5b07aecb8c752961f9640dea15e9e5ba1ca"

http_archive(
    name = "rules_jvm_external",
    sha256 = RULES_JVM_EXTERNAL_SHA,
    strip_prefix = "rules_jvm_external-%s" % RULES_JVM_EXTERNAL_TAG,
    url = "https://github.com/bazelbuild/rules_jvm_external/archive/%s.zip" % RULES_JVM_EXTERNAL_TAG,
)

rules_kotlin_version = "v1.5.0"

rules_kotlin_sha = "12d22a3d9cbcf00f2e2d8f0683ba87d3823cb8c7f6837568dd7e48846e023307"

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
    sonar_scanner_cli_sha256 = "8e116018669ac97cab0555d17145f6de9f0d8ab7a2521b378a19388d0625ec55",
    sonar_scanner_cli_version = "4.7.0.2747",
)
