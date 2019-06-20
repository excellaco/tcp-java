pipeline {
    agent any
    node (java_test){
    env.JAVA_HOME="${tool '/usr/lib/jvm/jdk-11.0.1'}"
    env.PATH="${env.JAVA_HOME}/bin:${env.PATH}"
    sh 'java -version'
    stages {
        stage('Clean') {
            steps {
                gradlew('clean')
            }
        }
        stage('Unit Tests') {
            steps {
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

}

def gradlew(String... args) {
    sh "./gradlew ${args.join(' ')} -s"
}
