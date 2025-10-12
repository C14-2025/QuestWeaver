package br.dev.projetoc14.playerData;

import br.dev.projetoc14.player.PlayerStats;
import br.dev.projetoc14.player.PlayerStatsManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
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

    @EventHandler(priority = EventPriority.LOWEST) // Executa primeiro
    public void onJoin(PlayerJoinEvent event) {
        var player = event.getPlayer();

        // SÓ carrega se o arquivo existir
        if (dataManager.hasPlayerData(player)) {
            PlayerStats loadedStats = dataManager.loadPlayerStats(player);
            statsManager.setStats(player, loadedStats);
            plugin.getLogger().info("[PlayerDataListener] Stats carregados e aplicados para " + player.getName());
        } else {
            plugin.getLogger().info("[PlayerDataListener] Novo jogador: " + player.getName());
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        var player = event.getPlayer();

        // SÓ salva se o jogador tiver stats
        if (statsManager.hasStats(player)) {
            PlayerStats stats = statsManager.getStats(player);
            dataManager.savePlayerStats(player, stats);
            plugin.getLogger().info("[PlayerDataListener] Stats salvos para " + player.getName());
        }
    }
}