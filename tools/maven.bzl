load("@google_bazel_common//tools/javadoc:javadoc.bzl", "javadoc_library")
load("@google_bazel_common//tools/maven:pom_file.bzl", default_pom_file = "pom_file")
load("@bazel_sonarqube//:defs.bzl", "sq_project")

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
        **kwargs):
    maven_coordinates = [maven_coordinates_tag(name, group_id, artifact_id)]

    native.java_library(
        name = name,
        srcs = srcs,
        tags = (["maven_artifact"] if deploy_java_library else []) + maven_coordinates,
        visibility = visibility,
        **kwargs
    )

    native.alias(
        name = "%s_srcjar" % name,
        actual = ":lib%s-src.jar" % name,
        tags = ["manual", "maven_srcjar"] + maven_coordinates
    )

    javadoc_library(
        name = "lib%s-javadoc" % name,
        srcs = srcs,
        root_packages = root_packages,
        deps = [":%s" % name],
        tags = ["manual", "maven_javadoc"] + maven_coordinates,
    )

    pom_file(
        name = "%s_pom" % name,
        targets = [":%s" % name],
        artifact_name = artifact_name,
        artifact_id = artifact_id or name,
        packaging = packaging,
        tags = ["manual"] + maven_coordinates,
    )

    if generate_sonarqube_project:
        sonarqube_project(
            name,
            srcs,
            artifact_name,
            artifact_id,
            group_id,
        )

def sonarqube_project(
        name,
        srcs,
        artifact_name,
        artifact_id = None,
        group_id = "com.cgi.eoss.eopp"):
    sq_project(
        name = "sq_%s" % name,
        srcs = srcs,
        project_key = "%s:%s" % (group_id, (artifact_id or name)),
        project_name = artifact_name,
        tags = ["manual"],
        targets = [":%s" % name],
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
