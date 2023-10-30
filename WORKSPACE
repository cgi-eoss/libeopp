workspace(name = "com_cgi_eoss_eopp")

load(":deps.bzl", "libeopp_dependencies")

libeopp_dependencies()

load("@bazel_skylib//:workspace.bzl", "bazel_skylib_workspace")

bazel_skylib_workspace()

load("@hermetic_cc_toolchain//toolchain:defs.bzl", zig_toolchains = "toolchains")

zig_toolchains()

register_toolchains(
    "@zig_sdk//toolchain:linux_amd64_gnu.2.28",
    "@zig_sdk//toolchain:linux_arm64_gnu.2.28",
    "@zig_sdk//toolchain:darwin_amd64",
    "@zig_sdk//toolchain:darwin_arm64",
    "@zig_sdk//toolchain:windows_amd64",
    "@zig_sdk//toolchain:windows_arm64",
)

load("@rules_python//python:repositories.bzl", "py_repositories")

py_repositories()

load("@rules_pkg//:deps.bzl", "rules_pkg_dependencies")

rules_pkg_dependencies()

load("@rules_java//java:repositories.bzl", "rules_java_dependencies", "rules_java_toolchains")

rules_java_dependencies()

rules_java_toolchains()

load("@rules_proto//proto:repositories.bzl", "rules_proto_dependencies", "rules_proto_toolchains")

rules_proto_dependencies()

rules_proto_toolchains()

load("@rules_jvm_external//:repositories.bzl", "rules_jvm_external_deps")

rules_jvm_external_deps()

load("@rules_jvm_external//:setup.bzl", "rules_jvm_external_setup")

rules_jvm_external_setup()

load("//third_party/java:java_repositories.bzl", "ARTIFACTS", "REPOSITORIES")
load("//third_party/protobuf:protobuf_repositories.bzl", "COM_GOOGLE_PROTOBUF_JAVA_OVERRIDE_TARGETS")
load("//third_party/grpc:grpc_repositories.bzl", "IO_GRPC_GRPC_JAVA_OVERRIDE_TARGETS", "IO_GRPC_GRPC_KOTLIN_OVERRIDE_TARGETS")
load("//third_party/kotlin:kotlin_repositories.bzl", "kotlin_repositories", "kt_register_toolchains")
load("@rules_jvm_external//:defs.bzl", "maven_install")

maven_install(
    name = "maven",
    artifacts = ARTIFACTS,
    fetch_sources = True,
    generate_compat_repositories = True,
    maven_install_json = "//third_party/java:maven_install.json",
    override_targets = dict(
        COM_GOOGLE_PROTOBUF_JAVA_OVERRIDE_TARGETS.items() +
        IO_GRPC_GRPC_JAVA_OVERRIDE_TARGETS.items() +
        IO_GRPC_GRPC_KOTLIN_OVERRIDE_TARGETS.items(),
    ),
    repositories = REPOSITORIES,
    strict_visibility = True,
    version_conflict_policy = "pinned",
)

load("@maven//:defs.bzl", "pinned_maven_install")

pinned_maven_install()

load("@maven//:compat.bzl", "compat_repositories")

compat_repositories()

kotlin_repositories()

kt_register_toolchains()

load("//third_party/protobuf:protobuf_dependency_repositories.bzl", "protobuf_dependency_repositories")

protobuf_dependency_repositories()

load("//third_party/grpc:grpc_dependency_repositories.bzl", "grpc_dependency_repositories")

grpc_dependency_repositories()

load("@bazel_sonarqube//:repositories.bzl", "bazel_sonarqube_repositories")

bazel_sonarqube_repositories(
    sonar_scanner_cli_sha256 = "817802ab3a476f739192d6c10504285e24e9224a0fbe2a518bb938ff88b7ea81",
    sonar_scanner_cli_version = "5.0.1.3006",
)
