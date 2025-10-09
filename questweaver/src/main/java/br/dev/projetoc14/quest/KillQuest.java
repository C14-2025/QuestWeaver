package br.dev.projetoc14.quest;

import net.kyori.adventure.text.Component;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class KillQuest extends Quest {
    private final String targetMob;
    private final int targetCount;
    private int currentCount;
    private final Location spawnLocation;

    // Construtor para compatibilidade com testes
    public KillQuest(String id, String name, String description, int experienceReward,
                     String targetMob, int targetCount, int currentCount) {
        this(id, name, description, experienceReward, targetMob, targetCount, currentCount, null);
    }

    // Construtor completo
    public KillQuest(String id, String name, String description, int experienceReward,
                     String targetMob, int targetCount, int currentCount, Location spawnLocation) {
        super(id, name, description, experienceReward);
        this.targetMob = targetMob;
        this.targetCount = targetCount;
        this.currentCount = currentCount;
        this.spawnLocation = spawnLocation;
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
            // Usando o novo metodo com Component
            entity.customName(Component.text(getName() + " Target"));
            entity.setCustomNameVisible(true);
        }
    }


    public void assignToPlayer(Player player) {
        spawnTargetEntities(player);
    }

    @Override
    public boolean checkCompletion() {
        return currentCount >= targetCount;
    }

    @Override
    public void updateProgress(Object... params) {
        if (params[0] instanceof String && params[0].equals(targetMob)) {
            currentCount++;
            completed = checkCompletion();
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
}