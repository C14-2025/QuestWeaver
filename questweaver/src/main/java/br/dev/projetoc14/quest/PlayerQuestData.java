package br.dev.projetoc14.quest;

import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.Map;

/*
 * Armazena as quests ativas e completadas de um jogador
 */
public class PlayerQuestData {

    private Player player;
    private Map<String, Quest> activeQuests;
    private Map<String, Quest> completedQuests;

    public PlayerQuestData(Player player) {
        this.player = player;
        this.activeQuests = new HashMap<>();
        this.completedQuests = new HashMap<>();
    }

    public void addQuest(Quest quest) {
        activeQuests.put(quest.getId(), quest);
    }

    public void completeQuest(String questId) {
        Quest quest = activeQuests.remove(questId);
        if (quest != null) {
            completedQuests.put(questId, quest);
        }
    }

    public Map<String, Quest> getActiveQuests() {
        return activeQuests;
    }

    public Map<String, Quest> getCompletedQuests() {
        return completedQuests;
    }

    public Player getPlayer() {
        return player;
    }
}