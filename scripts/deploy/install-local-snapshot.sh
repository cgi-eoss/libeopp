#!/bin/bash

set -eu

echo -e "Installing maven snapshot locally...\n"

for target in $(bazel query 'kind(maven_publish, //...)' 2>/dev/null); do
  bazel run \
    --stamp \
    --define=pom_version=LOCAL_SNAPSHOT \
    --define=maven_repo="file://${HOME}/.m2/repository" \
    "$target"
done

echo -e "Installed local snapshot"
