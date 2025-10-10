package br.dev.projetoc14.player;

public enum PlayerClass {
    WARRIOR("Guerreiro"),
    ARCHER("Arqueiro"),
    MAGE("Mago"),
    ASSASSIN("Assassino");

    private final String displayName;

    PlayerClass(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
