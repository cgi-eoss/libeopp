load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")
load("@bazel_tools//tools/build_defs/repo:utils.bzl", "maybe")
load("//third_party/grpc:grpc_repositories.bzl", "grpc_repositories")
load("//third_party/protobuf:protobuf_repositories.bzl", "protobuf_repositories")

def libeopp_dependencies():
    maybe(
        http_archive,
        name = "platforms",
        urls = [
            "https://mirror.bazel.build/github.com/bazelbuild/platforms/releases/download/0.0.10/platforms-0.0.10.tar.gz",
            "https://github.com/bazelbuild/platforms/releases/download/0.0.10/platforms-0.0.10.tar.gz",
        ],
        sha256 = "218efe8ee736d26a3572663b374a253c012b716d8af0c07e842e82f238a0a7ee",
    )

    maybe(
        http_archive,
        name = "google_bazel_common",
        sha256 = "fb7504d7307126767ce68b0335303c6ecc989b1d72d76a16c46251f3c8958865",
        strip_prefix = "bazel-common-b41d50b173bc8121812485410899a241be7815cf",
        urls = ["https://github.com/google/bazel-common/archive/b41d50b173bc8121812485410899a241be7815cf.tar.gz"],
    )

    maybe(
        http_archive,
        name = "bazel_skylib",
        sha256 = "bc283cdfcd526a52c3201279cda4bc298652efa898b10b4db0837dc51652756f",
        urls = [
            "https://mirror.bazel.build/github.com/bazelbuild/bazel-skylib/releases/download/1.7.1/bazel-skylib-1.7.1.tar.gz",
            "https://github.com/bazelbuild/bazel-skylib/releases/download/1.7.1/bazel-skylib-1.7.1.tar.gz",
        ],
    )

    maybe(
        http_archive,
        name = "bazel_features",
        sha256 = "5ac743bf5f05d88e84962e978811f2524df09602b789c92cf7ae2111ecdeda94",
        strip_prefix = "bazel_features-1.14.0",
        url = "https://github.com/bazel-contrib/bazel_features/releases/download/v1.14.0/bazel_features-v1.14.0.tar.gz",
    )

    maybe(
        http_archive,
        name = "aspect_bazel_lib",
        sha256 = "714cf8ce95a198bab0a6a3adaffea99e929d2f01bf6d4a59a2e6d6af72b4818c",
        strip_prefix = "bazel-lib-2.7.8",
        url = "https://github.com/aspect-build/bazel-lib/releases/download/v2.7.8/bazel-lib-v2.7.8.tar.gz",
    )

    HERMETIC_CC_TOOLCHAIN_VERSION = "v3.1.0"
    HERMETIC_CC_TOOLCHAIN_SHA = "df091afc25d73b0948ed371d3d61beef29447f690508e02bc24e7001ccc12d38"

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
        sha256 = "778aaeab3e6cfd56d681c89f5c10d7ad6bf8d2f1a72de9de55b23081b2d31618",
        strip_prefix = "rules_python-0.34.0",
        url = "https://github.com/bazelbuild/rules_python/releases/download/0.34.0/rules_python-0.34.0.tar.gz",
    )

    maybe(
        http_archive,
        name = "rules_pkg",
        urls = [
            "https://mirror.bazel.build/github.com/bazelbuild/rules_pkg/releases/download/1.0.1/rules_pkg-1.0.1.tar.gz",
            "https://github.com/bazelbuild/rules_pkg/releases/download/1.0.1/rules_pkg-1.0.1.tar.gz",
        ],
        sha256 = "d20c951960ed77cb7b341c2a59488534e494d5ad1d30c4818c736d57772a9fef",
    )

    maybe(
        http_archive,
        name = "rules_java",
        urls = [
            "https://github.com/bazelbuild/rules_java/releases/download/7.7.2/rules_java-7.7.2.tar.gz",
        ],
        sha256 = "3471c79b7fd6f657da8ead0e847b38cdf4a3f16d4ffd9fecbd1ae536eaca0187",
    )

    maybe(
        http_archive,
        name = "rules_proto",
        sha256 = "6fb6767d1bef535310547e03247f7518b03487740c11b6c6adb7952033fe1295",
        strip_prefix = "rules_proto-6.0.2",
        url = "https://github.com/bazelbuild/rules_proto/releases/download/6.0.2/rules_proto-6.0.2.tar.gz",
    )

    RULES_JVM_EXTERNAL_TAG = "6.2"
    RULES_JVM_EXTERNAL_SHA = "808cb5c30b5f70d12a2a745a29edc46728fd35fa195c1762a596b63ae9cebe05"

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
        sha256 = "3b772976fec7bdcda1d84b9d39b176589424c047eb2175bed09aac630e50af43",
        url = "https://github.com/bazelbuild/rules_kotlin/releases/download/v1.9.6/rules_kotlin-v1.9.6.tar.gz",
    )

    maybe(
        http_archive,
        name = "bazel_sonarqube",
        sha256 = "bc84bca13420dfff00fbe2d57f35690798a3ebde0c035d32c267759c67c4e31b",
        url = "https://github.com/Zetten/bazel-sonarqube/releases/download/1.0.2/bazel_sonarqube.1.0.2.tar.gz",
    )
