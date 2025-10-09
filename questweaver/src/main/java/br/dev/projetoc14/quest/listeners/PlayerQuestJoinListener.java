// =============================================
// ARQUIVO 5: br/dev/projetoc14/quests/listeners/PlayerQuestJoinListener.java
// Detecta quando jogador entra no servidor
// =============================================

package br.dev.projetoc14.quest.listeners;

import br.dev.projetoc14.quest.QuestManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/*
 * Cria a primeira quest quando jogador entra pela primeira vez
 */
public class PlayerQuestJoinListener implements Listener {

    private QuestManager questManager;

    public PlayerQuestJoinListener(QuestManager questManager) {
        this.questManager = questManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // Se é primeira vez, cria a quest inicial
        if (!questManager.hasQuests(player)) {
            questManager.createFirstQuest(player);
        }
    }
}