package br.dev.projetoc14.player.listeners;

import br.dev.projetoc14.player.PlayerStats;
import br.dev.projetoc14.player.PlayerStatsManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerListener implements Listener {
    private final PlayerStatsManager statsManager;
    private final JavaPlugin plugin;

    public PlayerListener(PlayerStatsManager statsManager, JavaPlugin plugin) {
        this.statsManager = statsManager;
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR) // Executa por último
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // SÓ aplica stats se o jogador já tiver (já escolheu classe)
        if (!statsManager.hasStats(player)) {
            plugin.getLogger().info("Player " + player.getName() + " é novo, aguardando seleção de classe...");
            return; // NÃO FAZ NADA - deixa o ClassSelectListener cuidar
        }

        plugin.getLogger().info("Player " + player.getName() + " entrou!");

        statsManager.applyStats(player);
        plugin.getLogger().info("Stats aplicados!");

        statsManager.createManaBar(player);
        plugin.getLogger().info("Barra de mana criada!");

        startManaRegen(player);
        plugin.getLogger().info("Regeneração iniciada!");
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        // Remove a barra de mana
        statsManager.removeManaBar(player);
    }

    private void startManaRegen(Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isOnline()) {
                    cancel();
                    return;
                }

                // Verifica se ainda tem stats (jogador pode ter resetado)
                if (!statsManager.hasStats(player)) {
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
        }.runTaskTimer(plugin, 20L, 20L);
    }
}