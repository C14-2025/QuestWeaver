// =============================================
// ARQUIRVO 3: br/dev/projetoc14/quests/QuestManager.java
// Gerenciador central de todas as quests
// =============================================

package br.dev.projetoc14.quest.utils;

import br.dev.projetoc14.quest.ExplorationQuest;
import br.dev.projetoc14.quest.HitQuest;
import br.dev.projetoc14.quest.KillQuest;
import br.dev.projetoc14.quest.Quest;
import br.dev.projetoc14.quest.archer.PrecisionHunterQuest;
import br.dev.projetoc14.quest.archer.RangedCombatQuest;
import br.dev.projetoc14.quest.archer.WindMasterQuest;
import br.dev.projetoc14.quest.assassin.DeadlySpeedQuest;
import br.dev.projetoc14.quest.assassin.PerfectAssassinationQuest;
import br.dev.projetoc14.quest.assassin.CleanKillQuest;
import br.dev.projetoc14.quest.mage.ElementalMaster;
import br.dev.projetoc14.quest.mage.FrostbiteQuest;
import br.dev.projetoc14.quest.mage.PyromancerQuest;
import br.dev.projetoc14.quest.structures.*;
import br.dev.projetoc14.quest.warrior.BerserkerRageQuest;
import br.dev.projetoc14.quest.warrior.FirstBlood;
import br.dev.projetoc14.quest.warrior.IronWillQuest;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/*
 * Gerencia todas as quests de todos os jogadores
 */
public class QuestManager {

    private Map<UUID, PlayerQuestData> playerQuests;
    private Map<UUID, QuestLineType> playerQuestLines;

    public QuestManager() {
        this.playerQuests = new HashMap<>();
        this.playerQuestLines = new HashMap<>();
    }

    public void startClassQuestLine(Player player, String className) {
        UUID playerId = player.getUniqueId();

        // Verifica se já tem uma linha de quests ativa
        if (playerQuestLines.containsKey(playerId)) {
            player.sendMessage("§cVocê já tem uma linha de quests ativa!");
            return;
        }

        QuestLineType questLine = switch (className.toLowerCase()) {
            case "guerreiro" -> QuestLineType.WARRIOR;
            case "mago" -> QuestLineType.MAGE;
            case "arqueiro" -> QuestLineType.ARCHER;
            case "assassino" -> QuestLineType.ASSASSIN;
            default -> null;
        };

        if (questLine == null) {
            player.sendMessage("§cClasse inválida: " + className);
            return;
        }

        playerQuestLines.put(playerId, questLine);
        player.sendMessage("§a✦ Linha de quests de " + className + " iniciada!");
        giveNextQuest(player);
    }

    private void giveNextQuest(Player player) {
        UUID playerId = player.getUniqueId();
        QuestLineType questLine = playerQuestLines.get(playerId);
        PlayerQuestData questData = getOrCreatePlayerQuestData(player);

        if (questLine == null || questData == null) {
            player.sendMessage("§cErro: Não foi possível encontrar sua linha de quests.");
            return;
        }

        // Verifica quantas quests já foram completadas
        int completedQuests = questData.getCompletedQuests().size();

        // Cria a próxima quest baseada na classe e progresso
        Quest nextQuest = createQuestForClass(questLine, completedQuests, player.getLocation());
        if (nextQuest != null) {
            questData.addQuest(nextQuest);
            notifyNewQuest(player, nextQuest);
            nextQuest.assignToPlayer(player);
        } else {
            player.sendMessage("§6✨ Parabéns! Você completou todas as quests de " + questLine.getDisplayName() + "!");
        }
    }

    private Quest createQuestForClass(QuestLineType type, int questProgress, Location playerLoc) {
        try {
            return switch (type) {
                case WARRIOR -> createWarriorQuest(questProgress, playerLoc);
                case MAGE -> createMageQuest(questProgress, playerLoc);
                case ARCHER -> createArcherQuest(questProgress, playerLoc);
                case ASSASSIN -> createAssassinQuest(questProgress, playerLoc);
            };
        } catch (Exception e) {
            System.err.println("Erro ao criar quest para " + type + " progresso " + questProgress + ": " + e.getMessage());
            return null;
        }
    }

    private Quest createWarriorQuest(int progress, Location playerLoc) {
        return switch (progress) {
            case 0 -> {
                // Quest 0: Encontrar a Arena
                QuestStructure structure = new WarriorArena();
                yield new ExplorationQuest(
                        "find_warrior_arena",
                        "O Chamado da Arena",
                        "Encontre a Arena de Ferro para provar seu valor",
                        50,
                        structure,
                        10.0
                );
            }
            case 1 -> new FirstBlood(playerLoc);
            case 2 -> new IronWillQuest(playerLoc);
            case 3 -> new BerserkerRageQuest(playerLoc);
            default -> null;
        };
    }

    private Quest createMageQuest(int progress, Location playerLoc) {
        return switch (progress) {
            case 0 -> {
                // Quest 0: Encontrar a Torre
                QuestStructure structure = new MageTower();
                yield new ExplorationQuest(
                        "find_mage_tower",
                        "Peregrinação Arcana",
                        "Encontre a antiga Torre Arcana onde o conhecimento reside",
                        50,
                        structure,
                        10.0
                );
            }
            case 1 -> new ElementalMaster(playerLoc);
            case 2 -> new FrostbiteQuest(playerLoc);
            case 3 -> new PyromancerQuest(playerLoc);
            default -> null;
        };
    }

    private Quest createArcherQuest(int progress, Location playerLoc) {
        return switch (progress) {
            case 0 -> {
                // Quest 0: Encontrar o campo de treinamento
                QuestStructure structure = new ArcherTrainingGrounds();
                yield new ExplorationQuest(
                        "find_archer_grounds",
                        "Em Busca do Campo de Treinamento",
                        "Encontre o lendário Campo de Treinamento de Arqueiros",
                        50,
                        structure,
                        10.0
                );
            }
            case 1 -> new RangedCombatQuest(playerLoc);      // Quest 1: Combate a Distância
            case 2 -> new PrecisionHunterQuest(playerLoc);   // Quest 2: Caçador Preciso
            case 3 -> new WindMasterQuest(playerLoc);        // Quest 3: Mestre dos Ventos
            default -> null;
        };
    }

    private Quest createAssassinQuest(int progress, Location playerLoc) {
        return switch (progress) {
            case 0 -> {
                QuestStructure structure = new AssassinCrypt();
                yield new ExplorationQuest(
                        "find_assassin_location",
                        "Nas Sombras",
                        "Encontre a " + structure.getName(),
                        50,
                        structure,
                        10.0
                );
            }
            case 1 -> new CleanKillQuest(playerLoc);
            case 2 -> new DeadlySpeedQuest(playerLoc);
            case 3 -> new PerfectAssassinationQuest(playerLoc);
            default -> null;
        };
    }

    private void notifyNewQuest(Player player, Quest quest) {
        player.sendMessage("§6═══════════════════════════");
        player.sendMessage("§e✦ §6Nova Quest Recebida!");
        player.sendMessage("§f" + quest.getName());
        player.sendMessage("§7" + quest.getDescription());
        player.sendMessage("§aRecompensa: §e" + quest.getExperienceReward() + " XP");
        player.sendMessage("§6═══════════════════════════");
    }

    private PlayerQuestData getOrCreatePlayerQuestData(Player player) {
        return playerQuests.computeIfAbsent(player.getUniqueId(),
                k -> new PlayerQuestData(player));
    }

    public void onQuestComplete(Player player, Quest quest) {
        PlayerQuestData questData = getPlayerQuests(player);
        if (questData != null) {
            // Dá as recompensas primeiro
            quest.giveRewards(player);

            // Marca como completada
            questData.completeQuest(quest.getId());

            // Mensagem de conclusão
            player.sendMessage("§6🎉 Quest \"" + quest.getName() + "\" completada!");

            // Dá a próxima quest da linha
            giveNextQuest(player);
        }
    }

    /*
     * Verifica se jogador já tem quests
     */
    public boolean hasQuests(Player player) {
        return playerQuests.containsKey(player.getUniqueId());
    }

    /*
     * Pega os dados de quest de um jogador
     */
    public PlayerQuestData getPlayerQuests(Player player) {
        return playerQuests.get(player.getUniqueId());
    }

    /*
     * Mostra as quests ativas do jogador - CORRIGIDO
     */
    public void showActiveQuests(Player player) {
        PlayerQuestData questData = getPlayerQuests(player);

        if (questData == null || questData.getActiveQuests().isEmpty()) {
            player.sendMessage("§cVocê não tem quests ativas.");
            return;
        }

        player.sendMessage("§6═══ §eSuas Quests Ativas §6═══");
        for (Quest quest : questData.getActiveQuests().values()) {
            player.sendMessage("§e• §f" + quest.getName());
            player.sendMessage("  §7" + quest.getDescription());

            // Suporte para diferentes tipos de quest
            switch (quest) {
                case KillQuest killQuest -> player.sendMessage("  §aProgresso: " + killQuest.getProgressText());
                case HitQuest hitQuest -> player.sendMessage("  §aProgresso: " + hitQuest.getProgressText());
                case ExplorationQuest explorationQuest -> player.sendMessage("  §aStatus: " +
                        (explorationQuest.checkCompletion() ? "§2✓ Concluída" : "§e Em andamento"));
                default -> player.sendMessage("  §aStatus: " +
                        (quest.checkCompletion() ? "§2✓ Concluída" : "§e Em andamento"));
            }

            player.sendMessage("  §6Recompensa: §e" + quest.getExperienceReward() + " XP");
            player.sendMessage("");
        }
    }

    /**
     * Remove um jogador do sistema de quests (logout, etc)
     */
    public void removePlayer(Player player) {
        UUID playerId = player.getUniqueId();
        playerQuests.remove(playerId);
        // Não remove a linha de quests para manter o progresso
    }

    /**
     * Verifica se o jogador tem uma linha de quests ativa
     */
    public boolean hasQuestLine(Player player) {
        return playerQuestLines.containsKey(player.getUniqueId());
    }

    /**
     * Pega o tipo de linha de quests do jogador
     */
    public QuestLineType getPlayerQuestLine(Player player) {
        return playerQuestLines.get(player.getUniqueId());
    }
}