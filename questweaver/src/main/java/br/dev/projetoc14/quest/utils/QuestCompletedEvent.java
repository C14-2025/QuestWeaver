package br.dev.projetoc14.quest.utils;

import br.dev.projetoc14.player.RPGPlayer;
import br.dev.projetoc14.quest.Quest;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class QuestCompletedEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final Quest quest;

    public QuestCompletedEvent(Player player, Quest quest) {
        this.player = player;
        this.quest = quest;
    }

    public Player getPlayer() {
        return player;
    }

    public Quest getQuest() {
        return quest;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
