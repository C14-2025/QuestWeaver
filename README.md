# 🛡️ RPG Avançado de Quests

Um **plugin para Minecraft** que transforma o jogo em um **RPG completo**, com **classes jogáveis**, **árvores de habilidades**, **sistema de experiência** e **missões**.  
Todos os progressos dos jogadores são salvos, garantindo uma experiência contínua e imersiva.

---

## ⚔️ Funcionalidades Principais

- 🧙 **Classes jogáveis** com estilos únicos:
    - ⚔️ **Guerreiro:** combate corpo-a-corpo, com alta defesa e dano físico bruto.
    - 🔮 **Mago:** especialista em magias de longo alcance e controle de área.
    - 🏹 **Arqueiro:** ataque à distância com precisão e dano perfurante.
    - 🗡️ **Assassino:** golpes rápidos e críticos corpo-a-corpo com foco em stealth.

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

## 🗡️ Quests do Assassino

### 🌑 Quest 1: Sombras Silenciosas (Fácil)
**Dificuldade:** ⭐  
**Recompensa:** 100 XP

**Objetivo:** Mate 6 zumbis atacando pelas costas (backstab).

**Mecânicas:**
- Spawna 6 zumbis que não queimam ao sol
- Detecta se o ataque foi feito **pelas costas** do mob
- Sistema usa cálculo vetorial (produto escalar) para determinar backstab
- Ensina posicionamento estratégico e stealth

**Como funciona:**
- O sistema compara a direção que o mob está olhando com a direção do ataque
- Se o ângulo for favorável (atacando por trás), conta como backstab
- Posicione-se atrás do mob antes de atacar!

**Mensagens de Feedback:**
- ✓ Backstab: `§a✓ Backstab executado!`
- ✗ Frontal: `§c✗ Ataque frontal não conta. Ataque pelas costas!`

---

### ⚡ Quest 2: Velocidade Mortal (Média)
**Dificuldade:** ⭐⭐  
**Recompensa:** 200 XP

**Objetivo:** Mate 8 esqueletos em sequência rápida (máximo de 3 segundos entre kills).

**Mecânicas Avançadas:**
- **Sistema de streak:** Cada kill consecutivo aumenta o contador
- **Tempo limite:** Máximo de 3 segundos entre cada kill
- **Penalidade:** Demorar muito reseta o progresso
- Spawna 8 esqueletos
- Ensina combate rápido e agressivo

**Condições de Falha:**
- ✗ Demorar mais de 3 segundos entre kills
- ✗ Streak é perdido e progresso reseta

**Mensagens de Feedback:**
- ✓ Streak: `§e⚡ STREAK x5! §7Mantenha a velocidade!`
- ✗ Timeout: `§c✗ Muito lento! Streak perdido. (Era 5/8)`
- ✓ Completa: `§6✦ §e§lSTREAK PERFEITO!`

**Dicas:**
- Mate os mobs rapidamente e em sequência
- Não pare para curar ou recuperar
- Use ataques rápidos e eficientes

---

### 💀 Quest 3: Assassinato Perfeito (Difícil)
**Dificuldade:** ⭐⭐⭐  
**Recompensa:** 350 XP

**Objetivo:** Mate 5 creepers sem tomar dano e sem deixá-los explodir.

**Mecânicas Extremamente Difíceis:**
- **Zero dano:** Qualquer dano recebido reseta o progresso
- **Sem explosões:** Se um creeper explodir, não conta
- **Rastreamento de HP:** Sistema monitora sua vida constantemente
- **Execução perfeita:** Requer timing e distância precisos
- Spawna 5 creepers (que explodem quando se aproximam)

**Condições de Falha:**
- ✗ Tomar qualquer dano de qualquer fonte
- ✗ Deixar um creeper explodir
- ✗ Qualquer falha reseta o progresso para 0/5

**Mensagens de Feedback:**
- ✓ Perfeito: `§a✓ Execução Perfeita! §7(3/5)`
- ⚠ Alerta: `§e⚠ Cuidado! Mantenha a distância dos creepers!`
- ✗ Dano: `§c✗ FALHOU! Você tomou dano. Execuções perfeitas exigem que você não seja atingido.`
- ✗ Explosão: `§c✗ O creeper explodiu! Isso não conta.`
- ✓ Completa: `§6✦ §e§lASSASSINATO PERFEITO COMPLETO!`

**Estratégia:**
1. Ataque rapidamente antes do creeper começar a explodir
2. Mantenha distância segura
3. Use golpes rápidos e precisos
4. Nunca deixe o creeper se aproximar demais
5. Cuidado com outros mobs no ambiente

**Progressão de Ensino:**
Esta quest combina todas as habilidades do assassino:
1. Posicionamento estratégico (Quest 1)
2. Velocidade de execução (Quest 2)
3. Perfeição sob pressão extrema (Quest 3)

---

## 📖 Sistema de Livro de Quests

Cada jogador recebe um **Livro de Quests** interativo que mostra:

- 📋 **Quests Ativas:** Com barra de progresso visual
```
  Assassinato Perfeito
  Mate 5 creepers sem tomar dano...
  
  3/5 ■■■■■■■■■■■■□□□□ 60%
  3/5 execuções perfeitas
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
- Progresso individual de cada quest (incluindo combos, streaks e HP)

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

### Sistema de Detecção de Hits/Kills
- Usa `EntityDamageByEntityEvent` para detectar ataques em tempo real
- Usa `EntityDeathEvent` para detectar mortes de mobs
- Calcula distância euclidiana 3D entre atacantes e alvos
- Verifica propriedades das armas e projéteis

### Sistema de Backstab (Assassino Quest 1)
- Calcula vetores de direção do mob e do atacante
- Usa produto escalar para determinar ângulo do ataque
- Backstab válido quando `dotProduct > 0.5` (atacando por trás)

### Sistema de Streak (Assassino Quest 2)
- Rastreamento temporal entre kills usando `System.currentTimeMillis()`
- Timeout de 3 segundos entre cada kill
- Reset automático em caso de timeout
- Feedback visual progressivo de combo

### Sistema de Perfect Kill (Assassino Quest 3)
- Rastreamento de HP do jogador por UUID
- Detecção de explosões de creeper via `EntityExplodeEvent`
- Comparação de HP antes/depois para validar "sem dano"
- Integração com sistema de HP do RPGPlayer

### Spawn Inteligente de Mobs
- Mobs marcados com `PersistentDataContainer`
- Configurações especiais (não queimam ao sol, custom name, etc.)
- Spawn em locações aleatórias próximas ao jogador
- Nome customizado: "Quest Target"

---

## 🚀 Instalação

1. Baixe o plugin `.jar` compilado.
2. Coloque-o na pasta `plugins/` do servidor.
3. Reinicie o servidor.
4. Ao entrar, escolha sua classe e comece a jornada!

---

## 📝 Comandos Disponíveis

- `/quests` - Abre o livro de quests interativo
    - Aliases: `/quest`, `/q`

- `/stats` - Mostra suas estatísticas completas (nível, XP, atributos)
    - Aliases: `/status`, `/st`, `/profile`

- `/help` - Mostra informações de ajuda do plugin
    - Aliases: `/ajuda`

---

## 🎯 Roadmap Futuro

- [ ] Quests para Guerreiro e Mago
- [ ] Sistema de recompensas de itens únicos
- [ ] Leaderboards de quests completadas
- [ ] Quests cooperativas para múltiplos jogadores
- [ ] Boss fights como quests épicas
- [ ] Sistema de conquistas (achievements)
- [ ] Reputação e facções

---

## 📊 Progressão de XP

### Sistema de Níveis
- **Fórmula:** XP necessário = Nível × 100
- **Nível 1 → 2:** 100 XP
- **Nível 2 → 3:** 200 XP
- **Nível 3 → 4:** 300 XP

### Recompensas por Quest
**Arqueiro:**
- Quest 1: 100 XP
- Quest 2: 200 XP
- Quest 3: 350 XP
- **Total:** 650 XP (Nível ~4-5)

**Assassino:**
- Quest 1: 100 XP
- Quest 2: 200 XP
- Quest 3: 350 XP
- **Total:** 650 XP (Nível ~4-5)

---

**Desenvolvido com ❤️ para criar experiências RPG imersivas no Minecraft**