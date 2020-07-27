workspace(name = "com_cgi_eoss_eopp")

load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

http_archive(
    name = "google_bazel_common",
    sha256 = "af494f72366fc9127a756d57042098b9f27b9d01f38f791d3de8a006f88d1271",
    strip_prefix = "bazel-common-53d2b4f9143ebe0ec7315e32bc66b63d72fc0307",
    urls = ["https://github.com/google/bazel-common/archive/53d2b4f9143ebe0ec7315e32bc66b63d72fc0307.zip"],
)

http_archive(
    name = "bazel_skylib",
    sha256 = "97e70364e9249702246c0e9444bccdc4b847bed1eb03c5a3ece4f83dfe6abc44",
    urls = [
        "https://mirror.bazel.build/github.com/bazelbuild/bazel-skylib/releases/download/1.0.2/bazel-skylib-1.0.2.tar.gz",
        "https://github.com/bazelbuild/bazel-skylib/releases/download/1.0.2/bazel-skylib-1.0.2.tar.gz",
    ],
)

load("@bazel_skylib//:workspace.bzl", "bazel_skylib_workspace")

bazel_skylib_workspace()

http_archive(
    name = "rules_cc",
    sha256 = "ce19fea12ee666a0d399e6e15b5a77264f6da2b70f2759adea767c9a7f79b17c",
    strip_prefix = "rules_cc-5cbd3dfbd1613f71ef29bbb7b10310b81e272975",
    urls = ["https://github.com/bazelbuild/rules_cc/archive/5cbd3dfbd1613f71ef29bbb7b10310b81e272975.tar.gz"],
)

http_archive(
    name = "rules_java",
    sha256 = "1969a89e8da396eb7754fd0247b7df39b6df433c3dcca0095b4ba30a5409cc9d",
    strip_prefix = "rules_java-32ddd6c4f0ad38a54169d049ec05febc393b58fc",
    urls = ["https://github.com/bazelbuild/rules_java/archive/32ddd6c4f0ad38a54169d049ec05febc393b58fc.tar.gz"],
)

http_archive(
    name = "rules_proto",
    sha256 = "dedb72afb9476b2f75da2f661a00d6ad27dfab5d97c0460cf3265894adfaf467",
    strip_prefix = "rules_proto-486aaf1808a15b87f1b6778be6d30a17a87e491a",
    urls = ["https://github.com/bazelbuild/rules_proto/archive/486aaf1808a15b87f1b6778be6d30a17a87e491a.tar.gz"],
)

http_archive(
    name = "rules_python",
    sha256 = "8a4c48ffc3f85b1ee03438d1dc0de0935fa055144c3535df1c07061515d036d4",
    strip_prefix = "rules_python-c82a8cc1f44ba6e81c65e801b1ec3e4f3852359e",
    urls = ["https://github.com/bazelbuild/rules_python/archive/c82a8cc1f44ba6e81c65e801b1ec3e4f3852359e.tar.gz"],
)

RULES_JVM_EXTERNAL_TAG = "3.3"

RULES_JVM_EXTERNAL_SHA = "d85951a92c0908c80bd8551002d66cb23c3434409c814179c0ff026b53544dab"

http_archive(
    name = "rules_jvm_external",
    sha256 = RULES_JVM_EXTERNAL_SHA,
    strip_prefix = "rules_jvm_external-%s" % RULES_JVM_EXTERNAL_TAG,
    url = "https://github.com/bazelbuild/rules_jvm_external/archive/%s.zip" % RULES_JVM_EXTERNAL_TAG,
)

rules_kotlin_version = "legacy-1.4.0-rc3"

rules_kotlin_sha = "da0e6e1543fcc79e93d4d93c3333378f3bd5d29e82c1bc2518de0dbe048e6598"

http_archive(
    name = "io_bazel_rules_kotlin",
    sha256 = rules_kotlin_sha,
    urls = ["https://github.com/bazelbuild/rules_kotlin/releases/download/%s/rules_kotlin_release.tgz" % rules_kotlin_version],
)

http_archive(
    name = "bazel_sonarqube",
    sha256 = "2fbb1596fa7ef5e24fa2631f810a116de9513dda7d15ea96fb6f16319cd2e42d",
    strip_prefix = "bazel-sonarqube-ab1774e5bbcbb602a956fb3b6215c81cbd716127",
    urls = ["https://github.com/Zetten/bazel-sonarqube/archive/ab1774e5bbcbb602a956fb3b6215c81cbd716127.zip"],
)

load("//third_party/java:java_repositories.bzl", "ARTIFACTS", "REPOSITORIES")
load("//third_party/kotlin:kotlin_repositories.bzl", "kotlin_repositories", "kt_register_toolchains")
load("//third_party/protobuf:protobuf_repositories.bzl", "COM_GOOGLE_PROTOBUF_JAVA_OVERRIDE_TARGETS", "protobuf_repositories")
load("//third_party/grpc:grpc_repositories.bzl", "IO_GRPC_GRPC_JAVA_OVERRIDE_TARGETS", "grpc_repositories")
load("//third_party/grpc:grpc_dependency_repositories.bzl", "grpc_dependency_repositories")
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

grpc_dependency_repositories()

load("//third_party/java:jarjar_repositories.bzl", "jarjar_repositories")

jarjar_repositories()

load("@bazel_sonarqube//:repositories.bzl", "bazel_sonarqube_repositories")

bazel_sonarqube_repositories(
    sonar_scanner_cli_sha256 = "8c78a2a1af24dfbc564d87ba6826795e6892d3035cb91a98c61d9e33e2b3cd46",
    sonar_scanner_cli_version = "4.3.0.2102",
)
