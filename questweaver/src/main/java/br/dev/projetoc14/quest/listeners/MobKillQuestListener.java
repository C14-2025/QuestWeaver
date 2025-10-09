// =============================================
// ARQUIVO 6: br/dev/projetoc14/quests/listeners/MobKillQuestListener.java
// Atualiza quest quando jogador mata mob
// =============================================

package br.dev.projetoc14.quest.listeners;

import br.dev.projetoc14.quest.PlayerQuestData;
import br.dev.projetoc14.quest.Quest;
import br.dev.projetoc14.quest.QuestManager;
import br.dev.projetoc14.quest.KillQuest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

/*
 * Atualiza progresso das KillQuests quando jogador mata mobs
 */
public class MobKillQuestListener implements Listener {

    private QuestManager questManager;

    public MobKillQuestListener(QuestManager questManager) {
        this.questManager = questManager;
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
                if (quest.checkCompletion() && !quest.isCompleted()) {
                    player.sendMessage("§a✓ Quest Completa: §e" + quest.getName());
                    player.sendMessage("§6+50 XP!");
                    questData.completeQuest(quest.getId());
                }
            }
        }
    }
}