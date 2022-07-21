# Adapted from rules_jvm_external
# Their maven_publish rule is a) internal and b) supports GCS stuff we don't need.
# We also add support for `--define=pom_version=XYZ` to dynamically set the artifact version

load("@rules_jvm_external//:defs.bzl", "MavenPublishInfo")

_TEMPLATE = """#!/usr/bin/env bash

echo "Uploading {coordinates} to {maven_repo}"
{uploader} {maven_repo} {gpg_sign} {user} {password} {coordinates} {pom} {artifact_jar} {source_jar} {javadoc}
"""

def _maven_publish_impl(ctx):
    executable = ctx.actions.declare_file("%s-publisher" % ctx.attr.name)

    maven_repo = ctx.var.get("maven_repo", "''")
    gpg_sign = ctx.var.get("gpg_sign", "'false'")
    user = ctx.var.get("maven_user", "''")
    password = ctx.var.get("maven_password", "''")

    javadocs_short_path = ctx.file.javadocs.short_path if ctx.file.javadocs else "''"

    upload_coordinates = ctx.attr.coordinates
    if ctx.var.get("pom_version", "") != "":
        upload_coordinates = upload_coordinates.replace("{pom_version}", ctx.var.get("pom_version"))

    ctx.actions.write(
        output = executable,
        is_executable = True,
        content = _TEMPLATE.format(
            uploader = ctx.executable._uploader.short_path,
            coordinates = upload_coordinates,
            gpg_sign = gpg_sign,
            maven_repo = maven_repo,
            password = password,
            user = user,
            pom = ctx.file.pom.short_path,
            artifact_jar = ctx.file.artifact_jar.short_path,
            source_jar = ctx.file.source_jar.short_path,
            javadoc = javadocs_short_path,
        ),
    )

    files = [
        ctx.file.artifact_jar,
        ctx.file.pom,
        ctx.file.source_jar,
    ]
    if ctx.file.javadocs:
        files.append(ctx.file.javadocs)

    return [
        DefaultInfo(
            files = depset([executable]),
            executable = executable,
            runfiles = ctx.runfiles(
                files = files,
                collect_data = True,
            ).merge(ctx.attr._uploader[DefaultInfo].data_runfiles),
        ),
        MavenPublishInfo(
            coordinates = ctx.attr.coordinates,
            artifact_jar = ctx.file.artifact_jar,
            javadocs = ctx.file.javadocs,
            source_jar = ctx.file.source_jar,
            pom = ctx.file.pom,
        ),
    ]

maven_publish = rule(
    _maven_publish_impl,
    doc = """Publish artifacts to a maven repository.

The maven repository may accessed locally using a `file://` URL, or
remotely using an `https://` URL. The following flags may be set
using `--define`:

  gpg_sign: Whether to sign artifacts using GPG
  maven_repo: A URL for the repo to use. May be "https" or "file".
  maven_user: The user name to use when uploading to the maven repository.
  maven_password: The password to use when uploading to the maven repository.

When signing with GPG, the current default key is used.
""",
    executable = True,
    attrs = {
        "coordinates": attr.string(
            mandatory = True,
        ),
        "pom": attr.label(
            mandatory = True,
            allow_single_file = True,
        ),
        "javadocs": attr.label(
            allow_single_file = True,
        ),
        "artifact_jar": attr.label(
            mandatory = True,
            allow_single_file = True,
        ),
        "source_jar": attr.label(
            mandatory = True,
            allow_single_file = True,
        ),
        "_uploader": attr.label(
            executable = True,
            cfg = "exec",
            default = "//tools/maven_publish/java/rules/jvm/external/maven:MavenPublisher",
            allow_files = True,
        ),
    },
)
