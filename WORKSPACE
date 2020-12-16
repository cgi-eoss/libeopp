workspace(name = "com_cgi_eoss_eopp")

load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

http_archive(
    name = "google_bazel_common",
    sha256 = "cf607d9004a518d8a9bc959b558ffca730ab26e31ee1a7a36248fb054ca84cb7",
    strip_prefix = "bazel-common-11b744fb848a5c73b7173e8b771bf857b8558c89",
    urls = ["https://github.com/google/bazel-common/archive/11b744fb848a5c73b7173e8b771bf857b8558c89.zip"],
)

http_archive(
    name = "bazel_skylib",
    sha256 = "1c531376ac7e5a180e0237938a2536de0c54d93f5c278634818e0efc952dd56c",
    urls = [
        "https://github.com/bazelbuild/bazel-skylib/releases/download/1.0.3/bazel-skylib-1.0.3.tar.gz",
        "https://mirror.bazel.build/github.com/bazelbuild/bazel-skylib/releases/download/1.0.3/bazel-skylib-1.0.3.tar.gz",
    ],
)

load("@bazel_skylib//:workspace.bzl", "bazel_skylib_workspace")

bazel_skylib_workspace()

http_archive(
    name = "rules_cc",
    sha256 = "00cb11a249c93bd59f8b2ae61fba9a34fa26a673a4839fb2f3a57c185a00524a",
    strip_prefix = "rules_cc-02becfef8bc97bda4f9bb64e153f1b0671aec4ba",
    urls = ["https://github.com/bazelbuild/rules_cc/archive/02becfef8bc97bda4f9bb64e153f1b0671aec4ba.tar.gz"],
)

http_archive(
    name = "rules_java",
    sha256 = "1969a89e8da396eb7754fd0247b7df39b6df433c3dcca0095b4ba30a5409cc9d",
    strip_prefix = "rules_java-32ddd6c4f0ad38a54169d049ec05febc393b58fc",
    urls = ["https://github.com/bazelbuild/rules_java/archive/32ddd6c4f0ad38a54169d049ec05febc393b58fc.tar.gz"],
)

http_archive(
    name = "rules_proto",
    sha256 = "aa1ee19226f707d44bee44c720915199c20c84a23318bb0597ed4e5c873ccbd5",
    strip_prefix = "rules_proto-40298556293ae502c66579620a7ce867d5f57311",
    urls = ["https://github.com/bazelbuild/rules_proto/archive/40298556293ae502c66579620a7ce867d5f57311.tar.gz"],
)

http_archive(
    name = "rules_python",
    sha256 = "93ed8a85868d7f0372a935a37dacfefed469960b84c947fed29fba148b37b3bc",
    strip_prefix = "rules_python-6ed1fe53f8b36ecd404d98634d8e7411531cd6f8",
    urls = ["https://github.com/bazelbuild/rules_python/archive/6ed1fe53f8b36ecd404d98634d8e7411531cd6f8.tar.gz"],
)

RULES_JVM_EXTERNAL_TAG = "3.3"

RULES_JVM_EXTERNAL_SHA = "d85951a92c0908c80bd8551002d66cb23c3434409c814179c0ff026b53544dab"

http_archive(
    name = "rules_jvm_external",
    sha256 = RULES_JVM_EXTERNAL_SHA,
    strip_prefix = "rules_jvm_external-%s" % RULES_JVM_EXTERNAL_TAG,
    url = "https://github.com/bazelbuild/rules_jvm_external/archive/%s.zip" % RULES_JVM_EXTERNAL_TAG,
)

rules_kotlin_version = "v1.5.0-alpha-2"

rules_kotlin_sha = "6194a864280e1989b6d8118a4aee03bb50edeeae4076e5bc30eef8a98dcd4f07"

http_archive(
    name = "io_bazel_rules_kotlin",
    sha256 = rules_kotlin_sha,
    urls = ["https://github.com/bazelbuild/rules_kotlin/releases/download/%s/rules_kotlin_release.tgz" % rules_kotlin_version],
)

http_archive(
    name = "bazel_sonarqube",
    sha256 = "e33f8222983b20cc4eb7d128db68bf83a63571d0b6e05b97bd3871b81f8fb9d9",
    strip_prefix = "bazel-sonarqube-56537ff1cf4e6c28fba2b06e0f20d1f4e186645e",
    urls = ["https://github.com/Zetten/bazel-sonarqube/archive/56537ff1cf4e6c28fba2b06e0f20d1f4e186645e.zip"],
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
    sonar_scanner_cli_sha256 = "a271a933d14da6e8705d58996d30afd0b4afc93c0bfe957eb377bed808c4fa89",
    sonar_scanner_cli_version = "4.5.0.2216",
)
