load(
    "@io_bazel_rules_kotlin//kotlin:kotlin.bzl",
    _kotlin_repositories = "kotlin_repositories",
    _kt_register_toolchains = "kt_register_toolchains",
)

KOTLIN_VERSION = "1.3.61"
KOTLINC_RELEASE_SHA = "3901151ad5d94798a268d1771c6c0b7e305a608c2889fc98a674802500597b1c"

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
