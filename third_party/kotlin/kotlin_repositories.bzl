load(
    "@io_bazel_rules_kotlin//kotlin:kotlin.bzl",
    _kotlin_repositories = "kotlin_repositories",
    _kt_register_toolchains = "kt_register_toolchains",
)

KOTLIN_VERSION = "1.3.71"
KOTLINC_RELEASE_SHA = "7adb77dad99c6d2f7bde9f8bafe4c6244a04587a8e36e62b074d00eda9f8e74a"

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
