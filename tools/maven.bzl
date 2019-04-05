load("@google_bazel_common//tools/javadoc:javadoc.bzl", "javadoc_library")
load("@google_bazel_common//tools/maven:pom_file.bzl", default_pom_file = "pom_file")

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
        tags = ["maven_coordinates=" + group_id + ":" + (artifact_id or name) + ":" + POM_VERSION],
        **kwargs
    )

    javadoc_library(
        name = "lib" + name + "-javadoc",
        srcs = srcs,
        root_packages = root_packages,
        deps = [":" + name],
    )

    pom_file(
        name = "pom",
        targets = [":" + name],
        artifact_name = artifact_name,
        artifact_id = artifact_id or name,
        packaging = packaging,
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
