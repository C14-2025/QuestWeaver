package br.dev.projetoc14.quest;

import org.bukkit.Location;
import net.kyori.adventure.text.Component;
import org.bukkit.World;
import org.bukkit.entity.*;

public class KillQuest extends Quest {
    private final String targetMob;
    private final int targetCount;
    private int currentCount;
    private final Location spawnLocation;

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

            if (entity instanceof LivingEntity livingEntity) {
                // Pega o HP máximo padrão do mob
                double defaultMaxHealth = livingEntity.getAttribute(
                        org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH
                ).getDefaultValue();

                // Triplica o HP
                double tripleHealth = defaultMaxHealth * 3;

                // Define o novo HP máximo
                livingEntity.getAttribute(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH)
                        .setBaseValue(tripleHealth);

                // Cura o mob para ter HP cheio
                livingEntity.setHealth(tripleHealth);
            }

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


    public void assignToPlayer(Player player) {
        spawnTargetEntities(player);
    }

    @Override
    public boolean checkCompletion() {
        return currentCount >= targetCount;
    }

    @Override
    public void updateProgress(Object... params) {
        if (params.length > 0 && params[0] instanceof String mobType) {
            if (mobType.equalsIgnoreCase(targetMob)) {
                currentCount++;
                completed = checkCompletion();
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
}