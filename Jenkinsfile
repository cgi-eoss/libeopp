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
        EXTRA_TEST_FLAGS = "${CHANGE_ID ? '' : '--nocache_test_results'}"
      }
      steps {
        container('libeopp-build') {
          sh "bazel coverage //... --test_output=errors --keep_going --noremote_accept_cached \${EXTRA_TEST_FLAGS}"
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

    stage('SQ Analysis') {
      when {
        branch 'master' // TODO Remove when the project exists in SQ and per-branch analysis is enabled
      }
      environment {
        BRANCH_ARGS = "${CHANGE_ID ? "-Dsonar.branch.name=${BRANCH_NAME} -Dsonar.branch.target=${baseBranch}" : "-Dsonar.branch.name=${BRANCH_NAME}"}"
      }
      steps {
        container('libeopp-build') {
          withSonarQubeEnv('CGI CI SonarQube') {
            sh "bazel run //:sq_libeopp -- -Dsonar.host.url=${SONAR_HOST_URL} -Dsonar.login=${SONAR_AUTH_TOKEN} \${BRANCH_ARGS}"
          }
        }
        script {
          def qg = waitForQualityGate() // Reuse taskId previously collected by withSonarQubeEnv
          gerritReview message: "SonarQube Quality Gate status: ${qg.status}"
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
