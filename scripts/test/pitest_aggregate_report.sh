#!/bin/bash

set -eu

loc=$(dirname "$(readlink -f "$0")")
workspace=$(readlink -f "${loc}/../..")

classes_path=$(readlink -m "${workspace}/target/classes")
rm -rf "$classes_path" && mkdir -p "$classes_path"

report_path=$(readlink -m "${workspace}/target/pitest-report")
rm -rf "$report_path"

# Ensure we have up-to-date test reports
readarray -t pitest_targets < <(bazel query 'attr("tags", "pitest", tests(//...))' --output=label 2>/dev/null)
bazel test "${pitest_targets[@]}"

# Find all the pitest outputs
pitest_outputs=()
for t in ${pitest_targets[*]}; do
  x=${t:2}
  test_pkg=${x%\:*}
  test_name=${x#*:}
  pitest_outputs=("${pitest_outputs[@]}" "${workspace}/bazel-testlogs/${test_pkg}/${test_name}/test.outputs/outputs.zip")

  # Extract all the classes depended on by this test target
  for dep in $(bazel query "kind('(java|kt_jvm)_library', deps(${t}) intersect //...)" --output=label 2>/dev/null); do
    jar=$(readlink -f "$(bazel build "$dep" 2>&1 | grep "^ " | head -1 | tr -d '[:space:]')")
    unzip -qo "$jar" -d "$classes_path"
  done
done

bazel run //tools/pitest:pitest_report_aggregator -- "$report_path" "$workspace" "$classes_path" "${pitest_outputs[@]}"
