package br.dev.projetoc14.quest.archer;

import br.dev.projetoc14.quest.Quest;
import br.dev.projetoc14.quest.utils.PlayerQuestData;
import br.dev.projetoc14.quest.utils.QuestBook;
import br.dev.projetoc14.quest.utils.QuestManager;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

/**
 * Listener simplificado para quests do arqueiro - Adaptado para novo sistema
 */
public class ArcherQuestListener implements Listener {

    private final QuestManager questManager;
    private final QuestBook questBook;

    public ArcherQuestListener(QuestManager questManager) {
        this.questManager = questManager;
        this.questBook = new QuestBook(questManager);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) return;

        if (!(event.getEntity() instanceof LivingEntity entity)) return;

        if (!(event.getDamager() instanceof Arrow arrow)) return;

        if (!(arrow.getShooter() instanceof Player shooter)) return;

        PlayerQuestData questData = questManager.getPlayerQuests(shooter);
        if (questData == null) return;

        String mobType = entity.getType().name();
        Material weapon = Material.BOW; // Sempre bow para quests de arqueiro

        // Procura por qualquer quest de arqueiro ativa
        for (Quest quest : questData.getActiveQuests().values()) {

            // ===== QUEST 1: Combate a Distância =====
            if (quest instanceof RangedCombatQuest rangedQuest) {
                if (!mobType.equalsIgnoreCase("SKELETON")) continue;

                // DELEGA toda a validação para a própria quest
                rangedQuest.updateProgress(mobType, weapon, shooter, arrow);
                questBook.updateBook(shooter);
                break;
            }

            // ===== QUEST 2: Caçador Preciso =====
            else if (quest instanceof PrecisionHunterQuest precisionQuest) {
                if (!mobType.equalsIgnoreCase("ZOMBIE")) continue;

                // DELEGA toda a validação para a própria quest
                precisionQuest.updateProgress(mobType, weapon, shooter, arrow);
                questBook.updateBook(shooter);
                break;
            }

            // ===== QUEST 3: Mestre dos Ventos =====
            else if (quest instanceof WindMasterQuest windQuest) {
                if (!mobType.equalsIgnoreCase("CREEPER")) continue;

                // DELEGA toda a validação para a própria quest
                windQuest.updateProgress(mobType, weapon, shooter, arrow);
                questBook.updateBook(shooter);
                break;
            }
        }
    }

    /**
     * Detecta quando uma flecha erra o alvo (para WindMasterQuest)
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onProjectileMiss(ProjectileHitEvent event) {
        if (!(event.getEntity() instanceof Arrow arrow)) return;

        if (!(arrow.getShooter() instanceof Player shooter)) return;

        // Se acertou uma entidade, não é miss
        if (event.getHitEntity() != null) return;

        PlayerQuestData questData = questManager.getPlayerQuests(shooter);
        if (questData == null) return;

        // Verifica se o jogador tem a WindMasterQuest ativa
        for (Quest quest : questData.getActiveQuests().values()) {
            if (quest instanceof WindMasterQuest windQuest) {
                windQuest.onMissedShot(shooter);
                questBook.updateBook(shooter);
                break;
            }
        }
    }
}