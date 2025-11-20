package br.dev.projetoc14.quest.listeners;

import br.dev.projetoc14.quest.ExplorationQuest;
import br.dev.projetoc14.quest.Quest;
import br.dev.projetoc14.quest.utils.PlayerQuestData;
import br.dev.projetoc14.quest.utils.QuestManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Listener que verifica periodicamente se o jogador está perto da estrutura
 */
public class ExplorationQuestListener implements Listener {

    private final QuestManager questManager;

    public ExplorationQuestListener(QuestManager questManager, Plugin plugin) {
        this.questManager = questManager;

        // Task que roda a cada 2 segundos (40 ticks)
        new BukkitRunnable() {
            @Override
            public void run() {
                checkPlayersNearStructures();
            }
        }.runTaskTimer(plugin, 0L, 40L);
    }

    /**
     * Verifica todos os jogadores online se estão perto de estruturas
     */
    private void checkPlayersNearStructures() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerQuestData questData = questManager.getPlayerQuests(player);
            if (questData == null) continue;

            // Procura por ExplorationQuests ativas
            for (Quest quest : questData.getActiveQuests().values()) {
                if (quest instanceof ExplorationQuest explorationQuest) {
                    if (!explorationQuest.checkCompletion()) {
                        // Atualiza progresso (verifica distância)
                        explorationQuest.updateProgress(player);
                    }
                }
            }
        }
    }
}