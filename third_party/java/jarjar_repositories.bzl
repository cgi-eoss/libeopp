# Copyright (C) 2018 The Google Bazel Common Authors.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
# http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
"""A subset of the google_bazel_common repositories for jarjar dependencies only"""

load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")
load("@bazel_tools//tools/build_defs/repo:java.bzl", "java_import_external")

_MAVEN_MIRRORS = [
    #"http://bazel-mirror.storage.googleapis.com/repo1.maven.org/maven2/",
    "https://repo1.maven.org/maven2/",
    "http://maven.ibiblio.org/maven2/",
]

def maven_import(group_id, artifact_id, version, sha256, licenses, **kwargs):
    """Import a JAR indexed by Maven.
    The target will be tagged so that a pom.xml dependency entry can be
    reconstructed from it.
    Args:
      group_id: (string) Group ID of the Maven JAR.
      artifact_id: (string) Atifact ID of the Maven JAR.
      version: (string) Version number of the Maven JAR.
      sha256: (string) The SHA256 hash of the JAR being imported.
      licenses: (List[string]) License types of the imported project.
      **kwargs: Other args to java_import_external
    Defines:
      <group_id>_<artifact_id>: (java_import_external) The imported library.
    """

    name = "{0}_{1}".format(group_id, artifact_id).replace(".", "_").replace("-", "_")
    url_suffix = "{0}/{1}/{2}/{1}-{2}.jar".format(group_id.replace(".", "/"), artifact_id, version)
    coordinates = "{0}:{1}:{2}".format(group_id, artifact_id, version)

    # TODO(cpovirk): Consider jvm_maven_import_external.
    java_import_external(
        name = name,
        jar_urls = [base + url_suffix for base in _MAVEN_MIRRORS],
        jar_sha256 = sha256,
        licenses = licenses,
        # TODO(cpovirk): Remove after https://github.com/bazelbuild/bazel/issues/10838 is fixed.
        rule_load = """load("@rules_java//java:defs.bzl", "java_import")""",
        tags = ["maven_coordinates=" + coordinates],
        **kwargs
    )

def jarjar_repositories():
    maven_import(
        group_id = "javax.annotation",
        artifact_id = "jsr250-api",
        version = "1.0",
        licenses = ["notice"],
        sha256 = "a1a922d0d9b6d183ed3800dfac01d1e1eb159f0e8c6f94736931c1def54a941f",
    )

    maven_import(
        group_id = "javax.inject",
        artifact_id = "javax.inject",
        version = "1",
        licenses = ["notice"],
        sha256 = "91c77044a50c481636c32d916fd89c9118a72195390452c81065080f957de7ff",
    )

    ASM_VERSION = "7.2"

    maven_import(
        group_id = "org.ow2.asm",
        artifact_id = "asm-tree",
        version = ASM_VERSION,
        licenses = ["notice"],
        sha256 = "c063f5a67fa03cdc9bd79fd1c2ea6816cc4a19473ecdfbd9e9153b408c6f2656",
    )

    maven_import(
        group_id = "org.ow2.asm",
        artifact_id = "asm-commons",
        version = ASM_VERSION,
        licenses = ["notice"],
        sha256 = "0e86b8b179c5fb223d1a880a0ff4960b6978223984b94e62e71135f2d8ea3558",
    )

    maven_import(
        group_id = "org.codehaus.plexus",
        artifact_id = "plexus-utils",
        version = "3.0.20",
        licenses = ["notice"],
        sha256 = "8f3a655545fc5b4cbf12b5eb8a154fccb0c1144423a1450511f44005a3d574a2",
    )

    maven_import(
        group_id = "org.codehaus.plexus",
        artifact_id = "plexus-classworlds",
        version = "2.5.2",
        licenses = ["notice"],
        sha256 = "b2931d41740490a8d931cbe0cfe9ac20deb66cca606e679f52522f7f534c9fd7",
    )

    maven_import(
        group_id = "org.codehaus.plexus",
        artifact_id = "plexus-component-annotations",
        version = "1.5.5",
        licenses = ["notice"],
        sha256 = "4df7a6a7be64b35bbccf60b5c115697f9ea3421d22674ae67135dde375fcca1f",
    )

    maven_import(
        group_id = "org.eclipse.sisu",
        artifact_id = "org.eclipse.sisu.plexus",
        version = "0.3.0",
        licenses = ["reciprocal"],
        sha256 = "807e9bc9e28d57ec0cb6daf04c317b3e13de5899c0282ee0f76c009198739350",
    )

    maven_import(
        group_id = "org.eclipse.sisu",
        artifact_id = "org.eclipse.sisu.inject",
        version = "0.3.0",
        licenses = ["reciprocal"],
        sha256 = "11eec6fcc7a47c50c8d7fb7ac69920c33c70cb8df6b7a0d8eb751c813fb1928a",
    )

    maven_import(
        group_id = "org.apache.maven",
        artifact_id = "maven-artifact",
        version = "3.3.3",
        licenses = ["notice"],
        sha256 = "c5d2db20550a3de4e796493876114c3b7717fe560c414135e2508c57b80e9a02",
    )

    maven_import(
        group_id = "org.apache.maven",
        artifact_id = "maven-model",
        version = "3.3.3",
        licenses = ["notice"],
        sha256 = "a7e386687b962b6064f44115052207fc23a2a997742a156dffd0b434237896d8",
    )

    maven_import(
        group_id = "org.apache.maven",
        artifact_id = "maven-plugin-api",
        version = "3.3.3",
        licenses = ["notice"],
        sha256 = "98585500928c4808d17f476e2554432af13ead1ce4720d72a943c0dedecb1fc0",
    )

    maven_import(
        group_id = "javax.enterprise",
        artifact_id = "cdi-api",
        version = "1.0",
        licenses = ["notice"],
        sha256 = "1f10b2204cc77c919301f20ff90461c3df1b6e6cb148be1c2d22107f4851d423",
    )

    maven_import(
        group_id = "org.pantsbuild",
        artifact_id = "jarjar",
        version = "1.7.2",
        licenses = ["notice"],
        sha256 = "0706a455e17b67718abe212e3a77688bbe8260852fc74e3e836d9f2e76d91c27",
    )

    maven_import(
        group_id = "org.apache.ant",
        artifact_id = "ant",
        version = "1.9.6",
        licenses = ["notice"],
        sha256 = "d74de0bc55631476ba8443c07f43c9c51654ed5a1e0c1942ca015724d633e9bf",
    )

    maven_import(
        group_id = "org.apache.ant",
        artifact_id = "ant-launcher",
        version = "1.9.6",
        licenses = ["notice"],
        sha256 = "f2c66a60fdacf78d6537734ef1c8edb77cf6c4532e705ee3482be1d1006c277a",
    )
