// =============================================
// ARQUIVO 3: br/dev/projetoc14/quests/QuestManager.java
// Gerenciador central de todas as quests
// =============================================

package br.dev.projetoc14.quest.utils;

import br.dev.projetoc14.quest.KillQuest;
import br.dev.projetoc14.quest.Quest;
import br.dev.projetoc14.quest.mage.ElementalMaster;
import br.dev.projetoc14.quest.warrior.FirstBlood;
import br.dev.projetoc14.quest.archer.RangedCombatQuest;
import org.bukkit.entity.Player;
import org.bukkit.Location;

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
        QuestLineType questLine = switch (className.toLowerCase()) {
            case "guerreiro" -> QuestLineType.WARRIOR;
            case "mago" -> QuestLineType.MAGE;
            case "arqueiro" -> QuestLineType.ARCHER;
            case "assassino" -> QuestLineType.ASSASSIN;
            default -> null;
        };

        if (questLine == null) {
            return;
        }

        playerQuestLines.put(player.getUniqueId(), questLine);
        giveNextQuest(player);
    }

    private void giveNextQuest(Player player) {
        UUID playerId = player.getUniqueId();
        QuestLineType questLine = playerQuestLines.get(playerId);
        PlayerQuestData questData = getOrCreatePlayerQuestData(player);

        if (questLine == null || questData == null) return;

        // Verifica quantas quests já foram completadas
        int completedQuests = questData.getCompletedQuests().size();

        // Cria a proxima quest baseada na classe e progresso
        Quest nextQuest = createQuestForClass(questLine, completedQuests, player.getLocation());
        if (nextQuest != null) {
            questData.addQuest(nextQuest);
            notifyNewQuest(player, nextQuest);
            nextQuest.assignToPlayer(player);
        }
    }

    private Quest createQuestForClass(QuestLineType type, int questProgress, Location playerLoc) {
        return switch (type) {
            case WARRIOR -> createWarriorQuest(questProgress, playerLoc);
            case MAGE -> createMageQuest(questProgress, playerLoc);
            case ARCHER -> createArcherQuest(questProgress, playerLoc);
            case ASSASSIN -> createAssassinQuest(questProgress, playerLoc);
        };
    }

    private Quest createWarriorQuest(int progress, Location playerLoc) {
        return switch (progress) {
            case 0 -> new FirstBlood(playerLoc); // Primeira quest do guerreiro
            // TODO: Adicione mais quests aqui...
            default -> null;
        };
    }


    private Quest createMageQuest(int progress, Location playerLoc) {
        return switch (progress) {
            case 0 -> new ElementalMaster(playerLoc); // Primeira quest do mago
            // TODO: Adicione mais quests aqui...
            default -> null;
        };
    }

    private Quest createArcherQuest(int progress, Location playerLoc) {
        return switch (progress) {
            case 0 -> new RangedCombatQuest(playerLoc); // Primeira quest do arqueiro
            // TODO: Adicione mais quests aqui...
            default -> null;
        };
    }

    private Quest createAssassinQuest(int progress, Location playerLoc) {
        // TODO: Implemente as quests do assassino
        return null;
    }

    private void notifyNewQuest(Player player, Quest quest) {
        player.sendMessage("§6═══════════════════════════");
        player.sendMessage("§e✦ §6Nova Quest Recebida!");
        player.sendMessage("§f" + quest.getName());
        player.sendMessage("§7" + quest.getDescription());
        player.sendMessage("§6═══════════════════════════");
    }

    private PlayerQuestData getOrCreatePlayerQuestData(Player player) {
        return playerQuests.computeIfAbsent(player.getUniqueId(),
                k -> new PlayerQuestData(player));
    }

    public void onQuestComplete(Player player, Quest quest) {
        PlayerQuestData questData = getPlayerQuests(player);
        if (questData != null) {
            quest.giveRewards(player);
            questData.completeQuest(quest.getId());
            // Da a proxima quest da linha
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
     * Mostra as quests ativas do jogador
     */
    public void showActiveQuests(Player player) {
        PlayerQuestData questData = getPlayerQuests(player);

        if (questData == null || questData.getActiveQuests().isEmpty()) {
            player.sendMessage("§cVocê não tem quests ativas.");
            return;
        }

        player.sendMessage("§6═══ §eSuas Quests Ativas §6═══");
        for (Quest quest : questData.getActiveQuests().values()) {
            if (quest instanceof KillQuest killQuest) {
                player.sendMessage("§e• §f" + quest.getName());
                player.sendMessage("  §7" + quest.getDescription());
                player.sendMessage("  §aProgresso: " + killQuest.getProgressText());
            }
        }
    }
}