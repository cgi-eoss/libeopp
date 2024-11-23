#!/bin/bash

set -eu

loc=$(dirname "$(readlink -f "$0")")
workspace=$(readlink -f "${loc}/../..")
cd "$workspace"

classes_path=$(readlink -m "${workspace}/target/classes")
rm -rf "$classes_path" && mkdir -p "$classes_path"

report_path=$(readlink -m "${workspace}/target/pitest-report")
rm -rf "$report_path"

# Ensure we have up-to-date test reports
printf "\nExecuting tests under pitest\n\n"
readarray -t pitest_targets < <(bazel query 'attr("tags", "pitest", tests(//...))' --output=label 2>/dev/null)
bazel test "${pitest_targets[@]}"

# Find all the pitest outputs
printf "\nLocating test dependencies to provide classes for pitest aggregation\n\n"
pitest_outputs=()
dep_targets=()
for t in "${pitest_targets[@]}"; do
  x=${t:2}
  test_pkg=${x%\:*}
  test_name=${x#*:}
  pitest_outputs=("${pitest_outputs[@]}" "${workspace}/bazel-testlogs/${test_pkg}/${test_name}/test.outputs/outputs.zip")

  # Find all the dependencies of this test target containing classes
  mapfile -t deps < <(bazel query "kind('maven_project_jar', deps(${t}) intersect //...)" --output=label 2>/dev/null)
  dep_targets+=("${deps[@]}")
done

# Select the unique set of dependency targets to build
read -ra dep_targets <<<"$(tr ' ' '\n' <<<"${dep_targets[@]}" | sort -u | tr '\n' ' ')"

# Build all dep_targets and get the output jar paths
bazel build "${dep_targets[@]}"
for jar in $(bazel cquery "set(${dep_targets[*]})" --output=files 2>/dev/null); do
  unzip -qo "$(readlink -f "$jar")" -d "$classes_path"
done

printf "\nRunning pitest aggregation\n\n"
bazel run //tools/pitest:pitest_report_aggregator -- "$report_path" "$workspace" "$classes_path" "${pitest_outputs[@]}"
