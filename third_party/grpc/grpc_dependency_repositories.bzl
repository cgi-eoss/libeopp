def grpc_dependency_repositories():
    # exported by @io_grpc_grpc_java//:repositories.bzl#grpc_java_repositories
    native.bind(
        name = "guava",
        actual = "@maven//:com_google_guava_guava",
    )
    native.bind(
        name = "gson",
        actual = "@maven//:com_google_code_gson_gson",
    )
    native.bind(
        name = "error_prone_annotations",
        actual = "@maven//:com_google_errorprone_error_prone_annotations",
    )
