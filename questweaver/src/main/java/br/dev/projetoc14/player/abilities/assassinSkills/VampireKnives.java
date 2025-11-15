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
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class VampireKnives extends Ability implements Listener {

    private final int DURATION = 200; // 10 segundos (200 ticks)
    private final Set<UUID> activePlayers = new HashSet<>();

    public VampireKnives() {
        super("Vampire Knives", 20, 30);
    }

    @Override
    public void onCast(RPGPlayer caster) {
        Player player = caster.getPlayer();
        UUID uuid = player.getUniqueId();

        activePlayers.add(uuid);

        player.sendActionBar(Component.text("§c[Vampire Knives] §7Roubo de vida ativado!")
                .color(NamedTextColor.RED));

        new BukkitRunnable() {
            int ticks = 0;

            @Override
            public void run() {
                if (ticks >= DURATION || !player.isOnline()) {
                    activePlayers.remove(uuid);
                    if (player.isOnline()) {
                        player.sendActionBar(Component.text("§c[Vampire Knives] §7Efeito finalizado.")
                                .color(NamedTextColor.GRAY));
                    }
                    cancel();
                    return;
                }

                // Partículas a cada segundo (20 ticks)
                if (ticks % 20 == 0) {
                    player.getWorld().spawnParticle(
                            Particle.DUST,
                            player.getLocation().add(0, 0.5, 0),
                            5,
                            0.3, 0.5, 0.3,
                            0.01,
                            new Particle.DustOptions(Color.fromRGB(139, 0, 0), 1.0f)
                    );
                }
                ticks++;
            }
        }.runTaskTimer(QuestWeaver.getInstance(), 0, 1);
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        // Verifica se o atacante é um jogador
        if (!(event.getDamager() instanceof Player player)) return;

        // Verifica se o alvo é uma entidade viva
        if (!(event.getEntity() instanceof LivingEntity target)) return;

        // Verifica se o jogador tem o buff ativo
        if (!activePlayers.contains(player.getUniqueId())) return;

        // Obtém o RPGPlayer do atacante
        RPGPlayer caster = ((QuestWeaver) QuestWeaver.getInstance()).getRPGPlayer(player);
        if (caster == null) return;

        // Calcula o lifesteal baseado no dano causado
        double damage = event.getFinalDamage();
        // 25% de roubo de vida
        float LIFESTEAL_PERCENT = 0.25f;
        int healAmount = (int) Math.ceil(damage * LIFESTEAL_PERCENT);
        caster.heal(healAmount);

        // Efeitos visuais
        player.getWorld().spawnParticle(
                Particle.HEART,
                player.getLocation().add(0, 2, 0),
                3,
                0.5, 0.3, 0.5
        );

        // Partículas no alvo
        target.getWorld().spawnParticle(
                Particle.DUST,
                target.getLocation().add(0, 1, 0),
                10,
                0.3, 0.5, 0.3,
                0.01,
                new Particle.DustOptions(Color.fromRGB(139, 0, 0), 1.0f)
        );

        // Feedback visual
        player.sendMessage("§c❤ +§l" + healAmount + " HP §7(Vampirismo)");
    }
}