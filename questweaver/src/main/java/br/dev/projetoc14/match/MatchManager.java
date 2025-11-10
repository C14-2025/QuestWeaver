package br.dev.projetoc14.match;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class MatchManager {

    private final Set<UUID> alive = new HashSet<>();

    public void addPlayer(Player p) {
        alive.add(p.getUniqueId());
    }

    public void removePlayer(Player p) {
        alive.remove(p.getUniqueId());
    }

    public int getAliveCount() {
        return alive.size();
    }

    public Optional<Player> getWinner() {
        return alive.size() == 1
                ? Optional.of(Bukkit.getPlayer(alive.iterator().next()))
                : Optional.empty();
    }
}
