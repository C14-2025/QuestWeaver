package br.dev.projetoc14.quest;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/*

Vou deixar essa classe definida por enquanto para quando for incrementar as quests no futuro

 */

public class LevelBasedQuest extends Quest {
    private int minLevel;
    private int maxLevel;

    public LevelBasedQuest(String id, String name, String description, int experienceReward) {
        super(id, name, description, experienceReward);
    }

    public boolean isAvailableForPlayer(Player player) {
        // Assumindo que você tem um método getLevel()
        int playerLevel = player.getLevel();
        return playerLevel >= minLevel && playerLevel <= maxLevel;
    }

    @Override
    public boolean checkCompletion() {
        return false;
    }

    @Override
    public void updateProgress(Object... params) {

    }

    @Override
    public ItemStack[] getRewardItems() {
        return new ItemStack[0];
    }

    @Override
    public void assignToPlayer(Player player) {
        // TODO: Adicionar esse metodo para esse tipo de quest
        return;
    }
}