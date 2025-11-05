package br.dev.projetoc14.player.abilities.warriorSkills;

import br.dev.projetoc14.QuestWeaver;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class BleedData {
    private static final int MAX_STACKS = 5;
    private static final int STACK_DURATION = 100;
    private static final double DAMAGE_PER_STACK = 2.0;

    private final Player player; // Jogador
    private final LivingEntity target; // Alvo
    private final BossBar bossBar;
    private int stacks = 0; // Qntd hits
    private BukkitRunnable decayTask;
    private final Map<UUID, Map<UUID, BleedData>> playerBleedData; // Dados do Sangramento

    public BleedData(Player player, LivingEntity target, Map<UUID, Map<UUID, BleedData>> playerBleedData) {
        this.player = player;
        this.target = target;
        this.playerBleedData = playerBleedData;

        // Evitar deprecated
        String targetName = target.customName() != null
                ? PlainTextComponentSerializer.plainText().serialize(Objects.requireNonNull(target.customName()))
                : target.getType().name();

        this.bossBar = Bukkit.createBossBar(
                "§c⚔ Sangramento: " + targetName + " §c[0/" + MAX_STACKS + "]",
                BarColor.RED,
                BarStyle.SEGMENTED_10
        );

        bossBar.addPlayer(player);
        bossBar.setProgress(0.0);
        bossBar.setVisible(true);
    }

    public void addStack() {
        if (stacks < MAX_STACKS) {
            stacks++;
            updateBar();

            target.damage(DAMAGE_PER_STACK, player);

            target.getWorld().spawnParticle(
                    Particle.CRIMSON_SPORE,
                    target.getLocation().add(0, 1, 0),
                    10,
                    0.3, 0.3, 0.3,
                    new org.bukkit.Particle.DustOptions(org.bukkit.Color.fromRGB(139, 0, 0), 1.0f)
            );

            player.sendMessage("§c[Sangramento] §7Stack: §c" + stacks + "/" + MAX_STACKS);
        }

        resetDecayTimer();
    }

    private void updateBar() {
        double progress = (double) stacks / MAX_STACKS;
        bossBar.setProgress(Math.min(progress, 1.0));

        String targetName = target.customName() != null
                ? PlainTextComponentSerializer.plainText().serialize(Objects.requireNonNull(target.customName()))
                : target.getType().name();

        bossBar.setTitle("§c⚔ Sangramento: " + targetName + " §c[" + stacks + "/" + MAX_STACKS + "]");

        if (stacks >= MAX_STACKS - 1) {
            bossBar.setColor(BarColor.PURPLE);
        } else if (stacks == 3) {
            bossBar.setColor(BarColor.RED);
        } else {
            bossBar.setColor(BarColor.PINK);
        }
    }

    private void resetDecayTimer() {
        if (decayTask != null) {
            decayTask.cancel();
        }

        decayTask = new BukkitRunnable() {
            @Override
            public void run() {
                decayStack();
            }
        };
        decayTask.runTaskLater(QuestWeaver.getInstance(), STACK_DURATION);
    }

    private void decayStack() {
        if (stacks > 0) {
            stacks--;
            updateBar();

            if (stacks == 0) {
                remove();
                playerBleedData.get(player.getUniqueId()).remove(target.getUniqueId());
                player.sendMessage("§c[Sangramento] §7O sangramento se dissipou...");
            } else {
                resetDecayTimer();
            }
        }
    }

    public int getStacks() {
        return stacks;
    }

    public void remove() {
        if (decayTask != null) {
            decayTask.cancel();
        }
        bossBar.removeAll();
        bossBar.setVisible(false);
    }
}
