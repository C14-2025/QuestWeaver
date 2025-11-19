package br.dev.projetoc14.quest.assassin;

import br.dev.projetoc14.quest.Quest;
import br.dev.projetoc14.quest.utils.PlayerQuestData;
import br.dev.projetoc14.quest.utils.QuestBook;
import br.dev.projetoc14.quest.utils.QuestManager;
import org.bukkit.Material;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

/**
 * Listener unificado para todas as quests do assassino
 */
public class AssassinQuestListener implements Listener {

    private final QuestManager questManager;
    private final QuestBook questBook;

    public AssassinQuestListener(QuestManager questManager) {
        this.questManager = questManager;
        this.questBook = new QuestBook(questManager);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        Player killer = entity.getKiller();

        if (killer == null) return;

        PlayerQuestData questData = questManager.getPlayerQuests(killer);
        if (questData == null) return;

        String mobType = entity.getType().name();
        Material weapon = killer.getInventory().getItemInMainHand().getType();

        // Procura por qualquer quest de assassino ativa
        for (Quest quest : questData.getActiveQuests().values()) {

            // ===== QUEST 1: Sombras Silenciosas (Backstabs) =====
            if (quest instanceof SilentShadowsQuest shadowQuest) {
                if (!mobType.equalsIgnoreCase("ZOMBIE")) continue;

                shadowQuest.updateProgress(mobType, weapon, killer, entity);

                int current = shadowQuest.getCurrentCount();
                int target = shadowQuest.getTargetCount();

                if (current <= target) {
                    killer.sendMessage("§aProgresso: " + shadowQuest.getProgressText());
                    questBook.updateBook(killer);
                }
                break;
            }

            // ===== QUEST 2: Velocidade Mortal (Speed Kills) =====
            else if (quest instanceof DeadlySpeedQuest speedQuest) {
                if (!mobType.equalsIgnoreCase("SKELETON")) continue;

                speedQuest.updateProgress(mobType, weapon, killer);

                int current = speedQuest.getCurrentCount();
                int target = speedQuest.getTargetCount();
                int streak = speedQuest.getStreak(killer);

                if (current <= target) {
                    killer.sendMessage("§aProgresso: " + speedQuest.getProgressText());

                    if (streak >= 5) {
                        killer.sendMessage("§6⚡ Streak alto! Continue rápido!");
                    }

                    questBook.updateBook(killer);
                }
                break;
            }

            // ===== QUEST 3: Assassinato Perfeito (Perfect Kills) =====
            else if (quest instanceof PerfectAssassinationQuest perfectQuest) {
                if (!mobType.equalsIgnoreCase("CREEPER")) continue;

                perfectQuest.updateProgress(mobType, weapon, killer, entity);

                int current = perfectQuest.getCurrentCount();
                int target = perfectQuest.getTargetCount();

                if (current <= target) {
                    killer.sendMessage("§aProgresso: " + perfectQuest.getProgressText());
                    questBook.updateBook(killer);
                }
                break;
            }
        }
    }

    /**
     * Detecta quando o jogador toma dano (para PerfectAssassinationQuest)
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerDamaged(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;

        if (!(event.getEntity() instanceof Player player)) return;

        PlayerQuestData questData = questManager.getPlayerQuests(player);
        if (questData == null) return;

        // Verifica se tem a quest de assassinato perfeito ativa
        for (Quest quest : questData.getActiveQuests().values()) {
            if (quest instanceof PerfectAssassinationQuest perfectQuest) {
                perfectQuest.onPlayerDamaged(player);
                questBook.updateBook(player);
                break;
            }
        }
    }

    /**
     * Detecta quando um creeper explode (para PerfectAssassinationQuest)
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onCreeperExplode(EntityExplodeEvent event) {
        if (!(event.getEntity() instanceof Creeper creeper)) return;

        // Notifica todas as quests ativas
        for (Player player : org.bukkit.Bukkit.getOnlinePlayers()) {
            PlayerQuestData questData = questManager.getPlayerQuests(player);
            if (questData == null) continue;

            for (Quest quest : questData.getActiveQuests().values()) {
                if (quest instanceof PerfectAssassinationQuest perfectQuest) {
                    perfectQuest.onCreeperExplode(creeper);
                }
            }
        }
    }
}