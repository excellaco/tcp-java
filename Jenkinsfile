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
        stage('Linter') {
            steps {
              slackSend(channel: '#tcp-java', color: '#FFFF00', message: ":jenkins-triggered: Build Triggered - ${env.JOB_NAME} ${env.BUILD_NUMBER} (<${env.BUILD_URL}|Open>)")
              gradlew('verGJF')
            }
        }
        stage('Clean') {
            steps {
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
        stage('Jacoco') {
            steps {
                gradlew('jacocoTestReport')
            }
        }
        stage('Jacoco Verficiation') {
            steps {
                gradlew('jacocoTestCoverageVerification')
            }
        }
        stage('SonarQube analysis') {
          steps{
            withSonarQubeEnv('default') {
              gradlew('sonarqube')
            }
          }
        }
        stage('Build Dev Image'){
          steps{
              sh './tcp-java-ecs/package-for-ecs dev'
          }
        }
        stage('Deploy Dev Image'){
          steps{
            dir('tcp-java-ecs'){
              sh './deploy-to-ecs dev'
            }
          }
        }
        /* stage('Build Test Image'){
          steps{
              sh './tcp-java-ecs/package-for-ecs test'
          }
        }
        stage('Deploy Test Image'){
          steps{
            dir('tcp-java-ecs'){
              sh './deploy-to-ecs test'
            }
          }
        }
        stage('Build Prod Image'){
          steps{
              sh './tcp-java-ecs/package-for-ecs prod'
          }
        }
        stage('Deploy Prod Image'){
          steps{
            dir('tcp-java-ecs'){
              sh './deploy-to-ecs prod'
            }
          }
        } */
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
