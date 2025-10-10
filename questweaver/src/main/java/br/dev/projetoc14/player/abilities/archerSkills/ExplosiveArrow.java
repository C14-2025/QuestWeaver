package br.dev.projetoc14.player.abilities.archerSkills;

import br.dev.projetoc14.player.RPGPlayer;
import br.dev.projetoc14.player.abilities.Ability;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.event.entity.ProjectileHitEvent;

public class ExplosiveArrow extends Ability {

    private final int damage = 20;

    public ExplosiveArrow() {
        super("Flecha Explosiva", 15, 6);
    }

    @Override
    public void cast(RPGPlayer caster) {
        Location loc = caster.getEyeLocation();
        Arrow arrow = caster.launchProjectile(Arrow.class);
        arrow.setCritical(true);
        arrow.setVelocity(arrow.getVelocity().multiply(1.5));

        caster.getWorld().playSound(loc, Sound.ENTITY_ARROW_SHOOT, 1f, 1.2f);
        caster.getWorld().spawnParticle(Particle.CRIT, loc, 10, 0.2, 0.2, 0.2, 0.01);

        caster.sendMessage("🏹 Você disparou uma Flecha Explosiva!");
    }

    public void onHit(ProjectileHitEvent event, RPGPlayer caster, RPGPlayer target) {
        if (target == null) return;
        int newHealth = target.getCurrentHealth() - damage;
        if (newHealth < 0) newHealth = 0;
        target.setCurrentHealth(newHealth);
        target.getWorld().createExplosion(target.getEyeLocation(), 0.0F); // efeito visual, sem dano em blocos
    }

    public int getDamage() {
        return damage;
    }
}
