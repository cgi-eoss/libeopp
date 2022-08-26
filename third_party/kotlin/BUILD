load("@io_bazel_rules_kotlin//kotlin:core.bzl", "define_kt_toolchain", "kt_kotlinc_options")

licenses(["notice"])

KOTLIN_LANGUAGE_LEVEL = "1.7"

JAVA_LANGUAGE_LEVEL = "11"

kt_kotlinc_options(
    name = "kt_kotlinc_options",
    x_optin = [
        "kotlin.RequiresOptIn",
    ],
)

define_kt_toolchain(
    name = "kotlin_toolchain",
    api_version = KOTLIN_LANGUAGE_LEVEL,
    jvm_target = JAVA_LANGUAGE_LEVEL,
    kotlinc_options = "@com_cgi_eoss_eopp//third_party/kotlin:kt_kotlinc_options",
    language_version = KOTLIN_LANGUAGE_LEVEL,
)
