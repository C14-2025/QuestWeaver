package br.dev.projetoc14.quest.utils;

/*
 * Enum que define os tipos de landmarks que o jogador pode encontrar.
 * Cada tipo tem um nome bonito para mostrar na tela.
 */

public enum LandmarkType {
    VILLAGE("Vila"),
    TEMPLE("Templo"),
    DUNGEON("Masmorra");

    private final String displayName;

    LandmarkType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}