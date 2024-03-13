load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")
load("@bazel_tools//tools/build_defs/repo:utils.bzl", "maybe")
load("//third_party/protobuf:protobuf_repositories.bzl", "protobuf_repositories")
load("//third_party/grpc:grpc_repositories.bzl", "grpc_repositories")

def libeopp_dependencies():
    maybe(
        http_archive,
        name = "platforms",
        urls = [
            "https://mirror.bazel.build/github.com/bazelbuild/platforms/releases/download/0.0.8/platforms-0.0.8.tar.gz",
            "https://github.com/bazelbuild/platforms/releases/download/0.0.8/platforms-0.0.8.tar.gz",
        ],
        sha256 = "8150406605389ececb6da07cbcb509d5637a3ab9a24bc69b1101531367d89d74",
    )

    maybe(
        http_archive,
        name = "google_bazel_common",
        sha256 = "6408e7dd11456b64a17fd63ed562045aae2b10eff29db9128d52c6eb85f5c140",
        strip_prefix = "bazel-common-5a98ecc1193057db05a19c19a5853f95703749c4",
        urls = ["https://github.com/google/bazel-common/archive/5a98ecc1193057db05a19c19a5853f95703749c4.tar.gz"],
    )

    maybe(
        http_archive,
        name = "bazel_skylib",
        sha256 = "cd55a062e763b9349921f0f5db8c3933288dc8ba4f76dd9416aac68acee3cb94",
        urls = [
            "https://mirror.bazel.build/github.com/bazelbuild/bazel-skylib/releases/download/1.5.0/bazel-skylib-1.5.0.tar.gz",
            "https://github.com/bazelbuild/bazel-skylib/releases/download/1.5.0/bazel-skylib-1.5.0.tar.gz",
        ],
    )

    maybe(
        http_archive,
        name = "aspect_bazel_lib",
        sha256 = "6c25c59581041ede31e117693047f972cc4700c89acf913658dc89d04c338f8d",
        strip_prefix = "bazel-lib-2.5.3",
        url = "https://github.com/aspect-build/bazel-lib/releases/download/v2.5.3/bazel-lib-v2.5.3.tar.gz",
    )

    HERMETIC_CC_TOOLCHAIN_VERSION = "v3.0.1"
    HERMETIC_CC_TOOLCHAIN_SHA = "3bc6ec127622fdceb4129cb06b6f7ab098c4d539124dde96a6318e7c32a53f7a"

    maybe(
        http_archive,
        name = "hermetic_cc_toolchain",
        sha256 = HERMETIC_CC_TOOLCHAIN_SHA,
        urls = [
            "https://mirror.bazel.build/github.com/uber/hermetic_cc_toolchain/releases/download/{0}/hermetic_cc_toolchain-{0}.tar.gz".format(HERMETIC_CC_TOOLCHAIN_VERSION),
            "https://github.com/uber/hermetic_cc_toolchain/releases/download/{0}/hermetic_cc_toolchain-{0}.tar.gz".format(HERMETIC_CC_TOOLCHAIN_VERSION),
        ],
    )

    protobuf_repositories()

    grpc_repositories()

    maybe(
        http_archive,
        name = "rules_cc",
        urls = ["https://github.com/bazelbuild/rules_cc/releases/download/0.0.9/rules_cc-0.0.9.tar.gz"],
        sha256 = "2037875b9a4456dce4a79d112a8ae885bbc4aad968e6587dca6e64f3a0900cdf",
        strip_prefix = "rules_cc-0.0.9",
    )

    maybe(
        http_archive,
        name = "rules_python",
        sha256 = "c68bdc4fbec25de5b5493b8819cfc877c4ea299c0dcb15c244c5a00208cde311",
        strip_prefix = "rules_python-0.31.0",
        url = "https://github.com/bazelbuild/rules_python/releases/download/0.31.0/rules_python-0.31.0.tar.gz",
    )

    maybe(
        http_archive,
        name = "rules_pkg",
        urls = [
            "https://github.com/bazelbuild/rules_pkg/releases/download/0.10.1/rules_pkg-0.10.1.tar.gz",
        ],
        sha256 = "d250924a2ecc5176808fc4c25d5cf5e9e79e6346d79d5ab1c493e289e722d1d0",
    )

    maybe(
        http_archive,
        name = "rules_java",
        urls = [
            "https://github.com/bazelbuild/rules_java/releases/download/7.4.0/rules_java-7.4.0.tar.gz",
        ],
        sha256 = "976ef08b49c929741f201790e59e3807c72ad81f428c8bc953cdbeff5fed15eb",
    )

    maybe(
        http_archive,
        name = "rules_proto",
        sha256 = "903af49528dc37ad2adbb744b317da520f133bc1cbbecbdd2a6c546c9ead080b",
        strip_prefix = "rules_proto-6.0.0-rc0",
        url = "https://github.com/bazelbuild/rules_proto/releases/download/6.0.0-rc0/rules_proto-6.0.0-rc0.tar.gz",
    )

    RULES_JVM_EXTERNAL_TAG = "6.0"
    RULES_JVM_EXTERNAL_SHA = "85fd6bad58ac76cc3a27c8e051e4255ff9ccd8c92ba879670d195622e7c0a9b7"

    maybe(
        http_archive,
        name = "rules_jvm_external",
        strip_prefix = "rules_jvm_external-%s" % RULES_JVM_EXTERNAL_TAG,
        sha256 = RULES_JVM_EXTERNAL_SHA,
        url = "https://github.com/bazelbuild/rules_jvm_external/releases/download/%s/rules_jvm_external-%s.tar.gz" % (RULES_JVM_EXTERNAL_TAG, RULES_JVM_EXTERNAL_TAG),
    )

    maybe(
        http_archive,
        name = "rules_kotlin",
        sha256 = "d9898c3250e0442436eeabde4e194c30d6c76a4a97f517b18cefdfd4e345725a",
        url = "https://github.com/bazelbuild/rules_kotlin/releases/download/v1.9.1/rules_kotlin-v1.9.1.tar.gz",
    )

    maybe(
        http_archive,
        name = "bazel_sonarqube",
        sha256 = "5b2d6e2f83b9fb375dd422b2c6571ac223d89b6e1915f5d0b8e37aa04354ba32",
        strip_prefix = "bazel-sonarqube-d6109e6627a00ad84c24c38df5d2d17159203f47",
        urls = ["https://github.com/Zetten/bazel-sonarqube/archive/d6109e6627a00ad84c24c38df5d2d17159203f47.zip"],
    )
