package br.dev.projetoc14.player.abilities.assassinSkills;

import br.dev.projetoc14.QuestWeaver;
import br.dev.projetoc14.player.RPGPlayer;
import br.dev.projetoc14.player.abilities.Ability;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class VampireKnives extends Ability {

    private final int DURATION = 200;

    public VampireKnives() {
        super("Vampire Knives", 20, 30);
    }

    private final Set<UUID> activePlayers = new HashSet<>();

    @Override
    protected void onCast(RPGPlayer caster) {
        Player player = caster.getPlayer();
        UUID uuid = player.getUniqueId();

        activePlayers.add(uuid);

        player.sendActionBar(Component.text("§c[Vampire Knives] §7Roubo de vida ativado!")
                .color(NamedTextColor.RED));

        new BukkitRunnable() {
            int ticks = 0;

            @Override
            public void run() {
                if (ticks > DURATION || !player.isOnline()) {
                    activePlayers.remove(uuid);
                    if(player.isOnline()) {
                        player.sendActionBar(Component.text("§c[Vampire Knives] §7Efeito finalizado.")
                                .color(NamedTextColor.GRAY));
                    }
                    cancel();
                    return;
                }
                if (ticks % 20 == 0){
                    player.getWorld().spawnParticle(
                            Particle.CRIMSON_SPORE,
                            player.getLocation().add(0, 0.5, 0),
                            5,
                            0.3, 0.5, 0.3,
                            new Particle.DustOptions(Color.fromRGB(139,0,0),1.0f)
                    );
                }
                ticks++;
            }
        }.runTaskTimer(QuestWeaver.getInstance(), 0, 1);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event){
        if (!(event.getDamager() instanceof Player player)) return;
        if (!(event.getEntity() instanceof LivingEntity)) return;

        if (!activePlayers.contains(player.getUniqueId())) return;

        // Obtém o RPGPlayer para usar o sistema de vida customizado
        RPGPlayer rpgPlayer = ((QuestWeaver) QuestWeaver.getInstance()).getRPGPlayer(player);
        if (rpgPlayer == null) return;

        double damage = event.getFinalDamage();
        float LIFESTEAL_PERCENT = 0.25f;
        int heal = (int) Math.ceil(damage * LIFESTEAL_PERCENT);

        // Usa o método heal() que já gerencia tudo automaticamente
        rpgPlayer.heal(heal);

        // Spawna partículas de coração
        player.getWorld().spawnParticle(
                org.bukkit.Particle.HEART,
                player.getLocation().add(0, 2, 0),
                3,
                0.5, 0.3, 0.5
        );
    }
}