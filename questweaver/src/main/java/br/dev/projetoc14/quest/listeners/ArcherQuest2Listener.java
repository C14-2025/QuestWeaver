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
import br.dev.projetoc14.quest.types.archer.RangedCombatQuest;

public class ArcherQuest2Listener implements Listener {

    private final QuestManager questManager;

    public ArcherQuest2Listener(QuestManager questManager) {
        this.questManager = questManager;
    }

    @EventHandler
    public void onArrowHit(EntityDamageByEntityEvent event) {

        if (!(event.getDamager() instanceof Arrow arrow)) return;
        if (!(arrow.getShooter() instanceof Player player)) return;

        //Checa se o player está na Quest 2
        if (!questManager.isDoingQuest(player, "arqueiro_2")) return;

        Entity target = event.getEntity();

        //Distância mínima recomendada (30 blocos)
        double distance = player.getLocation().distance(target.getLocation());
        if (distance < 30) return;

        //Se quiser validar headshot futuramente, pode colocar aqui
        // if (!isHeadshot(event)) return;

        //Progresso incrementado
        questManager.addProgress(player, "arqueiro_2", 1);

        int atual = questManager.getProgress(player, "arqueiro_2");
        int total = questManager.getGoal(player, "arqueiro_2");

        player.sendMessage("§a✔ Progresso: §f" + atual + "/" + total);

        //Completa se necessário
        if (atual >= total) {
            questManager.completeQuest(player, "arqueiro_2");
            player.sendMessage("§6🎯 Quest concluída! Olho de Águia desbloqueado!");
        }
    }
}
