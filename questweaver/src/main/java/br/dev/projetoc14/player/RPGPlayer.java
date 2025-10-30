package br.dev.projetoc14.player;

import br.dev.projetoc14.player.abilities.Ability;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class RPGPlayer {

    protected Player player; // player real do Bukkit
    protected PlayerClass playerClass;
    protected int level;
    protected int experience;
    protected PlayerStats stats;
    protected List<Ability> abilities = new ArrayList<>();

    // ==== Sistema de Vida ==== //
    protected int maxHealth;
    protected int currentHealth;

    // ==== Sistema de Mana ==== //
    protected int maxMana;
    protected int currentMana;

    public RPGPlayer(Player player, PlayerClass playerClass, int level, int experience, PlayerStats stats) {
        this.player = player;
        this.playerClass = playerClass;
        this.level = 1;
        this.experience = 0;
        this.stats = new PlayerStats();

        // Valores padrão
        this.maxHealth = 100;
        this.currentHealth = 100;
        this.maxMana = 100;
        this.currentMana = 100;

        initializeClass();
    }

    public RPGPlayer(Player player, PlayerClass playerClass) {
        this(player, playerClass, 1, 0, new PlayerStats());
    }

    public RPGPlayer get(Player p) {
        return this;
    }

    protected abstract void initializeClass();
    public abstract void levelUp();
    public abstract void getStartingEquipment();

    // ==== Vida ==== //
    public int getMaxHealth() { return maxHealth; }
    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
        this.currentHealth = Math.min(this.currentHealth, maxHealth);
    }

    public int getCurrentHealth() { return currentHealth; }
    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = Math.max(0, Math.min(currentHealth, this.maxHealth));
    }

    public void heal(int amount) {
        int newHealth = this.currentHealth + amount;
        setCurrentHealth(newHealth);

        // Feedback visual
        player.sendMessage("§a💚 +§l" + amount + " HP");
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.5f, 2f);
    }

    public void damage(int amount) {
        int newHealth = this.currentHealth - amount;
        setCurrentHealth(newHealth);

        // Feedback visual
        player.sendMessage("§c❤ -§l" + amount + " HP");
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT, 1f, 1f);
    }

    public boolean isAlive() {
        return this.currentHealth > 0;
    }

    // ==== Mana ==== //
    public int getMaxMana() { return maxMana; }
    public void setMaxMana(int maxMana) {
        this.maxMana = maxMana;
        this.currentMana = Math.min(this.currentMana, maxMana);
    }

    public int getCurrentMana() { return currentMana; }
    public void setCurrentMana(int currentMana) {
        this.currentMana = Math.max(0, Math.min(currentMana, this.maxMana));
    }

    public void restoreMana(int amount) {
        int newMana = this.currentMana + amount;
        setCurrentMana(newMana);

        player.sendMessage("§b✨ +§l" + amount + " Mana");
    }

    public void consumeMana(int amount) {
        int newMana = this.currentMana - amount;
        setCurrentMana(newMana);
    }

    public boolean hasMana(int amount) {
        return this.currentMana >= amount;
    }

    // ==== Experiência ==== //
    public void addExperience(int exp) {
        this.experience += exp;
        // Aqui você pode adicionar lógica de level up automático
    }

    // ==== Delegadores do Bukkit Player ==== //
    public UUID getUniqueId() { return player.getUniqueId(); }
    public Location getLocation() { return player.getLocation(); }
    public Location getEyeLocation() { return player.getEyeLocation(); }
    public <T extends Projectile> T launchProjectile(Class<T> projectileClass) {
        return player.launchProjectile(projectileClass);
    }
    public World getWorld() { return player.getWorld(); }
    public void sendMessage(String message) { player.sendMessage(message); }
    public void playSound(Location loc, Sound sound, float volume, float pitch) {
        player.playSound(loc, sound, volume, pitch);
    }

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

    public void setHealth(int i) {
    }
}