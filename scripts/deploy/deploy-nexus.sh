#!/bin/bash

set -eu

version_name=$1

if [[ "$version_name" =~ " " ]]; then
  echo "Version name must not have any spaces"
  exit 1
fi

echo -e "Deploying to CGI-EOSS Nexus...\n"

for target in $(bazel query 'kind(maven_publish, //...)' 2>/dev/null); do
  bazel run \
    --stamp \
    --define=pom_version="$version_name" \
    --define=maven_repo="https://nexus.observing.earth/repository/maven-cgi-eoss" \
    --define=maven_user="${MAVEN_USER}" \
    --define=maven_password="${MAVEN_PASSWORD}" \
    "$target"
done

echo -e "Deployed to CGI-EOSS Nexus"
