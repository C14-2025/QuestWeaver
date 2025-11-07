package br.dev.projetoc14.player.abilities.archerSkills;

import br.dev.projetoc14.player.RPGPlayer;
import br.dev.projetoc14.player.abilities.Ability;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

public class PoisonArrow extends Ability implements arrows{

    private final Plugin plugin;
    private final int damage = 12;

    public PoisonArrow(String name, int manaCost, int cooldown, Plugin plugin) {
        super(name, manaCost, cooldown);
        this.plugin = plugin;
    }

    @Override
    protected void onCast(RPGPlayer caster) {
        Location loc = caster.getEyeLocation();
        Arrow arrow = caster.launchProjectile(Arrow.class);
        arrow.setCritical(true);
        arrow.setVelocity(arrow.getVelocity().multiply(1.5));

        arrow.setMetadata("poison_arrow", new FixedMetadataValue(plugin, true));
        arrow.setMetadata("poison_arrow_shooter", new FixedMetadataValue(plugin, caster.getUniqueId()));

        caster.getWorld().playSound(loc, Sound.ENTITY_ARROW_SHOOT, 1f, 1.2f);
        caster.getWorld().spawnParticle(Particle.CRIT, loc, 10, 0.2, 0.2, 0.2, 0.01);
    }

    @Override
    public void onHit(ProjectileHitEvent event, RPGPlayer caster, RPGPlayer target) {
        if (!(event.getEntity() instanceof Arrow arrow)) return;

        Location hitLoc = arrow.getLocation();

        // Efeitos visuais e sonoros
        hitLoc.getWorld().spawnParticle(Particle.DAMAGE_INDICATOR, hitLoc, 20, 1, 1, 1, 0.05);
        hitLoc.getWorld().playSound(hitLoc, Sound.BLOCK_NOTE_BLOCK_BANJO, 1.2f, 1.0f);

        // Dano direto no alvo, se houver
        if (target != null) {
            int newHealth = target.getCurrentHealth() - damage;
            if (newHealth < 0) newHealth = 0;
            target.setCurrentHealth(newHealth);
        }
        arrow.remove();
    }

    @Override
    public int getDamage() {
        return damage;
    }
}
