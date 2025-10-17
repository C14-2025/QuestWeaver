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

### 🗺️ Geração Procedural de Quests
O sistema cria **missões aleatórias**, adaptadas ao perfil e nível do jogador.  
Exemplos:
- Derrotar um certo número de mobs específicos.
- Coletar itens raros.
- Explorar regiões do mapa.

Cada missão possui **recompensas personalizadas** e **níveis de dificuldade** crescentes.

---

## 💾 Persistência de Dados

Todos os dados são salvos automaticamente, incluindo:
- Classe do jogador  
- Nível e experiência  
- Habilidades desbloqueadas  
- Quests em andamento e concluídas  

> 💡 O salvamento é feito via **arquivos JSON**, por meio da depêndencia 'GSON'. 

---

## ⚙️ Tecnologias e APIs

- **Minecraft Spigot/Bukkit API**
- **Java 21**
- **Sistema de Eventos Bukkit**

---

## 🚀 Instalação

1. Baixe o plugin `.jar` compilado.  
2. Coloque-o na pasta:  
3. Reinicie o servidor.  
4. Ao entrar, escolha sua classe e comece a jornada!