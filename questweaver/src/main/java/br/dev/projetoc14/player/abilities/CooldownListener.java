package br.dev.projetoc14.player.abilities;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class CooldownListener implements Listener {

    private final CooldownManager cooldownManager;

    public CooldownListener(CooldownManager cooldownManager) {
        this.cooldownManager = cooldownManager;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // Remove o cooldown quando o jogador desconecta
        cooldownManager.cleanup(event.getPlayer().getUniqueId());
    }
}