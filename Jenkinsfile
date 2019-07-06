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
                sh 'java -version'
                gradlew('clean')
            }
        }
        stage('Unit Tests') {
            steps {
                gradlew('testNG')
            }
            post {
                always {
                    junit '**/build/test-results/testNG/TEST-*.xml'
                }
            }
        }

    }
}

def gradlew(String... args) {
    sh "./gradlew ${args.join(' ')} -s"
}
