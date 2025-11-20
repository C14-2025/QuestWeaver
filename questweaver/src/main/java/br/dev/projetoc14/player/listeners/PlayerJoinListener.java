package br.dev.projetoc14.player.listeners;

//import br.dev.sno0s.hgplugin.utils.PlayerJoinItems;

import br.dev.projetoc14.QuestWeaver;
import br.dev.projetoc14.items.players.SearchCompass;
import br.dev.projetoc14.match.PlayerFileManager;
import br.dev.projetoc14.player.PlayerJoinItems;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.Location;

import static br.dev.projetoc14.QuestWeaver.getServerName;
import static br.dev.projetoc14.QuestWeaver.isMatchRunning;

public class PlayerJoinListener implements Listener {
    QuestWeaver plugin;
    private final PlayerFileManager fileManager;

    public PlayerJoinListener(PlayerFileManager fileManager, QuestWeaver plugin) {
        this.fileManager = fileManager;
        this.plugin = plugin;
    }

    /*
        this method do:
        - get the world that the game will start
        - show welcome message
        - teleport the player to spawn
        - set gamemode adventure, prevent the player to break blocks
        - set player join items
        @author: sno0s
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        World world = Bukkit.getWorld("world");
        event.joinMessage(null);
        int online = Bukkit.getOnlinePlayers().size();


        if (world != null) {
            Player player = event.getPlayer();
            Location spawn = world.getSpawnLocation();
            if(!isMatchRunning() && online < 4){
                fileManager.setPlayerSpec(player, false);
                Bukkit.broadcastMessage(getServerName() + " §f" + player.getName() + " §eentrou no servidor!");

                // basic configs
                player.teleport(spawn);
                player.setGameMode(GameMode.ADVENTURE);
                player.setInvulnerable(true);
                player.getInventory().clear();
                PlayerJoinItems.give(player);

                plugin.getPlayerFileManager().createPlayerFile(player);
            }
            else{
                Bukkit.broadcastMessage(getServerName() + " §f" + player.getName() + " §eestá espectando a partida!");
                player.teleport(spawn);
                player.setGameMode(GameMode.CREATIVE);
                player.setInvulnerable(true);
                player.getInventory().clear();
                player.getInventory().setItem(0, SearchCompass.create());
                fileManager.setPlayerSpec(player, true);

            }

        }

    }
}
