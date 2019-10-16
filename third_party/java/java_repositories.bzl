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
        omit_ch_qos_logback_logback_classic = False,
        omit_ch_qos_logback_logback_core = False,
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
        omit_com_squareup_okhttp3_logging_interceptor = False,
        omit_com_squareup_okhttp3_mockwebserver = False,
        omit_com_squareup_okhttp3_okhttp = False,
        omit_com_squareup_okio_okio = False,
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
        omit_io_projectreactor_reactor_core = False,
        omit_javax_annotation_javax_annotation_api = False,
        omit_junit_junit = False,
        omit_net_bytebuddy_byte_buddy = False,
        omit_net_bytebuddy_byte_buddy_agent = False,
        omit_org_checkerframework_checker_compat_qual = False,
        omit_org_checkerframework_checker_qual = False,
        omit_org_codehaus_mojo_animal_sniffer_annotations = False,
        omit_org_hamcrest_hamcrest = False,
        omit_org_hamcrest_hamcrest_core = False,
        omit_org_jetbrains_annotations = False,
        omit_org_jetbrains_kotlin_kotlin_reflect = False,
        omit_org_jetbrains_kotlin_kotlin_stdlib = False,
        omit_org_jetbrains_kotlin_kotlin_stdlib_common = False,
        omit_org_mockito_mockito_core = False,
        omit_org_objenesis_objenesis = False,
        omit_org_reactivestreams_reactive_streams = False,
        omit_org_slf4j_slf4j_api = False,
        omit_org_springframework_cloud_spring_cloud_commons = False,
        omit_org_springframework_security_spring_security_crypto = False,
        omit_org_springframework_spring_core = False,
        omit_org_springframework_spring_jcl = False,
        fetch_sources = False,
        replacements = {}):
    if not omit_ch_qos_logback_logback_classic:
        ch_qos_logback_logback_classic(fetch_sources, replacements)
    if not omit_ch_qos_logback_logback_core:
        ch_qos_logback_logback_core(fetch_sources, replacements)
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
    if not omit_com_squareup_okhttp3_logging_interceptor:
        com_squareup_okhttp3_logging_interceptor(fetch_sources, replacements)
    if not omit_com_squareup_okhttp3_mockwebserver:
        com_squareup_okhttp3_mockwebserver(fetch_sources, replacements)
    if not omit_com_squareup_okhttp3_okhttp:
        com_squareup_okhttp3_okhttp(fetch_sources, replacements)
    if not omit_com_squareup_okio_okio:
        com_squareup_okio_okio(fetch_sources, replacements)
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
    if not omit_io_projectreactor_reactor_core:
        io_projectreactor_reactor_core(fetch_sources, replacements)
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
    if not omit_org_hamcrest_hamcrest:
        org_hamcrest_hamcrest(fetch_sources, replacements)
    if not omit_org_hamcrest_hamcrest_core:
        org_hamcrest_hamcrest_core(fetch_sources, replacements)
    if not omit_org_jetbrains_annotations:
        org_jetbrains_annotations(fetch_sources, replacements)
    if not omit_org_jetbrains_kotlin_kotlin_reflect:
        org_jetbrains_kotlin_kotlin_reflect(fetch_sources, replacements)
    if not omit_org_jetbrains_kotlin_kotlin_stdlib:
        org_jetbrains_kotlin_kotlin_stdlib(fetch_sources, replacements)
    if not omit_org_jetbrains_kotlin_kotlin_stdlib_common:
        org_jetbrains_kotlin_kotlin_stdlib_common(fetch_sources, replacements)
    if not omit_org_mockito_mockito_core:
        org_mockito_mockito_core(fetch_sources, replacements)
    if not omit_org_objenesis_objenesis:
        org_objenesis_objenesis(fetch_sources, replacements)
    if not omit_org_reactivestreams_reactive_streams:
        org_reactivestreams_reactive_streams(fetch_sources, replacements)
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

def ch_qos_logback_logback_classic(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "ch_qos_logback_logback_classic",
        artifact = "ch.qos.logback:logback-classic:1.2.3",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "fb53f8539e7fcb8f093a56e138112056ec1dc809ebb020b59d8a36a5ebac37e0",
        licenses = ["restricted"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "480cb5e99519271c9256716d4be1a27054047435ff72078d9deae5c6a19f63eb",
        exports = _replace_dependencies([
            "@org_slf4j_slf4j_api",
            "@ch_qos_logback_logback_core",
        ], replacements),
        tags = [
            "maven_coordinates=ch.qos.logback:logback-classic:1.2.3",
        ],
    )

def ch_qos_logback_logback_core(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "ch_qos_logback_logback_core",
        artifact = "ch.qos.logback:logback-core:1.2.3",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "5946d837fe6f960c02a53eda7a6926ecc3c758bbdd69aa453ee429f858217f22",
        licenses = ["restricted"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "1f69b6b638ec551d26b10feeade5a2b77abe347f9759da95022f0da9a63a9971",
        exports = _replace_dependencies([
        ], replacements),
        tags = [
            "maven_coordinates=ch.qos.logback:logback-core:1.2.3",
        ],
    )

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
        artifact = "com.google.code.gson:gson:2.8.6",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "c8fb4839054d280b3033f800d1f5a97de2f028eb8ba2eb458ad287e536f3f25f",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "da4d787939dc8de214724a20d88614b70ef8c3a4931d9c694300b5d9098ed9bc",
        exports = _replace_dependencies([
        ], replacements),
        tags = [
            "maven_coordinates=com.google.code.gson:gson:2.8.6",
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
        artifact = "com.google.protobuf:protobuf-java:3.11.1",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "4a01db8277b79eaf84151013f3348a4d24720c8c9e1b33e5ed292caca5567cf1",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "86eb14060c4744db0fb373db680f5cbfc386375921194cff1e2cff43a4704f89",
        exports = _replace_dependencies([
        ], replacements),
        tags = [
            "maven_coordinates=com.google.protobuf:protobuf-java:3.11.1",
        ],
    )

def com_google_protobuf_protobuf_java_util(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "com_google_protobuf_protobuf_java_util",
        artifact = "com.google.protobuf:protobuf-java-util:3.11.1",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "dc7d8c502199c1309b284f9a8d0afff82356f0477d9a155ba4e50abb06e15313",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "236b015889438e77ba203700675090753bdbf251e561984ebb94bc95f080590b",
        exports = _replace_dependencies([
            "@com_google_protobuf_protobuf_java",
            "@com_google_code_gson_gson",
        ], replacements),
        tags = [
            "maven_coordinates=com.google.protobuf:protobuf-java-util:3.11.1",
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

def com_squareup_okhttp3_logging_interceptor(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "com_squareup_okhttp3_logging_interceptor",
        artifact = "com.squareup.okhttp3:logging-interceptor:4.2.2",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "d87e656976b00097c6ffb93930b149409a00b2dcbebcb4b730f0af57e23e5035",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "c51b4d71243a0f9aeb9ad28e7208456bce1295e268361a9f119f44e57c16d595",
        exports = _replace_dependencies([
            "@com_squareup_okhttp3_okhttp",
        ], replacements),
        tags = [
            "maven_coordinates=com.squareup.okhttp3:logging-interceptor:4.2.2",
        ],
    )

def com_squareup_okhttp3_mockwebserver(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "com_squareup_okhttp3_mockwebserver",
        artifact = "com.squareup.okhttp3:mockwebserver:4.2.2",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "7ca50cb2b5a78d3fcff16249d063041f77c3e578a4aaa398ee3abfad3ecdef23",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "41780981887f61deb4c7c0000a4e22a073e8bddbc0ff32476a02d422681a66c8",
        exports = _replace_dependencies([
            "@com_squareup_okhttp3_okhttp",
            "@junit_junit",
        ], replacements),
        tags = [
            "maven_coordinates=com.squareup.okhttp3:mockwebserver:4.2.2",
        ],
    )

def com_squareup_okhttp3_okhttp(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "com_squareup_okhttp3_okhttp",
        artifact = "com.squareup.okhttp3:okhttp:4.2.2",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "5064182cff40b100ce57fbf3ce985e12e38cf4433724e81b175c1843eaecbb75",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "7c93b7255e61963df942722515af2b06c8e9de259c68098fd166ece3d0c1c081",
        exports = _replace_dependencies([
            "@com_squareup_okio_okio",
            "@org_jetbrains_kotlin_kotlin_stdlib",
        ], replacements),
        tags = [
            "maven_coordinates=com.squareup.okhttp3:okhttp:4.2.2",
        ],
    )

def com_squareup_okio_okio(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "com_squareup_okio_okio",
        artifact = "com.squareup.okio:okio:2.2.2",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "e58c97406a6bb1138893750299ac63c6aa04b38b6b49eae1bfcad1a63ef9ba1b",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "ecf3a2b274a5c5d6f808b80c39feb6d52011c3885200070471232de64bfbcf2c",
        exports = _replace_dependencies([
            "@org_jetbrains_kotlin_kotlin_stdlib",
        ], replacements),
        tags = [
            "maven_coordinates=com.squareup.okio:okio:2.2.2",
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
        artifact = "io.netty:netty-buffer:4.1.43.Final",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "03f4cc9bf6ec527ed271e60948a51817c98e3cdd1dfc504beb6b9e1653767626",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "2c17514b4e4fe7264917881e34252143f60ae121d6590beb82a0ee55713bbd86",
        exports = _replace_dependencies([
            "@io_netty_netty_common",
        ], replacements),
        tags = [
            "maven_coordinates=io.netty:netty-buffer:4.1.43.Final",
        ],
    )

def io_netty_netty_codec(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "io_netty_netty_codec",
        artifact = "io.netty:netty-codec:4.1.43.Final",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "94533866033c4c9d7d297497b5fa8a1660b947e179d71112245c7c05a96f909b",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "00cb509baf23b73f5eeca6f8fc8b729629b3d4730258be18b954430664d4dc37",
        exports = _replace_dependencies([
            "@io_netty_netty_transport",
            "@io_netty_netty_buffer",
            "@io_netty_netty_common",
        ], replacements),
        tags = [
            "maven_coordinates=io.netty:netty-codec:4.1.43.Final",
        ],
    )

def io_netty_netty_codec_http(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "io_netty_netty_codec_http",
        artifact = "io.netty:netty-codec-http:4.1.43.Final",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "cd28128d1d7ad1e7c070c94e0c15d33e01534a5fefe187833808c4c4d7222be5",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "3f3a3c3ecec22816ca3233e4db5d4b012f10feee2df96deb049339de415734bf",
        exports = _replace_dependencies([
            "@io_netty_netty_handler",
            "@io_netty_netty_codec",
            "@io_netty_netty_transport",
            "@io_netty_netty_buffer",
            "@io_netty_netty_common",
        ], replacements),
        tags = [
            "maven_coordinates=io.netty:netty-codec-http:4.1.43.Final",
        ],
    )

def io_netty_netty_codec_http2(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "io_netty_netty_codec_http2",
        artifact = "io.netty:netty-codec-http2:4.1.43.Final",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "86818adea1018d7a7a3788188eeac4f5e7fa03c3a10001745e18821d30bce493",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "9c48a1a641a020893c292f7be5083c018da97b9de2387e930397e6f805105efa",
        exports = _replace_dependencies([
            "@io_netty_netty_codec_http",
            "@io_netty_netty_handler",
            "@io_netty_netty_codec",
            "@io_netty_netty_transport",
            "@io_netty_netty_buffer",
            "@io_netty_netty_common",
        ], replacements),
        tags = [
            "maven_coordinates=io.netty:netty-codec-http2:4.1.43.Final",
        ],
    )

def io_netty_netty_codec_socks(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "io_netty_netty_codec_socks",
        artifact = "io.netty:netty-codec-socks:4.1.43.Final",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "29c0d934d7d678b2c55ffe399a74d6c5c12ac5b74b7701fb53c48764a059f71a",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "9ecaf2ffbce0e431e7c2d58ec1f0fc07831e4468f748760ffe19a535ab3f4eb9",
        exports = _replace_dependencies([
            "@io_netty_netty_codec",
            "@io_netty_netty_transport",
            "@io_netty_netty_buffer",
            "@io_netty_netty_common",
        ], replacements),
        tags = [
            "maven_coordinates=io.netty:netty-codec-socks:4.1.43.Final",
        ],
    )

def io_netty_netty_common(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "io_netty_netty_common",
        artifact = "io.netty:netty-common:4.1.43.Final",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "729737a2a57c460d3543aa309cbf971ad2f0ffec431751d48409f5e865bae88e",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "d240501384b5b5927832f7624ba484dc87454a5adf8754eda993c0c2f9a3f993",
        exports = _replace_dependencies([
        ], replacements),
        tags = [
            "maven_coordinates=io.netty:netty-common:4.1.43.Final",
        ],
    )

def io_netty_netty_handler(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "io_netty_netty_handler",
        artifact = "io.netty:netty-handler:4.1.43.Final",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "f16169d25565edc402b872d8f0b17f32338a9d395602eff8a69e99bbdcc53558",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "902dfe13febe6247a98698930410a780a121c328424f064f7544352e85874595",
        exports = _replace_dependencies([
            "@io_netty_netty_codec",
            "@io_netty_netty_transport",
            "@io_netty_netty_buffer",
            "@io_netty_netty_common",
        ], replacements),
        tags = [
            "maven_coordinates=io.netty:netty-handler:4.1.43.Final",
        ],
    )

def io_netty_netty_handler_proxy(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "io_netty_netty_handler_proxy",
        artifact = "io.netty:netty-handler-proxy:4.1.43.Final",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "714de68f915cd4853a9ff3aef09b9eb9faed1e169756a9c04f9172e51e2c25bc",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "b6b57b8ab9fa328306083daf73c12d8d3bdd628dabc7eee86e2c38d563b52575",
        exports = _replace_dependencies([
            "@io_netty_netty_codec_http",
            "@io_netty_netty_codec_socks",
            "@io_netty_netty_codec",
            "@io_netty_netty_transport",
            "@io_netty_netty_buffer",
            "@io_netty_netty_common",
        ], replacements),
        tags = [
            "maven_coordinates=io.netty:netty-handler-proxy:4.1.43.Final",
        ],
    )

def io_netty_netty_resolver(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "io_netty_netty_resolver",
        artifact = "io.netty:netty-resolver:4.1.43.Final",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "f39b887d6fcc56de1b144ef2e58d9232d78e9c0e5994a90fa4e6645074bea17e",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "dc9c4210c7acba3953b4d31bc35913d1e3d206003e0ac3b8e9eeb6f32149cc84",
        exports = _replace_dependencies([
            "@io_netty_netty_common",
        ], replacements),
        tags = [
            "maven_coordinates=io.netty:netty-resolver:4.1.43.Final",
        ],
    )

def io_netty_netty_transport(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "io_netty_netty_transport",
        artifact = "io.netty:netty-transport:4.1.43.Final",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "94f2b881f1c8d62afe9f349bf6c0ea0c8e58811715c74282872c735069b78503",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "e064da58f75210e21c0bf7066d8d9a6fcd377feb20cfd86bc5d14db09d7409ea",
        exports = _replace_dependencies([
            "@io_netty_netty_buffer",
            "@io_netty_netty_resolver",
            "@io_netty_netty_common",
        ], replacements),
        tags = [
            "maven_coordinates=io.netty:netty-transport:4.1.43.Final",
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

def io_projectreactor_reactor_core(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "io_projectreactor_reactor_core",
        artifact = "io.projectreactor:reactor-core:3.3.1.RELEASE",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "40280e627e5cdcdc71633ce39171c15006f4fae0fe51d2d7e585d41b385726ee",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "ff0a8b583f0135416045c3b9c5e9fffbb707c85645678c39c2b5585b768737d4",
        exports = _replace_dependencies([
            "@org_reactivestreams_reactive_streams",
        ], replacements),
        tags = [
            "maven_coordinates=io.projectreactor:reactor-core:3.3.1.RELEASE",
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
        artifact = "net.bytebuddy:byte-buddy:1.10.4",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "1bbad1cfb8100bf0f70a115653c7250dbf8d1df838058f21a651dd20d0bf4741",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "509b5c20a6a5c48778a9328f282f9c6e3d22faf9bb79e0ee56d6a0623932d804",
        exports = _replace_dependencies([
        ], replacements),
        tags = [
            "maven_coordinates=net.bytebuddy:byte-buddy:1.10.4",
        ],
    )

def net_bytebuddy_byte_buddy_agent(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "net_bytebuddy_byte_buddy_agent",
        artifact = "net.bytebuddy:byte-buddy-agent:1.10.4",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "a0cee1bba5626730e98e2777338fc4c5e22d47947906fdf63d43f271de07a170",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "b5b5aac7e050c0d6f63c624b2ffee14a2878a6f50f2e5a48c36f277fbb98574b",
        exports = _replace_dependencies([
        ], replacements),
        tags = [
            "maven_coordinates=net.bytebuddy:byte-buddy-agent:1.10.4",
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

def org_hamcrest_hamcrest(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "org_hamcrest_hamcrest",
        artifact = "org.hamcrest:hamcrest:2.1",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "ba93b2e3a562322ba432f0a1b53addcc55cb188253319a020ed77f824e692050",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "70ddd82a79f885022cae5320082cf3219055d1bd0f74406f814c67da29a74d31",
        exports = _replace_dependencies([
        ], replacements),
        tags = [
            "maven_coordinates=org.hamcrest:hamcrest:2.1",
        ],
    )

def org_hamcrest_hamcrest_core(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "org_hamcrest_hamcrest_core",
        artifact = "org.hamcrest:hamcrest-core:2.1",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "e09109e54a289d88506b9bfec987ddd199f4217c9464132668351b9a4f00bee9",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "7da51da1e717c08c9cdc3d5c1d3d80d7ce1f98afb1c20585d35d388c7daa1604",
        exports = _replace_dependencies([
            "@org_hamcrest_hamcrest",
        ], replacements),
        tags = [
            "maven_coordinates=org.hamcrest:hamcrest-core:2.1",
        ],
    )

def org_jetbrains_annotations(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "org_jetbrains_annotations",
        artifact = "org.jetbrains:annotations:13.0",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "ace2a10dc8e2d5fd34925ecac03e4988b2c0f851650c94b8cef49ba1bd111478",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "42a5e144b8e81d50d6913d1007b695e62e614705268d8cf9f13dbdc478c2c68e",
        exports = _replace_dependencies([
        ], replacements),
        tags = [
            "maven_coordinates=org.jetbrains:annotations:13.0",
        ],
    )

def org_jetbrains_kotlin_kotlin_reflect(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "org_jetbrains_kotlin_kotlin_reflect",
        artifact = "org.jetbrains.kotlin:kotlin-reflect:1.3.61",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "143e715c10ff6d65eb5a7695be7b696c6e013702dff103d23ba54760bf93867b",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "59280624892885b5029b2c0d64003612c60df39891d55ef624562fae07ff5b5e",
        exports = _replace_dependencies([
            "@org_jetbrains_kotlin_kotlin_stdlib",
        ], replacements),
        tags = [
            "maven_coordinates=org.jetbrains.kotlin:kotlin-reflect:1.3.61",
        ],
    )

def org_jetbrains_kotlin_kotlin_stdlib(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "org_jetbrains_kotlin_kotlin_stdlib",
        artifact = "org.jetbrains.kotlin:kotlin-stdlib:1.3.61",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "e51e512619a7e7650a30eb4eb3e9c03e6909c7b5e3c026404e076254c098b932",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "7c25d24976908a6c1731e3b2eba02978b1a8b62f16554eab1dfaa41758b720ac",
        exports = _replace_dependencies([
            "@org_jetbrains_kotlin_kotlin_stdlib_common",
            "@org_jetbrains_annotations",
        ], replacements),
        tags = [
            "maven_coordinates=org.jetbrains.kotlin:kotlin-stdlib:1.3.61",
        ],
    )

def org_jetbrains_kotlin_kotlin_stdlib_common(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "org_jetbrains_kotlin_kotlin_stdlib_common",
        artifact = "org.jetbrains.kotlin:kotlin-stdlib-common:1.3.61",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "a2e7f341cf3047b5f00a1917ef777d323cdab2a57377468b8ed62aa31469cf7f",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "272d20d52fe0c3c91b09d02125cea2c7fa2dabefb1397e0239a9c2601f002346",
        exports = _replace_dependencies([
        ], replacements),
        tags = [
            "maven_coordinates=org.jetbrains.kotlin:kotlin-stdlib-common:1.3.61",
        ],
    )

def org_mockito_mockito_core(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "org_mockito_mockito_core",
        artifact = "org.mockito:mockito-core:3.1.0",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "89b09e518e04f5c35f5ccf7abe45e72f594070a53d95cc2579001bd392c5afa6",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "4f23acaa29ce8783b00f794c6e556c26f3046ee4ad7da4ce08255c9676aba2ce",
        exports = _replace_dependencies([
            "@net_bytebuddy_byte_buddy",
            "@net_bytebuddy_byte_buddy_agent",
            "@org_objenesis_objenesis",
        ], replacements),
        tags = [
            "maven_coordinates=org.mockito:mockito-core:3.1.0",
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

def org_reactivestreams_reactive_streams(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "org_reactivestreams_reactive_streams",
        artifact = "org.reactivestreams:reactive-streams:1.0.3",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "1dee0481072d19c929b623e155e14d2f6085dc011529a0a0dbefc84cf571d865",
        licenses = ["unencumbered"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "d5b4070a22c9b1ca5b9b5aa668466bcca391dbe5d5fe8311c300765c1621feba",
        exports = _replace_dependencies([
        ], replacements),
        tags = [
            "maven_coordinates=org.reactivestreams:reactive-streams:1.0.3",
        ],
    )

def org_slf4j_slf4j_api(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "org_slf4j_slf4j_api",
        artifact = "org.slf4j:slf4j-api:1.7.29",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "47b624903c712f9118330ad2fb91d0780f7f666c3f22919d0fc14522c5cad9ea",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "859a6c367b811efb6f7b00cbf612383a2aca00fda53ec04270587ce7d7b9e813",
        exports = _replace_dependencies([
        ], replacements),
        tags = [
            "maven_coordinates=org.slf4j:slf4j-api:1.7.29",
        ],
    )

def org_springframework_cloud_spring_cloud_commons(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "org_springframework_cloud_spring_cloud_commons",
        artifact = "org.springframework.cloud:spring-cloud-commons:2.2.1.RELEASE",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "63eb5cac1b338c0b6ea83e5cde50afc9b08b89d7897b32db4b873b7ce3c84864",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "d3162d475649cc6b9d9852d162f574dcaa1f1fc6c2c00afd2a36096e43d35b98",
        exports = _replace_dependencies([
            "@org_springframework_security_spring_security_crypto",
        ], replacements),
        tags = [
            "maven_coordinates=org.springframework.cloud:spring-cloud-commons:2.2.1.RELEASE",
        ],
    )

def org_springframework_security_spring_security_crypto(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "org_springframework_security_spring_security_crypto",
        artifact = "org.springframework.security:spring-security-crypto:5.2.1.RELEASE",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "1cdfb9e938a60c326660970c8bd42f808bdeefa1ed79baeaabbbeb8e3508439f",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "3b452a8f1fd05ae508262f9028029f15a09a85b0992a38ccb5246cea0915dff9",
        exports = _replace_dependencies([
        ], replacements),
        tags = [
            "maven_coordinates=org.springframework.security:spring-security-crypto:5.2.1.RELEASE",
        ],
    )

def org_springframework_spring_core(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "org_springframework_spring_core",
        artifact = "org.springframework:spring-core:5.2.2.RELEASE",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "94459936895f669c8bdd794be79850b73a9b980cc01a4aec88f373f150002b70",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "da15b3adbd5dfd0f88a6902c1c59df9a82f75352ade78d9afe87cf40a7944ba7",
        exports = _replace_dependencies([
            "@org_springframework_spring_jcl",
        ], replacements),
        tags = [
            "maven_coordinates=org.springframework:spring-core:5.2.2.RELEASE",
        ],
    )

def org_springframework_spring_jcl(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "org_springframework_spring_jcl",
        artifact = "org.springframework:spring-jcl:5.2.2.RELEASE",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "db6ec0aa5330ab84a78933fd2c27db83581e3f0adbc1a562013c8647b3935dbd",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "315892faba19a2c518bac06ee62f49e0537cabff3b4af618f9f3d211e6bd0a24",
        exports = _replace_dependencies([
        ], replacements),
        tags = [
            "maven_coordinates=org.springframework:spring-jcl:5.2.2.RELEASE",
        ],
    )
