package br.dev.projetoc14.player.abilities.assassinSkills;

import br.dev.projetoc14.player.RPGPlayer;
import br.dev.projetoc14.player.abilities.Ability;
import br.dev.projetoc14.player.abilities.CastResult;
import br.dev.projetoc14.player.classes.AssassinPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DemonProjectile extends Ability {

    // Configurações da habilidade
    private static final double DAMAGE = 12.0;
    private static final double SPEED = 1.5;
    private static final int MAX_DISTANCE = 25; // blocos
    private static final int MAX_HITS_PER_ENTITY = 2;

    public DemonProjectile() {
        super("Demon Projectile", 25, 6); // nome, mana, cooldown
    }

    @Override
    protected void onCast(RPGPlayer caster) {
        Player player = caster.getPlayer();

        // Criar e lançar a Death Sickle
        launchDeathSickle(player);

        // Feedback visual e sonoro
        player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_FLAP, 1.0f, 1.8f);
        player.playSound(player.getLocation(), Sound.ENTITY_PHANTOM_SWOOP, 0.8f, 1.2f);
        player.sendActionBar(Component.text("🌙 Death Sickle lançada!")
                .color(NamedTextColor.DARK_PURPLE));
    }

    public void execute(Player player, AssassinPlayer assassin) {
        CastResult result = canCast(assassin);
        if (result != CastResult.SUCCESS) {
            return;
        }

        cast(assassin);
    }

    private void launchDeathSickle(Player player) {
        Location startLoc = player.getEyeLocation().add(player.getEyeLocation().getDirection().multiply(0.5));
        Vector direction = player.getEyeLocation().getDirection().normalize();

        // Criar armor stand invisível para segurar a foice
        ArmorStand sickle = startLoc.getWorld().spawn(startLoc, ArmorStand.class, stand -> {
            stand.setVisible(false);
            stand.setGravity(false);
            stand.setInvulnerable(true);
            stand.setMarker(true);
            stand.setSmall(true);
            stand.setBasePlate(false);
            stand.setArms(true);

            // Colocar a foice na mão
            ItemStack sickleItem = new ItemStack(Material.NETHERITE_HOE);
            stand.getEquipment().setHelmet(sickleItem);
        });

        // Lógica de movimento e rotação da foice
        createSickleLogic(sickle, direction, player);
    }

    private void createSickleLogic(ArmorStand sickle, Vector direction, Player shooter) {
        JavaPlugin plugin = JavaPlugin.getProvidingPlugin(getClass());

        // Mapa para rastrear quantas vezes cada entidade foi atingida - máx 2
        Map<UUID, Integer> hitCount = new HashMap<>();

        new BukkitRunnable() {
            int ticks = 0;
            double distanceTraveled = 0;
            float rotation = 0;

            @Override
            public void run() {
                // Verificar se deve parar
                if (!sickle.isValid() || distanceTraveled >= MAX_DISTANCE) {
                    createDisappearEffect(sickle.getLocation());
                    sickle.remove();
                    cancel();
                    return;
                }

                Location currentLoc = sickle.getLocation();

                // Mover a foice
                Vector movement = direction.clone().multiply(SPEED);
                Location newLoc = currentLoc.add(movement);
                sickle.teleport(newLoc);

                // Rotacionar a foice - 360 graus por segundo
                rotation += 18f;
                if (rotation >= 360f) rotation -= 360f;

                double radians = Math.toRadians(rotation);
                sickle.setHeadPose(new EulerAngle(0, 0, radians));

                // Aplicar efeitos visuais
                if (ticks % 2 == 0) {
                    newLoc.getWorld().spawnParticle(Particle.ENCHANT, newLoc, 5, 0.2, 0.2, 0.2, 0.5);
                    newLoc.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, newLoc, 2, 0.1, 0.1, 0.1, 0.02);
                }

                if (ticks % 10 == 0) {
                    newLoc.getWorld().playSound(newLoc, Sound.ENTITY_PLAYER_ATTACK_SWEEP, 0.3f, 1.5f);
                }

                // Verificar colisão
                for (Entity entity : newLoc.getWorld().getNearbyEntities(newLoc, 1.2, 1.2, 1.2)) {
                    if (entity instanceof LivingEntity target && !entity.equals(shooter) && entity != sickle) {

                        // Verificar quantas vezes já atingiu essa entidade
                        UUID targetId = target.getUniqueId();
                        int hits = hitCount.getOrDefault(targetId, 0);

                        if (hits < MAX_HITS_PER_ENTITY) {
                            dealDamage(shooter, target);
                            hitCount.put(targetId, hits + 1);

                            // Efeito visual no alvo
                            target.getWorld().spawnParticle(
                                    Particle.DAMAGE_INDICATOR,
                                    target.getEyeLocation(),
                                    8,
                                    0.3, 0.3, 0.3,
                                    0.1
                            );
                            target.getWorld().playSound(
                                    target.getLocation(),
                                    Sound.ENTITY_PLAYER_HURT,
                                    0.5f,
                                    1.0f
                            );
                        }
                    }
                }

                distanceTraveled += SPEED;
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }

    private void dealDamage(Player attacker, LivingEntity target) {
        target.damage(DAMAGE, attacker);
    }

    private void createDisappearEffect(Location loc) {
        // Efeito de desaparecimento
        loc.getWorld().spawnParticle(Particle.ENCHANT, loc, 20, 0.3, 0.3, 0.3, 1.0);
        loc.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, loc, 15, 0.2, 0.2, 0.2, 0.05);
        loc.getWorld().spawnParticle(Particle.SMOKE, loc, 10, 0.2, 0.2, 0.2, 0.02);
        loc.getWorld().playSound(loc, Sound.BLOCK_ENCHANTMENT_TABLE_USE, 0.5f, 1.5f);
    }
}