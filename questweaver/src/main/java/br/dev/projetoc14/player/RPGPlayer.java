package br.dev.projetoc14.player;

import br.dev.projetoc14.player.abilities.Ability;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class RPGPlayer {

    protected Player player;
    protected PlayerClass playerClass;
    protected int level;
    protected int experience;
    protected PlayerStats stats;
    protected List<Ability> abilities = new ArrayList<>();
    private PlayerStatsManager statsManager;
    private int currentHealth;

    public RPGPlayer(Player player, PlayerClass playerClass, int level, int experience, PlayerStats stats) {
        this.player = player;
        this.playerClass = playerClass;
        this.level = level;
        this.experience = experience;
        this.stats = stats != null ? stats : new PlayerStats();

        initializeClass();

        // Inicializa HP atual no máximo
        this.currentHealth = this.stats.getHealth();
    }

    public RPGPlayer(Player player, PlayerClass playerClass) {
        this.player = player;
        this.playerClass = playerClass;
        this.level = 1;
        this.experience = 0;
        this.stats = new PlayerStats(); // Stats base temporário

        // Necessário inicializar antes de setar a vida.
        initializeClass();

        this.currentHealth = this.stats.getHealth();
    }

    public RPGPlayer get(Player p) {
        return this;
    }

    public void setStatsManager(PlayerStatsManager manager) {
        this.statsManager = manager;
    }

    protected abstract void initializeClass();
    public abstract void levelUp();
    public abstract void getStartingEquipment();

    // ==== HP e Mana ==== //

    public int getMaxHealth() {
        return stats.getHealth();
    }

    public void setMaxHealth(int maxHealth) {
        stats.setHealth(maxHealth);
        if (currentHealth > maxHealth) {
            currentHealth = maxHealth;
        }
        if (statsManager != null) {
            statsManager.applyStats(player);
        }
    }

    // ✅ Agora usa o campo interno
    public int getCurrentHealth() {
        return currentHealth;
    }

    public void setCurrentHealth(int currentHealth) {
        int newHealth = Math.max(0, Math.min(currentHealth, getMaxHealth()));
        this.currentHealth = newHealth;
        player.setHealth(newHealth); // sincroniza com Bukkit
    }

    public void heal(int amount) {
        int newHealth = getCurrentHealth() + amount;
        setCurrentHealth(newHealth);

        if (statsManager != null){
            statsManager.applyStats(player);
        }

        player.sendMessage("§a💚 +§l" + amount + " HP");
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.5f, 2f);
    }

    public void damage(int amount) {
        int newHealth = getCurrentHealth() - amount;
        setCurrentHealth(newHealth);

        player.sendMessage("§c❤ -§l" + amount + " HP");
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_HURT, 1f, 1f);
    }

    public boolean isAlive() {
        return currentHealth > 0;
    }

    public int getMaxMana() {
        return stats.getMana();
    }

    public void setMaxMana(int maxMana) {
        stats.setMana(maxMana);
        if (stats.getCurrentMana() > maxMana) {
            stats.setCurrentMana(maxMana);
        }
        updateManaBar();
    }

    public int getCurrentMana() {
        return stats.getCurrentMana();
    }

    public void setCurrentMana(int currentMana) {
        int clamped = Math.max(0, Math.min(currentMana, getMaxMana()));
        stats.setCurrentMana(clamped);
        updateManaBar();
    }

    public void restoreMana(int amount) {
        int newMana = getCurrentMana() + amount;
        setCurrentMana(newMana);
        player.sendMessage("§b✨ +§l" + amount + " Mana");
    }

    public boolean hasMana(int amount) {
        return getCurrentMana() >= amount;
    }

    private void updateManaBar() {
        if (statsManager != null) {
            statsManager.updateManaBar(player);
        }
    }

    // ==== Experiência ==== //
    public void addExperience(int exp) {
        this.experience += exp;
        // Lógica de level up pode ser adicionada aqui
    }

    // ==== Utilitários ==== //
    public ItemStack createQuestBook() {
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) book.getItemMeta();

        TextColor classColor = getClassColor();
        meta.title(Component.text("Livro de Quests")
                .color(classColor)
                .decoration(TextDecoration.BOLD, true));
        meta.author(Component.text("QuestWeaver"));

        Component firstPage = Component.text()
                .append(Component.text("📖 Livro de Quests\n\n")
                        .decoration(TextDecoration.BOLD, true)
                        .color(classColor))
                .append(Component.text("Clique com botão\ndireito para ver\nsuas quests!\n\n")
                        .color(TextColor.color(0xAAAAAA)))
                .append(Component.text("Ou use:\n")
                        .color(TextColor.color(0xAAAAAA)))
                .append(Component.text("/quests")
                        .color(TextColor.color(0x5555FF))
                        .decoration(TextDecoration.UNDERLINED, true))
                .build();

        meta.pages(List.of(firstPage));
        book.setItemMeta(meta);
        return book;
    }

    private TextColor getClassColor() {
        return switch(playerClass) {
            case WARRIOR -> TextColor.color(0xFF5555);
            case MAGE -> TextColor.color(0x5555FF);
            case ARCHER -> TextColor.color(0x55FF55);
            case ASSASSIN -> TextColor.color(0x555555);
        };
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

    public void setHealth(int health) {
        setCurrentHealth(health);
    }

    public void refreshHealth() {
        int maxHealth = stats.getHealth();

        // 1. Define o máximo de vida do Bukkit
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(maxHealth);

        // 2. Define a vida atual como máxima
        player.setHealth(maxHealth);
        this.currentHealth = maxHealth;

        // 4. Garante fome e saturação
        player.setFoodLevel(20);
        player.setSaturation(20f);
        player.setExhaustion(0f);
    }
}
