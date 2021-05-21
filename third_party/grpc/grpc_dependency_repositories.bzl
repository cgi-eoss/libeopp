load("@com_salesforce_servicelibs_reactive_grpc//bazel:repositories.bzl", reactive_grpc_repositories = "repositories")

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

    reactive_grpc_repositories(
        omit_org_reactivestreams_reactive_streams = True,
        omit_io_projectreactor_reactor_core = True,
        omit_io_reactivex_rxjava2_rxjava = True,
        omit_io_grpc_grpc_java = True,
        omit_com_salesforce_servicelibs_jprotoc = True,
        omit_com_github_spullara_mustache_java_compiler = False,
    )
