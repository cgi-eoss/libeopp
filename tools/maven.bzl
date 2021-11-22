load("@rules_jvm_external//:defs.bzl", "javadoc")
load("@google_bazel_common//tools/maven:pom_file.bzl", default_pom_file = "pom_file")
load("@io_bazel_rules_kotlin//kotlin:jvm.bzl", "kt_jvm_library")
load("@bazel_sonarqube//:defs.bzl", "sq_project")
load("@rules_java//java:defs.bzl", "java_library")
load("//tools/pitest:pitest.bzl", "pitest_mutation_coverage_test")

POM_VERSION = "${project.version}"

def maven_coordinates_tag(name, group_id = "com.cgi.eoss.eopp", artifact_id = None):
    return "maven_coordinates=%s:%s:%s" % (group_id, (artifact_id or name), POM_VERSION)

def maven_library(
        name,
        srcs,
        artifact_name,
        artifact_id = None,
        group_id = "com.cgi.eoss.eopp",
        packaging = None,
        root_packages = ["com.cgi.eoss.eopp"],
        generate_sonarqube_project = True,
        visibility = ["//visibility:public"],
        deploy_java_library = True,
        build_kt_jvm_library = False,
        build_javadoc_library = True,
        analysis_srcs = None,
        analysis_targets = None,
        test_srcs = [],
        test_targets = [],
        test_suite = None,
        generate_pitest_coverage_target = False,
        **kwargs):
    maven_coordinates = maven_coordinates_tag(name, group_id, artifact_id)

    if build_kt_jvm_library:
        kt_jvm_library(
            name = name,
            srcs = srcs,
            tags = (["maven_artifact"] if deploy_java_library else []) + [maven_coordinates],
            visibility = visibility,
            **kwargs
        )

        java_library(
            name = "%s_srcjar" % name,
            resources = srcs,
            tags = ["manual", "maven_srcjar", maven_coordinates],
        )
    else:
        java_library(
            name = name,
            srcs = srcs,
            tags = (["maven_artifact"] if deploy_java_library else []) + [maven_coordinates],
            visibility = visibility,
            **kwargs
        )

        native.alias(
            name = "%s_srcjar" % name,
            actual = ":lib%s-src.jar" % name,
            tags = ["manual", "maven_srcjar", maven_coordinates],
        )

    if build_javadoc_library:
        javadoc(
            name = "lib%s-javadoc" % name,
            deps = [":%s" % name],
            tags = ["manual", "maven_javadoc", maven_coordinates],
        )

    pom_file(
        name = "%s_pom" % name,
        targets = [":%s" % name],
        artifact_name = artifact_name,
        artifact_id = artifact_id or name,
        group_id = group_id,
        packaging = packaging,
        tags = ["manual", maven_coordinates],
    )

    _test_srcs = test_srcs if test_srcs else native.glob(["src/test/java/**/*.java"])
    _test_targets = []
    _test_targets.extend(test_targets)
    if test_suite:
        # test source -> target mapping from @google_bazel_common//testing:test_defs.bzl
        _test_targets.extend([":%s" % test_file.replace(".java", "") for test_file in _test_srcs if test_file.endswith("Test.java")])

    if generate_sonarqube_project:
        sonarqube_project(
            name,
            analysis_srcs if analysis_srcs else srcs,
            artifact_name,
            artifact_id,
            group_id,
            targets = analysis_targets if analysis_targets else [":%s" % name],
            test_srcs = _test_srcs,
            test_targets = _test_targets,
        )

    if generate_pitest_coverage_target:
        pitest_mutation_coverage_test(
            name = "%s_pitest" % name,
            srcs = analysis_srcs if analysis_srcs else srcs,
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
        test_targets = []):
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
        visibility = ["//visibility:public"],
    )

def pom_file(
        name,
        targets,
        artifact_name,
        artifact_id,
        group_id = "com.cgi.eoss.eopp",
        packaging = None,
        **kwargs):
    default_pom_file(
        name = name,
        targets = targets,
        preferred_group_ids = ["com.cgi.eoss.eopp", "com.cgi"],
        template_file = "//tools:pom-template.xml",
        substitutions = {
            "{group_id}": group_id,
            "{artifact_id}": artifact_id,
            "{artifact_name}": artifact_name,
            "{packaging}": packaging or "jar",
        },
        **kwargs
    )
