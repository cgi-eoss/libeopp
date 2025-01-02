load(
    "@rules_kotlin//kotlin:repositories.bzl",
    "kotlinc_version",
    "ksp_version",
    _kotlin_repositories = "kotlin_repositories",
)

#KOTLINC_VERSION = "2.1.0"
#KOTLINC_RELEASE_SHA = "b6698d5728ad8f9edcdd01617d638073191d8a03139cc538a391b4e3759ad297"
#
#KSP_VERSION = "2.1.0-1.0.28"
#KSP_RELEASE_SHA = "fc27b08cadc061a4a989af01cbeccb613feef1995f4aad68f2be0f886a3ee251"
#
#KOTLINC_RELEASE = kotlinc_version(
#    release = KOTLINC_VERSION,
#    sha256 = KOTLINC_RELEASE_SHA,
#)
#
#KSP_COMPILER_RELEASE = ksp_version(
#    release = KSP_VERSION,
#    sha256 = KSP_RELEASE_SHA,
#)

def kotlin_repositories():
    _kotlin_repositories(
        #        compiler_release = KOTLINC_RELEASE,
        #        ksp_compiler_release = KSP_COMPILER_RELEASE,
    )

def kt_register_toolchains():
    native.register_toolchains("@com_cgi_eoss_eopp//third_party/kotlin:kotlin_toolchain")
