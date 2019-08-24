load("//tools:maven.bzl", "maven_library", "sonarqube_project")

maven_library(
    name = "resource",
    srcs = [
        "src/main/java/com/cgi/eoss/eopp/resource/EoppResource.java",
        "src/main/java/com/cgi/eoss/eopp/resource/WritableEoppResource.java",
    ],
    artifact_name = "libeopp :: resource",
    generate_sonarqube_project = False,
    deps = [
        "//file",
        "@org_springframework_spring_core",
    ],
)

maven_library(
    name = "path_resource",
    srcs = ["src/main/java/com/cgi/eoss/eopp/resource/EoppPathResource.java"],
    artifact_name = "libeopp :: resource :: path",
    generate_sonarqube_project = False,
    deps = [
        ":resource",
        "//file",
        "@org_springframework_spring_core",
    ],
)

sonarqube_project(
    name = "resource",
    srcs = glob(["src/main/**"]),
    artifact_name = "libeopp :: resource",
)
