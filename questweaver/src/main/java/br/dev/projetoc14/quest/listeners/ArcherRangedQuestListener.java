package br.dev.projetoc14.quest.listeners;

import br.dev.projetoc14.quest.utils.PlayerQuestData;
import br.dev.projetoc14.quest.utils.QuestManager;
import br.dev.projetoc14.quest.types.archer.RangedCombatQuest;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class ArcherRangedQuestListener implements Listener {

    private final QuestManager questManager;

    public ArcherRangedQuestListener(QuestManager questManager) {
        this.questManager = questManager;
    }

    @EventHandler
    public void onArrowHit(EntityDamageByEntityEvent event) {

        // Verifica se o dano foi por uma flecha
        if (!(event.getDamager() instanceof Arrow arrow)) return;

        // Verifica se o atirador é um jogador
        if (!(arrow.getShooter() instanceof Player player)) return;

        PlayerQuestData questData = questManager.getPlayerQuests(player);
        if (questData == null) return;

        // Pega todos quests do jogador e vê se alguma é de arqueiro
        for (var quest : questData.getActiveQuests().values()) {
            if (quest instanceof RangedCombatQuest rangedQuest) {

                Entity target = event.getEntity();

                double distance = player.getLocation().distance(target.getLocation());

                // ✅ Atualiza progresso apenas se passou da distância mínima
                if (distance >= rangedQuest.getRequiredDistance()) {
                    rangedQuest.incrementHits();
                    player.sendMessage("§a+1 acerto de longa distância! (" + ((RangedCombatQuest) quest).getProgressText() + ")");

                    // ✅ Checa se completou
                    if (quest.checkCompletion()) {
                        player.sendMessage("§e✔ Quest completa: §a" + quest.getName());
                        questData.completeQuest(quest.getId());
                        // Se tiver livro, atualizar
                        // plugin.getQuestBook().updateBook(player);
                    }
                }
            }
        }
    }
}
