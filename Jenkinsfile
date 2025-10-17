pipeline {
    agent any

    environment {
        JAVA_HOME = "/opt/java/openjdk-21"
        PATH = "${env.JAVA_HOME}/:${env.PATH}"
        MINECRAFT_PLUGIN_DIR = '/DATA/AppData/crafty/servers/a70ef6f2-570f-46b1-9a13-adc1b0a32793/plugins'
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
                echo "Gerando arquivo .jar pelo Gradle..."
                archiveArtifacts artifacts: 'questweaver/build/libs/*.jar', fingerprint: true
            }
        }

        stage('Publish Test Results') {
            steps {
                junit 'questweaver/build/test-results/test/*.xml'
            }
        }

        stage('Estatísticas') {
            steps {
                script {
                    echo "Data: ${new Date()}"
                    echo "Build: #${env.BUILD_NUMBER}"
                    echo ""

                    dir('questweaver') {
                        sh '''
                            if [ ! -d "src" ]; then
                                echo "Diretório src não encontrado, abortando análise."
                                exit 0
                            fi

                            TOTAL_JAVA=$(find src -name "*.java" 2>/dev/null | wc -l)
                            TOTAL_LINES=$(find src -name "*.java" -exec cat {} \\; 2>/dev/null | wc -l)
                            MAIN_FILES=$(find src/main -name "*.java" 2>/dev/null | wc -l)
                            TEST_FILES=$(find src/test -name "*.java" 2>/dev/null | wc -l)

                            echo ""
                            echo "Arquivos em src/main: $MAIN_FILES"
                            echo "Arquivos em src/test: $TEST_FILES"
                            echo "Total de linhas: $TOTAL_LINES"

                            if [ $MAIN_FILES -gt 0 ]; then
                                RATIO=$((TEST_FILES * 100 / MAIN_FILES))
                                echo "Proporção teste/código: $RATIO%"
                            fi

                            echo ""
                            echo "Top 5 maiores arquivos .java:"
                            find src -name "*.java" -exec wc -l {} \\; 2>/dev/null | sort -rn | head -5 | awk '{print $1 " linhas - " $2}'

                            echo ""
                            echo "Estatísticas resumidas geradas com sucesso!"
                        '''
                    }
                }
            }
        }

        stages {
            stage('Deploy Plugin') {
                steps {
                    script {
                        echo "Iniciando deploy para a pasta: ${env.MINECRAFT_PLUGIN_DIR}"

                        def jarFile = findFiles(glob: 'build/libs/*.jar')[0]?.path

                        if (jarFile) {
                            echo "Arquivo do plugin encontrado: ${jarFile}"

                            // Comando para copiar o arquivo para a pasta de plugins do servidor
                            sh "cp ${jarFile} ${env.MINECRAFT_PLUGIN_DIR}"

                            echo "Plugin copiado com sucesso!"
                            echo "Lembre-se de recarregar o servidor com o comando '/reload confirm'"
                        } else {
                            // Falha o build se o .jar não for encontrado
                            error "Nenhum arquivo .jar foi encontrado no workspace! O job de 'Package' precisa rodar primeiro."
                        }
                    }
                }
            }
        }
    }
}
