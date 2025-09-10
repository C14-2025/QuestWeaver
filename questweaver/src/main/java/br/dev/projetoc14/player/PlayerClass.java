package com.GrupoC14.questweaver.models.player;

public enum PlayerClass {
    WARRIOR("Guerreiro"),
    ARCHER("Arqueiro"),
    MAGE("Mago");

    private final String displayName;

    PlayerClass(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
