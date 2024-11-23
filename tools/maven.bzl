load("@bazel_sonarqube//:defs.bzl", "sq_project")
load("@com_github_grpc_grpc_kotlin//:kt_jvm_grpc.bzl", "kt_jvm_proto_library")
load("@com_google_protobuf//bazel:java_proto_library.bzl", "java_proto_library")
load("@rules_java//java:defs.bzl", "java_library")
load("@rules_jvm_external//:defs.bzl", "javadoc")
load("@rules_jvm_external//private/rules:maven_project_jar.bzl", "maven_project_jar")
load("@rules_kotlin//kotlin:jvm.bzl", "kt_jvm_library")
load("@rules_proto//proto:defs.bzl", "proto_library")
load("@rules_python//python:proto.bzl", "py_proto_library")
load("//tools/maven_publish:maven_publish.bzl", "maven_publish")
load("//tools/pitest:pitest.bzl", "pitest_mutation_coverage_test")
load(":pom_file.bzl", "DEFAULT_POM_VERSION", "pom_file")

def maven_coordinates(name, group_id = "com.cgi.eoss.eopp", artifact_id = None):
    return "%s:%s:%s" % (group_id, (artifact_id or name), DEFAULT_POM_VERSION)

def maven_coordinates_tag(name, group_id = "com.cgi.eoss.eopp", artifact_id = None):
    return "maven_coordinates=%s:%s:%s" % (group_id, (artifact_id or name), DEFAULT_POM_VERSION)

def maven_library(
        name,
        group_id = "com.cgi.eoss.eopp",
        artifact_id = None,
        packaging = None,
        artifact_name = None,
        protos = [],
        build_kt_jvm_proto_library = True,
        build_py_proto_library = True,
        srcs = [],
        deps = [],
        build_kt_jvm_library = False,
        build_javadoc_library = True,
        root_packages = ["com.cgi.eoss.eopp"],
        analysis_srcs = None,
        analysis_targets = None,
        generate_sonarqube_project = True,
        test_srcs = [],
        test_targets = [],
        test_suite = None,
        generate_pitest_coverage_target = True,
        visibility = ["//visibility:public"],
        tags = [],
        testonly = False,
        **kwargs):
    _maven_coordinates = maven_coordinates(name, group_id, artifact_id)
    _maven_coordinates_tag = maven_coordinates_tag(name, group_id, artifact_id)

    _lib_deps = deps

    for proto in protos:
        proto_library(
            name = "%s_proto" % proto.get("name"),
            srcs = [proto.get("srcs")],
            deps = proto.get("deps"),
            testonly = testonly,
            visibility = visibility,
        )

        java_proto_library(
            name = "%s_java_proto" % proto.get("name"),
            deps = [":%s_proto" % proto.get("name")],
            testonly = testonly,
            visibility = visibility,
        )
        deps.append("%s_java_proto" % proto.get("name"))

        if build_kt_jvm_proto_library:
            kt_jvm_proto_library(
                name = "%s_kt_jvm_proto" % proto.get("name"),
                java_deps = ["%s_java_proto" % proto.get("name")],
                deps = [":%s_proto" % proto.get("name")],
                testonly = testonly,
                visibility = visibility,
            )
            deps.append("%s_kt_jvm_proto" % proto.get("name"))

        if build_py_proto_library:
            py_proto_library(
                name = "%s_py_proto" % proto.get("name"),
                deps = [":%s_proto" % proto.get("name")],
                testonly = testonly,
                visibility = visibility,
            )

    if srcs != None:
        srcs_attr = [":%s_srcs" % name]
        native.filegroup(
            name = "%s_srcs" % name,
            srcs = srcs or native.glob([
                "src/main/java/**/*.java",
                "src/main/kotlin/**/*.kt",
            ]),
            testonly = testonly,
        )
    else:
        srcs_attr = []

    if build_kt_jvm_library:
        kt_jvm_library(
            name = "%s_lib" % name,
            srcs = srcs_attr,
            visibility = visibility,
            deps = deps,
            testonly = testonly,
            tags = tags + [_maven_coordinates_tag],
            **kwargs
        )
    else:
        java_library(
            name = "%s_lib" % name,
            srcs = srcs_attr,
            visibility = visibility,
            deps = deps,
            testonly = testonly,
            tags = tags + [_maven_coordinates_tag],
            **kwargs
        )

    maven_project_jar(
        name = name,
        target = "%s_lib" % name,
        tags = tags + [_maven_coordinates_tag],
        visibility = visibility,
        testonly = testonly,
    )

    native.filegroup(
        name = "%s_srcjar" % name,
        srcs = [":%s" % name],
        output_group = "maven_source",
        visibility = visibility,
        tags = tags + ["manual"],
        testonly = testonly,
    )

    if build_javadoc_library:
        javadoc(
            name = "%s_javadoc" % name,
            deps = [":%s" % name],
            tags = tags + ["manual"],
            testonly = testonly,
        )

    pom_file(
        name = "%s_pom" % name,
        target = ":%s_lib" % name,
        artifact_name = artifact_name or name,
        visibility = visibility,
        tags = tags + ["manual"],
    )

    classifier_artifacts = {":%s_srcjar" % name: "sources"}
    if build_javadoc_library:
        classifier_artifacts[":%s_javadoc" % name] = "javadoc"

    maven_publish(
        name = "%s.publish" % name,
        coordinates = _maven_coordinates,
        pom = ":%s_pom" % name,
        artifact = ":%s" % name,
        classifier_artifacts = classifier_artifacts,
        visibility = visibility,
        tags = tags + ["manual"],
    )

    _test_srcs = test_srcs or native.glob([
        "src/test/java/**/*.java",
        "src/test/kotlin/**/*.kt",
    ])
    _test_targets = []
    _test_targets.extend(test_targets)
    if test_suite:
        # test source -> target mapping from @google_bazel_common//testing:test_defs.bzl
        _test_targets.extend([":%s" % test_file.replace(".java", "") for test_file in _test_srcs if test_file.endswith("Test.java")])

    if generate_sonarqube_project:
        _analysis_srcs = analysis_srcs or srcs_attr or native.glob([
            "src/main/java/**/*.java",
            "src/main/kotlin/**/*.kt",
        ])
        sonarqube_project(
            name = name,
            srcs = _analysis_srcs,
            artifact_name = artifact_name or artifact_id,
            artifact_id = artifact_id,
            group_id = group_id,
            targets = analysis_targets or [":%s" % name],
            test_srcs = _test_srcs,
            test_targets = _test_targets,
            testonly = True,
        )

    if generate_pitest_coverage_target:
        pitest_mutation_coverage_test(
            name = "%s_pitest" % name,
            srcs = analysis_srcs or srcs,
            libraries = analysis_targets if analysis_targets else [":%s" % name],
            target_classes = ",".join(["%s.*" % p for p in root_packages]),
            target_tests = ",".join(["%s.*" % p for p in root_packages]),
            test_srcs = _test_srcs,
            test_targets = _test_targets,
            tags = ["manual", "pitest"],
        )

def sonarqube_project(
        name,
        srcs,
        artifact_name,
        artifact_id = None,
        group_id = "com.cgi.eoss.eopp",
        targets = [],
        test_srcs = [],
        test_targets = [],
        testonly = True):
    sq_project(
        name = "sq_%s" % name,
        srcs = srcs,
        test_srcs = test_srcs,
        project_key = "%s:%s" % (group_id, (artifact_id or name)),
        project_name = artifact_name,
        tags = ["manual"],
        targets = targets,
        test_reports = ["//:test_reports"],
        test_targets = test_targets,
        testonly = testonly,
        visibility = ["//visibility:public"],
    )
