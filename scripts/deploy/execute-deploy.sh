#!/bin/bash

set -eux

readonly MVN_GOAL="$1"
readonly VERSION_NAME="$2"
shift 2
readonly EXTRA_MAVEN_ARGS=("$@")

bazel_output_file() {
  local library=$1
  local output_file=bazel-bin/$library
  if [[ ! -e $output_file ]]; then
     output_file=bazel-genfiles/$library
  fi
  if [[ ! -e $output_file ]]; then
    echo "Could not find bazel output file for $library"
    exit 1
  fi
  echo -n $output_file
}

deploy_library() {
  local pomfile=$1
  local library=$2
  local srcjar=$3
  local javadoc=$4
  bazel build --define=pom_version="$VERSION_NAME" \
    $library $srcjar $javadoc $pomfile

  # Optional deployment components
  if [[ ! -z "$javadoc" ]]; then
    javadoc_arg="-Djavadoc=$(bazel_output_file $javadoc)"
  else
    javadoc_arg=""
  fi

  if [[ ! -z "$srcjar" ]]; then
    srcjar_arg="-Dsources=$(bazel_output_file $srcjar)"
  else
    srcjar_arg=""
  fi

  mvn $MVN_GOAL \
    "${EXTRA_MAVEN_ARGS[@]:+${EXTRA_MAVEN_ARGS[@]}}" \
    -DpomFile=$(bazel_output_file $pomfile) \
    -Dfile=$(bazel_output_file $library) \
    ${javadoc_arg} \
    ${srcjar_arg}
}

deploy_library \
    file/pom.xml \
    file/libfile_deploy.jar \
    file/libfile-src.jar \
    file/libfile-javadoc.jar

deploy_library \
    util/pom.xml \
    util/libutil.jar \
    util/libutil-src.jar \
    util/libutil-javadoc.jar

deploy_library \
    rpc/pom.xml \
    rpc/librpc.jar \
    rpc/librpc-src.jar \
    rpc/librpc-javadoc.jar


deploy_library \
    pom.xml \
    libeopp.jar \
    "" \
    ""
