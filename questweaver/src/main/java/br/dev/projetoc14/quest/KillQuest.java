package br.dev.projetoc14.quest;

import br.dev.projetoc14.QuestWeaver;
import br.dev.projetoc14.quest.utils.QuestCompletedEvent;
import org.bukkit.*;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class KillQuest extends Quest {
    protected final String targetMob;
    protected final int targetCount;
    protected int currentCount;
    protected final Location spawnLocation;
    protected final List<Material> validWeapons;

    // Construtor completo
    public KillQuest(String id, String name, String description, int experienceReward,
                     String targetMob, int targetCount, int currentCount, Location spawnLocation, List<Material> validWeapons) {
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
                    spawnLocation.getX() + (Math.random() * 4 - 2),
                    player.getLocation().getY(),
                    spawnLocation.getZ() + (Math.random() * 4 - 2)
            );

            Entity entity = world.spawnEntity(randomLocation, entityType);
            if (entity instanceof Zombie zombie) {
                // Garante que nao queime no sol
                zombie.setShouldBurnInDay(false);
            }

            if (entity instanceof Skeleton skeleton) {
                // Garante que nao queime no sol
                skeleton.setShouldBurnInDay(false);
            }

            NamespacedKey key = new NamespacedKey(QuestWeaver.getInstance(), "quest_target");
            entity.getPersistentDataContainer().set(key, PersistentDataType.BYTE, (byte) 1);
            // Usando o novo metodo com Component
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
        if (params.length >= 2 && params[0] instanceof String mobType && params[1] instanceof Material weapon) {
            if (mobType.equalsIgnoreCase(targetMob) && isValidWeapon(weapon)) {
                currentCount++;

                if (checkCompletion() && params[2] instanceof Player player) {
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
        return String.format("%d/%d %s eliminados", currentCount, targetCount, targetMob);
    }

    private boolean isValidWeapon(Material weapon) {
        return validWeapons.isEmpty() || validWeapons.contains(weapon);
    }
}