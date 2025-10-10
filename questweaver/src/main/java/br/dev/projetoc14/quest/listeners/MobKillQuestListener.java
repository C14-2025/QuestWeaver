package br.dev.projetoc14.quest.listeners;

import br.dev.projetoc14.QuestWeaver;
import br.dev.projetoc14.quest.utils.PlayerQuestData;
import br.dev.projetoc14.quest.Quest;
import br.dev.projetoc14.quest.utils.QuestManager;
import br.dev.projetoc14.quest.KillQuest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

/*
 * Atualiza progresso das KillQuests quando jogador mata mobs
 */
public class MobKillQuestListener implements Listener {

    private final QuestManager questManager;
    private final QuestWeaver plugin;

    public MobKillQuestListener(QuestManager questManager, QuestWeaver plugin) {
        this.questManager = questManager;
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        // Verifica se foi um player que matou
        if (!(event.getEntity().getKiller() instanceof Player)) {
            return;
        }

        Player player = event.getEntity().getKiller();
        String mobType = event.getEntity().getType().name();

        // Pega as quests do jogador
        PlayerQuestData questData = questManager.getPlayerQuests(player);
        if (questData == null) return;

        // Atualiza todas as KillQuests ativas
        for (Quest quest : questData.getActiveQuests().values()) {
            if (quest instanceof KillQuest) {
                quest.updateProgress(mobType);

                // Verifica se completou
                if (quest.checkCompletion()) {
                    player.sendMessage("§a✓ Quest Completa: §e" + quest.getName());
                    player.sendMessage("§6+" + quest.getExperienceReward() + " XP!");
                    questData.completeQuest(quest.getId());
                    plugin.getQuestBook().updateBook(player);
                }
            }
        }
    }
}