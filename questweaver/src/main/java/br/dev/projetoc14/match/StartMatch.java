package br.dev.projetoc14.match;


import br.dev.projetoc14.QuestWeaver;
import br.dev.projetoc14.items.SkillTree;
import br.dev.projetoc14.player.PlayerStatsManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;

public class StartMatch {

    private final PlayerFileManager fileManager;
    private final PlayerStatsManager statsManager;
    private final Plugin plugin;
    private StartMatchItems start;

    public StartMatch(PlayerFileManager fileManager, PlayerStatsManager statsManager, Plugin plugin) {
        this.fileManager = fileManager;
        this.statsManager = statsManager;
        this.plugin = plugin;
        this.start =  new StartMatchItems(fileManager, statsManager);
    }

    /*
        if all players are set in, the match will start
        - clean inventory
        - set gamemode survival
        - set kit items
        - set 1 min 45 sec invencibility (can be changed)
        - author: sno0s
     */

    public void execute() {
        Collection<? extends Player> jogadores = Bukkit.getOnlinePlayers();

        // players initial configs, in invincibility
        for (Player p : jogadores) {
            p.setInvulnerable(true);
            p.setGameMode(GameMode.SURVIVAL);
            p.getInventory().clear();

            /*
                TODO: aqui, chamar o método para cada item que deve ser dado ao player no inicio da partida
                TODO: teleportá-los para perto de suas estruturas para que não se enfrentem de início
                tempo de invencibilidade pode ser ajustado
             */
            start.setItems(p);
            p.getInventory().setItem(8, SkillTree.create());
        }

        // invincibility countdown
        new BukkitRunnable() {
            int countdown = 105; // 1m45s

            @Override
            public void run() {
                if (countdown == 105 || countdown == 30 || countdown == 15
                        || (countdown <= 5 && countdown > 0)) {
                    Bukkit.broadcast(Component.text("§eFaltam " + countdown + " segundos de invencibilidade."));
                }

                if (countdown == 0) {
                    Bukkit.broadcast(Component.text("§aA invencibilidade acabou!"));

                    for (Player p : Bukkit.getOnlinePlayers()) {
                        p.setInvulnerable(false);

                    }
                    cancel(); // Para o contador
                    return;
                }

                countdown--; // decrementa a cada segundo
            }
        }.runTaskTimer(plugin, 0L, 20L); // 20 ticks = 1 segundo
    }
}
