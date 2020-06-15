load(
    "@io_bazel_rules_kotlin//kotlin:kotlin.bzl",
    _kotlin_repositories = "kotlin_repositories",
    _kt_register_toolchains = "kt_register_toolchains",
)

KOTLIN_VERSION = "1.3.72"
KOTLINC_RELEASE_SHA = "ccd0db87981f1c0e3f209a1a4acb6778f14e63fe3e561a98948b5317e526cc6c"

KOTLINC_RELEASE = {
    "urls": [
        "https://github.com/JetBrains/kotlin/releases/download/v{v}/kotlin-compiler-{v}.zip".format(v = KOTLIN_VERSION),
    ],
    "sha256": KOTLINC_RELEASE_SHA,
}

def kotlin_repositories():
    _kotlin_repositories(compiler_release = KOTLINC_RELEASE)

def kt_register_toolchains():
    native.register_toolchains("//third_party/kotlin:kotlin_toolchain")
