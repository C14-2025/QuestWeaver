package br.dev.projetoc14.player.abilities.warriorSkills;

import br.dev.projetoc14.QuestWeaver;
import br.dev.projetoc14.player.RPGPlayer;
import br.dev.projetoc14.player.abilities.Ability;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import java.util.List;

public class PowerCharge extends Ability {

    private final QuestWeaver plugin;

    public PowerCharge(QuestWeaver plugin) {
        super("Investida Poderosa", 12, 8); // nome, custo de mana, cooldown (segundos)
        this.plugin = plugin;
    }

    @Override
    protected void onCast(RPGPlayer caster) {
        Player player = caster.getPlayer();
        Location start = player.getLocation();
        Vector direction = player.getEyeLocation().getDirection().normalize();

        // Movimento rápido (dash)
        // Distância da investida
        double dashDistance = 4.0;
        Vector dashVector = direction.multiply(dashDistance * 0.5);
        player.setVelocity(dashVector);

        // Efeitos visuais e sonoros
        player.getWorld().spawnParticle(Particle.CRIT, start, 40, 0.5, 0.5, 0.5, 0.1);
        player.getWorld().spawnParticle(Particle.WHITE_SMOKE, start, 20, 0.4, 0.4, 0.4, 0.01);
        player.getWorld().playSound(start, Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 0.8f);
        player.getWorld().playSound(start, Sound.ENTITY_IRON_GOLEM_ATTACK, 0.7f, 1.2f);

        // Aplica dano após o dash
        plugin.getServer().getScheduler().runTaskLater(
                plugin,
                () -> applyAreaDamage(player),
                10L // 0.5 segundos
        );

        player.sendMessage("⚡ Você avançou com uma Investida Poderosa!");
    }

    private void applyAreaDamage(Player player) {
        Location center = player.getLocation();

        // Raio de dano
        double radius = 2.5;
        List<LivingEntity> targets = player.getWorld().getNearbyEntities(center, radius, radius, radius)
                .stream()
                .filter(e -> e instanceof LivingEntity && e != player)
                .map(e -> (LivingEntity) e)
                .toList();

        for (LivingEntity target : targets) {
            // Dano físico
            // Dano base
            double damage = 20.0;
            target.damage(damage, player);

            // Empurrão
            // Força de empurrão
            double knockback = 1.2;
            Vector knock = target.getLocation().toVector()
                    .subtract(center.toVector())
                    .normalize()
                    .multiply(knockback);
            target.setVelocity(knock);

            // Partículas de impacto
            target.getWorld().spawnParticle(Particle.EXPLOSION, target.getLocation(), 10, 0.3, 0.3, 0.3, 0.01);
            target.getWorld().playSound(target.getLocation(), Sound.ENTITY_ZOMBIE_ATTACK_IRON_DOOR, 0.8f, 1.4f);
        }

        player.getWorld().spawnParticle(Particle.SWEEP_ATTACK, center, 10, 0.4, 0.4, 0.4, 0.01);
        player.getWorld().playSound(center, Sound.ENTITY_PLAYER_ATTACK_STRONG, 1f, 1f);
    }
}
