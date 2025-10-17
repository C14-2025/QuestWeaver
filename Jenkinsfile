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
    }
}
