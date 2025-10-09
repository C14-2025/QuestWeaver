// =============================================
// ARQUIVO 3: br/dev/projetoc14/quests/QuestManager.java
// Gerenciador central de todas as quests
// =============================================

package br.dev.projetoc14.quest;

import br.dev.projetoc14.quest.KillQuest;
import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/*
 * Gerencia todas as quests de todos os jogadores
 */
public class QuestManager {

    private Map<UUID, PlayerQuestData> playerQuests;

    public QuestManager() {
        this.playerQuests = new HashMap<>();
    }

    /*
     * Verifica se jogador já tem quests
     */
    public boolean hasQuests(Player player) {
        return playerQuests.containsKey(player.getUniqueId());
    }

    /*
     * Cria a primeira quest quando jogador entra pela primeira vez
     */
    public void createFirstQuest(Player player) {
        UUID playerId = player.getUniqueId();

        // Cria a KillQuest inicial
        KillQuest firstQuest = new KillQuest(
                "first_kill_quest",
                "Primeira Caçada",
                "Mate 5 zumbis para começar sua jornada",
                50,
                "ZOMBIE",
                5,
                0
        );

        // Cria dados do jogador
        PlayerQuestData questData = new PlayerQuestData(player);
        questData.addQuest(firstQuest);

        // Salva
        playerQuests.put(playerId, questData);

        // Mensagem pro jogador
        player.sendMessage("§6═══════════════════════════");
        player.sendMessage("§e✦ §6Nova Quest Recebida!");
        player.sendMessage("§f" + firstQuest.getName());
        player.sendMessage("§7" + firstQuest.getDescription());
        player.sendMessage("§6═══════════════════════════");
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
                player.sendMessage("  §aProgresso: " + killQuest.getCurrentCount() + "/" + killQuest.getTargetCount());
            }
        }
    }
}