package br.dev.projetoc14.quest.structures;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;

/**
 * Cripta abandonada para assassinos (versão expandida e melhorada)
 */
public class AssassinCrypt extends QuestStructure {

    // Constantes para configuração
    private static final int CRYPT_WIDTH = 17;
    private static final int CRYPT_HEIGHT = 8;
    private static final int CRYPT_DEPTH = 20;
    private static final int CRYPT_CEILING = 6;
    private static final int CRYPT_FLOOR = -3;

    // Materiais temáticos
    private static final Material WALL_MATERIAL = Material.STONE_BRICKS;
    private static final Material FLOOR_PRIMARY = Material.MOSSY_STONE_BRICKS;
    private static final Material FLOOR_SECONDARY = Material.CRACKED_STONE_BRICKS;
    private static final Material PILLAR_MATERIAL = Material.POLISHED_BLACKSTONE;
    private static final Material DECORATIVE_MATERIAL = Material.CHISELED_STONE_BRICKS;
    private static final Material LIGHT_SOURCE = Material.SOUL_LANTERN;

    public AssassinCrypt() {
        super("Cripta dos Assassinos", CRYPT_WIDTH, CRYPT_HEIGHT, CRYPT_DEPTH);
    }

    @Override
    protected void build(Location center) {
        buildEntranceCorridor(center);
        buildMainCrypt(center);
        buildPillars(center);
        buildSarcophagi(center);
        buildAltar(center);
        buildSecretAreas(center);
        buildLighting(center);
        buildDecorations(center);
        buildTraps(center);
    }

    private void buildEntranceCorridor(Location center) {
        // Corredor de acesso
        for (int z = -10; z < -6; z++) {
            clearCorridorSpace(center, z);
            buildCorridorFloor(center, z);
            buildCorridorWalls(center, z);
            buildCorridorCeiling(center, z);
        }

        buildEntrancePortal(center);
        buildEntranceLighting(center);
    }

    private void clearCorridorSpace(Location center, int z) {
        for (int x = -2; x <= 2; x++) {
            for (int y = -1; y <= 2; y++) {
                setBlock(center, x, y, z, Material.AIR);
            }
        }
    }

    private void buildCorridorFloor(Location center, int z) {
        for (int x = -2; x <= 2; x++) {
            setBlock(center, x, -2, z, Material.POLISHED_ANDESITE);
        }
    }

    private void buildCorridorWalls(Location center, int z) {
        for (int y = -1; y <= 2; y++) {
            setBlock(center, -3, y, z, WALL_MATERIAL);
            setBlock(center, 3, y, z, WALL_MATERIAL);
        }
    }

    private void buildCorridorCeiling(Location center, int z) {
        setBlock(center, -3, 3, z, WALL_MATERIAL);
        setBlock(center, -2, 3, z, Material.STONE_BRICK_SLAB);
        setBlock(center, -1, 3, z, Material.AIR);
        setBlock(center, 0, 3, z, Material.AIR);
        setBlock(center, 1, 3, z, Material.AIR);
        setBlock(center, 2, 3, z, Material.STONE_BRICK_SLAB);
        setBlock(center, 3, 3, z, WALL_MATERIAL);
    }

    private void buildEntrancePortal(Location center) {
        // Pilares do portal
        for (int y = -1; y <= 2; y++) {
            setBlock(center, -3, y, -6, DECORATIVE_MATERIAL);
            setBlock(center, 3, y, -6, DECORATIVE_MATERIAL);
        }

        // Arco superior
        for (int x = -2; x <= 2; x++) {
            setBlock(center, x, 3, -6, DECORATIVE_MATERIAL);
        }

        // Portão de ferro (pode ser aberto)
        setBlock(center, -1, 0, -6, Material.IRON_BARS);
        setBlock(center, -1, 1, -6, Material.IRON_BARS);
        setBlock(center, 0, 0, -6, Material.IRON_BARS);
        setBlock(center, 0, 1, -6, Material.IRON_BARS);
        setBlock(center, 1, 0, -6, Material.IRON_BARS);
        setBlock(center, 1, 1, -6, Material.IRON_BARS);
    }

    private void buildEntranceLighting(Location center) {
        setBlock(center, -3, 1, -8, LIGHT_SOURCE);
        setBlock(center, 3, 1, -8, LIGHT_SOURCE);
    }

    private void buildMainCrypt(Location center) {
        buildCryptFloor(center);
        buildCryptWalls(center);
        buildCryptCeiling(center);
    }

    private void buildCryptFloor(Location center) {
        for (int x = -7; x <= 7; x++) {
            for (int z = -6; z <= 8; z++) {
                // Limpa espaço subterrâneo
                for (int y = -3; y <= -1; y++) {
                    setBlock(center, x, y, z, Material.AIR);
                }

                // Piso padronizado
                Material floorMat = (x + z) % 3 == 0 ? FLOOR_SECONDARY : FLOOR_PRIMARY;
                setBlock(center, x, 0, z, floorMat);
            }
        }
    }

    private void buildCryptWalls(Location center) {
        for (int y = 1; y <= 5; y++) {
            // Parede norte (com entrada)
            for (int x = -7; x <= 7; x++) {
                if (x >= -1 && x <= 1 && y <= 3) continue; // Entrada maior
                setBlock(center, x, y, -6, WALL_MATERIAL);
            }

            // Parede sul
            for (int x = -7; x <= 7; x++) {
                setBlock(center, x, y, 8, WALL_MATERIAL);
            }

            // Parede leste
            for (int z = -6; z <= 8; z++) {
                setBlock(center, 7, y, z, WALL_MATERIAL);
            }

            // Parede oeste
            for (int z = -6; z <= 8; z++) {
                setBlock(center, -7, y, z, WALL_MATERIAL);
            }
        }
    }

    private void buildCryptCeiling(Location center) {
        for (int x = -7; x <= 7; x++) {
            for (int z = -6; z <= 8; z++) {
                Material ceilingMat = (x + z) % 4 == 0 ? FLOOR_SECONDARY : WALL_MATERIAL;
                setBlock(center, x, CRYPT_CEILING, z, ceilingMat);
            }
        }
    }

    private void buildPillars(Location center) {
        int[][] pillarPositions = {
                {-4, -3}, {4, -3}, {-4, 2}, {4, 2}, {-4, 6}, {4, 6}
        };

        for (int[] pos : pillarPositions) {
            int x = pos[0];
            int z = pos[1];

            // Corpo do pilar
            for (int y = 1; y <= 4; y++) {
                setBlock(center, x, y, z, PILLAR_MATERIAL);
            }

            // Topo decorado
            setBlock(center, x, 5, z, Material.CHISELED_POLISHED_BLACKSTONE);

            // Base reforçada
            setBlock(center, x, 0, z, Material.POLISHED_BLACKSTONE_BRICKS);
        }
    }

    private void buildSarcophagi(Location center) {
        int[][] sarcophagusPositions = {
                {-5, -1}, {5, -1}, {-5, 4}, {5, 4}
        };

        for (int[] pos : sarcophagusPositions) {
            int x = pos[0];
            int z = pos[1];
            buildSarcophagus(center, x, z);
        }
    }

    private void buildSarcophagus(Location center, int x, int z) {
        // Base do sarcófago
        setBlock(center, x, 1, z - 1, Material.POLISHED_BLACKSTONE_STAIRS);
        setBlock(center, x, 1, z, Material.CHISELED_STONE_BRICKS);
        setBlock(center, x, 1, z + 1, Material.POLISHED_BLACKSTONE_STAIRS);

        // Tampa decorativa
        setBlock(center, x, 2, z, Material.STONE_BRICK_SLAB);

        // Conteúdo dos sarcófagos (podem ter loot)
        setBlock(center, x, 0, z, Material.BARREL);
    }

    private void buildAltar(Location center) {
        // Base do altar
        for (int x = -1; x <= 1; x++) {
            for (int z = 2; z <= 4; z++) {
                setBlock(center, x, 1, z, Material.POLISHED_BLACKSTONE_BRICKS);
            }
        }

        // Centro do altar (spawn de mobs)
        setBlock(center, 0, 2, 3, Material.CRYING_OBSIDIAN);

        // Fogueiras rituais
        setBlock(center, -1, 2, 2, Material.SOUL_CAMPFIRE);
        setBlock(center, 1, 2, 2, Material.SOUL_CAMPFIRE);
        setBlock(center, -1, 2, 4, Material.SOUL_CAMPFIRE);
        setBlock(center, 1, 2, 4, Material.SOUL_CAMPFIRE);

        // Oferecimentos no altar
        setBlock(center, -1, 2, 3, Material.GOLDEN_SWORD);
        setBlock(center, 1, 2, 3, Material.BOW);
    }

    private void buildSecretAreas(Location center) {
        // Passagem secreta atrás da parede sul
        setBlock(center, 0, 1, 8, Material.AIR);
        setBlock(center, 0, 2, 8, Material.AIR);

        // Sala secreta
        for (int x = -1; x <= 1; x++) {
            for (int z = 9; z <= 11; z++) {
                for (int y = 1; y <= 3; y++) {
                    setBlock(center, x, y, z, Material.AIR);
                }
                setBlock(center, x, 0, z, Material.POLISHED_BLACKSTONE_BRICKS);
            }
        }

        // Baú secreto
        setBlock(center, 0, 1, 10, Material.CHEST);

        // Iluminação secreta
        setBlock(center, 0, 3, 10, LIGHT_SOURCE);
    }

    private void buildLighting(Location center) {
        int[][] lightPositions = {
                {-6, -4}, {6, -4}, {-6, 1}, {6, 1}, {-6, 7}, {6, 7}
        };

        for (int[] pos : lightPositions) {
            setBlock(center, pos[0], 3, pos[1], LIGHT_SOURCE);
        }

        // Iluminação adicional no teto
        for (int x = -6; x <= 6; x += 4) {
            for (int z = -4; z <= 6; z += 3) {
                setBlock(center, x, CRYPT_CEILING - 1, z, Material.CHAIN);
                setBlock(center, x, CRYPT_CEILING, z, LIGHT_SOURCE);
            }
        }
    }

    private void buildDecorations(Location center) {
        buildCobwebs(center);
        buildSkulls(center);
        buildWeaponRacks(center);
        buildBloodStains(center);
    }

    private void buildCobwebs(Location center) {
        int[][] webPositions = {
                {-7, 5, -3}, {7, 5, 3}, {-2, 5, 8}, {2, 5, -6},
                {-5, 4, 1}, {5, 4, 5}, {0, 4, 0}, {0, 4, 6}
        };

        for (int[] pos : webPositions) {
            setBlock(center, pos[0], pos[1], pos[2], Material.COBWEB);
        }
    }

    private void buildSkulls(Location center) {
        int[][] skullPositions = {
                {-6, 2, 0}, {6, 2, 0}, {-6, 2, 6}, {6, 2, 6},
                {-3, 1, -5}, {3, 1, -5}, {0, 1, 7}
        };

        for (int[] pos : skullPositions) {
            setBlock(center, pos[0], pos[1], pos[2], Material.SKELETON_SKULL);
        }
    }

    private void buildWeaponRacks(Location center) {
        // Suportes de armas nas paredes
        int[][] rackPositions = {
                {-7, 2, -2}, {-7, 2, 3}, {7, 2, -2}, {7, 2, 3}
        };

        for (int[] pos : rackPositions) {
            setBlock(center, pos[0], pos[1], pos[2], Material.ITEM_FRAME);
            setBlock(center, pos[0], pos[1] - 1, pos[2], Material.OAK_FENCE);
        }
    }

    private void buildBloodStains(Location center) {
        // Manchas de "sangue" no chão
        int[][] bloodPositions = {
                {-3, 0, 1}, {2, 0, -2}, {4, 0, 5}, {-5, 0, 3}
        };

        for (int[] pos : bloodPositions) {
            setBlock(center, pos[0], pos[1], pos[2], Material.REDSTONE_BLOCK);
            // Redstone wire para efeito de respingo
            setBlock(center, pos[0] + 1, pos[1], pos[2], Material.REDSTONE_WIRE);
            setBlock(center, pos[0] - 1, pos[1], pos[2], Material.REDSTONE_WIRE);
        }
    }

    private void buildTraps(Location center) {
        buildPressurePlateTrap(center, -3, 0, 4);
        buildPressurePlateTrap(center, 3, 0, 4);
        buildArrowTrap(center, 0, 3, -5);
    }

    private void buildPressurePlateTrap(Location center, int x, int y, int z) {
        setBlock(center, x, y, z, Material.STONE_PRESSURE_PLATE);
        setBlock(center, x, y - 1, z, Material.REDSTONE_BLOCK);
        // Armadilha conectada (poderia ser TNT ou dispensers com flechas)
        setBlock(center, x, y, z + 1, Material.TRIPWIRE_HOOK);
        setBlock(center, x + 1, y, z + 1, Material.TRIPWIRE);
        setBlock(center, x - 1, y, z + 1, Material.TRIPWIRE);
    }

    private void buildArrowTrap(Location center, int x, int y, int z) {
        // Dispenser escondido na parede
        setBlock(center, x, y, z, Material.DISPENSER);
        // Alvo no outro lado da sala
        setBlock(center, x, 1, z + 8, Material.TARGET);
    }

    private void buildStorage(Location center) {
        // Baús principais
        setBlock(center, 0, 1, 7, Material.CHEST);
        setBlock(center, -6, 1, -4, Material.CHEST);
        setBlock(center, 6, 1, 7, Material.BARREL);

        // Baú secreto adicional
        setBlock(center, -7, 1, 0, Material.ENDER_CHEST);
    }
}