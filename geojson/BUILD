load("//tools:maven.bzl", "maven_library", "sonarqube_project")

filegroup(
    name = "geojson_java_srcs",
    srcs = glob(["src/main/java/**/*.java"]),
)

maven_library(
    name = "geojson",
    srcs = [":geojson_java_srcs"],
    artifact_name = "libeopp :: geojson",
    root_packages = ["com.cgi.eoss.eopp.geojson"],
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
    name = "geojson_java_tests",
    testonly = True,
    srcs = glob(["src/test/java/**/*.java"]),
    resources = glob(["src/test/resources/**/*"]),
    deps = [
        ":geojson",
        ":geojson-schema",
        "@maven//:com_fasterxml_jackson_core_jackson_core",
        "@maven//:com_fasterxml_jackson_core_jackson_databind",
        "@maven//:com_google_guava_guava",
        "@maven//:com_google_truth_truth",
        "@maven//:com_networknt_json_schema_validator",
        "@maven//:junit_junit",
        "@maven//:org_locationtech_jts_jts_core",
    ],
)

java_test(
    name = "GeoJSONTest",
    size = "small",
    test_class = "com.cgi.eoss.eopp.geojson.GeoJSONTest",
    runtime_deps = [":geojson_java_tests"],
)

java_test(
    name = "FeatureTest",
    size = "small",
    test_class = "com.cgi.eoss.eopp.geojson.FeatureTest",
    runtime_deps = [":geojson_java_tests"],
)

java_test(
    name = "FeatureCollectionTest",
    size = "small",
    test_class = "com.cgi.eoss.eopp.geojson.FeatureCollectionTest",
    runtime_deps = [":geojson_java_tests"],
)

java_test(
    name = "GeometryCollectionTest",
    size = "small",
    test_class = "com.cgi.eoss.eopp.geojson.GeometryCollectionTest",
    runtime_deps = [":geojson_java_tests"],
)

java_test(
    name = "LinearRingTest",
    size = "small",
    test_class = "com.cgi.eoss.eopp.geojson.LinearRingTest",
    runtime_deps = [":geojson_java_tests"],
)

java_test(
    name = "LineStringTest",
    size = "small",
    test_class = "com.cgi.eoss.eopp.geojson.LineStringTest",
    runtime_deps = [":geojson_java_tests"],
)

java_test(
    name = "MultiLineStringTest",
    size = "small",
    test_class = "com.cgi.eoss.eopp.geojson.MultiLineStringTest",
    runtime_deps = [":geojson_java_tests"],
)

java_test(
    name = "MultiPointTest",
    size = "small",
    test_class = "com.cgi.eoss.eopp.geojson.MultiPointTest",
    runtime_deps = [":geojson_java_tests"],
)

java_test(
    name = "MultiPolygonTest",
    size = "small",
    test_class = "com.cgi.eoss.eopp.geojson.MultiPolygonTest",
    runtime_deps = [":geojson_java_tests"],
)

java_test(
    name = "PointTest",
    size = "small",
    test_class = "com.cgi.eoss.eopp.geojson.PointTest",
    runtime_deps = [":geojson_java_tests"],
)

java_test(
    name = "PolygonTest",
    size = "small",
    test_class = "com.cgi.eoss.eopp.geojson.PolygonTest",
    runtime_deps = [":geojson_java_tests"],
)

java_test(
    name = "PositionTest",
    size = "small",
    test_class = "com.cgi.eoss.eopp.geojson.PositionTest",
    runtime_deps = [":geojson_java_tests"],
)
