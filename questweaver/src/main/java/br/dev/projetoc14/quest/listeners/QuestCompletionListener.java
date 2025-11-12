package br.dev.projetoc14.quest.listeners;

import br.dev.projetoc14.QuestWeaver;
import br.dev.projetoc14.quest.Quest;
import br.dev.projetoc14.quest.utils.QuestCompletedEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class QuestCompletionListener implements Listener {

    @EventHandler
    public void onQuestCompleted(QuestCompletedEvent event) {
        Player player = event.getPlayer();
        Quest quest = event.getQuest();
        QuestWeaver plugin = (QuestWeaver) QuestWeaver.getInstance();

        plugin.getQuestManager().onQuestComplete(player, quest);
    }
}
