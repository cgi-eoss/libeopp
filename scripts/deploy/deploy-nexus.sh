#!/bin/bash

set -eu

version_name=$1
shift 1


if [[ "$version_name" =~ " " ]]; then
  echo "Version name must not have any spaces"
  exit 1
fi

echo -e "Deploying to CGI-EOSS Nexus...\n"

bash $(dirname $0)/execute-deploy.sh \
  "deploy:deploy-file" \
  "$version_name" \
  "-DrepositoryId=maven-cgi-eoss" \
  "-Durl=https://nexus.observing.earth/repository/maven-cgi-eoss/"

echo -e "Deployed to CGI-EOSS Nexus"
