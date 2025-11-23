package br.dev.projetoc14.player.abilities.cooldown;

import br.dev.projetoc14.QuestWeaver;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CooldownManager {

    private final Map<UUID, CooldownData> cooldowns = new HashMap<>();
    private final QuestWeaver plugin;

    public CooldownManager(QuestWeaver plugin) {
        this.plugin = plugin;
    }

    public void startCooldown(Player player, String abilityName, int seconds) {
        UUID uuid = player.getUniqueId();

        if (cooldowns.containsKey(uuid)) cooldowns.get(uuid).cancel(); // Remover resquício de cooldown

        BossBar cooldownBar = Bukkit.createBossBar(
                "§e⏱ " + abilityName + " §7- §c" + seconds + "s",
                BarColor.RED,
                BarStyle.SOLID
        );

        cooldownBar.addPlayer(player);
        cooldownBar.setProgress(1.0);

        // Criação do Cooldown:
        CooldownData data = new CooldownData(cooldownBar, seconds);
        cooldowns.put(uuid, data);

        // Atualização:
        new BukkitRunnable() {
            int remainingCooldown = seconds;

            @Override
            public void run() {
                if (!player.isOnline()) {
                    cleanup(uuid);
                    cancel();
                    return;
                }

                remainingCooldown--;

                if (remainingCooldown <= 0) {
                    // Fim
                    cooldownBar.setTitle("§a✔ " + abilityName + " §7- §aPronto!");
                    cooldownBar.setColor(BarColor.GREEN);
                    cooldownBar.setProgress(1.0);

                    Bukkit.getScheduler().runTaskLater(plugin, () -> cleanup(uuid), 20L); // 20 ticks

                    cancel();
                    return;
                }

                double progress = remainingCooldown * 1.0 / seconds;
                cooldownBar.setProgress(progress);
                cooldownBar.setTitle("§e⏱ " + abilityName + " §7- §c" + remainingCooldown + "s");

                // Cores como em semáforo:
                if (remainingCooldown <= 3) cooldownBar.setColor(BarColor.GREEN);
                else if (remainingCooldown <= 5) cooldownBar.setColor(BarColor.YELLOW);
                else cooldownBar.setColor(BarColor.RED);
            }
        }.runTaskTimer(plugin, 20L, 20L);
    }

    public boolean hasCooldown(Player player) {
        return cooldowns.containsKey(player.getUniqueId());
    }

    public int getRemainingTime(Player player) {
        CooldownData data = cooldowns.get(player.getUniqueId());
        return data != null ? data.remainingCooldown : 0;
    }

    public void cleanup(UUID uuid) {
        CooldownData data = cooldowns.remove(uuid);
        if (data != null) {
            data.cancel();
        }
    }

    public void cleanupAll() {
        for (CooldownData data : cooldowns.values()) {
            data.cancel();
        }
        cooldowns.clear();
    }
}