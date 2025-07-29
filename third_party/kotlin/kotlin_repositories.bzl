load(
    "@rules_kotlin//kotlin:repositories.bzl",
    "kotlinc_version",
    "ksp_version",
    _kotlin_repositories = "kotlin_repositories",
)

KOTLINC_VERSION = "2.1.21"
KOTLINC_RELEASE_SHA = "1ba08a8b45da99339a0601134cc037b54cf85e9bc0edbe76dcbd27c2d684a977"

KSP_VERSION = "2.1.21-2.0.2"
KSP_RELEASE_SHA = "8160c2ea47b34dd3f20ffdb412594c6638914fa0d043b634adf69ca049b4b79a"

def kotlin_repositories():
    _kotlin_repositories(
        compiler_release = kotlinc_version(
            release = KOTLINC_VERSION,
            sha256 = KOTLINC_RELEASE_SHA,
        ),
        ksp_compiler_release = ksp_version(
            release = KSP_VERSION,
            sha256 = KSP_RELEASE_SHA,
        ),
    )

def kt_register_toolchains():
    native.register_toolchains("@com_cgi_eoss_eopp//third_party/kotlin:kotlin_toolchain")
