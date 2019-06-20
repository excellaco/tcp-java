pipeline {
    agent any
    tools {
        jdk "/usr/lib/jvm/jdk-11.0.1"
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
                sh 'java -version'
                gradlew('test')
            }
            post {
                always {
                    junit '**/build/test-results/test/TEST-*.xml'
                }
            }
        }

    }
}

def gradlew(String... args) {
    sh "./gradlew ${args.join(' ')} -s"
}
