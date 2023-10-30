load(
    "@rules_kotlin//kotlin:repositories.bzl",
    _kotlin_repositories = "kotlin_repositories",
)
load("@rules_kotlin//src/main/starlark/core/repositories:versions.bzl", "version")

KOTLIN_VERSION = "1.9.22"
KOTLINC_RELEASE_SHA = "88b39213506532c816ff56348c07bbeefe0c8d18943bffbad11063cf97cac3e6"

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
