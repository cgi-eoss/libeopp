load("//tools:maven.bzl", "maven_library")
load("@google_bazel_common//testing:test_defs.bzl", "gen_java_tests")
load("@rules_java//java:defs.bzl", "java_library", "java_test")

filegroup(
    name = "geojson_java_srcs",
    srcs = glob(["src/main/java/**/*.java"]),
)

maven_library(
    name = "geojson",
    srcs = [":geojson_java_srcs"],
    artifact_name = "libeopp :: geojson",
    generate_pitest_coverage_target = True,
    root_packages = ["com.cgi.eoss.eopp.geojson"],
    test_suite = [":geojson_tests"],
    deps = [
        "@maven//:com_fasterxml_jackson_core_jackson_annotations",
        "@maven//:com_fasterxml_jackson_core_jackson_core",
        "@maven//:com_fasterxml_jackson_core_jackson_databind",
        "@maven//:com_google_code_findbugs_jsr305",
        "@maven//:org_locationtech_jts_jts_core",
    ],
)

maven_library(
    name = "geojson-schema",
    srcs = [],
    artifact_name = "libeopp :: geojson-schema",
    build_javadoc_library = False,
    generate_sonarqube_project = False,
    resources = glob(["src/main/resources/geojson-schema/*.json"]),
)

java_library(
    name = "geojson_test_resources",
    resources = glob(["src/test/resources/**/*"]),
)

gen_java_tests(
    name = "geojson_tests",
    srcs = glob(["src/test/java/**/*.java"]),
    deps = [
        ":geojson",
        ":geojson-schema",
        ":geojson_test_resources",
        "@maven//:com_fasterxml_jackson_core_jackson_core",
        "@maven//:com_fasterxml_jackson_core_jackson_databind",
        "@maven//:com_google_guava_guava",
        "@maven//:com_google_truth_truth",
        "@maven//:com_networknt_json_schema_validator",
        "@maven//:junit_junit",
        "@maven//:org_locationtech_jts_jts_core",
    ],
)
