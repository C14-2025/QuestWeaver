package br.dev.projetoc14.player;

import br.dev.projetoc14.match.PlayerFileManager;
import br.dev.projetoc14.playerData.PlayerDataManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerDisconnectListener implements Listener {
    private final PlayerStatsManager statsManager;
    private final PlayerDataManager dataManager;
    private final PlayerFileManager playerFileManager;

    /**
     *
     * @param e
     * Aqui vai toda a lógica de desconexão do player
     * DONE: implementar o reset de classe
     * TODO: implementar a desconexão meio à uma partida em andamento
     */
    @EventHandler
    public void onPlayerDisconnect(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        statsManager.removeStats(player);
        dataManager.deletePlayerData(player);
        playerFileManager.setPlayerClass(player, "Nenhum");
    }

    public PlayerDisconnectListener(PlayerFileManager playerFileManager, PlayerStatsManager statsManager, PlayerDataManager dataManager) {
        this.playerFileManager = playerFileManager;
        this.statsManager = statsManager;
        this.dataManager = dataManager;
    }
}
