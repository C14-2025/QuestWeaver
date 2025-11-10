pipeline {
    agent any

    environment {
        JAVA_HOME = "/opt/java/openjdk"
        PATH = "${env.JAVA_HOME}/:${env.PATH}"
        MINECRAFT_PLUGIN_DIR = "/minecraft-servers/a70ef6f2-570f-46b1-9a13-adc1b0a32793/plugins"
        MINECRAFT_RESOURCEPACK_DIR = "/minecraft-servers/a70ef6f2-570f-46b1-9a13-adc1b0a32793/resourcepacks"
        SERVER_PROPERTIES = "/minecraft-servers/a70ef6f2-570f-46b1-9a13-adc1b0a32793/server.properties"
        
        // Configuração do GitHub Pages
        GITHUB_REPO = "C14-2025/QuestWeaver"
        GITHUB_USERNAME = "C14-2025"
        RESOURCE_PACK_NAME = "QuestWeaver_ResourcePack.zip"
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

        stage('Resource Pack') {
            steps {
                script {
                    def resourcePackDir = 'questweaver/resourcepack'
                    def resourcePackName = env.RESOURCE_PACK_NAME
                    def resourcePackDest = env.MINECRAFT_RESOURCEPACK_DIR

                    echo "Iniciando empacotamento do Resource Pack..."

                    sh """
                        if [ ! -d "${resourcePackDir}" ]; then
                            echo "Nenhum resource pack encontrado em ${resourcePackDir}, pulando etapa."
                            exit 0
                        fi

                        cd ${resourcePackDir}
                        echo "Compactando com o comando JAR..."
                        jar cf ../${resourcePackName} .
                        cd ..
                        echo "Resource Pack compactado: ${resourcePackName}"
                        
                        # Calcula SHA1 do resource pack
                        SHA1=\$(sha1sum ${resourcePackName} | cut -d' ' -f1)
                        echo "SHA1 do Resource Pack: \$SHA1"
                        echo "\$SHA1" > ${resourcePackName}.sha1
                    """

                    echo "Copiando Resource Pack para backup local..."
                    sh """
                        mkdir -p ${resourcePackDest}
                        cp questweaver/${resourcePackName} ${resourcePackDest}/
                        cp questweaver/${resourcePackName}.sha1 ${resourcePackDest}/
                    """

                    echo "Resource Pack salvo localmente em: ${resourcePackDest}/${resourcePackName}"
                }
            }
        }

        stage('Update server.properties') {
            steps {
                script {
                    // URL do GitHub Pages
                    def resourcePackUrl = "https://${env.GITHUB_USERNAME}.github.io/QuestWeaver/${env.RESOURCE_PACK_NAME}"
                    
                    echo "Atualizando server.properties..."
                    echo "URL do Resource Pack (GitHub Pages): ${resourcePackUrl}"

                    sh """
                        if [ ! -f "${SERVER_PROPERTIES}" ]; then
                            echo "ERRO: server.properties não encontrado em ${SERVER_PROPERTIES}"
                            exit 1
                        fi

                        # Remove linhas malformadas que podem existir
                        sed -i '/^https=/d' ${SERVER_PROPERTIES}
                        
                        # Lê o SHA1 gerado
                        SHA1=\$(cat ${MINECRAFT_RESOURCEPACK_DIR}/${RESOURCE_PACK_NAME}.sha1)
                        echo "Usando SHA1: \$SHA1"
                        
                        # Atualiza as configurações do resource pack
                        sed -i 's|^resource-pack=.*|resource-pack=${resourcePackUrl}|' ${SERVER_PROPERTIES}
                        sed -i 's|^require-resource-pack=.*|require-resource-pack=true|' ${SERVER_PROPERTIES}
                        sed -i 's|^resource-pack-prompt=.*|resource-pack-prompt=§eBaixar o pacote de texturas do QuestWeaver?|' ${SERVER_PROPERTIES}
                        sed -i "s|^resource-pack-sha1=.*|resource-pack-sha1=\$SHA1|" ${SERVER_PROPERTIES}
                        
                        echo ""
                        echo "=== Configurações atualizadas ==="
                        grep "resource-pack" ${SERVER_PROPERTIES}
                        echo "================================="
                        echo ""
                        echo "server.properties atualizado com sucesso!"
                    """
                }
            }
        }

        stage('Verify Configuration') {
            steps {
                script {
                    echo "Verificando configuração final..."
                    
                    sh """
                        echo ""
                        echo "=== RESUMO DO DEPLOY ==="
                        echo "Plugin: \$(ls -lh ${MINECRAFT_PLUGIN_DIR}/questweaver*.jar | awk '{print \$9, \$5}')"
                        echo "Resource Pack: \$(ls -lh ${MINECRAFT_RESOURCEPACK_DIR}/${RESOURCE_PACK_NAME} | awk '{print \$9, \$5}')"
                        echo ""
                        echo "=== CONFIGURAÇÃO DO RESOURCE PACK ==="
                        grep "resource-pack" ${SERVER_PROPERTIES} | sed 's/^/  /'
                        echo "========================="
                        echo ""
                        echo "  IMPORTANTE: Para aplicar as mudanças, você precisa:"
                        echo "   1. Fazer commit do ${RESOURCE_PACK_NAME} para a branch 'gh-pages' ou 'resourcePack'"
                        echo "   2. Ativar GitHub Pages no repositório"
                        echo "   3. Reiniciar o servidor Minecraft"
                        echo "   4. Os jogadores devem limpar cache (pasta .minecraft/server-resource-packs/)"
                        echo ""
                        echo "URL do GitHub Pages será: https://${GITHUB_USERNAME}.github.io/QuestWeaver/${RESOURCE_PACK_NAME}"
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

            Deploy concluído!
            
            Resource Pack URL (GitHub Pages): https://${env.GITHUB_USERNAME}.github.io/QuestWeaver/${env.RESOURCE_PACK_NAME}
            
            Próximos passos:
            1. Fazer commit do resource pack para a branch do GitHub Pages
            2. Reiniciar o servidor Minecraft
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