load("//tools:maven.bzl", "maven_library", "sonarqube_project")

maven_library(
    name = "resolver",
    srcs = [
        "src/main/java/com/cgi/eoss/eopp/resolver/DefaultResolverFacade.java",
        "src/main/java/com/cgi/eoss/eopp/resolver/Resolver.java",
        "src/main/java/com/cgi/eoss/eopp/resolver/ResolverFacade.java",
    ],
    artifact_name = "libeopp :: resolver",
    sq_srcs = glob(["src/main/java/**"]),
    sq_targets = [
        ":resolver",
        ":path-resolver",
    ],
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
    deps = [
        ":resolver",
        "//resource",
        "//resource:path-resource",
        "@com_google_guava_guava",
    ],
)

java_library(
    name = "resolver_java_tests",
    testonly = True,
    srcs = glob(["src/test/java/**/*.java"]),
    deps = [
        ":path-resolver",
        ":resolver",
        "//file",
        "//resource",
        "@com_google_guava_guava",
        "@com_google_truth_truth",
        "@junit_junit",
        "@org_mockito_mockito_core",
    ],
)

java_test(
    name = "DefaultResolverFacadeTest",
    size = "small",
    test_class = "com.cgi.eoss.eopp.resolver.DefaultResolverFacadeTest",
    runtime_deps = [":resolver_java_tests"],
)

java_test(
    name = "PathResolverTest",
    size = "small",
    test_class = "com.cgi.eoss.eopp.resolver.PathResolverTest",
    runtime_deps = [":resolver_java_tests"],
)
