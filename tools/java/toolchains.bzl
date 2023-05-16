# source: rules_java/java/repositories.bzl:466
def java_toolchains(version = 11, platforms = "amd64"):
    """An utility method to load all Java toolchains.

    Args:
        name: The name of this macro (not used)
    """
    JDK_VERSIONS = ["11", "17", "19"]
    PLATFORMS = ["linux", "macos", "macos_aarch64", "win"]

    # Remote JDK repos for those Linux platforms are only defined for JDK 11.
    EXTRA_REMOTE_JDK11_REPOS = [
        "remotejdk11_linux_aarch64",
        "remotejdk11_linux_ppc64le",
        "remotejdk11_linux_s390x",
    ]

    REMOTE_JDK_REPOS = [("remotejdk" + version + "_" + platform) for version in JDK_VERSIONS for platform in PLATFORMS] + EXTRA_REMOTE_JDK11_REPOS

    # Disable toolchains:all, which registers remotejdk17 as a default
    #native.register_toolchains("//toolchains:all")
    native.register_toolchains("@local_jdk//:runtime_toolchain_definition")
    for name in REMOTE_JDK_REPOS:
        native.register_toolchains("@" + name + "_toolchain_config_repo//:toolchain")
