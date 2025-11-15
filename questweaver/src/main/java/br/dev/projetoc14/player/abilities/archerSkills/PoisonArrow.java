package br.dev.projetoc14.player.abilities.archerSkills;

import br.dev.projetoc14.player.RPGPlayer;
import br.dev.projetoc14.player.abilities.Ability;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class PoisonArrow extends Ability implements arrows{

    private final Plugin plugin;
    private final int damage = 6;

    public PoisonArrow(Plugin plugin) {
        super("Flecha Venenosa", 20, 9);
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

            // Só aplica veneno se o player existir
            if (target.getPlayer() != null) {
                applyPoisonEffect(target);
            }
        }
    }

    private void applyPoisonEffect(RPGPlayer target) {
        final Player p = target.getPlayer();
        if (p == null || !p.isOnline()) return;
        new BukkitRunnable() {
            int ticks = 0;

            @Override
            public void run() {
                if (ticks >= 80 || !p.isOnline()) {
                    this.cancel();
                    return;
                }

                if (ticks % 20 == 0) {
                    applyPoisonTick(target);
                }

                spawnPoisonParticles(p);
                ticks++;
            }
        }.runTaskTimer(plugin, 1L, 1L);
    }

    private void applyPoisonTick(RPGPlayer target) {
        int newHealth = target.getCurrentHealth() - 3; // dano por tick
        if (newHealth < 0) newHealth = 0;

        target.setCurrentHealth(newHealth);

        target.getPlayer().playSound(
                target.getPlayer().getLocation(),
                Sound.ENTITY_SILVERFISH_HURT,
                0.7f, 0.6f
        );
    }

    private void spawnPoisonParticles(Player p) {
        p.getWorld().spawnParticle(
                Particle.ENTITY_EFFECT,
                p.getLocation().add(0, 1, 0),
                8,
                0.3, 0.6, 0.3,
                0,
                new Particle.DustOptions(
                        org.bukkit.Color.fromRGB(0, 255, 0), 1.0f
                )
        );
    }

    @Override
    public int getDamage() {
        return damage;
    }
}
