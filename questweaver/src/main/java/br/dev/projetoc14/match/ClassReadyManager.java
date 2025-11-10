package br.dev.projetoc14.match;

import br.dev.projetoc14.QuestWeaver;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class ClassReadyManager {

    private final QuestWeaver plugin;
    private final Set<Player> playersReady = new HashSet<>();

    public ClassReadyManager(QuestWeaver plugin) {
        this.plugin = plugin;
    }

    public void markPlayerReady(Player player) {
        playersReady.add(player);
        checkAllPlayersReady();
    }

    private void checkAllPlayersReady() {
        Set<Player> onlinePlayers = new HashSet<>(Bukkit.getOnlinePlayers());

        // if(onlinePlayers.size() < 2) return; todo: habilitar somente quando em produção

        if (playersReady.containsAll(onlinePlayers)) {
            // Todos prontos, iniciar a partida
            StartMatch match = new StartMatch(plugin.getPlayerFileManager(), plugin.getMatchManager(), plugin.getStatsManager(), plugin);
            match.execute();

            // Limpa para a próxima partida
            playersReady.clear();
        }
    }
}
