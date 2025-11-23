package br.dev.projetoc14.player;

import br.dev.projetoc14.QuestWeaver;
import br.dev.projetoc14.items.players.SearchCompass;
import br.dev.projetoc14.match.PlayerFileManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import static br.dev.projetoc14.QuestWeaver.getServerName;
import static br.dev.projetoc14.QuestWeaver.isMatchRunning;

public class PlayerDeathListener implements Listener {
    private final PlayerFileManager fileManager;
    private QuestWeaver questWeaver;

    public PlayerDeathListener(PlayerFileManager fileManager, QuestWeaver questWeaver) {
        this.fileManager = fileManager;
        this.questWeaver = questWeaver;
    }

    @EventHandler
    public void onPlayerDeath(PlayerRespawnEvent e) {
        Player p = e.getPlayer();
        World world = Bukkit.getWorld("world");
        Location spawn = world.getSpawnLocation();

        if(isMatchRunning()){
            Bukkit.broadcastMessage(getServerName() + " §f" + p.getName() + " §emorreu e agora está espectando a partida!");
            p.teleport(spawn);
            p.setGameMode(GameMode.CREATIVE);
            p.setInvulnerable(true);
            p.getInventory().clear();
            p.getInventory().setItem(0, SearchCompass.create());
            fileManager.setPlayerSpec(p, true);

            for (Player online : Bukkit.getOnlinePlayers()) {
                if (online.equals(p)) continue;
                online.hidePlayer(questWeaver, p);
            }
        }
    }

    @EventHandler
    public void onSpectatorHit(EntityDamageByEntityEvent event) {
        Player p = (Player) event.getEntity();
        if(!fileManager.isPlayerSpec(p)) return;

        if (event.getDamager() instanceof Player attacker) {
            event.setCancelled(true);
            attacker.sendMessage("§cVocê está no modo espectador e não pode causar dano!");
        }

    }
}
