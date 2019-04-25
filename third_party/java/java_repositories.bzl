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
        artifact = "com.google.api.grpc:proto-google-common-protos:1.16.0",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "e6eff21b0a5cc049b0bf2c571fac23abe8dd9d5f9143189f501c04164dc37da2",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "c98eed7be961594b248d5b99c0cfe8e52f8c383239964c0365bdf23c16db6e7d",
        exports = _replace_dependencies([
            "@com_google_protobuf_protobuf_java",
        ], replacements),
        tags = [
            "maven_coordinates=com.google.api.grpc:proto-google-common-protos:1.16.0",
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
        artifact = "com.google.errorprone:error_prone_annotations:2.3.2",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "357cd6cfb067c969226c442451502aee13800a24e950fdfde77bcdb4565a668d",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "7ce688ed1582a67097228c050192b7cfd00479a81d2b921f7cd5116994f1402d",
        exports = _replace_dependencies([
        ], replacements),
        tags = [
            "maven_coordinates=com.google.errorprone:error_prone_annotations:2.3.2",
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
        artifact = "com.google.guava:guava:27.1-jre",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "4a5aa70cc968a4d137e599ad37553e5cfeed2265e8c193476d7119036c536fe7",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "9de05c573971cedfcd53fb85fc7a58a5f453053026a9bf18594cffc79a1d6874",
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
            "maven_coordinates=com.google.guava:guava:27.1-jre",
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
        artifact = "com.google.protobuf:protobuf-java:3.7.1",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "22779eacfe0f33b9e8ceffa3cdef5935cae4e53f736e027d912b707522fea645",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "4401d00d4774fadb66a5a122b33b412c1bb790fcb5fc6f2ba81eca4eac76294a",
        exports = _replace_dependencies([
        ], replacements),
        tags = [
            "maven_coordinates=com.google.protobuf:protobuf-java:3.7.1",
        ],
    )

def com_google_truth_extensions_truth_java8_extension(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "com_google_truth_extensions_truth_java8_extension",
        artifact = "com.google.truth.extensions:truth-java8-extension:0.44",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "da4d94dc952c65914bf9e3c0b4f4a05f5aea536aa5de9a4660b859409dda4bad",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "5ec7e2db5dabc9bfc2fe53e84b0d7e51146abd6fde69cab659f93e34d5ebf4b4",
        exports = _replace_dependencies([
            "@com_google_truth_truth",
            "@org_checkerframework_checker_compat_qual",
        ], replacements),
        tags = [
            "maven_coordinates=com.google.truth.extensions:truth-java8-extension:0.44",
        ],
    )

def com_google_truth_extensions_truth_liteproto_extension(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "com_google_truth_extensions_truth_liteproto_extension",
        artifact = "com.google.truth.extensions:truth-liteproto-extension:0.44",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "49aa51bb8a356a618c9a2fa982a67e4efadc4e266033e20cd984ce72c2f7f01a",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "25855a71a50dc9b35b9520f969d6cd3c47d62c4aa7f17e39063e15833e9100e6",
        exports = _replace_dependencies([
            "@com_google_truth_truth",
            "@com_google_guava_guava",
            "@com_google_errorprone_error_prone_annotations",
            "@org_checkerframework_checker_compat_qual",
            "@com_google_auto_value_auto_value_annotations",
        ], replacements),
        tags = [
            "maven_coordinates=com.google.truth.extensions:truth-liteproto-extension:0.44",
        ],
    )

def com_google_truth_extensions_truth_proto_extension(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "com_google_truth_extensions_truth_proto_extension",
        artifact = "com.google.truth.extensions:truth-proto-extension:0.44",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "d964495cee74d6933512c7b414c8723285a6413a4e3f46f558fbaf624dfd7c9f",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "82075e50fe1f4246aac1bbdaa9f36bda9a40cab1376e78e235d1032b335f4268",
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
            "maven_coordinates=com.google.truth.extensions:truth-proto-extension:0.44",
        ],
    )

def com_google_truth_truth(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "com_google_truth_truth",
        artifact = "com.google.truth:truth:0.44",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "a9e6796786c9c77a5fe19b08e72fe0a620d53166df423d8861af9ebef4dc4247",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "427b1ea595dbdb4483e4ba10b095026cd243776a648b3bb78362dc8ddb542411",
        exports = _replace_dependencies([
            "@com_google_guava_guava",
            "@junit_junit",
            "@com_google_errorprone_error_prone_annotations",
            "@org_checkerframework_checker_compat_qual",
            "@com_google_auto_value_auto_value_annotations",
            "@com_googlecode_java_diff_utils_diffutils",
        ], replacements),
        tags = [
            "maven_coordinates=com.google.truth:truth:0.44",
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

def io_grpc_grpc_context(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "io_grpc_grpc_context",
        artifact = "io.grpc:grpc-context:1.20.0",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "fe1294d0705433a3f5b64b568307609018ecc0840bbdbaa0f4db49fa1f933f05",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "0af7e53f01c3d5cabe6226dc63ac5d789bd7e301c4dc235edb7847e38429573c",
        exports = _replace_dependencies([
        ], replacements),
        tags = [
            "maven_coordinates=io.grpc:grpc-context:1.20.0",
        ],
    )

def io_grpc_grpc_core(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "io_grpc_grpc_core",
        artifact = "io.grpc:grpc-core:1.20.0",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "a8dfda813f7152255a4e252193f8d8e5979e099bf8b3d9ef9f66c8032a236255",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "223a1f990cc2aeaf4b8986c4c99afbc77b43b6ea7c920a0c513503b5d5cdd801",
        exports = _replace_dependencies([
            "@com_google_guava_guava",
            "@com_google_code_findbugs_jsr305",
            "@com_google_errorprone_error_prone_annotations",
            "@org_codehaus_mojo_animal_sniffer_annotations",
            "@io_grpc_grpc_context",
            "@com_google_code_gson_gson",
            "@com_google_android_annotations",
            "@io_opencensus_opencensus_contrib_grpc_metrics",
            "@io_opencensus_opencensus_api",
        ], replacements),
        tags = [
            "maven_coordinates=io.grpc:grpc-core:1.20.0",
        ],
    )

def io_grpc_grpc_stub(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "io_grpc_grpc_stub",
        artifact = "io.grpc:grpc-stub:1.20.0",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "3b5d3e58684d336a6297c58efe396fb7ee87e0d706a34efa44c83c1bc7999950",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "ee9c49f28b90b3135c7e87df792f1e2815023205ca913d25770bad8af6c700ce",
        exports = _replace_dependencies([
            "@io_grpc_grpc_core",
        ], replacements),
        tags = [
            "maven_coordinates=io.grpc:grpc-stub:1.20.0",
        ],
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
        srcjar_sha256 = "a8cde5809e483d357bd08661bb5279403fca006a6a97db91b7681e82a5f6a7d0",
        exports = _replace_dependencies([
        ], replacements),
        tags = [
            "maven_coordinates=io.opencensus:opencensus-api:0.19.2",
        ],
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
        srcjar_sha256 = "7004c3dc5f702d6247cc63c702e0de43cc7734d2686749ab37c58311c0464a79",
        exports = _replace_dependencies([
            "@io_opencensus_opencensus_api",
        ], replacements),
        tags = [
            "maven_coordinates=io.opencensus:opencensus-contrib-grpc-metrics:0.19.2",
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
        artifact = "net.bytebuddy:byte-buddy:1.9.12",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "3688c3d434bebc3edc5516296a2ed0f47b65e451071b4afecad84f902f0efc11",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "048ea8b9e6c177033e0ff06157471c50b625a03a2a838a393dfeb61314abf034",
        exports = _replace_dependencies([
        ], replacements),
        tags = [
            "maven_coordinates=net.bytebuddy:byte-buddy:1.9.12",
        ],
    )

def net_bytebuddy_byte_buddy_agent(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "net_bytebuddy_byte_buddy_agent",
        artifact = "net.bytebuddy:byte-buddy-agent:1.9.12",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "a9c89697162c0a28f97f60b507458deefe6fb65f0b74c5d49358000dd6afacd0",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "d81b86afbbdb41ef14d0062517b7acb8b804ae1f48b1c69127581f044cdc1391",
        exports = _replace_dependencies([
        ], replacements),
        tags = [
            "maven_coordinates=net.bytebuddy:byte-buddy-agent:1.9.12",
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
        artifact = "org.checkerframework:checker-qual:2.5.2",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "64b02691c8b9d4e7700f8ee2e742dce7ea2c6e81e662b7522c9ee3bf568c040a",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "821c5c63a6f156a3bb498c5bbb613580d9d8f4134131a5627d330fc4018669d2",
        exports = _replace_dependencies([
        ], replacements),
        tags = [
            "maven_coordinates=org.checkerframework:checker-qual:2.5.2",
        ],
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
        srcjar_sha256 = "2571474a676f775a8cdd15fb9b1da20c4c121ed7f42a5d93fca0e7b6e2015b40",
        exports = _replace_dependencies([
        ], replacements),
        tags = [
            "maven_coordinates=org.codehaus.mojo:animal-sniffer-annotations:1.17",
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
        artifact = "org.slf4j:slf4j-api:1.7.26",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "6d9e5b86cfd1dd44c676899285b5bb4fa0d371cf583e8164f9c8a0366553242b",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "9e25ad98a324e6685752fd01fbbd0588ceec5df564e53c49486946a2d19dc482",
        exports = _replace_dependencies([
        ], replacements),
        tags = [
            "maven_coordinates=org.slf4j:slf4j-api:1.7.26",
        ],
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
        srcjar_sha256 = "53338bc1f4f54a22e24cae2d18e0e9e621547189009dcb73984ff0b2cc0b9e37",
        exports = _replace_dependencies([
            "@org_springframework_security_spring_security_crypto",
        ], replacements),
        tags = [
            "maven_coordinates=org.springframework.cloud:spring-cloud-commons:2.1.1.RELEASE",
        ],
    )

def org_springframework_security_spring_security_crypto(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "org_springframework_security_spring_security_crypto",
        artifact = "org.springframework.security:spring-security-crypto:5.1.5.RELEASE",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "90c18376c1ec4a981f5a155898d64e73664fa136085cbd0b4d760ac27be59c04",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "363ce3f42c8a5984ea5a62022f40a3fad32f71711129274c673429f9e4978fd1",
        exports = _replace_dependencies([
        ], replacements),
        tags = [
            "maven_coordinates=org.springframework.security:spring-security-crypto:5.1.5.RELEASE",
        ],
    )

def org_springframework_spring_core(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "org_springframework_spring_core",
        artifact = "org.springframework:spring-core:5.1.6.RELEASE",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "4d5577bef039ad08955cd34def6d87c21cbb52a9aa7cd8f9d6ecc5dbe4b4258d",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "e04ac27db15d2dd7e20acc6cf3a7fb1e19e8844e28897d6c57ca53cae6e54855",
        exports = _replace_dependencies([
            "@org_springframework_spring_jcl",
        ], replacements),
        tags = [
            "maven_coordinates=org.springframework:spring-core:5.1.6.RELEASE",
        ],
    )

def org_springframework_spring_jcl(fetch_sources, replacements):
    jvm_maven_import_external(
        name = "org_springframework_spring_jcl",
        artifact = "org.springframework:spring-jcl:5.1.6.RELEASE",
        server_urls = [
            "https://jcenter.bintray.com/",
        ],
        artifact_sha256 = "cf9c6a6af363bd67faaa93d3d81d4601a8562870d3ad52d1a55b9990924f1509",
        licenses = ["notice"],
        fetch_sources = fetch_sources,
        srcjar_sha256 = "e3b3d3e1314cb71b024461953534d3bb10f243b03614af1df51a43d1e1d75c7e",
        exports = _replace_dependencies([
        ], replacements),
        tags = [
            "maven_coordinates=org.springframework:spring-jcl:5.1.6.RELEASE",
        ],
    )
