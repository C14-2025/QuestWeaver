package br.dev.projetoc14.quest.archer;

import br.dev.projetoc14.quest.HitQuest;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;

import java.util.ArrayList;

/**
 * Quest Fácil: Arena de Treinamento Controlada - CORRIGIDA
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
        // Arena a 20 blocos de distância para garantir os 15 blocos mínimos
        this.arenaCenter = spawnLocation.clone().add(0, 0, 20);
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

        // **PLATAFORMA DOS ESQUELETOS - MAIOR E MAIS SEGURA**
        // Plataforma principal (20x20) - maior para os esqueletos se moverem
        for (int x = -10; x <= 10; x++) {
            for (int z = -10; z <= 10; z++) {
                // Cria uma plataforma elevada (2 blocos acima do chão)
                setBlockSafe(world, center.clone().add(x, 1, z), Material.OAK_PLANKS);
                // Garante que tem bloco sólido embaixo
                setBlockSafe(world, center.clone().add(x, 0, z), Material.DIRT);
            }
        }

        // **BORDAS SEGURAS** - impede que esqueletos caiam
        for (int x = -11; x <= 11; x++) {
            for (int z = -11; z <= 11; z++) {
                if (Math.abs(x) == 11 || Math.abs(z) == 11) {
                    setBlockSafe(world, center.clone().add(x, 1, z), Material.OAK_FENCE);
                    setBlockSafe(world, center.clone().add(x, 2, z), Material.OAK_FENCE);
                }
            }
        }

        // **PLATAFORMA DO JOGADOR MELHORADA**
        Location playerPlatform = player.getLocation().clone();

        // Plataforma elevada (3 blocos de altura)
        for (int x = -3; x <= 3; x++) {
            for (int z = -3; z <= 3; z++) {
                setBlockSafe(world, playerPlatform.clone().add(x, 2, z), Material.STONE_BRICKS);
                setBlockSafe(world, playerPlatform.clone().add(x, 1, z), Material.STONE_BRICKS);
                setBlockSafe(world, playerPlatform.clone().add(x, 0, z), Material.STONE_BRICKS);
            }
        }

        // **PARAPEITO SEGURO** - com aberturas para atirar
        for (int x = -4; x <= 4; x++) {
            for (int z = -4; z <= 4; z++) {
                if (Math.abs(x) == 4 || Math.abs(z) == 4) {
                    // Deixa aberturas a cada 2 blocos para atirar
                    if (!(Math.abs(x) == 4 && Math.abs(z) == 4) && // não nos cantos
                            !(x % 2 == 0 && z % 2 == 0)) { // aberturas estratégicas
                        setBlockSafe(world, playerPlatform.clone().add(x, 3, z), Material.STONE_BRICK_WALL);
                    }
                }
            }
        }

        // **ESCADA para subir na plataforma**
        setBlockSafe(world, playerPlatform.clone().add(3, 0, 0), Material.OAK_STAIRS);
        setBlockSafe(world, playerPlatform.clone().add(3, 1, 0), Material.OAK_STAIRS);
        setBlockSafe(world, playerPlatform.clone().add(3, 2, 0), Material.OAK_STAIRS);

        environmentBuilt = true;
        player.sendMessage("§a⚔ Arena de treinamento construída!");
        player.sendMessage("§e💡 Suba na plataforma elevada para atirar nos esqueletos!");
    }

    private void spawnArenaSkeletons(Player player) {
        if (arenaCenter == null) return;

        World world = player.getWorld();

        // **SPAWN ESTRATÉGICO** - esqueletos bem distribuídos na arena
        int[] spawnDistances = {3, 5, 7}; // Distâncias variadas da arena
        int skeletonsPerDistance = (int) Math.ceil((double) getTargetCount() / spawnDistances.length);

        int spawned = 0;
        for (int distance : spawnDistances) {
            for (int i = 0; i < skeletonsPerDistance && spawned < getTargetCount(); i++) {
                double angle = (2 * Math.PI * i) / skeletonsPerDistance;
                double x = arenaCenter.getX() + distance * Math.cos(angle);
                double z = arenaCenter.getZ() + distance * Math.sin(angle);

                // **LOCAL SEGURO** - sempre no centro da plataforma, 2 blocos acima
                Location spawnLoc = new Location(world, x, arenaCenter.getY() + 2, z);

                // **VERIFICA SE O LOCAL É SEGURO** antes de spawnar
                if (isSafeSpawnLocation(world, spawnLoc)) {
                    spawnQuestEntity(world, spawnLoc, org.bukkit.entity.EntityType.SKELETON, "§7Alvo de Treinamento");
                    spawned++;
                }
            }
        }

        player.sendMessage("§e🎯 " + spawned + " esqueletos apareceram na arena!");
        player.sendMessage("§6🎯 Acerte-os a pelo menos " + (int)MIN_DISTANCE + " blocos de distância!");
    }

    /** Verifica se o local de spawn é seguro (não dentro de blocos) */
    private boolean isSafeSpawnLocation(World world, Location location) {
        // Verifica se o bloco na posição é ar e o bloco abaixo é sólido
        return world.getBlockAt(location).getType() == Material.AIR &&
                world.getBlockAt(location.clone().add(0, -1, 0)).getType().isSolid();
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
            shooter.sendMessage(String.format("§a✓ Boa! %.1f blocos de distância!", distance));
            return true;
        } else {
            shooter.sendMessage(String.format("§c✗ Muito perto! (%.1f/%.0f blocos)", distance, MIN_DISTANCE));
            return false;
        }
    }

    @Override
    public String getProgressText() {
        return String.format("%d/%d esqueletos (mín. %d blocos)",
                getCurrentCount(), getTargetCount(), (int)MIN_DISTANCE);
    }

    public static double getMinDistance() {
        return MIN_DISTANCE;
    }
}