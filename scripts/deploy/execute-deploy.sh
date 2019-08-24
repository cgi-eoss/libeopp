#!/bin/bash

set -eu

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
    pom.xml \
    libeopp.jar \
    "" \
    ""

deploy_library \
    file/file_pom.xml \
    file/libfile_deploy.jar \
    file/libfile-src.jar \
    file/libfile-javadoc.jar

OLDIFS=$IFS
IFS=','

for target in util,util rpc,rpc resource,resource resource,path_resource resolver,resolver resolver,path_resolver
do
    set -- $target
    deploy_library \
        $1/$2_pom.xml \
        $1/lib$2.jar \
        $1/lib$2-src.jar \
        $1/lib$2-javadoc.jar
done

IFS=$OLDIFS
