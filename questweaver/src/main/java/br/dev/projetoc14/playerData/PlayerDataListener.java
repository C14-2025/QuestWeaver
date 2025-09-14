package br.dev.projetoc14.playerData;

import br.dev.projetoc14.player.PlayerStats;
import br.dev.projetoc14.player.PlayerStatsManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerDataListener implements Listener {
    private final PlayerDataManager dataManager;
    private final PlayerStatsManager statsManager;
    private final JavaPlugin plugin;

    public PlayerDataListener(JavaPlugin plugin, PlayerStatsManager statsManager) {
        this.plugin = plugin;
        this.statsManager = statsManager;
        this.dataManager = new PlayerDataManager(plugin);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        var player = event.getPlayer();

        // Carrega stats do JSON
        PlayerStats loadedStats = dataManager.loadPlayerStats(player);

        // Insere os stats carregados no PlayerStatsManager
        statsManager.setStats(player, loadedStats);

        plugin.getLogger().info("[PlayerDataListener] Stats aplicados para " + player.getName());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        var player = event.getPlayer();

        // Recupera stats do PlayerStatsManager
        PlayerStats stats = statsManager.getStats(player);
        // Salva no JSON
        dataManager.savePlayerStats(player, stats);
        plugin.getLogger().info("[PlayerDataListener] Stats salvos para " + player.getName());
    }
}
