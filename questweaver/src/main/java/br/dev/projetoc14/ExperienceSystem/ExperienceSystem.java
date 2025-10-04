package br.dev.projetoc14.ExperienceSystem;

import org.bukkit.entity.LivingEntity;

import org.bukkit.event.Listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import org.bukkit.event.entity.EntityDeathEvent;


public class ExperienceSystem implements Listener {
    final String alvo = "Bicho"; // nick do bicho específico que a gente vai ter que matar
    final int maxLevel = 15; // nível máximo que o player pode atingir
    final int xpPerKill = 1; // quantidade de levels que sobe por kill

    @EventHandler
    public void onMobDeath(EntityDeathEvent e){
        LivingEntity mob = e.getEntity();
        Player killer = mob.getKiller();

        if (killer == null) return;

        String mobName = mob.getCustomName();

        if (mobName == null) return;

        if(mobName.equalsIgnoreCase(alvo)){
            if(killer.getLevel() < maxLevel){
                killer.giveExpLevels(xpPerKill);
                killer.sendMessage("§a+1XP.");
            } else {
                killer.sendMessage("§cNível máximo atingido.");
            }
        }
    }

    public int calcularNovoNivel(String mobName, int nivelAtual) {

        if (mobName == null) return nivelAtual;

        if (mobName.equalsIgnoreCase(alvo) && nivelAtual < maxLevel) {
            return nivelAtual + xpPerKill;
        }
        return nivelAtual; // não ganha XP
    }
}
