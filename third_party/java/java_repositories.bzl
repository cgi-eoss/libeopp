load("@bazel_tools//tools/build_defs/repo:jvm.bzl", "jvm_maven_import_external")

def _replace_dependencies(dependencies, replacements):
    new_dependencies = depset()
    for dep in dependencies:
        if dep in replacements.keys():
            new_dependencies = depset(transitive = [new_dependencies, depset(direct = replacements.get(dep))])
        else:
            new_dependencies = depset(transitive = [new_dependencies, depset(direct = [dep])])
    return new_dependencies.to_list()

def java_repositories(
        omit_com_google_auto_value_auto_value_annotations = False,
        omit_com_google_code_findbugs_jsr305 = False,
        omit_com_google_errorprone_error_prone_annotations = False,
        omit_com_google_guava_failureaccess = False,
        omit_com_google_guava_guava = False,
        omit_com_google_guava_listenablefuture = False,
        omit_com_google_j2objc_j2objc_annotations = False,
        omit_com_google_protobuf_protobuf_java = False,
        omit_com_google_truth_extensions_truth_java8_extension = False,
        omit_com_google_truth_extensions_truth_liteproto_extension = False,
        omit_com_google_truth_extensions_truth_proto_extension = False,
        omit_com_google_truth_truth = False,
        omit_com_googlecode_java_diff_utils_diffutils = False,
        omit_junit_junit = False,
        omit_org_checkerframework_checker_compat_qual = False,
        omit_org_checkerframework_checker_qual = False,
        omit_org_codehaus_mojo_animal_sniffer_annotations = False,
        omit_org_hamcrest_hamcrest_core = False,
        fetch_sources = False,
        replacements = {}):
    if not omit_com_google_auto_value_auto_value_annotations:
        com_google_auto_value_auto_value_annotations(fetch_sources, replacements)
    if not omit_com_google_code_findbugs_jsr305:
        com_google_code_findbugs_jsr305(fetch_sources, replacements)
    if not omit_com_google_errorprone_error_prone_annotations:
        com_google_errorprone_error_prone_annotations(fetch_sources, replacements)
    if not omit_com_google_guava_failureaccess:
        com_google_guava_failureaccess(fetch_sources, replacements)
    if not omit_com_google_guava_guava:
        com_google_guava_guava(fetch_sources, replacements)
    if not omit_com_google_guava_listenablefuture:
        com_google_guava_listenablefuture(fetch_sources, replacements)
    if not omit_com_google_j2objc_j2objc_annotations:
        com_google_j2objc_j2objc_annotations(fetch_sources, replacements)
    if not omit_com_google_protobuf_protobuf_java:
        com_google_protobuf_protobuf_java(fetch_sources, replacements)
    if not omit_com_google_truth_extensions_truth_java8_extension:
        com_google_truth_extensions_truth_java8_extension(fetch_sources, replacements)
    if not omit_com_google_truth_extensions_truth_liteproto_extension:
        com_google_truth_extensions_truth_liteproto_extension(fetch_sources, replacements)
    if not omit_com_google_truth_extensions_truth_proto_extension:
        com_google_truth_extensions_truth_proto_extension(fetch_sources, replacements)
    if not omit_com_google_truth_truth:
        com_google_truth_truth(fetch_sources, replacements)
    if not omit_com_googlecode_java_diff_utils_diffutils:
        com_googlecode_java_diff_utils_diffutils(fetch_sources, replacements)
    if not omit_junit_junit:
        junit_junit(fetch_sources, replacements)
    if not omit_org_checkerframework_checker_compat_qual:
        org_checkerframework_checker_compat_qual(fetch_sources, replacements)
    if not omit_org_checkerframework_checker_qual:
        org_checkerframework_checker_qual(fetch_sources, replacements)
    if not omit_org_codehaus_mojo_animal_sniffer_annotations:
        org_codehaus_mojo_animal_sniffer_annotations(fetch_sources, replacements)
    if not omit_org_hamcrest_hamcrest_core:
        org_hamcrest_hamcrest_core(fetch_sources, replacements)

def com_google_auto_value_auto_value_annotations(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "com_google_auto_value_auto_value_annotations",
        artifact = "com.google.auto.value:auto-value-annotations:1.6.3",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "0e951fee8c31f60270bc46553a8586001b7b93dbb12aec06373aa99a150392c0",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        runtime_deps = _replace_dependencies([
        ], replacements),
    )

def com_google_code_findbugs_jsr305(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "com_google_code_findbugs_jsr305",
        artifact = "com.google.code.findbugs:jsr305:3.0.2",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "766ad2a0783f2687962c8ad74ceecc38a28b9f72a2d085ee438b7813e928d0c7",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        runtime_deps = _replace_dependencies([
        ], replacements),
    )

def com_google_errorprone_error_prone_annotations(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "com_google_errorprone_error_prone_annotations",
        artifact = "com.google.errorprone:error_prone_annotations:2.3.1",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "10a5949aa0f95c8de4fd47edfe20534d2acefd8c224f8afea1f607e112816120",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        runtime_deps = _replace_dependencies([
        ], replacements),
    )

def com_google_guava_failureaccess(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "com_google_guava_failureaccess",
        artifact = "com.google.guava:failureaccess:1.0.1",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "a171ee4c734dd2da837e4b16be9df4661afab72a41adaf31eb84dfdaf936ca26",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        runtime_deps = _replace_dependencies([
        ], replacements),
    )

def com_google_guava_guava(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "com_google_guava_guava",
        artifact = "com.google.guava:guava:27.1-jre",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "4a5aa70cc968a4d137e599ad37553e5cfeed2265e8c193476d7119036c536fe7",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        runtime_deps = _replace_dependencies([
            "@com_google_guava_failureaccess",
            "@com_google_guava_listenablefuture",
            "@com_google_code_findbugs_jsr305",
            "@org_checkerframework_checker_qual",
            "@com_google_errorprone_error_prone_annotations",
            "@com_google_j2objc_j2objc_annotations",
            "@org_codehaus_mojo_animal_sniffer_annotations",
        ], replacements),
    )

def com_google_guava_listenablefuture(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "com_google_guava_listenablefuture",
        artifact = "com.google.guava:listenablefuture:9999.0-empty-to-avoid-conflict-with-guava",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "b372a037d4230aa57fbeffdef30fd6123f9c0c2db85d0aced00c91b974f33f99",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        runtime_deps = _replace_dependencies([
        ], replacements),
    )

def com_google_j2objc_j2objc_annotations(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "com_google_j2objc_j2objc_annotations",
        artifact = "com.google.j2objc:j2objc-annotations:1.1",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "40ceb7157feb263949e0f503fe5f71689333a621021aa20ce0d0acee3badaa0f",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        runtime_deps = _replace_dependencies([
        ], replacements),
    )

def com_google_protobuf_protobuf_java(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "com_google_protobuf_protobuf_java",
        artifact = "com.google.protobuf:protobuf-java:3.6.1",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "fb66d913ff0578553b2e28a3338cbbbe2657e6cfe0e98d939f23aea219daf508",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        runtime_deps = _replace_dependencies([
        ], replacements),
    )

def com_google_truth_extensions_truth_java8_extension(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "com_google_truth_extensions_truth_java8_extension",
        artifact = "com.google.truth.extensions:truth-java8-extension:0.43",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "a409f4af222722fbf8fd7ec012a276e5f4ba2e1aabfd55e2521fd075a755fd44",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        runtime_deps = _replace_dependencies([
            "@com_google_truth_truth",
            "@org_checkerframework_checker_compat_qual",
        ], replacements),
    )

def com_google_truth_extensions_truth_liteproto_extension(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "com_google_truth_extensions_truth_liteproto_extension",
        artifact = "com.google.truth.extensions:truth-liteproto-extension:0.43",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "30cc23160edb203e82cccfc429839f056fb79c426b9e0234575f929561cc8e71",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        runtime_deps = _replace_dependencies([
            "@com_google_truth_truth",
            "@com_google_guava_guava",
            "@com_google_errorprone_error_prone_annotations",
            "@org_checkerframework_checker_compat_qual",
            "@com_google_auto_value_auto_value_annotations",
        ], replacements),
    )

def com_google_truth_extensions_truth_proto_extension(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "com_google_truth_extensions_truth_proto_extension",
        artifact = "com.google.truth.extensions:truth-proto-extension:0.43",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "3e14232cdaf089e6e035fa34d282c0f0a0c2e0b7a8e7373d21b034a1b4569cdf",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        runtime_deps = _replace_dependencies([
            "@com_google_truth_extensions_truth_liteproto_extension",
            "@com_google_truth_truth",
            "@com_google_guava_guava",
            "@com_google_errorprone_error_prone_annotations",
            "@org_checkerframework_checker_compat_qual",
            "@com_google_auto_value_auto_value_annotations",
            "@com_google_protobuf_protobuf_java",
        ], replacements),
    )

def com_google_truth_truth(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "com_google_truth_truth",
        artifact = "com.google.truth:truth:0.43",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "dd91316ff74e21e05d6524d45e40f170fe0bdf340cb8fb917bce7ad466626423",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        runtime_deps = _replace_dependencies([
            "@com_google_guava_guava",
            "@junit_junit",
            "@com_google_errorprone_error_prone_annotations",
            "@org_checkerframework_checker_compat_qual",
            "@com_google_auto_value_auto_value_annotations",
            "@com_googlecode_java_diff_utils_diffutils",
        ], replacements),
    )

def com_googlecode_java_diff_utils_diffutils(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "com_googlecode_java_diff_utils_diffutils",
        artifact = "com.googlecode.java-diff-utils:diffutils:1.3.0",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "61ba4dc49adca95243beaa0569adc2a23aedb5292ae78aa01186fa782ebdc5c2",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        runtime_deps = _replace_dependencies([
        ], replacements),
    )

def junit_junit(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "junit_junit",
        artifact = "junit:junit:4.12",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "59721f0805e223d84b90677887d9ff567dc534d7c502ca903c0c2b17f05c116a",
        licenses = ["reciprocal"],
        fetch_sources = fetch_sources,
        runtime_deps = _replace_dependencies([
            "@org_hamcrest_hamcrest_core",
        ], replacements),
    )

def org_checkerframework_checker_compat_qual(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "org_checkerframework_checker_compat_qual",
        artifact = "org.checkerframework:checker-compat-qual:2.5.5",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "11d134b245e9cacc474514d2d66b5b8618f8039a1465cdc55bbc0b34e0008b7a",
        licenses = ["restricted"],
        fetch_sources = fetch_sources,
        runtime_deps = _replace_dependencies([
        ], replacements),
    )

def org_checkerframework_checker_qual(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "org_checkerframework_checker_qual",
        artifact = "org.checkerframework:checker-qual:2.5.2",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "64b02691c8b9d4e7700f8ee2e742dce7ea2c6e81e662b7522c9ee3bf568c040a",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        runtime_deps = _replace_dependencies([
        ], replacements),
    )

def org_codehaus_mojo_animal_sniffer_annotations(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "org_codehaus_mojo_animal_sniffer_annotations",
        artifact = "org.codehaus.mojo:animal-sniffer-annotations:1.17",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "92654f493ecfec52082e76354f0ebf87648dc3d5cec2e3c3cdb947c016747a53",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        runtime_deps = _replace_dependencies([
        ], replacements),
    )

def org_hamcrest_hamcrest_core(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "org_hamcrest_hamcrest_core",
        artifact = "org.hamcrest:hamcrest-core:1.3",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "66fdef91e9739348df7a096aa384a5685f4e875584cce89386a7a47251c4d8e9",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        runtime_deps = _replace_dependencies([
        ], replacements),
    )
