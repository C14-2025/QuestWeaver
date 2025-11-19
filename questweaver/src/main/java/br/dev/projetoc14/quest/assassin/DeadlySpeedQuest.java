package br.dev.projetoc14.quest.assassin;

import br.dev.projetoc14.quest.KillQuest;
import br.dev.projetoc14.quest.utils.QuestCompletedEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Quest média do assassino: Kill streak rápido
 */
public class DeadlySpeedQuest extends KillQuest {

    private static final long MAX_TIME_BETWEEN_KILLS = 6000; // 6 segundos

    // Rastreia o tempo do último kill por jogador
    private final Map<UUID, Long> lastKillTime = new HashMap<>();
    private final Map<UUID, Integer> killStreak = new HashMap<>();

    public DeadlySpeedQuest(Location spawnLocation) {
        super("deadly_speed_quest",
                "Velocidade Mortal",
                "Mate 8 esqueletos em sequência rápida (máx. 3s entre kills)",
                200,
                "SKELETON",
                2,
                0,
                spawnLocation,
                List.of(Material.IRON_SWORD, Material.DIAMOND_SWORD, Material.NETHERITE_SWORD));
    }

    @Override
    public void updateProgress(Object... params) {
        if (params.length >= 3 &&
                params[0] instanceof String mobType &&
                params[1] instanceof Material weapon &&
                params[2] instanceof Player player) {

            // Verifica se já está completa
            if (checkCompletion()) {
                return;
            }

            if (mobType.equalsIgnoreCase(targetMob) && isValidWeapon(weapon)) {
                UUID playerId = player.getUniqueId();
                long currentTime = System.currentTimeMillis();

                // Verifica se ainda está no streak
                if (lastKillTime.containsKey(playerId)) {
                    long timeSinceLastKill = currentTime - lastKillTime.get(playerId);

                    if (timeSinceLastKill > MAX_TIME_BETWEEN_KILLS) {
                        // Perdeu o streak!
                        int lostStreak = killStreak.getOrDefault(playerId, 0);
                        if (lostStreak > 0) {
                            player.sendMessage(String.format("§c✗ Muito lento! Streak perdido. (Era %d/8)", lostStreak));
                            currentCount = 0; // Reseta progresso
                        }
                        killStreak.put(playerId, 0);
                    }
                }

                // Adiciona kill ao streak
                int streak = killStreak.getOrDefault(playerId, 0) + 1;
                killStreak.put(playerId, streak);
                lastKillTime.put(playerId, currentTime);

                currentCount++;

                // Feedback de streak
                if (streak >= 3) {
                    player.sendMessage(String.format("§e⚡ STREAK x%d! §7Mantenha a velocidade!", streak));
                }

                if (checkCompletion()) {
                    player.sendMessage("§6✦ §e§lSTREAK PERFEITO!");
                    resetPlayerData(playerId);
                    QuestCompletedEvent customEvent = new QuestCompletedEvent(player, this);
                    Bukkit.getServer().getPluginManager().callEvent(customEvent);
                }
            }
        }
    }

    public void onKillTimeout(Player player) {
        UUID playerId = player.getUniqueId();
        int lostStreak = killStreak.getOrDefault(playerId, 0);

        if (lostStreak > 0) {
            player.sendMessage(String.format("§c✗ Streak perdido por timeout! (Era %d/8)", lostStreak));
            currentCount = 0;
            resetPlayerData(playerId);
        }
    }

    private void resetPlayerData(UUID playerId) {
        killStreak.remove(playerId);
        lastKillTime.remove(playerId);
    }

    @Override
    public String getProgressText() {
        return String.format("%d/%d kills em sequência", currentCount, targetCount);
    }

    public int getStreak(Player player) {
        return killStreak.getOrDefault(player.getUniqueId(), 0);
    }
}