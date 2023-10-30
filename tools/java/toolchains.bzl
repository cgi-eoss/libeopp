# source: rules_java/java/repositories.bzl:531

def rules_java_toolchains(name = "toolchains"):
    """An utility method to load all Java toolchains.

    Args:
        name: The name of this macro (not used)
    """
    JDKS = {
        # Must match JDK repos defined in remote_jdk11_repos()
        "11": ["linux", "linux_aarch64", "linux_ppc64le", "linux_s390x", "macos", "macos_aarch64", "win", "win_arm64"],
        # Must match JDK repos defined in remote_jdk17_repos()
        "17": ["linux", "linux_aarch64", "linux_ppc64le", "linux_s390x", "macos", "macos_aarch64", "win", "win_arm64"],
        # Must match JDK repos defined in remote_jdk21_repos()
        "21": ["linux", "linux_aarch64", "macos", "macos_aarch64", "win"],
    }

    REMOTE_JDK_REPOS = [("remotejdk" + version + "_" + platform) for version in JDKS for platform in JDKS[version]]

    native.register_toolchains(
        # Disable toolchains:all, to avoid registering remotejdk17 as a default
        #"//toolchains:all",
        "@local_jdk//:runtime_toolchain_definition",
        "@local_jdk//:bootstrap_runtime_toolchain_definition",
    )
    for name in REMOTE_JDK_REPOS:
        native.register_toolchains(
            "@" + name + "_toolchain_config_repo//:toolchain",
            "@" + name + "_toolchain_config_repo//:bootstrap_runtime_toolchain",
        )
