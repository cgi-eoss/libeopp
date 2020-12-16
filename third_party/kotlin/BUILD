load("@io_bazel_rules_kotlin//kotlin:kotlin.bzl", "define_kt_toolchain", "kt_javac_options", "kt_kotlinc_options")

licenses(["notice"])

KOTLIN_LANGUAGE_LEVEL = "1.4"

JAVA_LANGUAGE_LEVEL = "11"

kt_kotlinc_options(
    name = "kotlinc_options",
)

kt_javac_options(
    name = "default_javac_options",
)

define_kt_toolchain(
    name = "kotlin_toolchain",
    api_version = KOTLIN_LANGUAGE_LEVEL,
    javac_options = ":default_javac_options",
    jvm_target = JAVA_LANGUAGE_LEVEL,
    kotlinc_options = ":kotlinc_options",
    language_version = KOTLIN_LANGUAGE_LEVEL,
)
