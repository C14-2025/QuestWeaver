pipeline {
    agent any

    environment {
        JAVA_HOME = "/opt/java/openjdk"
        PATH = "${env.JAVA_HOME}/:${env.PATH}"
        MINECRAFT_PLUGIN_DIR = "/minecraft-servers/a70ef6f2-570f-46b1-9a13-adc1b0a32793/plugins"
    }

    stages {
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

        stage('Stats') {
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

        stage('Package Validation') {
            steps {
                dir('questweaver') {
                    sh '''
                        echo "Iniciando Validação do JAR"

                        # Localiza o JAR
                        JAR_FILE=$(ls build/libs/*.jar | grep -v "plain" | head -n 1)

                        if [ -z "$JAR_FILE" ]; then
                            echo "ERRO: JAR não encontrado!"
                            exit 1
                        fi

                        JAR_NAME=$(basename "$JAR_FILE")
                        JAR_SIZE=$(du -h "$JAR_FILE" | cut -f1)
                        echo "JAR: $JAR_NAME ($JAR_SIZE)"

                        # Valida integridade e plugin.yml
                        if ! jar tf "$JAR_FILE" > /dev/null 2>&1; then
                            echo "ERRO: JAR corrompido"
                            exit 1
                        fi

                        # Verifica plugin.yml (Obrigatório para Minecraft)
                        if ! jar tf "$JAR_FILE" | grep -q "^plugin.yml$"; then
                            echo "ERRO: plugin.yml não encontrado - falha no Minecraft!"
                            exit 1
                        fi

                        # Extrai e exibe informações essenciais
                        jar xf "$JAR_FILE" plugin.yml
                        echo "Informações Plugin:"
                        grep -E "^(name|version):" plugin.yml | sed 's/^/  /'
                        rm plugin.yml

                        # Checksum (Importante para deploy/cache)
                        echo "MD5: $(md5sum "$JAR_FILE" | cut -d' ' -f1)"

                        echo "Validação concluída com sucesso!"
                    '''
                }
            }
        }

            stage('Deploy') {
                steps {
                    script {
                        echo "Iniciando deploy para a pasta: ${env.MINECRAFT_PLUGIN_DIR}"

                        def jarFile = sh(
                            script: 'ls questweaver/build/libs/*.jar | grep -v "plain" | head -n 1',
                            returnStdout: true
                        ).trim()

                        if (jarFile) {
                            echo "Arquivo do plugin encontrado: ${jarFile}"

                            // removendo versões antigas do plugin
                            echo "Removendo versões antigas do plugin..."
                            sh "rm -f ${env.MINECRAFT_PLUGIN_DIR}/questweaver*.jar"
                            sh "rm -rf ${env.MINECRAFT_PLUGIN_DIR}/QuestWeaver"

                            // Comando para copiar o arquivo para a pasta de plugins do servidor
                            sh "cp ${jarFile} ${env.MINECRAFT_PLUGIN_DIR}"
                            echo "Plugin copiado com sucesso!"
                            
                        } else {
                            // Falha o build se o .jar não for encontrado
                            error "Nenhum arquivo .jar foi encontrado no workspace! O job de 'Package' precisa rodar primeiro."
                        }
                    }
                }
            }
    }
    post {
        always {
            emailext(
                subject: "Pipeline '${env.JOB_NAME} #${env.BUILD_NUMBER}' finalizada",
                body: """\
            Olá! 

            A pipeline do Jenkins acabou de rodar com o seguinte status:

            Projeto: ${env.JOB_NAME}
            Build: #${env.BUILD_NUMBER}
            Resultado: ${currentBuild.currentResult}
            Detalhes: ${env.BUILD_URL}

            Deploy e reinício do servidor foram executados.
            """,
                to: "matheus.maciel@gec.inatel.br"
            )
        }
    }
}
