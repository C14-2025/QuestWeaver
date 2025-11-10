package br.dev.projetoc14.player.abilities.archerSkills;

import br.dev.projetoc14.player.RPGPlayer;
import org.bukkit.event.entity.ProjectileHitEvent;

public interface arrows {
    public void onHit(ProjectileHitEvent event, RPGPlayer caster, RPGPlayer target);

    public int getDamage();
}
