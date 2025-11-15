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
import org.bukkit.potion.PotionEffectType;

public class KnockbackArrow extends Ability implements arrows {

    private final int damage = 15;
    private final Plugin plugin;

    public KnockbackArrow(Plugin plugin) {
        super("Flecha Repulsora", 15, 4);
        this.plugin = plugin;
    }

    @Override
    protected void onCast(RPGPlayer caster) {
        Location loc = caster.getEyeLocation();
        Arrow arrow = caster.launchProjectile(Arrow.class);
        arrow.setCritical(true);
        arrow.setVelocity(arrow.getVelocity().multiply(1.5));

        arrow.setMetadata("knockback_arrow", new FixedMetadataValue(plugin, true));
        arrow.setMetadata("knockback_arrow_shooter", new FixedMetadataValue(plugin, caster.getUniqueId()));

        caster.getWorld().playSound(loc, Sound.ENTITY_ARROW_SHOOT, 1f, 1.2f);
        caster.getWorld().spawnParticle(Particle.CRIT, loc, 10, 0.2, 0.2, 0.2, 0.01);
    }

    @Override
    public void onHit(ProjectileHitEvent event, RPGPlayer caster, RPGPlayer target) {
        if (!(event.getEntity() instanceof Arrow arrow)) return;

        Location hitLoc = arrow.getLocation();

        playHitEffects(hitLoc);

        if (target != null) {
            applyDamage(target);
            applySlowness(target);
            applyNausea(target);
            applyShockwaves(caster, target);
        }
        arrow.remove();
    }

    private void applyDamage(RPGPlayer target) {
        int newHealth = target.getCurrentHealth() - damage;
        target.setCurrentHealth(Math.max(newHealth, 0));
    }

    private void playHitEffects(Location hitLoc) {
        hitLoc.getWorld().spawnParticle(Particle.DAMAGE_INDICATOR, hitLoc, 20, 1, 1, 1, 0.05);
        hitLoc.getWorld().playSound(hitLoc, Sound.ENTITY_PLAYER_ATTACK_KNOCKBACK, 1.2f, 1.0f);
    }

    private void applySlowness(RPGPlayer target) {
        target.getPlayer().addPotionEffect(
                new org.bukkit.potion.PotionEffect(
                        PotionEffectType.SLOWNESS,
                        20, // 1 segundo
                        1,
                        false, false, false
                )
        );
    }

    private void applyNausea(RPGPlayer target) {
        target.getPlayer().addPotionEffect(
                new org.bukkit.potion.PotionEffect(
                        PotionEffectType.NAUSEA,
                        16, // 0.8s
                        0,
                        false, false, false
                )
        );
    }

    private void applyShockwavePush(RPGPlayer caster, org.bukkit.entity.Player bukkitTarget) {
        if (caster == null) return;

        org.bukkit.util.Vector push =
                bukkitTarget.getLocation().toVector()
                        .subtract(caster.getLocation().toVector())
                        .normalize()
                        .multiply(0.3);

        bukkitTarget.setVelocity(bukkitTarget.getVelocity().add(push));
    }

    private void applyShockwaves(RPGPlayer caster, RPGPlayer target) {
        final org.bukkit.entity.Player bukkitTarget = target.getPlayer();

        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            int ticks = 0;

            @Override
            public void run() {
                if (ticks >= 16 || !bukkitTarget.isOnline()) {
                    plugin.getServer().getScheduler().cancelTask(this.hashCode());
                    return;
                }

                applyShockwavePush(caster, bukkitTarget);
                playShockwaveParticles(bukkitTarget);

                if (ticks % 4 == 0) {
                    playShockwaveSound(bukkitTarget);
                }

                ticks++;
            }
        }, 1L, 1L);
    }

    private void playShockwaveParticles(org.bukkit.entity.Player bukkitTarget) {
        bukkitTarget.getWorld().spawnParticle(
                Particle.CRIT,
                bukkitTarget.getLocation().add(0, 1, 0),
                4, 0.3, 0.3, 0.3, 0.01
        );
    }

    private void playShockwaveSound(org.bukkit.entity.Player bukkitTarget) {
        bukkitTarget.getWorld().playSound(
                bukkitTarget.getLocation(),
                Sound.BLOCK_ANVIL_LAND,
                0.4f,
                1.5f
        );
    }

    @Override
    public int getDamage() {
        return damage;
    }
}
