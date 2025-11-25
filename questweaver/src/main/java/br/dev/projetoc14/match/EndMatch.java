package br.dev.projetoc14.match;

import br.dev.projetoc14.QuestWeaver;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import static br.dev.projetoc14.QuestWeaver.isMatchRunning;

public class EndMatch implements Listener {

    private final QuestWeaver plugin;
    private MatchManager matchManager;
    private File pasta = new File("worlds");

    public EndMatch(QuestWeaver plugin, MatchManager matchManager) {
        this.plugin = plugin;
        this.matchManager = matchManager;
    }

    /**
     * verifica se só tem um jogador vivo
     */
    public void checkForWinner() {
        if(!isMatchRunning()) return;

        matchManager.getWinner().ifPresent(this::celebrateWinner);
    }

    /**
     * Essa função cria uma plataforma para o player celebrar a vitória quando só restar ele no final da partida
     * @param winner
     */
    private void celebrateWinner(Player winner) {
        Location loc = winner.getLocation().clone();

        int y = loc.getBlockY() + 70;
        World world = winner.getWorld();

        for (int x = -2; x < 2; x++) {
            for (int z = -2; z < 2; z++) {
                Location blockLoc = new Location(world, loc.getBlockX() + x, y, loc.getBlockZ() + z);
                blockLoc.getBlock().setType(Material.GLASS);
            }
        }

        for (int x = -2; x < 2; x++) {
            for (int z = -2; z < 2; z++) {
                Location blockLoc = new Location(world, loc.getBlockX() + x, y + 1, loc.getBlockZ() + z);
                blockLoc.getBlock().setType(Material.CAKE);
            }
        }

        Location teleportLoc = new Location(world, loc.getBlockX(), y + 2, loc.getBlockZ());
        winner.teleport(teleportLoc);
        winner.sendTitle("§6🏆 VITÓRIA!", "§eVocê é o último sobrevivente!", 10, 70, 20);

        world.spawnParticle(Particle.FIREWORK, teleportLoc, 100, 2, 2, 2, 0.1);
        deleteFolder(pasta);
        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.broadcastMessage("§cReiniciando o servidor...");
                Bukkit.spigot().restart();
            }
        }.runTaskLater(plugin, 20L * 20);
    }

    /**
     *
     * @param e
     */
    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        if (!isMatchRunning()) return;

        matchManager.removePlayer(e.getEntity());
        matchManager.getWinner().ifPresent(this::celebrateWinner);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (isMatchRunning()) {
            matchManager.addPlayer(e.getPlayer());
            checkForWinner();
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        if (isMatchRunning()) {
            matchManager.removePlayer(e.getPlayer());
            checkForWinner();
        }
    }

    public static void deleteFolder(File folder) {
        if (!folder.exists()) return;

        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteFolder(file); // apaga subpastas
                } else {
                    file.delete(); // apaga arquivos
                }
            }
        }

        folder.delete(); // apaga a própria pasta
    }
}
