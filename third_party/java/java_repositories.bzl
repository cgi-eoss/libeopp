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
        omit_com_google_api_grpc_proto_google_common_protos = False,
        omit_com_google_auto_value_auto_value_annotations = False,
        omit_com_google_code_findbugs_jsr305 = False,
        omit_com_google_code_gson_gson = False,
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
        omit_io_grpc_grpc_context = False,
        omit_io_grpc_grpc_core = False,
        omit_io_grpc_grpc_stub = False,
        omit_io_opencensus_opencensus_api = False,
        omit_io_opencensus_opencensus_contrib_grpc_metrics = False,
        omit_javax_annotation_javax_annotation_api = False,
        omit_junit_junit = False,
        omit_net_bytebuddy_byte_buddy = False,
        omit_net_bytebuddy_byte_buddy_agent = False,
        omit_org_checkerframework_checker_compat_qual = False,
        omit_org_checkerframework_checker_qual = False,
        omit_org_codehaus_mojo_animal_sniffer_annotations = False,
        omit_org_hamcrest_hamcrest_core = False,
        omit_org_mockito_mockito_core = False,
        omit_org_objenesis_objenesis = False,
        omit_org_slf4j_slf4j_api = False,
        omit_org_springframework_cloud_spring_cloud_commons = False,
        omit_org_springframework_security_spring_security_crypto = False,
        omit_org_springframework_spring_core = False,
        omit_org_springframework_spring_jcl = False,
        fetch_sources = False,
        replacements = {}):
    if not omit_com_google_api_grpc_proto_google_common_protos:
        com_google_api_grpc_proto_google_common_protos(fetch_sources, replacements)
    if not omit_com_google_auto_value_auto_value_annotations:
        com_google_auto_value_auto_value_annotations(fetch_sources, replacements)
    if not omit_com_google_code_findbugs_jsr305:
        com_google_code_findbugs_jsr305(fetch_sources, replacements)
    if not omit_com_google_code_gson_gson:
        com_google_code_gson_gson(fetch_sources, replacements)
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
    if not omit_io_grpc_grpc_context:
        io_grpc_grpc_context(fetch_sources, replacements)
    if not omit_io_grpc_grpc_core:
        io_grpc_grpc_core(fetch_sources, replacements)
    if not omit_io_grpc_grpc_stub:
        io_grpc_grpc_stub(fetch_sources, replacements)
    if not omit_io_opencensus_opencensus_api:
        io_opencensus_opencensus_api(fetch_sources, replacements)
    if not omit_io_opencensus_opencensus_contrib_grpc_metrics:
        io_opencensus_opencensus_contrib_grpc_metrics(fetch_sources, replacements)
    if not omit_javax_annotation_javax_annotation_api:
        javax_annotation_javax_annotation_api(fetch_sources, replacements)
    if not omit_junit_junit:
        junit_junit(fetch_sources, replacements)
    if not omit_net_bytebuddy_byte_buddy:
        net_bytebuddy_byte_buddy(fetch_sources, replacements)
    if not omit_net_bytebuddy_byte_buddy_agent:
        net_bytebuddy_byte_buddy_agent(fetch_sources, replacements)
    if not omit_org_checkerframework_checker_compat_qual:
        org_checkerframework_checker_compat_qual(fetch_sources, replacements)
    if not omit_org_checkerframework_checker_qual:
        org_checkerframework_checker_qual(fetch_sources, replacements)
    if not omit_org_codehaus_mojo_animal_sniffer_annotations:
        org_codehaus_mojo_animal_sniffer_annotations(fetch_sources, replacements)
    if not omit_org_hamcrest_hamcrest_core:
        org_hamcrest_hamcrest_core(fetch_sources, replacements)
    if not omit_org_mockito_mockito_core:
        org_mockito_mockito_core(fetch_sources, replacements)
    if not omit_org_objenesis_objenesis:
        org_objenesis_objenesis(fetch_sources, replacements)
    if not omit_org_slf4j_slf4j_api:
        org_slf4j_slf4j_api(fetch_sources, replacements)
    if not omit_org_springframework_cloud_spring_cloud_commons:
        org_springframework_cloud_spring_cloud_commons(fetch_sources, replacements)
    if not omit_org_springframework_security_spring_security_crypto:
        org_springframework_security_spring_security_crypto(fetch_sources, replacements)
    if not omit_org_springframework_spring_core:
        org_springframework_spring_core(fetch_sources, replacements)
    if not omit_org_springframework_spring_jcl:
        org_springframework_spring_jcl(fetch_sources, replacements)

def com_google_api_grpc_proto_google_common_protos(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "com_google_api_grpc_proto_google_common_protos",
        artifact = "com.google.api.grpc:proto-google-common-protos:1.15.0",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "aa1dc41c8252f94f0818343e7afec13a1559918c8e167f9e1863fe814679c40e",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        runtime_deps = _replace_dependencies([
            "@com_google_protobuf_protobuf_java",
        ], replacements),
    )

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

def com_google_code_gson_gson(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "com_google_code_gson_gson",
        artifact = "com.google.code.gson:gson:2.8.5",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "233a0149fc365c9f6edbd683cfe266b19bdc773be98eabdaf6b3c924b48e7d81",
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
            "@com_google_protobuf_protobuf_java",
            "@com_google_errorprone_error_prone_annotations",
            "@org_checkerframework_checker_compat_qual",
            "@com_google_auto_value_auto_value_annotations",
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

def io_grpc_grpc_context(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "io_grpc_grpc_context",
        artifact = "io.grpc:grpc-context:1.19.0",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "8f4df8618c500f3c1fdf88b755caeb14fe2846ea59a9e762f614852178b64318",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        runtime_deps = _replace_dependencies([
        ], replacements),
    )

def io_grpc_grpc_core(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "io_grpc_grpc_core",
        artifact = "io.grpc:grpc-core:1.19.0",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "3cfaae2db268e4da2609079cecade8434afcb7ab23a126a57d870b722b2b6ab9",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        runtime_deps = _replace_dependencies([
            "@com_google_guava_guava",
            "@com_google_code_findbugs_jsr305",
            "@com_google_errorprone_error_prone_annotations",
            "@org_codehaus_mojo_animal_sniffer_annotations",
            "@io_grpc_grpc_context",
            "@com_google_code_gson_gson",
            "@io_opencensus_opencensus_contrib_grpc_metrics",
            "@io_opencensus_opencensus_api",
        ], replacements),
    )

def io_grpc_grpc_stub(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "io_grpc_grpc_stub",
        artifact = "io.grpc:grpc-stub:1.19.0",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "711dad5734b4e8602a271cb383eda504d6d1bf5385ced045a0ca91176ae73821",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        runtime_deps = _replace_dependencies([
            "@io_grpc_grpc_core",
        ], replacements),
    )

def io_opencensus_opencensus_api(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "io_opencensus_opencensus_api",
        artifact = "io.opencensus:opencensus-api:0.19.2",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "0e2e5d3f4f6fd296017a00b1cd8fb8e4261331cc0c3b6818c0533b01bf7945dc",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        runtime_deps = _replace_dependencies([
        ], replacements),
    )

def io_opencensus_opencensus_contrib_grpc_metrics(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "io_opencensus_opencensus_contrib_grpc_metrics",
        artifact = "io.opencensus:opencensus-contrib-grpc-metrics:0.19.2",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "0e23c03414612c7fbef1fdb347076eb69368e596de768cd4b98e081d92206f15",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        runtime_deps = _replace_dependencies([
            "@io_opencensus_opencensus_api",
        ], replacements),
    )

def javax_annotation_javax_annotation_api(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "javax_annotation_javax_annotation_api",
        artifact = "javax.annotation:javax.annotation-api:1.3.2",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "e04ba5195bcd555dc95650f7cc614d151e4bcd52d29a10b8aa2197f3ab89ab9b",
        licenses = ["restricted"],
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

def net_bytebuddy_byte_buddy(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "net_bytebuddy_byte_buddy",
        artifact = "net.bytebuddy:byte-buddy:1.9.10",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "2936debc4d7b6c534848d361412e2d0f8bd06f7f27a6f4e728a20e97648d2bf3",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        runtime_deps = _replace_dependencies([
        ], replacements),
    )

def net_bytebuddy_byte_buddy_agent(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "net_bytebuddy_byte_buddy_agent",
        artifact = "net.bytebuddy:byte-buddy-agent:1.9.10",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "8ed739d29132103250d307d2e8e3c95f07588ef0543ab11d2881d00768a5e182",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        runtime_deps = _replace_dependencies([
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

def org_mockito_mockito_core(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "org_mockito_mockito_core",
        artifact = "org.mockito:mockito-core:2.23.4",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "d77e018b6bc211d78ddcec54bc508732c4677b9a9eb9103793be85441b20bc5d",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        runtime_deps = _replace_dependencies([
            "@net_bytebuddy_byte_buddy",
            "@net_bytebuddy_byte_buddy_agent",
            "@org_objenesis_objenesis",
        ], replacements),
    )

def org_objenesis_objenesis(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "org_objenesis_objenesis",
        artifact = "org.objenesis:objenesis:2.6",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "5e168368fbc250af3c79aa5fef0c3467a2d64e5a7bd74005f25d8399aeb0708d",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        runtime_deps = _replace_dependencies([
        ], replacements),
    )

def org_slf4j_slf4j_api(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "org_slf4j_slf4j_api",
        artifact = "org.slf4j:slf4j-api:1.7.25",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "18c4a0095d5c1da6b817592e767bb23d29dd2f560ad74df75ff3961dbde25b79",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        runtime_deps = _replace_dependencies([
        ], replacements),
    )

def org_springframework_cloud_spring_cloud_commons(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "org_springframework_cloud_spring_cloud_commons",
        artifact = "org.springframework.cloud:spring-cloud-commons:2.1.1.RELEASE",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "2ff496d899534f9f00ba83f2c385c9e8bf7c7b78af88475e5abe7e39fe5bebc4",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        runtime_deps = _replace_dependencies([
            "@org_springframework_security_spring_security_crypto",
        ], replacements),
    )

def org_springframework_security_spring_security_crypto(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "org_springframework_security_spring_security_crypto",
        artifact = "org.springframework.security:spring-security-crypto:5.1.4.RELEASE",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "27090c26af3da57b3c49332044b971f4b458d4aedb3536b6333df44d97eb5576",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        runtime_deps = _replace_dependencies([
        ], replacements),
    )

def org_springframework_spring_core(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "org_springframework_spring_core",
        artifact = "org.springframework:spring-core:5.1.5.RELEASE",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "f771b605019eb9d2cf8f60c25c050233e39487ff54d74c93d687ea8de8b7285a",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        runtime_deps = _replace_dependencies([
            "@org_springframework_spring_jcl",
        ], replacements),
    )

def org_springframework_spring_jcl(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "org_springframework_spring_jcl",
        artifact = "org.springframework:spring-jcl:5.1.5.RELEASE",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "194423c7c485e40706d74c01a36b1f0aad436f0b55958f06ac95bcbac65bf4e6",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        runtime_deps = _replace_dependencies([
        ], replacements),
    )
