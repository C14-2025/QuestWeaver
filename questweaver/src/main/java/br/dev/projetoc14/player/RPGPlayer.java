package br.dev.projetoc14.player;

import br.dev.projetoc14.player.abilities.Ability;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public abstract class RPGPlayer {

    protected Player player; // player real do Bukkit
    protected PlayerClass playerClass;
    protected int level;
    protected int experience;
    protected PlayerStats stats;
    protected List<Ability> abilities = new ArrayList<>();

    // ==== Novo sistema de vida ==== //
    protected int health;
    protected int currentHealth;

    public RPGPlayer(Player player, PlayerClass playerClass, int level, int experience, PlayerStats stats) {
        this.player = player;
        this.playerClass = playerClass;
        this.level = 1;
        this.experience = 0;
        this.stats = new PlayerStats();

        this.health = 100;        // padrão
        this.currentHealth = 100; // começa cheio

        initializeClass();
    }

    public RPGPlayer(Player player, PlayerClass playerClass) {
        this(player, playerClass, 1, 0, new PlayerStats());
    }

    protected abstract void initializeClass();
    public abstract void levelUp();
    public abstract ItemStack[] getStartingEquipment();

    // ==== Vida ==== //
    public int getHealth() { return health; }
    public void setHealth(int health) { this.health = health; }

    public int getCurrentHealth() { return currentHealth; }
    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = Math.min(currentHealth, this.health); // nunca passar do máximo
    }

    public void heal(int amount) {
        this.currentHealth = Math.min(this.currentHealth + amount, this.health);
    }

    public void damage(int amount) {
        this.currentHealth = Math.max(this.currentHealth - amount, 0);
    }

    public boolean isAlive() {
        return this.currentHealth > 0;
    }

    // ==== Experiência ==== //
    public void addExperience(int exp) {
        this.experience += exp;
    }

    // ==== Delegadores do Bukkit Player ==== //
    public Location getEyeLocation() { return player.getEyeLocation(); }
    public <T extends Projectile> T launchProjectile(Class<T> projectileClass) { return player.launchProjectile(projectileClass); }
    public World getWorld() { return player.getWorld(); }
    public void sendMessage(String message) { player.sendMessage(message); }

    // ==== Abilities ==== //
    public void addAbility(Ability ability) { abilities.add(ability); }
    public List<Ability> getAbilities() { return abilities; }

    // ==== Getters ==== //
    public Player getPlayer() { return player; }
    public PlayerClass getPlayerClass() { return playerClass; }
    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }
    public int getExperience() { return experience; }
    public PlayerStats getStats() { return stats; }
}