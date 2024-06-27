load(
    "@rules_kotlin//kotlin:repositories.bzl",
    "kotlinc_version",
    "ksp_version",
    _kotlin_repositories = "kotlin_repositories",
)

KOTLINC_VERSION = "1.9.24"
KOTLINC_RELEASE_SHA = "eb7b68e01029fa67bc8d060ee54c12018f2c60ddc438cf21db14517229aa693b"

KSP_VERSION = "1.9.24-1.0.20"
KSP_RELEASE_SHA = "24f7091cfd6ad6a67857b22884d5f8d1bb7815ee2115b45a9f0709774daadb56"

KOTLINC_RELEASE = kotlinc_version(
    release = KOTLINC_VERSION,
    sha256 = KOTLINC_RELEASE_SHA,
)

KSP_COMPILER_RELEASE = ksp_version(
    release = KSP_VERSION,
    sha256 = KSP_RELEASE_SHA,
)

def kotlin_repositories():
    _kotlin_repositories(
        compiler_release = KOTLINC_RELEASE,
        ksp_compiler_release = KSP_COMPILER_RELEASE,
    )

def kt_register_toolchains():
    native.register_toolchains("@com_cgi_eoss_eopp//third_party/kotlin:kotlin_toolchain")
