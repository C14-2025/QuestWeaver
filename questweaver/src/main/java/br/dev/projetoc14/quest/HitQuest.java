package br.dev.projetoc14.quest;

import br.dev.projetoc14.QuestWeaver;
import br.dev.projetoc14.quest.utils.QuestCompletedEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

/**
 * Quest que conta acertos (hits) em entidades, não mortes
 */
public class HitQuest extends Quest {
    protected final String targetMob;
    protected final int targetCount;
    protected int currentCount;
    protected final Location spawnLocation;
    protected final List<Material> validWeapons;

    public HitQuest(String id, String name, String description, int experienceReward,
                    String targetMob, int targetCount, int currentCount,
                    Location spawnLocation, List<Material> validWeapons) {
        super(id, name, description, experienceReward);
        this.targetMob = targetMob;
        this.targetCount = targetCount;
        this.currentCount = currentCount;
        this.spawnLocation = spawnLocation;
        this.validWeapons = validWeapons != null ? validWeapons : new ArrayList<>();
    }

    public void spawnTargetEntities(Player player) {
        if (spawnLocation == null) return;

        EntityType entityType;
        try {
            entityType = EntityType.valueOf(targetMob.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Tipo de entidade invalido: " + targetMob);
            return;
        }

        World world = player.getWorld();
        for (int i = 0; i < targetCount; i++) {
            Location randomLocation = new Location(
                    world,
                    spawnLocation.getX() + (Math.random() * 6 - 2),
                    player.getLocation().getY(),
                    spawnLocation.getZ() + (Math.random() * 6 - 2)
            );

            Entity entity = world.spawnEntity(randomLocation, entityType);

            // Configurações especiais por tipo de mob
            if (entity instanceof Zombie zombie) {
                zombie.setShouldBurnInDay(false);
            }
            if (entity instanceof Skeleton skeleton) {
                skeleton.setShouldBurnInDay(false);
            }

            // Marca como alvo da quest
            NamespacedKey key = new NamespacedKey(QuestWeaver.getInstance(), "quest_target");
            entity.getPersistentDataContainer().set(key, PersistentDataType.BYTE, (byte) 1);
            entity.customName(Component.text("Quest Target"));
            entity.setCustomNameVisible(true);
        }
    }

    @Override
    public ItemStack[] getRewardItems() {
        return new ItemStack[0];
    }

    @Override
    public void assignToPlayer(Player player) {
        spawnTargetEntities(player);
    }

    @Override
    public boolean checkCompletion() {
        return currentCount >= targetCount;
    }

    @Override
    public void updateProgress(Object... params) {
        // Formato: mobType, weapon, player, arrow (opcional)
        if (params.length >= 3 &&
                params[0] instanceof String mobType &&
                params[1] instanceof Material weapon &&
                params[2] instanceof Player player) {

            // Se tiver arrow como 4º parâmetro, valida flecha especial
            if (params.length >= 4 && params[3] instanceof Arrow arrow) {
                if (mobType.equalsIgnoreCase(targetMob) && isValidProjectile(arrow)) {
                    currentCount++;

                    if (checkCompletion()) {
                        QuestCompletedEvent customEvent = new QuestCompletedEvent(player, this);
                        Bukkit.getServer().getPluginManager().callEvent(customEvent);
                    }
                }
            }
            // Senão, valida pela arma normal
            else if (mobType.equalsIgnoreCase(targetMob) && isValidWeapon(weapon)) {
                currentCount++;

                if (checkCompletion()) {
                    QuestCompletedEvent customEvent = new QuestCompletedEvent(player, this);
                    Bukkit.getServer().getPluginManager().callEvent(customEvent);
                }
            }
        }
    }

    // Getters
    public int getCurrentCount() {
        return currentCount;
    }

    public int getTargetCount() {
        return targetCount;
    }

    public String getProgressText() {
        return String.format("%d/%d acertos em %s", currentCount, targetCount, targetMob);
    }

    protected boolean isValidWeapon(Material weapon) {
        return validWeapons.isEmpty() || validWeapons.contains(weapon);
    }

    /**
     * Método para validar projéteis especiais (flechas customizadas, etc)
     * Sobrescreva em subclasses para implementar validação específica
     */
    protected boolean isValidProjectile(Arrow arrow) {
        return true; // Aceita qualquer projétil por padrão
    }
}