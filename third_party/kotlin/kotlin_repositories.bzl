load(
    "@io_bazel_rules_kotlin//kotlin:repositories.bzl",
    _kotlin_repositories = "kotlin_repositories",
)
load("@io_bazel_rules_kotlin//src/main/starlark/core/repositories:versions.bzl", "version")

KOTLIN_VERSION = "1.5.31"
KOTLINC_RELEASE_SHA = "661111286f3e5ac06aaf3a9403d869d9a96a176b62b141814be626a47249fe9e"

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
