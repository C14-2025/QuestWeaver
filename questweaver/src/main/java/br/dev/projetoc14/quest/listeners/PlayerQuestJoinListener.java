package br.dev.projetoc14.quest.listeners;

import br.dev.projetoc14.quest.utils.QuestManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/*
 * Cria a primeira quest quando jogador entra pela primeira vez
 */
public class PlayerQuestJoinListener implements Listener {

    private final QuestManager questManager;

    public PlayerQuestJoinListener(QuestManager questManager) {
        this.questManager = questManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (!questManager.hasQuests(player)) {
            questManager.createFirstQuest(player);
        }
    }
}