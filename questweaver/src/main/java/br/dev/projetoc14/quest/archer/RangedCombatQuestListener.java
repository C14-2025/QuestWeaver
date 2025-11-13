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
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class RangedCombatQuestListener implements Listener {

    private final QuestManager questManager;
    private final QuestBook questBook;

    public RangedCombatQuestListener(QuestManager questManager) {
        this.questManager = questManager;
        this.questBook = new QuestBook(questManager);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        // Verifica se quem tomou dano é uma entidade viva
        if (!(event.getEntity() instanceof LivingEntity entity)) {
            return;
        }

        // Verifica se é um zumbi
        if (!entity.getType().name().equalsIgnoreCase("ZOMBIE")) {
            return;
        }

        // Verifica se o dano foi causado por uma flecha
        if (!(event.getDamager() instanceof Arrow arrow)) {
            return;
        }

        // Verifica se a flecha foi atirada por um jogador
        if (!(arrow.getShooter() instanceof Player shooter)) {
            return;
        }

        // Pega as quests do jogador através do QuestManager
        PlayerQuestData questData = questManager.getPlayerQuests(shooter);
        if (questData == null) {
            return;
        }

        // Procura pela RangedCombatQuest nas quests ativas
        for (Quest quest : questData.getActiveQuests().values()) {
            if (quest instanceof RangedCombatQuest rangedQuest) {
                // Atualiza o progresso da quest passando a arrow como parâmetro
                String mobType = entity.getType().name();
                Material weapon = Material.BOW;

                rangedQuest.updateProgress(mobType, weapon, shooter, arrow);

                // Feedback visual para o jogador
                int current = rangedQuest.getCurrentCount();
                int target = rangedQuest.getTargetCount();

                if (current <= target) {
                    shooter.sendMessage("§a✓ Progresso: " + rangedQuest.getProgressText());

                    // Atualiza o livro de quests do jogador (se ele tiver)
                    questBook.updateBook(shooter);
                }

                break; // Sai do loop após processar a quest
            }
        }
    }
}