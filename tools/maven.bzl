load("@google_bazel_common//tools/javadoc:javadoc.bzl", "javadoc_library")
load("@google_bazel_common//tools/maven:pom_file.bzl", default_pom_file = "pom_file")
load("@bazel_sonarqube//:defs.bzl", "sq_project")

def maven_library(
        name,
        srcs,
        artifact_name,
        artifact_id = None,
        group_id = "com.cgi.eoss.eopp",
        packaging = None,
        root_packages = [],
        **kwargs):
    native.java_library(
        name = name,
        srcs = srcs,
        tags = ["maven_coordinates=%s:%s:%s" % (group_id, (artifact_id or name), POM_VERSION)],
        **kwargs
    )

    javadoc_library(
        name = "lib%s-javadoc" % name,
        srcs = srcs,
        root_packages = root_packages,
        deps = [":%s" % name],
        tags = ["manual"],
    )

    pom_file(
        name = "pom",
        targets = [":%s" % name],
        artifact_name = artifact_name,
        artifact_id = artifact_id or name,
        packaging = packaging,
        tags = ["manual"],
    )

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

POM_VERSION = "${project.version}"
