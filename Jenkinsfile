pipeline {
    agent any
    tools {
        jdk "Java 11"
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
                gradlew('test')
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
