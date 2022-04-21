load(
    "@io_bazel_rules_kotlin//kotlin:repositories.bzl",
    _kotlin_repositories = "kotlin_repositories",
)
load("@io_bazel_rules_kotlin//src/main/starlark/core/repositories:versions.bzl", "version")

KOTLIN_VERSION = "1.6.21"
KOTLINC_RELEASE_SHA = "632166fed89f3f430482f5aa07f2e20b923b72ef688c8f5a7df3aa1502c6d8ba"

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
