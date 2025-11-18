# 🛡️ RPG Avançado de Quests

Um **plugin para Minecraft** que transforma o jogo em um **RPG completo**, com **classes jogáveis**, **árvores de habilidades**, **sistema de experiência** e **missões**.  
Todos os progressos dos jogadores são salvos, garantindo uma experiência contínua e imersiva.

---

## ⚔️ Funcionalidades Principais

- 🧙 **Classes jogáveis** com estilos únicos:
    - ⚔️ **Guerreiro:** combate corpo-a-corpo, com alta defesa e dano físico bruto.
    - 🔮 **Mago:** especialista em magias de longo alcance e controle de área.
    - 🏹 **Arqueiro:** ataque à distância com precisão e dano perfurante.
    - 🗡️ **Assassino:** golpes rápidos e críticos corpo-a-corpo.

- 🌳 **Árvore de habilidades desbloqueável** via experiência.
- 🎯 **Sistema de níveis e progressão** com recompensas dinâmicas.
- 📜 **Quests procedurais**, geradas automaticamente conforme o nível e classe do jogador.
- 💾 **Persistência de dados** — progresso salvo automaticamente (classe, nível, XP, habilidades, etc.).
- 🎁 **Recompensas** por missões: itens raros, equipamentos e experiência.

---

## 🧩 Estrutura do Sistema

### 🧍 Escolha de Classe
Ao entrar no servidor pela primeira vez, o jogador é convidado a escolher uma das quatro classes.  
Após a escolha, ele recebe um **kit inicial específico da classe**, contendo armas, armaduras e habilidades básicas.

### 🌱 Progressão
- Ganhe **experiência (XP)** ao completar quests e derrotar inimigos.
- Suba de nível e desbloqueie **novas habilidades** únicas da sua classe.
- Cada habilidade possui **efeitos especiais** e **custos de energia/mana**.

### 🗺️ Sistema de Quests

O sistema cria **missões progressivas**, adaptadas ao perfil e nível do jogador.  
Cada classe possui uma **linha de quests exclusiva** que ensina mecânicas específicas e aumenta em dificuldade.

---

## 🏹 Quests do Arqueiro

### 📘 Quest 1: Combate a Distância (Fácil)
**Dificuldade:** ⭐  
**Recompensa:** 100 XP

**Objetivo:** Acerte 5 flechas em esqueletos a uma distância de pelo menos 15 blocos.

**Mecânicas:**
- Spawna 5 esqueletos que não queimam ao sol
- Conta apenas acertos feitos a 15+ blocos de distância
- Feedback em tempo real mostrando a distância do tiro
- Ensina o jogador a dominar combate de longa distância

**Mensagens de Feedback:**
- ✓ Acertou: `§a✓ Acerto de longa distância! (17.3 blocos)`
- ✗ Errou: `§c✗ Muito perto! (8.5/15 blocos)`

---

### 📗 Quest 2: Caçador Preciso (Média)
**Dificuldade:** ⭐⭐  
**Recompensa:** 200 XP

**Objetivo:** Acerte 8 tiros críticos em zumbis.

**Mecânicas:**
- Spawna 8 zumbis que não queimam ao sol
- Conta apenas flechas disparadas com o arco **totalmente carregado** (tiros críticos)
- Flechas críticas têm partículas especiais no Minecraft
- Ensina o jogador a maximizar dano através de precisão

**Mensagens de Feedback:**
- ✓ Crítico: `§e⚡ Tiro Crítico! §a✓`
- ✗ Não crítico: `§c✗ Não foi crítico! Atire com o arco totalmente puxado.`

---

### 📕 Quest 3: Mestre dos Ventos (Difícil)
**Dificuldade:** ⭐⭐⭐  
**Recompensa:** 350 XP

**Objetivo:** Acerte 10 flechas seguidas em creepers sem errar.

**Mecânicas Avançadas:**
- **Distância mínima:** 12 blocos por acerto
- **Tempo limite:** Máximo de 5 segundos entre cada acerto
- **Sistema de combo:** Cada acerto consecutivo aumenta o combo
- **Penalidade severa:** Qualquer erro reseta o progresso para 0/10
- **Detecção de erro:** Flechas que não acertam nada quebram o combo
- Spawna creepers (mobs perigosos que explodem)

**Condições de Falha:**
- ✗ Errar o alvo (flecha não acerta nada)
- ✗ Demorar mais de 5 segundos entre acertos
- ✗ Acertar a menos de 12 blocos

**Mensagens de Feedback:**
- ✓ Acerto: `§e⚡ COMBO x7 §7(14.2 blocos)`
- ✓ Progresso: `§6✦ Continue assim! Faltam 3 acertos!`
- ✗ Erro: `§c✗ Errou! Combo perdido. (Era 7/10)`
- ✗ Timeout: `§c✗ Combo perdido! Você demorou demais entre os tiros.`
- ✓ Completa: `§6✦ §e§lCOMBO PERFEITO! §6Quest completada!`

**Progressão de Ensino:**
Esta quest ensina o jogador a manter **consistência sob pressão**, combinando todas as habilidades anteriores:
1. Precisão de longa distância (Quest 1)
2. Tiros críticos (Quest 2)
3. Velocidade e consistência (Quest 3)

---

## 📖 Sistema de Livro de Quests

Cada jogador recebe um **Livro de Quests** interativo que mostra:

- 📋 **Quests Ativas:** Com barra de progresso visual
```
  Mestre dos Ventos
  Acerte 10 flechas seguidas em creepers...
  
  7/10 ■■■■■■■■■■■□□□□□ 70%
  7/10 acertos em sequência perfeita
```

- ✅ **Quests Completadas:** Histórico de conquistas
- 📊 **Estatísticas:** Total de quests, XP ganho, etc.

O livro é **atualizado em tempo real** conforme o progresso nas quests.

---

## 💾 Persistência de Dados

Todos os dados são salvos automaticamente, incluindo:
- Classe do jogador
- Nível e experiência
- Habilidades desbloqueadas
- Quests em andamento e concluídas
- Progresso individual de cada quest (incluindo combos da Quest 3)

> 💡 O salvamento é feito via **arquivos JSON**, por meio da dependência 'GSON'.

---

## ⚙️ Tecnologias e APIs

- **Minecraft Spigot/Bukkit API**
- **Java 21**
- **Sistema de Eventos Bukkit**
- **GSON** para serialização de dados
- **Kyori Adventure API** para componentes de texto modernos

---

## 🎮 Mecânicas Técnicas

### Sistema de Detecção de Hits
- Usa `EntityDamageByEntityEvent` para detectar acertos em tempo real
- Calcula distância euclidiana 3D entre atirador e alvo
- Verifica propriedades das flechas (crítico, metadata, etc.)

### Sistema de Combo (Quest 3)
- Rastreamento por UUID do jogador
- Timestamp de último hit para timeout
- Reset automático em caso de falha
- Feedback visual progressivo

### Spawn Inteligente de Mobs
- Mobs marcados com `PersistentDataContainer`
- Configurações especiais (não queimam ao sol)
- Spawn em locações aleatórias próximas ao jogador
- Nome customizado: "Quest Target"

---

## 🚀 Instalação

1. Baixe o plugin `.jar` compilado.
2. Coloque-o na pasta `plugins/` do servidor.
3. Reinicie o servidor.
4. Ao entrar, escolha sua classe e comece a jornada!

---

## 🎯 Roadmap Futuro

- [ ] Quests para Guerreiro, Mago e Assassino
- [ ] Sistema de recompensas de itens únicos
- [ ] Leaderboards de quests completadas
- [ ] Quests cooperativas para múltiplos jogadores
- [ ] Boss fights como quests épicas

---

**Desenvolvido com ❤️ para criar experiências RPG imersivas no Minecraft**