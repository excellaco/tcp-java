void setBuildStatus(String message, String state) {
  step([
      $class: "GitHubCommitStatusSetter",
      reposSource: [$class: "ManuallyEnteredRepositorySource", url: "https://github.com/excellaco/tcp-java"],
      contextSource: [$class: "ManuallyEnteredCommitContextSource", context: "ci/jenkins/build-status"],
      errorHandlers: [[$class: "ChangingBuildStatusErrorHandler", result: "UNSTABLE"]],
      statusResultSource: [ $class: "ConditionalStatusResultSource", results: [[$class: "AnyBuildResult", message: message, state: state]] ]
  ]);
}

pipeline {
    agent {
      label 'excellanator'
    }
    tools {
        jdk "11"
    }
    stages {
        stage('Clean') {
            steps {
                slackSend(channel: '#tcp-java', color: '#FFFF00', message: ":jenkins-triggered: Build Started - ${env.JOB_NAME} ${env.BUILD_NUMBER} (<${env.BUILD_URL}|Open>)")
                gradlew('clean')
            }
        }
        stage('Unit Tests') {
            steps {
                gradlew('testNG')
            }
            post{
              always {
                junit '**/build/test-results/testNG/TEST-*.xml'
              }
            }
        }
    }
    post {
        success {
          setBuildStatus("Build succeeded", "SUCCESS");
          slackSend(channel: '#tcp-java', color: '#00FF00', message: ":jenkins_ci: Build Successful!  ${env.JOB_NAME} ${env.BUILD_NUMBER} (<${env.BUILD_URL}|Open>) :jenkins_ci:")
        }
        failure {
          setBuildStatus("Build failed", "FAILURE");
          slackSend(channel: '#tcp-java', color: '#FF0000', message: ":alert: :jenkins_exploding: *Build Failed!  WHO BROKE THE FREAKING CODE??* ${env.JOB_NAME} ${env.BUILD_NUMBER} (<${env.BUILD_URL}|Open>) :jenkins_exploding: :alert:")
        }
    }
}

def gradlew(String... args) {
    sh "./gradlew ${args.join(' ')} -s"
}
