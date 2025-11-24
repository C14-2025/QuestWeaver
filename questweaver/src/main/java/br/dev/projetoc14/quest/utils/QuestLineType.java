package br.dev.projetoc14.quest.utils;

public enum QuestLineType {
    WARRIOR("Guerreiro"),
    MAGE("Mago"),
    ARCHER("Arqueiro"),
    ASSASSIN("Assassino");

    private final String displayName;

    QuestLineType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}