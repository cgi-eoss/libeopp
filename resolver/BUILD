load("//tools:maven.bzl", "maven_library")
load("@google_bazel_common//testing:test_defs.bzl", "gen_java_tests")
load("@rules_java//java:defs.bzl", "java_library", "java_test")

maven_library(
    name = "resolver",
    srcs = [
        "src/main/java/com/cgi/eoss/eopp/resolver/DefaultResolverFacade.java",
        "src/main/java/com/cgi/eoss/eopp/resolver/Resolver.java",
        "src/main/java/com/cgi/eoss/eopp/resolver/ResolverFacade.java",
    ],
    analysis_srcs = glob(["src/main/java/**"]),
    analysis_targets = [
        ":okhttp-resolver",
        ":path-resolver",
        ":resolver",
    ],
    artifact_name = "libeopp :: resolver",
    generate_pitest_coverage_target = True,
    root_packages = ["com.cgi.eoss.eopp.resolver"],
    test_suite = [":resolver_tests"],
    deps = [
        "//resource",
        "@org_slf4j_slf4j_api",
    ],
)

maven_library(
    name = "path-resolver",
    srcs = ["src/main/java/com/cgi/eoss/eopp/resolver/PathResolver.java"],
    artifact_name = "libeopp :: resolver :: path",
    generate_sonarqube_project = False,
    root_packages = ["com.cgi.eoss.eopp.resolver"],
    deps = [
        ":resolver",
        "//resource",
        "//resource:path-resource",
    ],
)

maven_library(
    name = "okhttp-resolver",
    srcs = ["src/main/java/com/cgi/eoss/eopp/resolver/OkHttpResolver.java"],
    artifact_name = "libeopp :: resolver :: okhttp",
    generate_sonarqube_project = False,
    root_packages = ["com.cgi.eoss.eopp.resolver"],
    deps = [
        ":resolver",
        "//resource",
        "//resource:okhttp-resource",
        "@maven//:com_squareup_okhttp3_okhttp",
    ],
)

gen_java_tests(
    name = "resolver_tests",
    srcs = glob(["src/test/java/**/*.java"]),
    deps = [
        ":okhttp-resolver",
        ":path-resolver",
        ":resolver",
        "//file",
        "//resource",
        "//util:eopp-headers",
        "//util:timestamps",
        "@maven//:com_google_guava_guava",
        "@maven//:com_google_truth_extensions_truth_proto_extension",
        "@maven//:com_google_truth_truth",
        "@maven//:com_squareup_okhttp3_mockwebserver",
        "@maven//:com_squareup_okhttp3_okhttp",
        "@maven//:junit_junit",
        "@maven//:org_mockito_mockito_core",
    ],
)
