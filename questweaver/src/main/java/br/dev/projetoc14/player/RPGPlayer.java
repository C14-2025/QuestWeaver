package br.dev.projetoc14.player;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class RPGPlayer {

    protected Player player; // Este player já vem do bukkit
    protected PlayerClass playerClass;
    protected int level;
    protected int experience;
    protected PlayerStats stats;

    public RPGPlayer(Player player, PlayerClass playerClass, int level, int experience, PlayerStats stats) {
        this.player = player;
        this.playerClass = playerClass;
        this.level = 1;
        this.experience = 0;
        this.stats = new PlayerStats();
        initializeClass();
    }

    public RPGPlayer(Player player, PlayerClass playerClass) {
        this(player, playerClass, 1, 0, new PlayerStats());
    }

    protected abstract void initializeClass();

    public abstract void levelUp();
    public abstract ItemStack[] getStartingEquipment();
    public void addExperience(int exp) { this.experience += exp; }

    public Player getPlayer() { return player; }
    public PlayerClass getPlayerClass() { return playerClass; }
    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }
    public int getExperience() { return experience; }
    public PlayerStats getStats() { return stats; }


}
