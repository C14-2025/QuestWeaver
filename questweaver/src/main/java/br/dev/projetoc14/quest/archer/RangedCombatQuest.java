package br.dev.projetoc14.quest.archer;

import br.dev.projetoc14.quest.HitQuest;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;

import java.util.ArrayList;

/**
 * Quest Fácil: Arena de Treinamento Controlada - Adaptada para nova HitQuest
 */
public class RangedCombatQuest extends HitQuest {
    private static final double MIN_DISTANCE = 15.0;
    private Location arenaCenter;

    public RangedCombatQuest(Location spawnLocation) {
        super("ranged_combat_quest",
                "Combate a Distância",
                "Derrote 5 esqueletos na arena de treinamento a pelo menos " + (int)MIN_DISTANCE + " blocos",
                100,
                "SKELETON",
                5,
                0,
                spawnLocation,
                new ArrayList<>());
        this.arenaCenter = spawnLocation.clone().add(0, 0, 10); // Arena a 10 blocos de distância
    }

    @Override
    public void buildQuestEnvironment(Player player) {
        buildTrainingArena(player);
    }

    @Override
    public void spawnStrategicEntities(Player player) {
        spawnArenaSkeletons(player);
    }

    private void buildTrainingArena(Player player) {
        if (environmentBuilt) return;

        World world = player.getWorld();
        Location center = arenaCenter;

        // Plataforma principal (15x15) - usando método seguro da superclasse
        for (int x = -7; x <= 7; x++) {
            for (int z = -7; z <= 7; z++) {
                setBlockSafe(world, center.clone().add(x, -1, z), Material.OAK_PLANKS);
            }
        }

        // Bordas da arena
        for (int x = -8; x <= 8; x++) {
            for (int z = -8; z <= 8; z++) {
                if (Math.abs(x) == 8 || Math.abs(z) == 8) {
                    setBlockSafe(world, center.clone().add(x, 0, z), Material.OAK_FENCE);
                    setBlockSafe(world, center.clone().add(x, 1, z), Material.OAK_FENCE);
                }
            }
        }

        // Plataforma do jogador (5x5)
        Location playerPlatform = player.getLocation().clone();
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                setBlockSafe(world, playerPlatform.clone().add(x, -1, z), Material.STONE_BRICKS);
            }
        }

        // Parapeito de proteção
        for (int x = -3; x <= 3; x++) {
            for (int z = -3; z <= 3; z++) {
                if (Math.abs(x) == 3 || Math.abs(z) == 3) {
                    setBlockSafe(world, playerPlatform.clone().add(x, 0, z), Material.STONE_BRICK_WALL);
                }
            }
        }

        environmentBuilt = true;
        player.sendMessage("§a⚔ Arena de treinamento construída! Use sua posição elevada para atirar!");
    }

    private void spawnArenaSkeletons(Player player) {
        if (arenaCenter == null) return;

        World world = player.getWorld();

        // Spawna esqueletos DENTRO da arena usando o método da superclasse
        for (int i = 0; i < getTargetCount(); i++) {
            double angle = (2 * Math.PI * i) / getTargetCount();
            double x = arenaCenter.getX() + 4 * Math.cos(angle);
            double z = arenaCenter.getZ() + 4 * Math.sin(angle);

            Location spawnLoc = new Location(world, x, arenaCenter.getY(), z);

            // Usa o método utilitário da superclasse
            spawnQuestEntity(world, spawnLoc, org.bukkit.entity.EntityType.SKELETON, "§7Alvo de Treinamento");
        }

        player.sendMessage("§e🎯 " + getTargetCount() + " esqueletos apareceram na arena!");
    }

    @Override
    protected boolean isValidProjectile(Arrow arrow) {
        if (!(arrow.getShooter() instanceof Player shooter)) {
            return false;
        }

        Location shooterLoc = shooter.getLocation();
        Location arrowLoc = arrow.getLocation();
        double distance = shooterLoc.distance(arrowLoc);

        if (distance >= MIN_DISTANCE) {
            return true;
        } else {
            shooter.sendMessage(String.format("§c✗ Muito perto! (%.1f/%.0f blocos)", distance, MIN_DISTANCE));
            return false;
        }
    }

    @Override
    public void updateProgress(Object... params) {
        // Delega para a superclasse que já tem a lógica completa
        super.updateProgress(params);

        // Adiciona feedback específico para esta quest
        if (params.length >= 3 && params[2] instanceof Player player) {
            int current = getCurrentCount();
            int target = getTargetCount();

            if (current > 0 && current < target) {
                // Feedback a cada acerto bem-sucedido
                player.sendMessage("§a✓ " + getProgressText());
            }
        }
    }

    @Override
    public String getProgressText() {
        return String.format("%d/%d esqueletos derrotados (mín. %d blocos)",
                getCurrentCount(), getTargetCount(), (int)MIN_DISTANCE);
    }

    @Override
    public void assignToPlayer(Player player) {
        // Usa a implementação da superclasse que chama buildQuestEnvironment e spawnStrategicEntities
        super.assignToPlayer(player);
    }

    // Método antigo - removido pois não é mais necessário
    // private void setBlock(World world, Location location, Material material) {
    //     world.getBlockAt(location).setType(material);
    // }

    public static double getMinDistance() {
        return MIN_DISTANCE;
    }
}