def baseBranch = 'master'

pipeline {
  options {
    buildDiscarder(logRotator(artifactDaysToKeepStr: '', artifactNumToKeepStr: '1', daysToKeepStr: '30', numToKeepStr: ''))
    timeout(time: 1, unit: 'HOURS')
    timestamps()
  }
  agent {
    kubernetes {
      label 'libeopp-build'
      yaml """
apiVersion: v1
kind: Pod
metadata:
  annotations:
    cluster-autoscaler.kubernetes.io/safe-to-evict: "false"
spec:
  securityContext:
    runAsUser: 10000
  containers:
  - name: libeopp-build
    image: cgici/eopp-build-container:1.15.0
    imagePullPolicy: IfNotPresent
    command:
    - cat
    tty: true
    resources:
      requests:
        cpu: 2
        memory: 4Gi
"""
    }
  }

  stages {
    stage('Build') {
      steps {
        container('libeopp-build') {
          gerritReview message: "Starting build: ${env.BUILD_URL}"
          sh "bazel build //..."
          sh "./scripts/deploy/install-local-snapshot.sh"
        }
      }
    }

    stage('Test') {
      environment {
        EXTRA_TEST_FLAGS = "${CHANGE_ID ? '' : '--nocache_test_results'}"
      }
      steps {
        container('libeopp-build') {
          sh "bazel coverage //... --test_output=errors --keep_going --noremote_accept_cached \${EXTRA_TEST_FLAGS}"
        }
      }
      post {
        always {
          container('libeopp-build') {
            sh "find bazel-testlogs -follow -name test.xml -exec bash -c 'path={}; d=tmp/testlogs/\$(dirname \$path); mkdir -p \$d ; cp \$path \$d' \\;"
          }
          junit allowEmptyResults: true, testResults: 'tmp/testlogs/**/test.xml'
        }
      }
    }

    stage('Deploy') {
      when { tag '' }
      steps {
        container('libeopp-build') {
          configFileProvider([configFile(fileId: 'b756e5e9-d77e-4627-ac35-2cbd08efac8b', variable: 'MAVEN_SETTINGS')]) {
            sh "./scripts/deploy/deploy-nexus.sh ${TAG_NAME} -s ${MAVEN_SETTINGS}"
          }
        }
      }
    }

    stage('SQ Analysis') {
      environment {
        BRANCH_ARGS = "${CHANGE_ID ? "-Dsonar.branch.name=${BRANCH_NAME} -Dsonar.branch.target=${baseBranch}" : "-Dsonar.branch.name=${BRANCH_NAME}"}"
        VERSION_ARGS = "${TAG_NAME ? "-Dsonar.projectVersion=${TAG_NAME}" : "-Dsonar.projectVersion=${baseBranch}"}"
        ABORT_ON_QUALITY_GATE = "${CHANGE_ID ? "true" : "false"}"
      }
      steps {
        container('libeopp-build') {
          withSonarQubeEnv('CGI CI SonarQube') {
            sh "bazel run //:sq_libeopp -- -Dsonar.host.url=${SONAR_HOST_URL} -Dsonar.login=${SONAR_AUTH_TOKEN} \${BRANCH_ARGS} \${VERSION_ARGS}"
            sh "mkdir -p tmp/ && cp --target-directory=tmp/ --dereference bazel-bin/sq_libeopp.runfiles/com_cgi_eoss_eopp/.scannerwork/report-task.txt" // Jenkins can't find things outside the workspace, so copy the SQ marker locally
          }
        }
        waitForQualityGate abortPipeline: ABORT_ON_QUALITY_GATE.toBoolean()
      }
    }
  }

  post {
    success { gerritReview labels: [Verified: 1], message: "Build successful: ${env.BUILD_URL}" }
    unstable { gerritReview labels: [Verified: -1], message: "Build is unstable: ${env.BUILD_URL}" }
    failure { gerritReview labels: [Verified: -1], message: "Build failure: ${env.BUILD_URL}" }
    aborted { gerritReview labels: [Verified: -1], message: "Build was aborted or timed out: ${env.BUILD_URL}" }
  }
}
