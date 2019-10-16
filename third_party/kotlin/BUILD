licenses(["notice"])

load("@io_bazel_rules_kotlin//kotlin:kotlin.bzl", "define_kt_toolchain")

KOTLIN_LANGUAGE_LEVEL = "1.3"

JAVA_LANGUAGE_LEVEL = "1.8"

define_kt_toolchain(
    name = "kotlin_toolchain",
    api_version = KOTLIN_LANGUAGE_LEVEL,
    jvm_target = JAVA_LANGUAGE_LEVEL,
    language_version = KOTLIN_LANGUAGE_LEVEL,
)
