package br.dev.projetoc14.player.abilities.mageSkills;

import br.dev.projetoc14.QuestWeaver;
import br.dev.projetoc14.player.RPGPlayer;
import br.dev.projetoc14.player.abilities.Ability;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FrostRay extends Ability implements Listener {

    private final int damage = 10;
    private final QuestWeaver plugin;
    private final NamespacedKey frostKey;
    private final Map<UUID, Long> frozenEntities = new HashMap<>();

    public FrostRay(QuestWeaver plugin) {
        super("Raio Gélido", 8, 2);
        this.plugin = plugin;
        this.frostKey = new NamespacedKey("questweaver", "frost_ray");
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @Override
    protected void onCast(RPGPlayer caster) {
        Location eyeLoc = caster.getEyeLocation();
        Vector direction = eyeLoc.getDirection();

        // Som inicial de gelo se formando
        caster.getWorld().playSound(eyeLoc, Sound.BLOCK_GLASS_BREAK, 1.2f, 0.8f);
        caster.getWorld().playSound(eyeLoc, Sound.ENTITY_PLAYER_HURT_FREEZE, 1.0f, 1.2f);

        // RayTrace para detectar o alvo (alcance de 20 blocos)
        RayTraceResult result = caster.getWorld().rayTraceEntities(
                eyeLoc,
                direction,
                20.0,
                0.5,
                entity -> entity instanceof LivingEntity && entity != caster
        );

        LivingEntity target = null;
        Location endLocation;

        if (result != null && result.getHitEntity() != null) {
            target = (LivingEntity) result.getHitEntity();
            endLocation = target.getEyeLocation();
        } else {
            endLocation = eyeLoc.clone().add(direction.multiply(20));
        }

        // Efeito visual do raio de gelo
        createFrostBeam(eyeLoc, endLocation, target);

        // Se acertou um alvo, aplica dano e efeitos
        if (target != null) {
            applyFrostEffect(caster, target);
        }
    }

    private void createFrostBeam(Location start, Location end, LivingEntity target) {
        Vector direction = end.toVector().subtract(start.toVector()).normalize();
        double distance = start.distance(end);

        new BukkitRunnable() {
            double traveled = 0;
            int ticks = 0;

            @Override
            public void run() {
                if (traveled >= distance || ticks > 40) {
                    // Explosão final de gelo
                    if (target != null) {
                        createImpactEffect(target.getLocation());
                    } else {
                        createImpactEffect(end);
                    }
                    cancel();
                    return;
                }

                Location current = start.clone().add(direction.clone().multiply(traveled));

                // Partículas principais do raio (cristais de gelo)
                Particle.DustOptions frostBlue = new Particle.DustOptions(Color.fromRGB(173, 216, 230), 1.2f);
                Particle.DustOptions iceWhite = new Particle.DustOptions(Color.fromRGB(240, 248, 255), 1.5f);

                current.getWorld().spawnParticle(Particle.DUST, current, 3, 0.1, 0.1, 0.1, 0, frostBlue);
                current.getWorld().spawnParticle(Particle.DUST, current, 2, 0.15, 0.15, 0.15, 0, iceWhite);
                current.getWorld().spawnParticle(Particle.SNOWFLAKE, current, 4, 0.15, 0.15, 0.15, 0);
                current.getWorld().spawnParticle(Particle.CLOUD, current, 1, 0.05, 0.05, 0.05, 0.01);

                // Efeito de cristais girando ao redor do raio
                double radius = 0.3;
                for (int i = 0; i < 3; i++) {
                    double angle = (ticks * 0.5 + i * 120) * Math.PI / 180;
                    double x = radius * Math.cos(angle);
                    double z = radius * Math.sin(angle);

                    Vector offset = new Vector(x, 0, z);
                    Location spiralLoc = current.clone().add(offset);
                    spiralLoc.getWorld().spawnParticle(Particle.DUST, spiralLoc, 1, 0, 0, 0, 0, iceWhite);
                }

                // Som contínuo de gelo
                if (ticks % 5 == 0) {
                    current.getWorld().playSound(current, Sound.BLOCK_GLASS_PLACE, 0.3f, 1.8f);
                }

                traveled += 1.0;
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }

    private void createImpactEffect(Location loc) {
        // Som de impacto gélido
        loc.getWorld().playSound(loc, Sound.BLOCK_GLASS_BREAK, 1.5f, 0.5f);
        loc.getWorld().playSound(loc, Sound.ENTITY_PLAYER_HURT_FREEZE, 1.2f, 0.8f);
        loc.getWorld().playSound(loc, Sound.BLOCK_AMETHYST_BLOCK_BREAK, 1.0f, 1.5f);

        // Explosão de partículas de gelo
        Particle.DustOptions frostBlue = new Particle.DustOptions(Color.fromRGB(173, 216, 230), 1.5f);
        Particle.DustOptions iceWhite = new Particle.DustOptions(Color.fromRGB(240, 248, 255), 2.0f);

        loc.getWorld().spawnParticle(Particle.SNOWFLAKE, loc, 50, 0.5, 0.5, 0.5, 0.1);
        loc.getWorld().spawnParticle(Particle.DUST, loc, 30, 0.4, 0.4, 0.4, 0, frostBlue);
        loc.getWorld().spawnParticle(Particle.DUST, loc, 20, 0.3, 0.3, 0.3, 0, iceWhite);
        loc.getWorld().spawnParticle(Particle.CLOUD, loc, 15, 0.3, 0.3, 0.3, 0.05);
        loc.getWorld().spawnParticle(Particle.FIREWORK, loc, 10, 0.3, 0.3, 0.3, 0.1);

        // Anel de cristais de gelo expandindo
        new BukkitRunnable() {
            double radius = 0.5;
            int tick = 0;

            @Override
            public void run() {
                if (tick > 10) {
                    cancel();
                    return;
                }

                for (int i = 0; i < 16; i++) {
                    double angle = i * 22.5 * Math.PI / 180;
                    double x = radius * Math.cos(angle);
                    double z = radius * Math.sin(angle);

                    Location ringLoc = loc.clone().add(x, 0.1, z);
                    ringLoc.getWorld().spawnParticle(Particle.DUST, ringLoc, 1, 0, 0, 0, 0, iceWhite);
                    ringLoc.getWorld().spawnParticle(Particle.SNOWFLAKE, ringLoc, 1, 0, 0, 0, 0);
                }

                radius += 0.2;
                tick++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }

    public void applyDamage(LivingEntity entity, double damage) {
        entity.damage(damage);
    }

    private void applyFrostEffect(RPGPlayer caster, LivingEntity target) {
        applyDamage(target, damage);

        // Marca a entidade como congelada
        UUID targetId = target.getUniqueId();
        frozenEntities.put(targetId, System.currentTimeMillis());

        target.getPersistentDataContainer().set(
                frostKey,
                PersistentDataType.BOOLEAN,
                true
        );

        // Aplica efeito de lentidão (Slowness II por 3 segundos)
        target.addPotionEffect(new PotionEffect(
                PotionEffectType.SLOWNESS,
                60, // 3 segundos (60 ticks)
                1,  // Nível II
                false,
                true,
                true
        ));

        // Efeito visual de congelamento no alvo
        createFreezeEffect(target);

        // Remove a marca após 3 segundos
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            frozenEntities.remove(targetId);
            target.getPersistentDataContainer().remove(frostKey);
        }, 60L);
    }

    private void createFreezeEffect(LivingEntity target) {
        new BukkitRunnable() {
            int ticks = 0;

            @Override
            public void run() {
                if (ticks >= 60 || target.isDead() || !target.isValid()) {
                    cancel();
                    return;
                }

                Location loc = target.getLocation().add(0, 1, 0);

                // Partículas de gelo ao redor do alvo
                Particle.DustOptions frostBlue = new Particle.DustOptions(Color.fromRGB(173, 216, 230), 1.0f);
                loc.getWorld().spawnParticle(Particle.SNOWFLAKE, loc, 3, 0.3, 0.5, 0.3, 0.02);
                loc.getWorld().spawnParticle(Particle.DUST, loc, 2, 0.3, 0.5, 0.3, 0, frostBlue);

                // Cristais de gelo subindo
                if (ticks % 5 == 0) {
                    loc.getWorld().spawnParticle(Particle.CLOUD, loc, 2, 0.2, 0.3, 0.2, 0.01);
                }

                // Som de gelo ocasional
                if (ticks % 20 == 0) {
                    loc.getWorld().playSound(loc, Sound.BLOCK_GLASS_PLACE, 0.3f, 1.5f);
                }

                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }

    @EventHandler
    public void onFrozenEntityDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof LivingEntity)) return;

        LivingEntity target = (LivingEntity) event.getEntity();

        // Se a entidade está congelada, adiciona partículas extras ao receber dano
        if (target.getPersistentDataContainer().has(frostKey, PersistentDataType.BOOLEAN)) {
            Location loc = target.getLocation().add(0, 1, 0);
            Particle.DustOptions iceWhite = new Particle.DustOptions(Color.fromRGB(240, 248, 255), 1.2f);
            loc.getWorld().spawnParticle(Particle.SNOWFLAKE, loc, 10, 0.3, 0.5, 0.3, 0.05);
            loc.getWorld().spawnParticle(Particle.DUST, loc, 5, 0.2, 0.4, 0.2, 0, iceWhite);
            loc.getWorld().playSound(loc, Sound.BLOCK_GLASS_BREAK, 0.5f, 1.5f);
        }
    }

    public String getName() {
        return "Raio Gélido";
    }

    public int getManaCost() {
        return 8;
    }

    public int getCooldown() {
        return 2;
    }

    public int getDamage() {
        return damage;
    }

    public boolean isEntityFrozen(UUID entityId) {
        return frozenEntities.containsKey(entityId);
    }
}