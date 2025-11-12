package br.dev.projetoc14.quest.archer;

import br.dev.projetoc14.quest.Quest;
import br.dev.projetoc14.quest.utils.PlayerQuestData;
import br.dev.projetoc14.quest.utils.QuestManager;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class RangedCombatQuestListener implements Listener {

    private final QuestManager questManager;

    public RangedCombatQuestListener(QuestManager questManager) {
        this.questManager = questManager;
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();

        // Verifica se é um zumbi
        if (!entity.getType().name().equalsIgnoreCase("ZOMBIE")) {
            return;
        }

        // Verifica se foi morto por uma flecha
        if (!(entity.getLastDamageCause() instanceof EntityDamageByEntityEvent damageEvent)) {
            return;
        }

        if (!(damageEvent.getDamager() instanceof Arrow arrow)) {
            return;
        }

        // Verifica se a flecha foi atirada por um jogador
        if (!(arrow.getShooter() instanceof Player killer)) {
            return;
        }

        // Pega as quests do jogador através do QuestManager
        PlayerQuestData questData = questManager.getPlayerQuests(killer);
        if (questData == null) {
            return;
        }

        // Procura pela RangedCombatQuest nas quests ativas
        for (Quest quest : questData.getActiveQuests().values()) {
            if (quest instanceof RangedCombatQuest rangedQuest) {
                // Atualiza o progresso da quest passando a arrow como parâmetro
                String mobType = entity.getType().name();
                Material weapon = Material.BOW;

                rangedQuest.updateProgress(mobType, weapon, killer, arrow);

                // Feedback visual para o jogador
                int current = rangedQuest.getCurrentCount();
                int target = rangedQuest.getTargetCount();

                if (current <= target) {
                    killer.sendMessage("§a✓ Progresso: " + rangedQuest.getProgressText());
                }

                break; // Sai do loop após processar a quest
            }
        }
    }
}