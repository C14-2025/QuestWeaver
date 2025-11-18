package br.dev.projetoc14.quest.archer;

import br.dev.projetoc14.quest.HitQuest;
import br.dev.projetoc14.quest.utils.QuestCompletedEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Quest difícil do arqueiro: Acertar sequência perfeita de hits sem errar
 * e com tempo limite entre cada acerto
 */
public class WindMasterQuest extends HitQuest {

    private static final int MAX_COMBO = 10; // Precisa acertar 10 seguidos
    private static final long COMBO_TIMEOUT = 5000; // 5 segundos para o próximo hit
    private static final double MIN_DISTANCE = 12.0; // Distância mínima

    // Controle de combo por jogador
    private final Map<UUID, Integer> playerCombos = new HashMap<>();
    private final Map<UUID, Long> lastHitTime = new HashMap<>();
    private final Map<UUID, Integer> missedShots = new HashMap<>();

    public WindMasterQuest(Location spawnLocation) {
        super("wind_master_quest",
                "Mestre dos Ventos",
                "Acerte 10 flechas seguidas em creepers sem errar (dist. mín. 12 blocos, 5s entre tiros)",
                350,
                "CREEPER",
                10,
                0,
                spawnLocation,
                new ArrayList<>());
    }

    @Override
    protected boolean isValidProjectile(Arrow arrow) {
        if (!(arrow.getShooter() instanceof Player shooter)) {
            return false;
        }

        UUID playerId = shooter.getUniqueId();
        long currentTime = System.currentTimeMillis();

        // Verifica se o combo expirou
        if (lastHitTime.containsKey(playerId)) {
            long timeSinceLastHit = currentTime - lastHitTime.get(playerId);
            if (timeSinceLastHit > COMBO_TIMEOUT) {
                // Combo quebrado por timeout
                resetCombo(shooter);
                shooter.sendMessage("§c✗ Combo perdido! Você demorou demais entre os tiros.");
                return false;
            }
        }

        // Verifica distância
        Location shooterLoc = shooter.getLocation();
        Location arrowLoc = arrow.getLocation();
        double distance = shooterLoc.distance(arrowLoc);

        if (distance < MIN_DISTANCE) {
            shooter.sendMessage(String.format("§c✗ Muito perto! (%.1f/%.0f blocos)",
                    distance, MIN_DISTANCE));
            return false;
        }

        // Atualiza combo
        int combo = playerCombos.getOrDefault(playerId, 0) + 1;
        playerCombos.put(playerId, combo);
        lastHitTime.put(playerId, currentTime);

        shooter.sendMessage(String.format("§e⚡ COMBO x%d §7(%.1f blocos)", combo, distance));

        return true;
    }

    @Override
    public void updateProgress(Object... params) {
        if (params.length >= 3 &&
                params[0] instanceof String mobType &&
                params[1] instanceof Material weapon &&
                params[2] instanceof Player player) {

            UUID playerId = player.getUniqueId();

            // Se tiver arrow como 4º parâmetro
            if (params.length >= 4 && params[3] instanceof Arrow arrow) {
                if (mobType.equalsIgnoreCase(targetMob) && isValidProjectile(arrow)) {
                    currentCount++;

                    if (checkCompletion()) {
                        resetCombo(player);
                        QuestCompletedEvent customEvent = new QuestCompletedEvent(player, this);
                        Bukkit.getServer().getPluginManager().callEvent(customEvent);
                        player.sendMessage("§6✦ §e§lCOMBO PERFEITO! §6Quest completada!");
                    }
                }
            }
        }
    }

    public void onMissedShot(Player player) {
        UUID playerId = player.getUniqueId();
        int missed = missedShots.getOrDefault(playerId, 0) + 1;
        missedShots.put(playerId, missed);

        int combo = playerCombos.getOrDefault(playerId, 0);
        if (combo > 0) {
            player.sendMessage(String.format("§c✗ Errou! Combo perdido. (Era %d/10)", combo));
            resetCombo(player);
        }
    }

    private void resetCombo(Player player) {
        UUID playerId = player.getUniqueId();
        int lostCombo = playerCombos.getOrDefault(playerId, 0);

        playerCombos.remove(playerId);
        lastHitTime.remove(playerId);

        // Reseta o progresso para 0 se perdeu o combo
        if (lostCombo > 0 && currentCount > 0) {
            currentCount = 0;
            player.sendMessage("§c⚠ Progresso resetado! Você precisa acertar 10 seguidos sem errar.");
        }
    }

    @Override
    public String getProgressText() {
        return String.format("%d/%d acertos em sequência perfeita",
                getCurrentCount(), getTargetCount());
    }

    public int getCombo(Player player) {
        return playerCombos.getOrDefault(player.getUniqueId(), 0);
    }
}