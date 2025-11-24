package br.dev.projetoc14.player.abilities.mageSkills;

import br.dev.projetoc14.QuestWeaver;
import br.dev.projetoc14.player.RPGPlayer;
import br.dev.projetoc14.player.abilities.Ability;
import org.bukkit.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
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
        this.frostKey = new NamespacedKey(plugin, "frost_ray");
    }

    @Override
    protected void onCast(RPGPlayer caster) {
        Player player = caster.getPlayer();

        Location eyeLoc = player.getEyeLocation();
        Vector direction = eyeLoc.getDirection();

        // Sons iniciais
        player.getWorld().playSound(eyeLoc, Sound.BLOCK_GLASS_BREAK, 1.2f, 0.8f);
        player.getWorld().playSound(eyeLoc, Sound.ENTITY_PLAYER_HURT_FREEZE, 1.0f, 1.2f);

        RayTraceResult result = caster.getWorld().rayTrace(
                eyeLoc,
                direction,
                20.0,
                FluidCollisionMode.NEVER,
                true,
                0.2,
                entity -> entity instanceof LivingEntity && entity != player
        );

        LivingEntity target = null;
        Location endLoc;

        if (result != null && result.getHitEntity() instanceof LivingEntity ent) {
            target = ent;
            endLoc = ent.getEyeLocation();
        } else {
            endLoc = eyeLoc.clone().add(direction.multiply(20));
        }

        createFrostBeam(eyeLoc, endLoc, target);

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
                    if (target != null) {
                        createImpactEffect(target.getLocation());
                    } else {
                        createImpactEffect(end);
                    }
                    cancel();
                    return;
                }

                Location current = start.clone().add(direction.clone().multiply(traveled));

                Particle.DustOptions frostBlue = new Particle.DustOptions(Color.fromRGB(173, 216, 230), 1.2f);
                Particle.DustOptions iceWhite = new Particle.DustOptions(Color.fromRGB(240, 248, 255), 1.5f);

                current.getWorld().spawnParticle(Particle.DUST, current, 3, 0.1, 0.1, 0.1, 0, frostBlue);
                current.getWorld().spawnParticle(Particle.DUST, current, 2, 0.12, 0.12, 0.12, 0, iceWhite);
                current.getWorld().spawnParticle(Particle.SNOWFLAKE, current, 4, 0.1, 0.1, 0.1, 0);

                if (ticks % 5 == 0) {
                    current.getWorld().playSound(current, Sound.BLOCK_GLASS_PLACE, 0.3f, 1.8f);
                }

                traveled += 0.7;
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }

    private void createImpactEffect(Location loc) {
        loc.getWorld().playSound(loc, Sound.BLOCK_GLASS_BREAK, 1.5f, 0.5f);
        loc.getWorld().playSound(loc, Sound.ENTITY_PLAYER_HURT_FREEZE, 1.2f, 0.8f);

        Particle.DustOptions frostBlue = new Particle.DustOptions(Color.fromRGB(173, 216, 230), 1.5f);
        loc.getWorld().spawnParticle(Particle.SNOWFLAKE, loc, 50, 0.5, 0.5, 0.5, 0.1);
        loc.getWorld().spawnParticle(Particle.DUST, loc, 30, 0.4, 0.4, 0.4, 0, frostBlue);
    }

    public void applyDamage(LivingEntity target, double damage, Player caster) {
        if (target != caster) {
            target.damage(damage, caster);
        }
    }

    private void applyFrostEffect(RPGPlayer caster, LivingEntity target) {
        // === CORREÇÃO: Aplica a TAG ANTES do dano ===
        // Isso garante que se o mob morrer no hit, ele já morre "congelado"
        UUID targetId = target.getUniqueId();
        frozenEntities.put(targetId, System.currentTimeMillis());

        target.getPersistentDataContainer().set(
                frostKey, PersistentDataType.INTEGER, 1
        );

        // Agora aplica o dano
        applyDamage(target, damage, caster.getPlayer());

        // Efeitos adicionais
        target.addPotionEffect(new PotionEffect(
                PotionEffectType.SLOWNESS,
                60,
                1,
                false,
                true,
                true
        ));

        createFreezeEffect(target);

        // Remove a marca após 3 segundos
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            // Só remove se ainda estiver no mapa (não removemos se ele morreu, pois a tag precisa persistir pro evento de morte)
            if (target.isValid()) {
                frozenEntities.remove(targetId);
                target.getPersistentDataContainer().remove(frostKey);
            }
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
                Particle.DustOptions frostBlue = new Particle.DustOptions(Color.fromRGB(173, 216, 230), 1.0f);
                loc.getWorld().spawnParticle(Particle.SNOWFLAKE, loc, 3, 0.3, 0.5, 0.3, 0.02);
                loc.getWorld().spawnParticle(Particle.DUST, loc, 2, 0.3, 0.5, 0.3, 0, frostBlue);
                ticks++;
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }

    @EventHandler
    public void onFrozenDamage(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof LivingEntity target)) return;
        if (!target.getPersistentDataContainer().has(frostKey, PersistentDataType.INTEGER)) return;

        Location loc = target.getLocation().add(0, 1, 0);
        Particle.DustOptions iceWhite = new Particle.DustOptions(Color.fromRGB(240, 248, 255), 1.2f);
        loc.getWorld().spawnParticle(Particle.SNOWFLAKE, loc, 10, 0.3, 0.5, 0.3, 0.05);
        loc.getWorld().spawnParticle(Particle.DUST, loc, 5, 0.2, 0.4, 0.2, 0, iceWhite);
        loc.getWorld().playSound(loc, Sound.BLOCK_GLASS_BREAK, 0.5f, 1.5f);
    }
}