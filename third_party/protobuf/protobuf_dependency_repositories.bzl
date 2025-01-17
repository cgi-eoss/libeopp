load("@com_google_protobuf//:protobuf_deps.bzl", "PROTOBUF_MAVEN_ARTIFACTS", "protobuf_deps")
load("@rules_jvm_external//:defs.bzl", "maven_install")
load("@rules_jvm_external//:specs.bzl", "maven")
load("//third_party/java:java_repositories.bzl", "ARTIFACTS", "REPOSITORIES")

def protobuf_dependency_repositories(protobuf_maven_overrides = ARTIFACTS):
    """
    Load protobuf Bazel dependencies. Allow overriding Maven dependency versions with matching group & artifact.
    """

    protobuf_deps()

    # Assume PROTOBUF_MAVEN_ARTIFACTS contains simple group:artifact:version strings
    overridden_protobuf_maven_artifacts = []
    for original in PROTOBUF_MAVEN_ARTIFACTS:
        coords = original.split(":")
        protobuf_dep = maven.artifact(coords[0], coords[1], coords[2])

        found_override = None
        for override in protobuf_maven_overrides:
            if override["group"] == protobuf_dep["group"] and override["artifact"] == protobuf_dep["artifact"]:
                found_override = override
                break
        if found_override:
            overridden_protobuf_maven_artifacts.append(found_override)
        else:
            overridden_protobuf_maven_artifacts.append(protobuf_dep)

    maven_install(
        name = "protobuf_maven",
        artifacts = overridden_protobuf_maven_artifacts,
        maven_install_json = "@//:protobuf_maven_install.json",
        repositories = REPOSITORIES,
    )
