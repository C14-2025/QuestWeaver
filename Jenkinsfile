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

                        JAR_FILE=$(ls build/libs/*.jar | grep -v "plain" | head -n 1)
                        if [ -z "$JAR_FILE" ]; then
                            echo "ERRO: JAR não encontrado!"
                            exit 1
                        fi

                        JAR_NAME=$(basename "$JAR_FILE")
                        JAR_SIZE=$(du -h "$JAR_FILE" | cut -f1)
                        echo "JAR: $JAR_NAME ($JAR_SIZE)"

                        if ! jar tf "$JAR_FILE" > /dev/null 2>&1; then
                            echo "ERRO: JAR corrompido"
                            exit 1
                        fi

                        if ! jar tf "$JAR_FILE" | grep -q "^plugin.yml$"; then
                            echo "ERRO: plugin.yml não encontrado - falha no Minecraft!"
                            exit 1
                        fi

                        jar xf "$JAR_FILE" plugin.yml
                        echo "Informações Plugin:"
                        grep -E "^(name|version):" plugin.yml | sed 's/^/  /'
                        rm plugin.yml

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

                        sh "rm -f ${env.MINECRAFT_PLUGIN_DIR}/questweaver*.jar"
                        sh "rm -rf ${env.MINECRAFT_PLUGIN_DIR}/QuestWeaver"

                        sh "cp ${jarFile} ${env.MINECRAFT_PLUGIN_DIR}"
                        echo "Plugin copiado com sucesso!"
                    } else {
                        error "Nenhum arquivo .jar foi encontrado no workspace!"
                    }
                }
            }
        }

        stage('Verify Deployment') {
            steps {
                script {
                    echo "Verificando deploy..."
                    
                    sh """
                        echo ""
                        echo "=== RESUMO DO DEPLOY ==="
                        echo "Plugin: \$(ls -lh ${MINECRAFT_PLUGIN_DIR}/questweaver*.jar | awk '{print \$9, \$5}')"
                        echo "========================"
                        echo ""
                        echo "✓ Deploy concluído com sucesso!"
                        echo ""
                        echo "  ATENÇÃO: Resource Pack deve ser gerenciado manualmente:"
                        echo "   1. Gere o .zip no IntelliJ"
                        echo "   2. Faça upload para GitHub Releases ou outro host"
                        echo "   3. Atualize o server.properties manualmente com a URL e SHA1"
                        echo ""
                    """
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

            Deploy do plugin concluído!
            
            LEMBRETE: Resource Pack deve ser atualizado manualmente:
            - Gere o .zip no IntelliJ e o SHA1
            - Faça upload para GitHub Releases
            - Atualize server.properties
            """,
                to: ""
            )
        }
        
        success {
            echo "✓ Build e deploy concluídos com sucesso!"
        }
        
        failure {
            echo "✗ Build falhou. Verifique os logs acima."
        }
    }
}