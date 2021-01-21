load("@io_bazel_rules_kotlin//kotlin:kotlin.bzl", "define_kt_toolchain", "kt_javac_options", "kt_kotlinc_options")

licenses(["notice"])

KOTLIN_LANGUAGE_LEVEL = "1.4"

JAVA_LANGUAGE_LEVEL = "11"

define_kt_toolchain(
    name = "kotlin_toolchain",
    api_version = KOTLIN_LANGUAGE_LEVEL,
    jvm_target = JAVA_LANGUAGE_LEVEL,
    language_version = KOTLIN_LANGUAGE_LEVEL,
)
