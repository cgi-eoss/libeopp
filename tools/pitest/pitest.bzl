load("@aspect_bazel_lib//lib:tar.bzl", "mtree_mutate", "mtree_spec", "tar")
load("@rules_java//java:defs.bzl", "java_test")

def _quote(filename, protect = "="):
    """Quote the filename, by escaping = by \\= and \\ by \\\\"""
    return filename.replace("\\", "\\\\").replace(protect, "\\" + protect)

def _java_transitive_runtime_dependencies_impl(ctx):
    transitive_dependency_depsets = []

    for target in ctx.attr.targets:
        transitive_dependency_depsets.append(target[JavaInfo].compilation_info.runtime_classpath)
        transitive_dependency_depsets.append(target[JavaInfo].transitive_runtime_jars)

    return [
        DefaultInfo(files = depset(transitive = transitive_dependency_depsets)),
    ]

_java_transitive_runtime_dependencies = rule(
    attrs = {
        "targets": attr.label_list(
            providers = [[JavaInfo]],
            allow_files = False,
        ),
    },
    implementation = _java_transitive_runtime_dependencies_impl,
)

def pitest_mutation_coverage_test(
        name,
        target_classes,
        target_tests,
        srcs = [],
        libraries = [],
        test_srcs = [],
        test_targets = [],
        data = [],
        tags = []):
    mtree_spec(
        name = "_%s_srcs_mtree_in" % name,
        srcs = srcs,
        testonly = True,
        tags = ["manual"],
    )

    mtree_mutate(
        name = "_%s_srcs_mtree" % name,
        mtree = "_%s_srcs_mtree_in" % name,
        package_dir = "srcs",
        testonly = True,
        tags = ["manual"],
    )

    mtree_spec(
        name = "_%s_test_srcs_mtree_in" % name,
        srcs = test_srcs,
        testonly = True,
        tags = ["manual"],
    )

    mtree_mutate(
        name = "_%s_test_srcs_mtree" % name,
        mtree = "_%s_test_srcs_mtree_in" % name,
        package_dir = "test_srcs",
        testonly = True,
        tags = ["manual"],
    )

    mtree_spec(
        name = "_%s_libraries_mtree_in" % name,
        srcs = libraries,
        testonly = True,
        tags = ["manual"],
    )

    mtree_mutate(
        name = "_%s_libraries_mtree" % name,
        mtree = "_%s_libraries_mtree_in" % name,
        package_dir = "libraries",
        testonly = True,
        tags = ["manual"],
    )

    mtree_spec(
        name = "_%s_test_libraries_mtree_in" % name,
        srcs = test_targets,
        testonly = True,
        include_runfiles = False,
        tags = ["manual"],
    )

    mtree_mutate(
        name = "_%s_test_libraries_mtree" % name,
        mtree = "_%s_test_libraries_mtree_in" % name,
        package_dir = "test_libraries",
        testonly = True,
        tags = ["manual"],
    )

    _java_transitive_runtime_dependencies(
        name = "_%s_data_test_dependencies_files" % name,
        targets = test_targets,
        testonly = True,
        tags = ["manual"],
    )

    mtree_spec(
        name = "_%s_test_dependencies_mtree_in" % name,
        srcs = ["_%s_data_test_dependencies_files" % name],
        testonly = True,
        include_runfiles = False,
        tags = ["manual"],
    )

    mtree_mutate(
        name = "_%s_test_dependencies_mtree" % name,
        mtree = "_%s_test_dependencies_mtree_in" % name,
        package_dir = "test_dependencies",
        testonly = True,
        tags = ["manual"],
    )

    native.genrule(
        name = "_%s_data_mtree" % name,
        srcs = [
            ":_%s_srcs_mtree" % name,
            ":_%s_test_srcs_mtree" % name,
            ":_%s_libraries_mtree" % name,
            ":_%s_test_libraries_mtree" % name,
            ":_%s_test_dependencies_mtree" % name,
        ],
        outs = ["_%s_data_mtree.mtree" % name],
        cmd = "cat $(SRCS) | sort >$@",
        testonly = True,
        tags = ["manual"],
    )

    #TODO rewrite pitest to take arguments to the files directly, rather than untarring this
    tar(
        name = "_%s_data2" % name,
        srcs = srcs + test_srcs + libraries + test_targets,
        mtree = ":_%s_data_mtree" % name,
        testonly = True,
        tags = ["manual"],
    )

    java_test(
        name = name,
        test_class = "com.cgi.eoss.eopp.testing.pitest.PitestCoverageTest",
        runtime_deps = ["//tools/pitest:pitest_coverage_test"],
        visibility = ["//visibility:public"],
        data = [":_%s_data2" % name] + data,
        jvm_flags = [
            "-Dlibeopp.pitest.data=$(location :_%s_data2)" % name,
            "-Dlibeopp.pitest.targetClasses=%s" % target_classes,
            "-Dlibeopp.pitest.targetTests=%s" % target_tests,
        ],
        tags = tags,
    )
