load("@rules_java//java:defs.bzl", "java_test")
load("@rules_pkg//:pkg.bzl", "pkg_zip")
load("@rules_pkg//:mappings.bzl", "pkg_filegroup", "pkg_files", "strip_prefix")

def _quote(filename, protect = "="):
    """Quote the filename, by escaping = by \\= and \\ by \\\\"""
    return filename.replace("\\", "\\\\").replace(protect, "\\" + protect)

def _java_transitive_runtime_dependencies_impl(ctx):
    transitive_dependency_files = []

    for target in ctx.attr.targets:
        _deps = depset([], transitive = [
            target[JavaInfo].compilation_info.runtime_classpath,
            target[JavaInfo].transitive_runtime_deps,
        ])
        for dep in _deps.to_list():
            if dep not in target.files.to_list():
                transitive_dependency_files.extend([dep])

    return [
        DefaultInfo(files = depset(transitive_dependency_files)),
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
    pkg_files(
        name = "_%s_data_srcs" % name,
        prefix = "srcs",
        strip_prefix = strip_prefix.from_root(),
        srcs = srcs,
        testonly = True,
        tags = ["manual"],
    )

    pkg_files(
        name = "_%s_data_test_srcs" % name,
        prefix = "test_srcs",
        strip_prefix = strip_prefix.from_root(),
        srcs = test_srcs,
        testonly = True,
        tags = ["manual"],
    )

    pkg_files(
        name = "_%s_data_libraries" % name,
        prefix = "libraries",
        strip_prefix = strip_prefix.from_root(),
        srcs = libraries,
        testonly = True,
        tags = ["manual"],
    )

    pkg_files(
        name = "_%s_data_test_libraries" % name,
        prefix = "test_libraries",
        strip_prefix = strip_prefix.from_root(),
        srcs = test_targets,
        testonly = True,
        tags = ["manual"],
    )

    _java_transitive_runtime_dependencies(
        name = "_%s_data_test_dependencies_files" % name,
        targets = test_targets,
        testonly = True,
        tags = ["manual"],
    )

    pkg_files(
        name = "_%s_data_test_dependencies" % name,
        prefix = "test_dependencies",
        strip_prefix = strip_prefix.from_root(),
        srcs = [":_%s_data_test_dependencies_files" % name],
        testonly = True,
        tags = ["manual"],
    )

    pkg_filegroup(
        name = "_%s_data_filegroup" % name,
        srcs = [
            "_%s_data_srcs" % name,
            "_%s_data_test_srcs" % name,
            "_%s_data_libraries" % name,
            "_%s_data_test_libraries" % name,
            "_%s_data_test_dependencies" % name,
        ],
        testonly = True,
        tags = ["manual"],
    )

    pkg_zip(
        name = "_%s_data" % name,
        srcs = ["_%s_data_filegroup" % name],
        testonly = True,
        tags = ["manual"],
    )

    java_test(
        name = name,
        test_class = "com.cgi.eoss.eopp.testing.pitest.PitestCoverageTest",
        runtime_deps = ["//tools/pitest:pitest_coverage_test"],
        visibility = ["//visibility:public"],
        data = [":_%s_data.zip" % name] + data,
        jvm_flags = [
            "-Dlibeopp.pitest.data=$(location :_%s_data.zip)" % name,
            "-Dlibeopp.pitest.targetClasses=%s" % target_classes,
            "-Dlibeopp.pitest.targetTests=%s" % target_tests,
        ],
        tags = tags,
    )

    native.filegroup(
        name = "_%s_pitest_report_support_files" % name,
        srcs = srcs,
        tags = ["manual"],
        visibility = ["//visibility:public"],
    )
