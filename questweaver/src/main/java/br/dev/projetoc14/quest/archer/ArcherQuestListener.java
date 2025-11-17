package br.dev.projetoc14.quest.archer;

import br.dev.projetoc14.quest.Quest;
import br.dev.projetoc14.quest.utils.PlayerQuestData;
import br.dev.projetoc14.quest.utils.QuestBook;
import br.dev.projetoc14.quest.utils.QuestManager;
import org.bukkit.Location;
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
 * Listener unificado para todas as quests do arqueiro
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

        // Procura por qualquer quest de arqueiro ativa
        for (Quest quest : questData.getActiveQuests().values()) {

            // ===== QUEST 1: Combate a Distância =====
            if (quest instanceof RangedCombatQuest rangedQuest) {
                if (!entity.getType().name().equalsIgnoreCase("SKELETON")) continue;

                Location shooterLoc = shooter.getLocation();
                Location hitLoc = entity.getLocation();
                double distance = shooterLoc.distance(hitLoc);

                String mobType = entity.getType().name();
                Material weapon = Material.BOW;

                rangedQuest.updateProgress(mobType, weapon, shooter, arrow);

                int current = rangedQuest.getCurrentCount();
                int target = rangedQuest.getTargetCount();

                if (current <= target) {
                    if (distance >= RangedCombatQuest.getMinDistance()) {
                        shooter.sendMessage(String.format("§a✓ Acerto de longa distância! (%.1f blocos)", distance));
                        shooter.sendMessage("§a✓ Progresso: " + rangedQuest.getProgressText());
                    } else {
                        shooter.sendMessage(String.format("§c✗ Muito perto! (%.1f/%.0f blocos)",
                                distance, RangedCombatQuest.getMinDistance()));
                    }
                    questBook.updateBook(shooter);
                }
                break;
            }

            // ===== QUEST 2: Caçador Preciso =====
            else if (quest instanceof PrecisionHunterQuest precisionQuest) {
                if (!entity.getType().name().equalsIgnoreCase("ZOMBIE")) continue;

                String mobType = entity.getType().name();
                Material weapon = Material.BOW;

                precisionQuest.updateProgress(mobType, weapon, shooter, arrow);

                int current = precisionQuest.getCurrentCount();
                int target = precisionQuest.getTargetCount();

                if (current <= target) {
                    if (arrow.isCritical()) {
                        shooter.sendMessage("§e⚡ Tiro Crítico! §a✓");
                        shooter.sendMessage("§aProgresso: " + precisionQuest.getProgressText());
                    } else {
                        shooter.sendMessage("§c✗ Não foi crítico! Atire com o arco totalmente puxado.");
                    }
                    questBook.updateBook(shooter);
                }
                break;
            }

            // ===== QUEST 3: Mestre dos Ventos =====
            else if (quest instanceof WindMasterQuest windQuest) {
                if (!entity.getType().name().equalsIgnoreCase("CREEPER")) continue;

                String mobType = entity.getType().name();
                Material weapon = Material.BOW;

                windQuest.updateProgress(mobType, weapon, shooter, arrow);

                int current = windQuest.getCurrentCount();
                int target = windQuest.getTargetCount();
                int combo = windQuest.getCombo(shooter);

                if (current <= target) {
                    shooter.sendMessage("§aProgresso: " + windQuest.getProgressText());

                    if (combo >= 5) {
                        shooter.sendMessage("§6✦ Continue assim! Faltam " + (target - current) + " acertos!");
                    }

                    questBook.updateBook(shooter);
                }
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