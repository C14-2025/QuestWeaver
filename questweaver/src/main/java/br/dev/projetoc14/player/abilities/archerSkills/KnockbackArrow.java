package br.dev.projetoc14.player.abilities.archerSkills;

import br.dev.projetoc14.player.RPGPlayer;
import br.dev.projetoc14.player.abilities.Ability;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class KnockbackArrow extends Ability implements arrows, Listener {

    private final int damage = 4;
    private final Plugin plugin;

    public KnockbackArrow(Plugin plugin) {
        super("Flecha Repulsora", 15, 4);
        this.plugin = plugin;
    }

    @Override
    public void onCast(RPGPlayer caster) {
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

        // Se atingiu um RPGPlayer
        if (target != null) {
            applyDamage(target);

            // Só aplica efeitos se o player existir
            if (target.getPlayer() != null) {
                applySlowness(target);
                applyNausea(target);
                applyShockwaves(caster, target);
            }
        }
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
        if (target.getPlayer() == null) return;

        target.getPlayer().addPotionEffect(
                new PotionEffect(
                        PotionEffectType.SLOWNESS,
                        60, // 3 segundos
                        1,  // Slowness II
                        false, false, true
                )
        );
    }

    private void applyNausea(RPGPlayer target) {
        if (target.getPlayer() == null) return;

        target.getPlayer().addPotionEffect(
                new PotionEffect(
                        PotionEffectType.NAUSEA,
                        100, // 5 segundos
                        0,
                        false, false, true
                )
        );
    }

    private void applyShockwavePush(RPGPlayer caster, Player bukkitTarget) {
        if (caster == null || bukkitTarget == null || !bukkitTarget.isOnline()) return;

        Vector push = bukkitTarget.getLocation().toVector()
                .subtract(caster.getLocation().toVector())
                .normalize()
                .multiply(0.08);

        bukkitTarget.setVelocity(bukkitTarget.getVelocity().add(push));
    }

    private void applyShockwaves(RPGPlayer caster, RPGPlayer target) {
        final Player bukkitTarget = target.getPlayer();
        if (bukkitTarget == null || !bukkitTarget.isOnline()) return;

        new BukkitRunnable() {
            int ticks = 0;

            @Override
            public void run() {
                if (ticks >= 16 || !bukkitTarget.isOnline()) {
                    this.cancel();
                    return;
                }

                applyShockwavePush(caster, bukkitTarget);
                playShockwaveParticles(bukkitTarget);

                if (ticks % 4 == 0) {
                    playShockwaveSound(bukkitTarget);
                }

                ticks++;
            }
        }.runTaskTimer(plugin, 1L, 1L);
    }

    private void playShockwaveParticles(Player bukkitTarget) {
        bukkitTarget.getWorld().spawnParticle(
                Particle.CRIT,
                bukkitTarget.getLocation().add(0, 1, 0),
                4, 0.3, 0.3, 0.3, 0.01
        );
    }

    private void playShockwaveSound(Player bukkitTarget) {
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