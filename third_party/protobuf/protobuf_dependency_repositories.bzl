load("@com_google_protobuf//:protobuf_deps.bzl", "PROTOBUF_MAVEN_ARTIFACTS", "protobuf_deps")
load("@rules_jvm_external//:defs.bzl", "maven_install")
load("//third_party/java:java_repositories.bzl", "REPOSITORIES")

def protobuf_dependency_repositories():
    protobuf_deps()

    maven_install(
        name = "protobuf_maven",
        artifacts = PROTOBUF_MAVEN_ARTIFACTS,
        maven_install_json = "@com_google_protobuf//:maven_install.json",
        repositories = [
            "https://repo1.maven.org/maven2",
            "https://repo.maven.apache.org/maven2",
        ],
    )
