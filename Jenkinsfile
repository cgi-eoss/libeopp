def baseBranch = 'master'

def bazelStep(Closure doStep) {
  configFileProvider([
      configFile(fileId: 'cgici-bazelrc', targetLocation: '.bazelrc.local')
  ]) {
    // Update Bazel resources if changing JenkinsfilePod.yaml - ensure it gets accurate resource limits
    sh """
      echo "" >>.bazelrc.local
      echo "build --local_resources=cpu=4" >>.bazelrc.local
      echo "build --local_resources=memory=8000" >>.bazelrc.local
      cat .bazelrc.local
    """
    doStep()
  }
}

pipeline {
  options {
    buildDiscarder(logRotator(artifactDaysToKeepStr: '', artifactNumToKeepStr: '1', daysToKeepStr: '30', numToKeepStr: ''))
    timeout(time: 1, unit: 'HOURS')
    timestamps()
  }
  agent {
    kubernetes {
      workspaceVolume dynamicPVC(accessModes: 'ReadWriteOnce', requestsSize: '10Gi')
      yamlFile 'JenkinsfilePod.yaml'
    }
  }

  stages {
    stage('Setup') {
      steps {
        gerritReview message: "Starting build: ${env.BUILD_URL}"

        container('libeopp-build') {
          echo "Updating repository URLs to local proxies..."
          sh """
            sed -i 's#https\\\\(:/\\\\)\\\\?/repo.maven.apache.org/maven2/#http\\\\1/cgici-nexus-nexus.cgici.svc.cluster.local/repository/maven-central/#' third_party/java/java_repositories.bzl
            sed -i 's#https\\\\(:/\\\\)\\\\?/repo.maven.apache.org/maven2/#http\\\\1/cgici-nexus-nexus.cgici.svc.cluster.local/repository/maven-central/#' third_party/java/maven_install.json
            (cd third_party/java && ./gradlew --no-daemon rehashMavenInstall)
          """
        }
      }
    }

    stage('Build') {
      steps {
        container('libeopp-build') {
          bazelStep { sh "bazel build //..." }
        }
      }
    }

    stage('Test Maven Install') {
      steps {
        container('libeopp-build') {
          bazelStep { sh "./scripts/deploy/install-local-snapshot.sh" }
        }
      }
    }

    stage('Test') {
      steps {
        container('libeopp-build') {
          bazelStep { sh "bazel coverage //... --test_output=errors --keep_going" }
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
          withCredentials([usernamePassword(credentialsId: 'cgici-jenkins-nexus-userpass', passwordVariable: 'MAVEN_PASSWORD', usernameVariable: 'MAVEN_USER')]) {
            bazelStep { sh "./scripts/deploy/deploy-nexus.sh ${TAG_NAME}" }
          }
        }
      }
    }

    stage('SQ Analysis') {
      environment {
        BRANCH_ARGS = "${CHANGE_ID ? "-Dsonar.pullrequest.key=${CHANGE_ID} -Dsonar.pullrequest.branch=${CHANGE_BRANCH} -Dsonar.pullrequest.base=${baseBranch}" : "-Dsonar.branch.name=${baseBranch}"}"
        VERSION_ARGS = "${TAG_NAME ? "-Dsonar.projectVersion=${TAG_NAME}" : "-Dsonar.projectVersion=${baseBranch}"}"
        ABORT_ON_QUALITY_GATE = "${CHANGE_ID ? "true" : "false"}"
      }
      steps {
        container('libeopp-build') {
          withSonarQubeEnv('CGI CI SonarQube') {
            bazelStep { sh "bazel run //:sq_libeopp -- -Dsonar.host.url=${SONAR_HOST_URL} -Dsonar.login=${SONAR_AUTH_TOKEN} \${BRANCH_ARGS} \${VERSION_ARGS}" }
            sh "mkdir -p tmp/ && cp --target-directory=tmp/ --dereference bazel-bin/sq_libeopp.runfiles/com_cgi_eoss_eopp/.scannerwork/report-task.txt"
            // Jenkins can't find things outside the workspace, so copy the SQ marker locally
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
