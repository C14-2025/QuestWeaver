package br.dev.projetoc14.player;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

// Esta classe vai "ouvir" eventos relacionados a players
public class PlayerListener implements Listener {
    private final PlayerStatsManager statsManager;
    private final JavaPlugin plugin;

    public PlayerListener(PlayerStatsManager statsManager, JavaPlugin plugin) {
        this.statsManager = statsManager;
        this.plugin = plugin;
    }

    // Player ao entrar no servidor:
    @EventHandler // Esta anotação marca um métod0 como "ouvinte de evento"
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        plugin.getLogger().info("Player " + player.getName() + " entrou!");

        statsManager.applyStats(player);
        plugin.getLogger().info("Stats aplicados!");

        statsManager.createManaBar(player);
        plugin.getLogger().info("Barra de mana criada!");

        startManaRegen(player);
        plugin.getLogger().info("Regeneração iniciada!");
    }

    // Quando o Player sai:
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        // Remove a barra de mana
        statsManager.removeManaBar(player);
    }

    // Regeneração de Mana:
    private void startManaRegen(Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isOnline()) {
                    cancel();
                    return;
                }


                // Recupera stats do player
                PlayerStats stats = statsManager.getStats(player);

                // Se já está no máximo, não faz nada
                if (stats.getCurrentMana() >= stats.getMana()) return;

                // Regen de mana: +1/s
                stats.restoreMana(1);

                // Atualização da barra de mana:
                statsManager.updateManaBar(player);
            }
        }.runTaskTimer(plugin, 20L,20L);
    }
}
