package br.dev.projetoc14.player.abilities.cooldown;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CooldownListener implements Listener {

    private final CooldownManager cooldownManager;

    // Mapa adicional para rastreamento rápido de cooldowns por habilidade
    private final Map<UUID, Map<String, Long>> abilityCooldowns = new HashMap<>();

    public CooldownListener(CooldownManager cooldownManager) {
        this.cooldownManager = cooldownManager;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // Remove o cooldown quando o jogador desconecta
        cooldownManager.cleanup(event.getPlayer().getUniqueId());
        abilityCooldowns.remove(event.getPlayer().getUniqueId());
    }

    /**
        Define um cooldown para uma habilidade específica
     */
    public void setCooldown(Player player, String abilityName, int seconds) {
        UUID playerId = player.getUniqueId();
        long expirationTime = System.currentTimeMillis() + (seconds * 1000L);

        // Registrar no mapa interno
        abilityCooldowns.computeIfAbsent(playerId, k -> new HashMap<>())
                .put(abilityName, expirationTime);

        // Start do Cooldown
        cooldownManager.startCooldown(player, abilityName, seconds);
    }

    /**
        Verifica se o jogador está em cooldown para uma habilidade
     */
    public boolean isOnCooldown(Player player, String abilityName) {
        UUID playerId = player.getUniqueId();

        if (!abilityCooldowns.containsKey(playerId)) return false;

        Map<String, Long> playerCooldowns = abilityCooldowns.get(playerId);
        if (!playerCooldowns.containsKey(abilityName)) return false;

        long expirationTime = playerCooldowns.get(abilityName);

        if (System.currentTimeMillis() >= expirationTime) {
            playerCooldowns.remove(abilityName);
            return false;
        }

        return true;
    }

    /**
        Obtém o tempo restante de cooldown em segundos
     */
    public long getRemainingCooldown(Player player, String abilityName) {
        UUID playerId = player.getUniqueId();

        if (!abilityCooldowns.containsKey(playerId)) return 0;

        Map<String, Long> playerCooldowns = abilityCooldowns.get(playerId);
        if (!playerCooldowns.containsKey(abilityName)) return 0;

        long expirationTime = playerCooldowns.get(abilityName);
        long remaining = (expirationTime - System.currentTimeMillis()) / 1000;

        return Math.max(0, remaining);
    }

    /**
     * Remove o cooldown de uma habilidade específica
     */
    public void removeCooldown(Player player, String abilityName) {
        UUID playerId = player.getUniqueId();

        if (abilityCooldowns.containsKey(playerId)) {
            abilityCooldowns.get(playerId).remove(abilityName);
        }
    }
}