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
        omit_com_google_android_annotations = False,
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
        omit_com_google_protobuf_protobuf_java_util = False,
        omit_com_google_protobuf_protobuf_lite = False,
        omit_com_google_truth_extensions_truth_java8_extension = False,
        omit_com_google_truth_extensions_truth_liteproto_extension = False,
        omit_com_google_truth_extensions_truth_proto_extension = False,
        omit_com_google_truth_truth = False,
        omit_com_googlecode_java_diff_utils_diffutils = False,
        omit_io_grpc_grpc_api = False,
        omit_io_grpc_grpc_context = False,
        omit_io_grpc_grpc_core = False,
        omit_io_grpc_grpc_netty = False,
        omit_io_grpc_grpc_protobuf = False,
        omit_io_grpc_grpc_protobuf_lite = False,
        omit_io_grpc_grpc_services = False,
        omit_io_grpc_grpc_stub = False,
        omit_io_netty_netty_buffer = False,
        omit_io_netty_netty_codec = False,
        omit_io_netty_netty_codec_http = False,
        omit_io_netty_netty_codec_http2 = False,
        omit_io_netty_netty_codec_socks = False,
        omit_io_netty_netty_common = False,
        omit_io_netty_netty_handler = False,
        omit_io_netty_netty_handler_proxy = False,
        omit_io_netty_netty_resolver = False,
        omit_io_netty_netty_transport = False,
        omit_io_opencensus_opencensus_api = False,
        omit_io_opencensus_opencensus_contrib_grpc_metrics = False,
        omit_io_perfmark_perfmark_api = False,
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
    if not omit_com_google_android_annotations:
        com_google_android_annotations(fetch_sources, replacements)
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
    if not omit_com_google_protobuf_protobuf_java_util:
        com_google_protobuf_protobuf_java_util(fetch_sources, replacements)
    if not omit_com_google_protobuf_protobuf_lite:
        com_google_protobuf_protobuf_lite(fetch_sources, replacements)
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
    if not omit_io_grpc_grpc_api:
        io_grpc_grpc_api(fetch_sources, replacements)
    if not omit_io_grpc_grpc_context:
        io_grpc_grpc_context(fetch_sources, replacements)
    if not omit_io_grpc_grpc_core:
        io_grpc_grpc_core(fetch_sources, replacements)
    if not omit_io_grpc_grpc_netty:
        io_grpc_grpc_netty(fetch_sources, replacements)
    if not omit_io_grpc_grpc_protobuf:
        io_grpc_grpc_protobuf(fetch_sources, replacements)
    if not omit_io_grpc_grpc_protobuf_lite:
        io_grpc_grpc_protobuf_lite(fetch_sources, replacements)
    if not omit_io_grpc_grpc_services:
        io_grpc_grpc_services(fetch_sources, replacements)
    if not omit_io_grpc_grpc_stub:
        io_grpc_grpc_stub(fetch_sources, replacements)
    if not omit_io_netty_netty_buffer:
        io_netty_netty_buffer(fetch_sources, replacements)
    if not omit_io_netty_netty_codec:
        io_netty_netty_codec(fetch_sources, replacements)
    if not omit_io_netty_netty_codec_http:
        io_netty_netty_codec_http(fetch_sources, replacements)
    if not omit_io_netty_netty_codec_http2:
        io_netty_netty_codec_http2(fetch_sources, replacements)
    if not omit_io_netty_netty_codec_socks:
        io_netty_netty_codec_socks(fetch_sources, replacements)
    if not omit_io_netty_netty_common:
        io_netty_netty_common(fetch_sources, replacements)
    if not omit_io_netty_netty_handler:
        io_netty_netty_handler(fetch_sources, replacements)
    if not omit_io_netty_netty_handler_proxy:
        io_netty_netty_handler_proxy(fetch_sources, replacements)
    if not omit_io_netty_netty_resolver:
        io_netty_netty_resolver(fetch_sources, replacements)
    if not omit_io_netty_netty_transport:
        io_netty_netty_transport(fetch_sources, replacements)
    if not omit_io_opencensus_opencensus_api:
        io_opencensus_opencensus_api(fetch_sources, replacements)
    if not omit_io_opencensus_opencensus_contrib_grpc_metrics:
        io_opencensus_opencensus_contrib_grpc_metrics(fetch_sources, replacements)
    if not omit_io_perfmark_perfmark_api:
        io_perfmark_perfmark_api(fetch_sources, replacements)
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

def com_google_android_annotations(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "com_google_android_annotations",
        artifact = "com.google.android:annotations:4.1.1.4",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "ba734e1e84c09d615af6a09d33034b4f0442f8772dec120efb376d86a565ae15",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "e9b667aa958df78ea1ad115f7bbac18a5869c3128b1d5043feb360b0cfce9d40",
        exports = _replace_dependencies([
        ], replacements),
        tags = [
            "maven_coordinates=com.google.android:annotations:4.1.1.4",
        ],
    )

def com_google_api_grpc_proto_google_common_protos(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "com_google_api_grpc_proto_google_common_protos",
        artifact = "com.google.api.grpc:proto-google-common-protos:1.17.0",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "ad25472c73ee470606fb500b376ae5a97973d5406c2f5c3b7d07fb25b4648b65",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "7a0128ee3a58953ed8f30828812d9ac54c10f36494fc9eed88a5398c23c29c85",
        exports = _replace_dependencies([
            "@com_google_protobuf_protobuf_java",
        ], replacements),
        tags = [
            "maven_coordinates=com.google.api.grpc:proto-google-common-protos:1.17.0",
        ],
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
        srcjar_sha256 = "254d55ea959608a833fa07056710488c1f4978875f86f6a106b8d937b29b1170",
        exports = _replace_dependencies([
        ], replacements),
        tags = [
            "maven_coordinates=com.google.auto.value:auto-value-annotations:1.6.3",
        ],
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
        srcjar_sha256 = "1c9e85e272d0708c6a591dc74828c71603053b48cc75ae83cce56912a2aa063b",
        exports = _replace_dependencies([
        ], replacements),
        tags = [
            "maven_coordinates=com.google.code.findbugs:jsr305:3.0.2",
        ],
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
        srcjar_sha256 = "512b4bf6927f4864acc419b8c5109c23361c30ed1f5798170248d33040de068e",
        exports = _replace_dependencies([
        ], replacements),
        tags = [
            "maven_coordinates=com.google.code.gson:gson:2.8.5",
        ],
    )

def com_google_errorprone_error_prone_annotations(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "com_google_errorprone_error_prone_annotations",
        artifact = "com.google.errorprone:error_prone_annotations:2.3.3",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "ec59f1b702d9afc09e8c3929f5c42777dec623a6ea2731ac694332c7d7680f5a",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "f58446b80b5f1e98bcb74dae5c0710ed8e52baafe5a4bb315f769f306d85634a",
        exports = _replace_dependencies([
        ], replacements),
        tags = [
            "maven_coordinates=com.google.errorprone:error_prone_annotations:2.3.3",
        ],
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
        srcjar_sha256 = "092346eebbb1657b51aa7485a246bf602bb464cc0b0e2e1c7e7201fadce1e98f",
        exports = _replace_dependencies([
        ], replacements),
        tags = [
            "maven_coordinates=com.google.guava:failureaccess:1.0.1",
        ],
    )

def com_google_guava_guava(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "com_google_guava_guava",
        artifact = "com.google.guava:guava:28.1-jre",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "30beb8b8527bd07c6e747e77f1a92122c2f29d57ce347461a4a55eb26e382da4",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "914b703ff2b0ebe442732bc8ae3b5c269182f6291843a4cad3d18b20cac6c21f",
        exports = _replace_dependencies([
            "@com_google_guava_failureaccess",
            "@com_google_guava_listenablefuture",
            "@com_google_code_findbugs_jsr305",
            "@org_checkerframework_checker_qual",
            "@com_google_errorprone_error_prone_annotations",
            "@com_google_j2objc_j2objc_annotations",
            "@org_codehaus_mojo_animal_sniffer_annotations",
        ], replacements),
        tags = [
            "maven_coordinates=com.google.guava:guava:28.1-jre",
        ],
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
        fetch_sources = False,
        srcjar_sha256 = None,
        exports = _replace_dependencies([
        ], replacements),
        tags = [
            "maven_coordinates=com.google.guava:listenablefuture:9999.0-empty-to-avoid-conflict-with-guava",
        ],
    )

def com_google_j2objc_j2objc_annotations(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "com_google_j2objc_j2objc_annotations",
        artifact = "com.google.j2objc:j2objc-annotations:1.3",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "21af30c92267bd6122c0e0b4d20cccb6641a37eaf956c6540ec471d584e64a7b",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "ba4df669fec153fa4cd0ef8d02c6d3ef0702b7ac4cabe080facf3b6e490bb972",
        exports = _replace_dependencies([
        ], replacements),
        tags = [
            "maven_coordinates=com.google.j2objc:j2objc-annotations:1.3",
        ],
    )

def com_google_protobuf_protobuf_java(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "com_google_protobuf_protobuf_java",
        artifact = "com.google.protobuf:protobuf-java:3.10.0",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "161d7d61a8cb3970891c299578702fd079646e032329d6c2cabf998d191437c9",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "47012b36fcd7c4325e07a3a3b43c72e1b2d7a7d79d8e2605f2327b1e81348133",
        exports = _replace_dependencies([
        ], replacements),
        tags = [
            "maven_coordinates=com.google.protobuf:protobuf-java:3.10.0",
        ],
    )

def com_google_protobuf_protobuf_java_util(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "com_google_protobuf_protobuf_java_util",
        artifact = "com.google.protobuf:protobuf-java-util:3.10.0",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "619b0b0dc344cb141e493cbedc5687c8fb7c985e609a1b035e621bfab2f89021",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "9e875b10c3f4bc5b46b8e29b87f64debca47aa81e86fed9f3b4e68eccd5f67f8",
        exports = _replace_dependencies([
            "@com_google_protobuf_protobuf_java",
            "@com_google_code_gson_gson",
        ], replacements),
        tags = [
            "maven_coordinates=com.google.protobuf:protobuf-java-util:3.10.0",
        ],
    )

def com_google_protobuf_protobuf_lite(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "com_google_protobuf_protobuf_lite",
        artifact = "com.google.protobuf:protobuf-lite:3.0.1",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "1413393db84e4adef79b2997d9dfeb4793d8f93d196f8347808d15711f0bc69c",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "b3331d42ffeb8089878e769074e30a2468bd84a85f13ca5044c5a731c35d3997",
        exports = _replace_dependencies([
        ], replacements),
        tags = [
            "maven_coordinates=com.google.protobuf:protobuf-lite:3.0.1",
        ],
    )

def com_google_truth_extensions_truth_java8_extension(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "com_google_truth_extensions_truth_java8_extension",
        artifact = "com.google.truth.extensions:truth-java8-extension:1.0",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "fd270fc302cf40703df5068f95a1f784754a855c762646e7d76bf66a2b789d7f",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "b406a148a251089034bdd662917e1764a35096e1a9283ff88b2f2f7aa05fef1f",
        exports = _replace_dependencies([
            "@com_google_truth_truth",
            "@org_checkerframework_checker_compat_qual",
        ], replacements),
        tags = [
            "maven_coordinates=com.google.truth.extensions:truth-java8-extension:1.0",
        ],
    )

def com_google_truth_extensions_truth_liteproto_extension(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "com_google_truth_extensions_truth_liteproto_extension",
        artifact = "com.google.truth.extensions:truth-liteproto-extension:1.0",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "906d22963e9cca387fbca990d1c7254a3aa1c87124f590b67c49395be687c888",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "f63a4fb769379bb8bebfe3b27c8742f922165cfc64c23603231965f29269e8e7",
        exports = _replace_dependencies([
            "@com_google_truth_truth",
            "@com_google_guava_guava",
            "@com_google_errorprone_error_prone_annotations",
            "@org_checkerframework_checker_compat_qual",
            "@com_google_auto_value_auto_value_annotations",
        ], replacements),
        tags = [
            "maven_coordinates=com.google.truth.extensions:truth-liteproto-extension:1.0",
        ],
    )

def com_google_truth_extensions_truth_proto_extension(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "com_google_truth_extensions_truth_proto_extension",
        artifact = "com.google.truth.extensions:truth-proto-extension:1.0",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "325f042dd30508a244da526c35f5ac55cb4b9d00d782295c25421dfd5aec86a9",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "b159faddaf4bbdbb9687490e087c74f5b0e05cffbd201cd9c829f244cce8c9df",
        exports = _replace_dependencies([
            "@com_google_truth_extensions_truth_liteproto_extension",
            "@com_google_truth_truth",
            "@com_google_guava_guava",
            "@com_google_protobuf_protobuf_java",
            "@com_google_errorprone_error_prone_annotations",
            "@org_checkerframework_checker_compat_qual",
            "@com_google_auto_value_auto_value_annotations",
        ], replacements),
        tags = [
            "maven_coordinates=com.google.truth.extensions:truth-proto-extension:1.0",
        ],
    )

def com_google_truth_truth(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "com_google_truth_truth",
        artifact = "com.google.truth:truth:1.0",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "edaa12f3b581fcf1c07311e94af8766919c4f3d904b00d3503147b99bf5b4004",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "ba985ea463d2f2ba00fb058b0e2ebe22c00d21184517ff913afc85edb7a05a8a",
        exports = _replace_dependencies([
            "@com_google_guava_guava",
            "@junit_junit",
            "@com_google_errorprone_error_prone_annotations",
            "@org_checkerframework_checker_compat_qual",
            "@com_google_auto_value_auto_value_annotations",
            "@com_googlecode_java_diff_utils_diffutils",
        ], replacements),
        tags = [
            "maven_coordinates=com.google.truth:truth:1.0",
        ],
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
        srcjar_sha256 = "7f4d40e97827f8a3285c3e47e8d28797ecfd45fb2ff94bd12cb6a83760a5f427",
        exports = _replace_dependencies([
        ], replacements),
        tags = [
            "maven_coordinates=com.googlecode.java-diff-utils:diffutils:1.3.0",
        ],
    )

def io_grpc_grpc_api(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "io_grpc_grpc_api",
        artifact = "io.grpc:grpc-api:1.25.0",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "a269094009588213ab5386a6fb92426b8056a130b2653d3b4e59e971f2f1ef08",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "874baf0df28c81c923cef23dd9eba5a0ed975a7a375ae76308b51ac9b865dd7e",
        exports = _replace_dependencies([
            "@com_google_guava_guava",
            "@io_grpc_grpc_context",
            "@com_google_code_findbugs_jsr305",
            "@com_google_errorprone_error_prone_annotations",
            "@org_codehaus_mojo_animal_sniffer_annotations",
        ], replacements),
        tags = [
            "maven_coordinates=io.grpc:grpc-api:1.25.0",
        ],
    )

def io_grpc_grpc_context(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "io_grpc_grpc_context",
        artifact = "io.grpc:grpc-context:1.25.0",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "f4c8f878c320f6fb56c1c14692618f6df8253314b556176e32727afbc5921a73",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "f33582d91cb14195bc63f2088e389c543967aaa7a122095b05ec3ddf18236d75",
        exports = _replace_dependencies([
        ], replacements),
        tags = [
            "maven_coordinates=io.grpc:grpc-context:1.25.0",
        ],
    )

def io_grpc_grpc_core(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "io_grpc_grpc_core",
        artifact = "io.grpc:grpc-core:1.25.0",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "d67fa113fd9cc45a02710f9c41dda9c15191448c14e9e96fcc21839a41345d4c",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "941389693e8ffc8118907112fad886e7bca400c3a700505ee4c062fed9fb5279",
        exports = _replace_dependencies([
            "@io_grpc_grpc_api",
            "@io_perfmark_perfmark_api",
            "@com_google_code_gson_gson",
            "@com_google_android_annotations",
            "@io_opencensus_opencensus_contrib_grpc_metrics",
            "@io_opencensus_opencensus_api",
        ], replacements),
        tags = [
            "maven_coordinates=io.grpc:grpc-core:1.25.0",
        ],
    )

def io_grpc_grpc_netty(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "io_grpc_grpc_netty",
        artifact = "io.grpc:grpc-netty:1.25.0",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "167e834788f6d4f7bd129bfb244d4e09d061a0e7165288378386ae871e3cfe51",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "8dfe577f429e00a5d3e23e3ac667ea4318ea3e7b460a07361c1df34d37c1a923",
        exports = _replace_dependencies([
            "@io_grpc_grpc_core",
            "@io_netty_netty_codec_http2",
            "@io_netty_netty_handler_proxy",
        ], replacements),
        tags = [
            "maven_coordinates=io.grpc:grpc-netty:1.25.0",
        ],
    )

def io_grpc_grpc_protobuf(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "io_grpc_grpc_protobuf",
        artifact = "io.grpc:grpc-protobuf:1.25.0",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "454dae7e246dac25526ed5b795d97a5dafedd3cc2042cfc810f02051d7d3e3cb",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "fcc1992e99679d262fcf9be1501a511f3515f74f7ac6a1b4e52af6c55dc6f3f6",
        exports = _replace_dependencies([
            "@com_google_api_grpc_proto_google_common_protos",
            "@io_grpc_grpc_protobuf_lite",
            "@io_grpc_grpc_api",
            "@com_google_guava_guava",
            "@com_google_protobuf_protobuf_java",
        ], replacements),
        tags = [
            "maven_coordinates=io.grpc:grpc-protobuf:1.25.0",
        ],
    )

def io_grpc_grpc_protobuf_lite(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "io_grpc_grpc_protobuf_lite",
        artifact = "io.grpc:grpc-protobuf-lite:1.25.0",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "9ba9aaa3e6997a04c707793c25e3ec88c6bad86f8d6f6b8b7a1a0c33ea2429d8",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "543ed22b35fea2dc65e6fa7e0b83b95d7c5808341c3ade9c6b5ea7ada410d9c0",
        exports = _replace_dependencies([
            "@io_grpc_grpc_api",
            "@com_google_guava_guava",
            "@com_google_protobuf_protobuf_lite",
        ], replacements),
        tags = [
            "maven_coordinates=io.grpc:grpc-protobuf-lite:1.25.0",
        ],
    )

def io_grpc_grpc_services(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "io_grpc_grpc_services",
        artifact = "io.grpc:grpc-services:1.25.0",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "d4274ee0d8f7a0adbb9ff8512b916b74f3f5e88eb000b4f041eff9b1e82cf998",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "2bad51ba810ccd0a047969c54e1863a5e2b59d28356a08231ad4382ee6d6d09b",
        exports = _replace_dependencies([
            "@io_grpc_grpc_protobuf",
            "@io_grpc_grpc_core",
            "@io_grpc_grpc_stub",
            "@com_google_protobuf_protobuf_java_util",
        ], replacements),
        tags = [
            "maven_coordinates=io.grpc:grpc-services:1.25.0",
        ],
    )

def io_grpc_grpc_stub(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "io_grpc_grpc_stub",
        artifact = "io.grpc:grpc-stub:1.25.0",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "1532e291c0e9fd8230a6416c8ebbd902d99c7e2760241ae638ea761aa3dd5f43",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "3ebc649b8125ed0d4ecbb868f7424263a5ccf420fc7967f920a062045373cb40",
        exports = _replace_dependencies([
            "@io_grpc_grpc_api",
        ], replacements),
        tags = [
            "maven_coordinates=io.grpc:grpc-stub:1.25.0",
        ],
    )

def io_netty_netty_buffer(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "io_netty_netty_buffer",
        artifact = "io.netty:netty-buffer:4.1.39.Final",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "a1544547c275cffe8b3efeb11c3ff2e79dcda675bcd1750968e1d1e1d60ea760",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "4ef45fd1baf33206906fc64f9597c244b5729bd9848e6bb0f59a05431b4b4578",
        exports = _replace_dependencies([
            "@io_netty_netty_common",
        ], replacements),
        tags = [
            "maven_coordinates=io.netty:netty-buffer:4.1.39.Final",
        ],
    )

def io_netty_netty_codec(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "io_netty_netty_codec",
        artifact = "io.netty:netty-codec:4.1.39.Final",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "61c787409a19117a27521ae778480870b04cf6e4962924430dabb95e90a9bcda",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "c60179f7f139949fb3bc04e388b4c44806084598fda10b153dab6802f85c30da",
        exports = _replace_dependencies([
            "@io_netty_netty_transport",
            "@io_netty_netty_buffer",
            "@io_netty_netty_common",
        ], replacements),
        tags = [
            "maven_coordinates=io.netty:netty-codec:4.1.39.Final",
        ],
    )

def io_netty_netty_codec_http(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "io_netty_netty_codec_http",
        artifact = "io.netty:netty-codec-http:4.1.39.Final",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "e92eccbf379f98be01e185d142754505c17cf95c1aa44d383ff7a9e8c4211063",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "d2cc14f101fd14542a3ed01dc7fcacefe1addc446644970db5abd13b4f21d6f5",
        exports = _replace_dependencies([
            "@io_netty_netty_handler",
            "@io_netty_netty_codec",
            "@io_netty_netty_transport",
            "@io_netty_netty_buffer",
            "@io_netty_netty_common",
        ], replacements),
        tags = [
            "maven_coordinates=io.netty:netty-codec-http:4.1.39.Final",
        ],
    )

def io_netty_netty_codec_http2(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "io_netty_netty_codec_http2",
        artifact = "io.netty:netty-codec-http2:4.1.39.Final",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "dc6f3eadea065ebad7234b5e3e267a73da88da6d5c505ddfbbb93722196d339e",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "d945addb09297720d170b8154ed01e7626f77025c413153b829ae3ab987eb1a9",
        exports = _replace_dependencies([
            "@io_netty_netty_codec_http",
            "@io_netty_netty_handler",
            "@io_netty_netty_codec",
            "@io_netty_netty_transport",
            "@io_netty_netty_buffer",
            "@io_netty_netty_common",
        ], replacements),
        tags = [
            "maven_coordinates=io.netty:netty-codec-http2:4.1.39.Final",
        ],
    )

def io_netty_netty_codec_socks(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "io_netty_netty_codec_socks",
        artifact = "io.netty:netty-codec-socks:4.1.39.Final",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "b934724962f3377e2bf2ac2f0ab894595aac7f4b28be80f2f294d21e96c91526",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "14d7abf5e970b3961e39869f2562ec81709a17525bc189d82dbc835c76584353",
        exports = _replace_dependencies([
            "@io_netty_netty_codec",
            "@io_netty_netty_transport",
            "@io_netty_netty_buffer",
            "@io_netty_netty_common",
        ], replacements),
        tags = [
            "maven_coordinates=io.netty:netty-codec-socks:4.1.39.Final",
        ],
    )

def io_netty_netty_common(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "io_netty_netty_common",
        artifact = "io.netty:netty-common:4.1.39.Final",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "67a000d99a71e4fda26391ad4dfa98571e34cda773aec007a5f45242e8bef66f",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "3900ed0250bb43f9003f0567a8ff384d162cb3f980c114a403cdd3ef92faa056",
        exports = _replace_dependencies([
        ], replacements),
        tags = [
            "maven_coordinates=io.netty:netty-common:4.1.39.Final",
        ],
    )

def io_netty_netty_handler(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "io_netty_netty_handler",
        artifact = "io.netty:netty-handler:4.1.39.Final",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "b0311957f28b284739b982fdcdf1d500b0d99f047b195fb74cf1d877d045fc5d",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "768f2ccd64825c7abbf0e4779a24daa76ad26926d82b2420ba5f52725f047f1e",
        exports = _replace_dependencies([
            "@io_netty_netty_codec",
            "@io_netty_netty_transport",
            "@io_netty_netty_buffer",
            "@io_netty_netty_common",
        ], replacements),
        tags = [
            "maven_coordinates=io.netty:netty-handler:4.1.39.Final",
        ],
    )

def io_netty_netty_handler_proxy(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "io_netty_netty_handler_proxy",
        artifact = "io.netty:netty-handler-proxy:4.1.39.Final",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "115702178befe500dac6edbc0f7324dd84f93ec1e4e0943c8083d39a913e4a22",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "cf30ee8e6828fbb565a06d854f3682209be7187dd0a0fcfc387bcc82be2af485",
        exports = _replace_dependencies([
            "@io_netty_netty_codec_http",
            "@io_netty_netty_codec_socks",
            "@io_netty_netty_codec",
            "@io_netty_netty_transport",
            "@io_netty_netty_buffer",
            "@io_netty_netty_common",
        ], replacements),
        tags = [
            "maven_coordinates=io.netty:netty-handler-proxy:4.1.39.Final",
        ],
    )

def io_netty_netty_resolver(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "io_netty_netty_resolver",
        artifact = "io.netty:netty-resolver:4.1.39.Final",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "61b80dcec8c95ccf6c8a134143e37d6f26251b8070e5f7a6886ffd5dd69e4a72",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "e81a2eaa9da373b1ada5b4f590258a9a2d41c12491c8388069aa6c5c5b898572",
        exports = _replace_dependencies([
            "@io_netty_netty_common",
        ], replacements),
        tags = [
            "maven_coordinates=io.netty:netty-resolver:4.1.39.Final",
        ],
    )

def io_netty_netty_transport(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "io_netty_netty_transport",
        artifact = "io.netty:netty-transport:4.1.39.Final",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "cf018b7e5f0739ff5f972761dcca3d62825a5426bd04f556d242400934b245f5",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "04d3953e9fb9e6b3d9f94bccba71fbce0c530ffd2fdba7a2ac5ebaf8f3565a7c",
        exports = _replace_dependencies([
            "@io_netty_netty_buffer",
            "@io_netty_netty_resolver",
            "@io_netty_netty_common",
        ], replacements),
        tags = [
            "maven_coordinates=io.netty:netty-transport:4.1.39.Final",
        ],
    )

def io_opencensus_opencensus_api(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "io_opencensus_opencensus_api",
        artifact = "io.opencensus:opencensus-api:0.21.0",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "8e2cb0f6391d8eb0a1bcd01e7748883f0033b1941754f4ed3f19d2c3e4276fc8",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "a185e02627df9dd25ac982f8f1e81f6ac059550d82b0e8c149f9954bd750ad7f",
        exports = _replace_dependencies([
        ], replacements),
        tags = [
            "maven_coordinates=io.opencensus:opencensus-api:0.21.0",
        ],
    )

def io_opencensus_opencensus_contrib_grpc_metrics(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "io_opencensus_opencensus_contrib_grpc_metrics",
        artifact = "io.opencensus:opencensus-contrib-grpc-metrics:0.21.0",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "29fc79401082301542cab89d7054d2f0825f184492654c950020553ef4ff0ef8",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "6536dcddc505c73c53d8e031f12276dfd345b093a59c1943d050bf55dba4730f",
        exports = _replace_dependencies([
            "@io_opencensus_opencensus_api",
        ], replacements),
        tags = [
            "maven_coordinates=io.opencensus:opencensus-contrib-grpc-metrics:0.21.0",
        ],
    )

def io_perfmark_perfmark_api(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "io_perfmark_perfmark_api",
        artifact = "io.perfmark:perfmark-api:0.19.0",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "b734ba2149712409a44eabdb799f64768578fee0defe1418bb108fe32ea43e1a",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "05cfbdd34e6fc1f10181c755cec67cf1ee517dfee615e25d1007a8aabd569dba",
        exports = _replace_dependencies([
            "@com_google_code_findbugs_jsr305",
            "@com_google_errorprone_error_prone_annotations",
        ], replacements),
        tags = [
            "maven_coordinates=io.perfmark:perfmark-api:0.19.0",
        ],
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
        srcjar_sha256 = "128971e52e0d84a66e3b6e049dab8ad7b2c58b7e1ad37fa2debd3d40c2947b95",
        exports = _replace_dependencies([
        ], replacements),
        tags = [
            "maven_coordinates=javax.annotation:javax.annotation-api:1.3.2",
        ],
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
        srcjar_sha256 = "9f43fea92033ad82bcad2ae44cec5c82abc9d6ee4b095cab921d11ead98bf2ff",
        exports = _replace_dependencies([
            "@org_hamcrest_hamcrest_core",
        ], replacements),
        tags = [
            "maven_coordinates=junit:junit:4.12",
        ],
    )

def net_bytebuddy_byte_buddy(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "net_bytebuddy_byte_buddy",
        artifact = "net.bytebuddy:byte-buddy:1.9.16",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "6b71e4f70c96b67d420f592148aa4fd1966aba458b35d11f491ff13de97dc862",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "9f239c1599715faeff23676d2ccb64d3365ea9ee651c5b3ed0d4011a886db419",
        exports = _replace_dependencies([
        ], replacements),
        tags = [
            "maven_coordinates=net.bytebuddy:byte-buddy:1.9.16",
        ],
    )

def net_bytebuddy_byte_buddy_agent(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "net_bytebuddy_byte_buddy_agent",
        artifact = "net.bytebuddy:byte-buddy-agent:1.9.16",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "c315f9c2eba3eee41b57b7b78c787011953145a8118e3a732dd56b42329bcff5",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "0a7b5643bd2de08a0579ff4368ec14ad42886620e3463dde4f7afc21fa3f03b2",
        exports = _replace_dependencies([
        ], replacements),
        tags = [
            "maven_coordinates=net.bytebuddy:byte-buddy-agent:1.9.16",
        ],
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
        srcjar_sha256 = "7c63a4a46b2ef903f941aeac63da87dd345be3243b472796aa945fa715bf3ca9",
        exports = _replace_dependencies([
        ], replacements),
        tags = [
            "maven_coordinates=org.checkerframework:checker-compat-qual:2.5.5",
        ],
    )

def org_checkerframework_checker_qual(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "org_checkerframework_checker_qual",
        artifact = "org.checkerframework:checker-qual:2.8.1",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "9103499008bcecd4e948da29b17864abb64304e15706444ae209d17ebe0575df",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "abff0b3dfbfe94c1c6f00eb03a964ef57747008ee95e911a318387df565bd0b2",
        exports = _replace_dependencies([
        ], replacements),
        tags = [
            "maven_coordinates=org.checkerframework:checker-qual:2.8.1",
        ],
    )

def org_codehaus_mojo_animal_sniffer_annotations(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "org_codehaus_mojo_animal_sniffer_annotations",
        artifact = "org.codehaus.mojo:animal-sniffer-annotations:1.18",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "47f05852b48ee9baefef80fa3d8cea60efa4753c0013121dd7fe5eef2e5c729d",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "ee078a91bf7136ee1961abd612b54d1cd9877352b960a7e1e7e3e4c17ceafcf1",
        exports = _replace_dependencies([
        ], replacements),
        tags = [
            "maven_coordinates=org.codehaus.mojo:animal-sniffer-annotations:1.18",
        ],
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
        srcjar_sha256 = "e223d2d8fbafd66057a8848cc94222d63c3cedd652cc48eddc0ab5c39c0f84df",
        exports = _replace_dependencies([
        ], replacements),
        tags = [
            "maven_coordinates=org.hamcrest:hamcrest-core:1.3",
        ],
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
        srcjar_sha256 = "2e624439ee1de251c0e963a7183f47383eb67b29260d175ab6688675e05c9342",
        exports = _replace_dependencies([
            "@net_bytebuddy_byte_buddy",
            "@net_bytebuddy_byte_buddy_agent",
            "@org_objenesis_objenesis",
        ], replacements),
        tags = [
            "maven_coordinates=org.mockito:mockito-core:2.23.4",
        ],
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
        srcjar_sha256 = "52d9f4dba531677fc074eff00ea07f22a1d42e5a97cc9e8571c4cd3d459b6be0",
        exports = _replace_dependencies([
        ], replacements),
        tags = [
            "maven_coordinates=org.objenesis:objenesis:2.6",
        ],
    )

def org_slf4j_slf4j_api(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "org_slf4j_slf4j_api",
        artifact = "org.slf4j:slf4j-api:1.7.28",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "fb6e4f67a2a4689e3e713584db17a5d1090c1ebe6eec30e9e0349a6ee118141e",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "b1b8bfa4f2709684606001685d09ef905adc1b72ec53444ade90f44bfbcebcff",
        exports = _replace_dependencies([
        ], replacements),
        tags = [
            "maven_coordinates=org.slf4j:slf4j-api:1.7.28",
        ],
    )

def org_springframework_cloud_spring_cloud_commons(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "org_springframework_cloud_spring_cloud_commons",
        artifact = "org.springframework.cloud:spring-cloud-commons:2.1.3.RELEASE",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "4161fcf51d8e8fe69c1e0613cc86dd9bcba77a79e58f021621f9c26e0ba87edf",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "757476263ccb0b6aaa4ad359d86e30a84ae8eed6faa2ad17f4c12c75a3882f04",
        exports = _replace_dependencies([
            "@org_springframework_security_spring_security_crypto",
        ], replacements),
        tags = [
            "maven_coordinates=org.springframework.cloud:spring-cloud-commons:2.1.3.RELEASE",
        ],
    )

def org_springframework_security_spring_security_crypto(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "org_springframework_security_spring_security_crypto",
        artifact = "org.springframework.security:spring-security-crypto:5.1.6.RELEASE",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "b71bca31cfae853c40c96aef072eb12249927e2122b8e66656e59551692abf8a",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "ba7e4fcc52183cddcb0bdbf8e79cd8f1de92bf6223926e3b41afbcdb90b1b6d9",
        exports = _replace_dependencies([
        ], replacements),
        tags = [
            "maven_coordinates=org.springframework.security:spring-security-crypto:5.1.6.RELEASE",
        ],
    )

def org_springframework_spring_core(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "org_springframework_spring_core",
        artifact = "org.springframework:spring-core:5.1.9.RELEASE",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "427406f5423e032e08e5d43e5d3eccfbc83350b0d7c6ec22db839755ff1120de",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "d4c90b76cd68d695234862426234ec33ebed8f1a033bb5952054191b8d91ca88",
        exports = _replace_dependencies([
            "@org_springframework_spring_jcl",
        ], replacements),
        tags = [
            "maven_coordinates=org.springframework:spring-core:5.1.9.RELEASE",
        ],
    )

def org_springframework_spring_jcl(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "org_springframework_spring_jcl",
        artifact = "org.springframework:spring-jcl:5.1.9.RELEASE",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "e6f5a8162bc57aec3d9260fec9efc019cee904de2b0c5a6abe02598a17d10456",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "6109feb26549148bfa13986ac713f21d5dcf73c660f4930460818d21b47c0350",
        exports = _replace_dependencies([
        ], replacements),
        tags = [
            "maven_coordinates=org.springframework:spring-jcl:5.1.9.RELEASE",
        ],
    )
