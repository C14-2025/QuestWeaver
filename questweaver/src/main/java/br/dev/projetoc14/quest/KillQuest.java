package br.dev.projetoc14.quest;

import org.bukkit.Location;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class KillQuest extends Quest {
    private final String targetMob;
    private final int targetCount;
    private int currentCount;
    private final Location spawnLocation;
    private final List<Material> validWeapons;

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
                // Garante que não queime no sol
                zombie.setShouldBurnInDay(false);
            }

            if (entity instanceof Skeleton skeleton) {
                // Garante que não queime no sol
                skeleton.setShouldBurnInDay(false);
            }

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
            if (mobType.equalsIgnoreCase(targetMob) && (validWeapons.isEmpty() || validWeapons.contains(weapon))) {
                currentCount++;
                completed = checkCompletion();

                if (completed && params.length >= 3 && params[2] instanceof Player player) {
                        giveRewards(player);
                }
            }
        }
    }

    // Getters
    public int getCurrentCount() {
        return currentCount;
    }

    public String getTargetMob() {
        return targetMob;
    }

    public int getTargetCount() {
        return targetCount;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public String getProgressText() {
        return String.format("%d/%d %s eliminados", currentCount, targetCount, targetMob);
    }

    public boolean isValidWeapon(Material weapon) {
        return validWeapons.isEmpty() || validWeapons.contains(weapon);
    }
}