pipeline {
    agent any

    environment {
        JAVA_HOME = "/opt/java/openjdk-21"
        PATH = "${env.JAVA_HOME}/bin:${env.PATH}"
    }

    stages {
        stage('Setup Java') {
            steps {
                sh '''
                    if [ ! -d "/opt/java/openjdk-21" ]; then
                        echo "Instalando Java 21..."
                        sudo apt update
                        sudo apt install -y openjdk-21-jdk
                    fi
                    java -version
                '''
            }
        }

        stage('Run Tests') {
            steps {
                dir('questweaver') {
                    sh './gradlew test'
                }
            }
        }

        stage('Publish Test Results') {
            steps {
                junit 'questweaver/build/test-results/test/*.xml'
            }
        }
    }
}
