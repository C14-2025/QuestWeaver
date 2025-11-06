package br.dev.projetoc14.quest.listeners;

import br.dev.projetoc14.QuestWeaver;
import br.dev.projetoc14.player.RPGPlayer;
import br.dev.projetoc14.player.PlayerClass;
import br.dev.projetoc14.quest.utils.PlayerQuestData;
import br.dev.projetoc14.quest.Quest;
import br.dev.projetoc14.quest.utils.QuestManager;
import br.dev.projetoc14.quest.KillQuest;
import br.dev.projetoc14.quest.types.archer.RangedCombatQuest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class MobKillQuestListener implements Listener {

    private final QuestManager questManager;
    private final QuestWeaver plugin;

    public MobKillQuestListener(QuestManager questManager, QuestWeaver plugin) {
        this.questManager = questManager;
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (!(event.getEntity().getKiller() instanceof Player player)) {
            return;
        }

        String mobType = event.getEntity().getType().name();
        PlayerQuestData questData = questManager.getPlayerQuests(player);
        if (questData == null) return;

        for (Quest quest : questData.getActiveQuests().values()) {
            if (quest instanceof KillQuest) {
                quest.updateProgress(mobType);

                if (quest.checkCompletion()) {
                    player.sendMessage("§a✓ Quest Completa: §e" + quest.getName());
                    player.sendMessage("§6+" + quest.getExperienceReward() + " XP!");
                    questData.completeQuest(quest.getId());
                    plugin.getQuestBook().updateBook(player);


                    RPGPlayer rpgPlayer = plugin.getRPGPlayer(player);
                    if (rpgPlayer != null && rpgPlayer.getPlayerClass() == PlayerClass.ARCHER) {
                        if (!questData.getActiveQuests().containsKey("ranged_combat_intro")) {
                            RangedCombatQuest rangedQuest = new RangedCombatQuest(
                                    "ranged_combat_intro",
                                    "Tiro Certeiro",
                                    "Acerte 10 flechas em mobs a mais de 20 blocos de distância",
                                    100,
                                    10,
                                    20.0
                            );

                            questData.addQuest(rangedQuest);
                            player.sendMessage("§6═══════════════════════════");
                            player.sendMessage("§e✦ §6Nova Quest Desbloqueada!");
                            player.sendMessage("§f" + rangedQuest.getName());
                            player.sendMessage("§7" + rangedQuest.getDescription());
                            player.sendMessage("§6═══════════════════════════");
                        }
                    }
                }
            }
        }
    }
}