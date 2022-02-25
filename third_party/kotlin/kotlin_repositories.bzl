load(
    "@io_bazel_rules_kotlin//kotlin:repositories.bzl",
    _kotlin_repositories = "kotlin_repositories",
)
load("@io_bazel_rules_kotlin//src/main/starlark/core/repositories:versions.bzl", "version")

KOTLIN_VERSION = "1.6.10"
KOTLINC_RELEASE_SHA = "432267996d0d6b4b17ca8de0f878e44d4a099b7e9f1587a98edc4d27e76c215a"

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
