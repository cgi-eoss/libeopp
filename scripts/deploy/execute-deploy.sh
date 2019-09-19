#!/bin/bash

set -eux

readonly MVN_GOAL="$1"
readonly VERSION_NAME="$2"
shift 2
readonly EXTRA_MAVEN_ARGS=("$@")

bazel_output_file() {
  local artifact=$1
  local output_file=bazel-bin/$artifact
  if [[ ! -e $output_file ]]; then
     output_file=bazel-genfiles/$artifact
  fi
  if [[ ! -e $output_file ]]; then
    echo "Could not find bazel output file for $artifact"
    exit 1
  fi
  echo -n $output_file
}

deploy_library() {
  # Ensure libraries are built and get their paths
  pomfile=$(bazel build --define=pom_version="$VERSION_NAME" $1 2>&1 | grep "^ " | head -1 | tr -d '[:space:]')
  artifact=$(bazel build --define=pom_version="$VERSION_NAME" $2 2>&1 | grep "^ " | head -1  | tr -d '[:space:]')

  # Optional deployment components
  if [[ "${3:-}" ]]; then
    srcjar_arg="-Dsources=$(bazel build --define=pom_version="$VERSION_NAME" $3 2>&1 | grep "^ " | head -1  | tr -d '[:space:]')"
  else
    srcjar_arg=""
  fi

  if [[ "${4:-}" ]]; then
    javadoc_arg="-Djavadoc=$(bazel build --define=pom_version="$VERSION_NAME" $4 2>&1 | grep "^ " | head -1  | tr -d '[:space:]')"
  else
    javadoc_arg=""
  fi

  mvn $MVN_GOAL \
    "${EXTRA_MAVEN_ARGS[@]:+${EXTRA_MAVEN_ARGS[@]}}" \
    -DpomFile=$pomfile \
    -Dfile=$artifact \
    ${javadoc_arg} \
    ${srcjar_arg}
}


for pomfile in $(bazel query "kind(pom_file, attr('tags', 'maven_coordinates.*', //...) except //third_party/...)" 2>/dev/null); do
  maven_coordinates=$(bazel query $pomfile --output=build 2>/dev/null | grep maven_coordinates | sed 's/^.*maven_coordinates=\([^"]*\)".*/\1/' 2>/dev/null)

  artifact=$(bazel query "attr(tags, maven_artifact, attr(tags, 'maven_coordinates=$(printf '%q\n' "$maven_coordinates")', //...))" 2>/dev/null)
  srcjar=$(bazel query "attr(tags, maven_srcjar, attr(tags, 'maven_coordinates=$(printf '%q\n' "$maven_coordinates")', //...))" 2>/dev/null)
  javadoc=$(bazel query "attr(tags, maven_javadoc, attr(tags, 'maven_coordinates=$(printf '%q\n' "$maven_coordinates")', //...))" 2>/dev/null)

  deploy_library \
    $pomfile \
    $artifact \
    $srcjar \
    $javadoc
done
