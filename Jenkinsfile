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
    image: cgici/eopp-build-container:1.8.0
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
          gerritReview message: "Starting build: ${BUILD_URL}"
          sh "bazel build //..."
        }
      }
    }

    stage('Test') {
      environment {
        EXTRA_TEST_FLAGS = "${CHANGE_ID ? '' : '--nocache_test_results --collect_code_coverage --coverage_report_generator=@bazel_sonarqube//:sonarqube_coverage_generator --combined_report=lcov'}"
      }
      steps {
        container('libeopp-build') {
          sh "bazel test //... --test_output=errors --keep_going \${EXTRA_TEST_FLAGS}"
        }
      }
      post {
        always {
          junit allowEmptyResults: true, testResults: 'bazel-testlogs/**/test.xml'
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
  }

  post {
    success { gerritReview labels: [Verified: 1], message: "Build successful: ${BUILD_URL}" }
    unstable { gerritReview labels: [Verified: -1], message: "Build is unstable: ${BUILD_URL}" }
    failure { gerritReview labels: [Verified: -1], message: "Build failure: ${BUILD_URL}" }
    aborted { gerritReview labels: [Verified: -1], message: "Build was aborted or timed out: ${BUILD_URL}" }
  }
}
