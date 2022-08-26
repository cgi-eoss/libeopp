load(
    "@io_bazel_rules_kotlin//kotlin:repositories.bzl",
    _kotlin_repositories = "kotlin_repositories",
)
load("@io_bazel_rules_kotlin//src/main/starlark/core/repositories:versions.bzl", "version")

KOTLIN_VERSION = "1.7.20"
KOTLINC_RELEASE_SHA = "5e3c8d0f965410ff12e90d6f8dc5df2fc09fd595a684d514616851ce7e94ae7d"

KOTLINC_RELEASE = version(
    version = KOTLIN_VERSION,
    url_templates = [
        "https://github.com/JetBrains/kotlin/releases/download/v{version}/kotlin-compiler-{version}.zip",
    ],
    sha256 = KOTLINC_RELEASE_SHA,
)

def kotlin_repositories():
    _kotlin_repositories(compiler_release = KOTLINC_RELEASE)

def kt_register_toolchains():
    native.register_toolchains("@com_cgi_eoss_eopp//third_party/kotlin:kotlin_toolchain")
