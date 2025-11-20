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

public class ExplosiveArrow extends Ability implements arrows, Listener {

    private final int damage = 5;
    private final int areaDamage = 3;
    private final double explosionRadius = 3.5;
    private final Plugin plugin;

    public ExplosiveArrow(Plugin plugin) {
        super("Flecha Explosiva", 15, 6);
        this.plugin = plugin;
    }

    @Override
    public void onCast(RPGPlayer caster) {
        Location loc = caster.getEyeLocation();
        Arrow arrow = caster.launchProjectile(Arrow.class);
        arrow.setCritical(true);
        arrow.setVelocity(arrow.getVelocity().multiply(1.5));
        arrow.setFireTicks(100); // Flecha em chamas

        arrow.setMetadata("explosive_arrow", new FixedMetadataValue(plugin, true));
        arrow.setMetadata("explosive_arrow_shooter", new FixedMetadataValue(plugin, caster.getUniqueId()));

        caster.getWorld().playSound(loc, Sound.ENTITY_ARROW_SHOOT, 1f, 1.2f);
        caster.getWorld().spawnParticle(Particle.CRIT, loc, 10, 0.2, 0.2, 0.2, 0.01);
        caster.getWorld().spawnParticle(Particle.FLAME, loc, 5, 0.1, 0.1, 0.1, 0.01);
    }

    @Override
    public void onHit(ProjectileHitEvent event, RPGPlayer caster, RPGPlayer target) {
        if (!(event.getEntity() instanceof Arrow arrow)) return;

        Location hitLoc = arrow.getLocation();

        playExplosionEffects(hitLoc);

        // Dano direto no alvo, se houver
        if (target != null) {
            applyDirectDamage(target);

            if (target.getPlayer() != null) {
                applyBurnEffect(target.getPlayer());
                applyExplosionKnockback(caster, target.getPlayer(), hitLoc);
            }
        }

        applyAreaDamage(hitLoc, caster);
        createShockwaveAnimation(hitLoc);
    }

    private void applyDirectDamage(RPGPlayer target) {
        int newHealth = target.getCurrentHealth() - damage;
        target.setCurrentHealth(Math.max(newHealth, 0));
    }

    private void playExplosionEffects(Location hitLoc) {
        // Partículas de explosão
        hitLoc.getWorld().spawnParticle(Particle.EXPLOSION, hitLoc, 3, 0.5, 0.5, 0.5, 0);
        hitLoc.getWorld().spawnParticle(Particle.FLAME, hitLoc, 30, 1.5, 1.5, 1.5, 0.1);
        hitLoc.getWorld().spawnParticle(Particle.LAVA, hitLoc, 15, 1, 1, 1, 0);
        hitLoc.getWorld().spawnParticle(Particle.SMOKE, hitLoc, 20, 1, 1, 1, 0.05);

        // Sons de explosão
        hitLoc.getWorld().playSound(hitLoc, Sound.ENTITY_GENERIC_EXPLODE, 1.5f, 1.0f);
        hitLoc.getWorld().playSound(hitLoc, Sound.ENTITY_FIREWORK_ROCKET_BLAST, 1.0f, 0.8f);
    }

    private void applyBurnEffect(Player player) {
        if (player == null || !player.isOnline()) return;

        // Define o jogador em chamas
        player.setFireTicks(60); // 3 segundos

        // Adiciona efeito de fraqueza temporária
        player.addPotionEffect(
                new PotionEffect(
                        PotionEffectType.WEAKNESS,
                        40, // 2 segundos
                        0,
                        false, false, true
                )
        );
    }

    private void applyExplosionKnockback(RPGPlayer caster, Player target, Location explosionCenter) {
        if (caster == null || target == null || !target.isOnline()) return;

        Vector knockback = target.getLocation().toVector()
                .subtract(explosionCenter.toVector())
                .normalize()
                .multiply(1.2)
                .setY(0.4); // Empurra para cima também

        target.setVelocity(knockback);
    }

    private void applyAreaDamage(Location center, RPGPlayer caster) {
        center.getWorld().getNearbyEntities(center, explosionRadius, explosionRadius, explosionRadius)
                .forEach(entity -> {
                    if (entity instanceof LivingEntity livingEntity) {
                        // Não atinge o próprio
                        if (caster != null && caster.getPlayer() != null &&
                                entity.equals(caster.getPlayer())) {
                            return;
                        }

                        double distance = entity.getLocation().distance(center);

                        // Dano diminui com a distância
                        if (distance <= explosionRadius) {
                            applyAreaDamageToEntity(livingEntity, center, distance);
                        }
                    }
                });
    }

    private void applyAreaDamageToEntity(LivingEntity entity, Location center, double distance) {
        // Dano diminui proporcionalmente à distância
        double damageMultiplier = 1.0 - (distance / explosionRadius);
        int finalDamage = (int) Math.ceil(areaDamage * damageMultiplier);

        if (entity instanceof RPGPlayer rpgPlayer) {
            int newHealth = rpgPlayer.getCurrentHealth() - finalDamage;
            rpgPlayer.setCurrentHealth(Math.max(newHealth, 0));
        } else {
            // Para mobs normais
            entity.damage(finalDamage);
        }

        entity.setFireTicks(40);

        // Empurra a entidade
        Vector push = entity.getLocation().toVector()
                .subtract(center.toVector())
                .normalize()
                .multiply(0.6)
                .setY(0.3);
        entity.setVelocity(entity.getVelocity().add(push));
    }

    private void createShockwaveAnimation(Location center) {
        new BukkitRunnable() {
            double radius = 0.5;
            int ticks = 0;

            @Override
            public void run() {
                if (ticks >= 20 || radius > explosionRadius) {
                    this.cancel();
                    return;
                }

                // Criar anel de partículas
                for (int i = 0; i < 30; i++) {
                    double angle = 2 * Math.PI * i / 30;
                    double x = center.getX() + radius * Math.cos(angle);
                    double z = center.getZ() + radius * Math.sin(angle);
                    Location particleLoc = new Location(center.getWorld(), x, center.getY() + 0.1, z);

                    center.getWorld().spawnParticle(
                            Particle.FLAME,
                            particleLoc,
                            1, 0, 0, 0, 0
                    );

                    if (ticks % 2 == 0) {
                        center.getWorld().spawnParticle(
                                Particle.SMOKE,
                                particleLoc,
                                1, 0, 0, 0, 0
                        );
                    }
                }

                // Som de propagação
                if (ticks % 5 == 0) {
                    center.getWorld().playSound(center, Sound.BLOCK_FIRE_AMBIENT, 0.3f, 1.5f);
                }

                radius += 0.3;
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }

    @Override
    public int getDamage() {
        return damage;
    }

    public int getAreaDamage() {
        return areaDamage;
    }

    public double getExplosionRadius() {
        return explosionRadius;
    }
}