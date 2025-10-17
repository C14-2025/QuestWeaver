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
                    echo "Java configurado:"
                    java -version
                '''
            }
        }

        stage('Build') {
            steps {
                dir('questweaver') {
                    sh '''
                        echo "Dando permissão de execução ao gradlew..."
                        chmod +x gradlew
                        ./gradlew clean build
                    '''
                }
            }
        }

        stage('Test') {
            steps {
                dir('questweaver') {
                    sh './gradlew test'
                }
            }
        }

        stage('Archive') {
            steps {
                echo "🔹 Arquivando .jar gerado pelo Gradle..."
                archiveArtifacts artifacts: 'questweaver/build/libs/*.jar', fingerprint: true
            }
        }

        stage('Publish Test Results') {
            steps {
                junit 'questweaver/build/test-results/test/*.xml'
            }
        }
    }
}
