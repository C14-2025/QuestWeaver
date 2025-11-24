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

    private static final long MAX_TIME_BETWEEN_KILLS = 5000; // 5 segundos

    // Rastreia o tempo do último kill por jogador
    private final Map<UUID, Long> lastKillTime = new HashMap<>();
    private final Map<UUID, Integer> killStreak = new HashMap<>();

    public DeadlySpeedQuest(Location spawnLocation) {
        super("deadly_speed_quest",
                "Velocidade Mortal",
                "Mate 6 esqueletos em sequência rápida (máx. 5s entre kills)",
                200,
                "SKELETON",
                6,
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

            UUID playerId = player.getUniqueId();
            long currentTime = System.currentTimeMillis();

            // Verifica se é um kill válido ANTES de processar o streak
            if (!mobType.equalsIgnoreCase(targetMob) || !isValidWeapon(weapon)) {
                // Mob ou arma errada - quebra o streak
                resetPlayerStreak(playerId, "Mob ou arma inválida");
                return;
            }

            // Verifica se ainda está no streak
            if (lastKillTime.containsKey(playerId)) {
                long timeSinceLastKill = currentTime - lastKillTime.get(playerId);

                if (timeSinceLastKill > MAX_TIME_BETWEEN_KILLS) {
                    // Perdeu o streak por timeout!
                    resetPlayerStreak(playerId, "Muito lento! Streak perdido.");
                    return;
                }
            }

            // Kill válido - adiciona ao streak
            int streak = killStreak.getOrDefault(playerId, 0) + 1;
            killStreak.put(playerId, streak);
            lastKillTime.put(playerId, currentTime);

            // Para testes, assumimos um jogador por quest
            currentCount = streak;

            // Feedback de streak
            if (streak >= 3) {
                player.sendMessage(String.format("§e⚡ STREAK x%d! §7Mantenha a velocidade!", streak));
            }

            if (checkCompletion()) {
                player.sendMessage("§6✦ §e§lSTREAK PERFEITO!");
                // **CORREÇÃO: Não resetamos os dados na completion para testes**
                QuestCompletedEvent customEvent = new QuestCompletedEvent(player, this);
                Bukkit.getServer().getPluginManager().callEvent(customEvent);
            }
        }
    }

    /**
     * Reseta o streak do jogador com mensagem
     */
    private void resetPlayerStreak(UUID playerId, String reason) {
        int lostStreak = killStreak.getOrDefault(playerId, 0);

        // Só mostra mensagem se tinha algum streak
        if (lostStreak > 0) {
            Player player = Bukkit.getPlayer(playerId);
            if (player != null) {
                player.sendMessage("§c✗ " + reason + " §7(Era " + lostStreak + "/" + targetCount + ")");
            }
        }

        // Reseta ambos streak e currentCount
        killStreak.put(playerId, 0);
        lastKillTime.put(playerId, System.currentTimeMillis());
        currentCount = 0;
    }

    public void onKillTimeout(Player player) {
        UUID playerId = player.getUniqueId();
        resetPlayerStreak(playerId, "Streak perdido por timeout!");
    }

    @Override
    public String getProgressText() {
        return String.format("%d/%d kills em sequência", currentCount, targetCount);
    }

    public int getStreak(Player player) {
        return killStreak.getOrDefault(player.getUniqueId(), 0);
    }
}