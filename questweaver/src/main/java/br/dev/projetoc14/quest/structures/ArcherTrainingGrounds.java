package br.dev.projetoc14.quest.structures;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;

/**
 * Campo de treinamento para arqueiros (versão completa)
 * Estrutura: Torre de observação alta com campo de tiro completo
 */
public class ArcherTrainingGrounds extends QuestStructure {

    // Constantes para melhor manutenção
    private static final int TOWER_SIZE = 5;
    private static final int TOWER_HEIGHT = 9;
    private static final int PLATFORM_HEIGHT = 10;
    private static final int FIELD_RADIUS = 10;

    // Materiais reutilizáveis
    private static final Material TOWER_WALL = Material.COBBLESTONE;
    private static final Material TOWER_CORNER = Material.STONE_BRICKS;
    private static final Material MAIN_FLOOR = Material.STONE_BRICKS;
    private static final Material FLOOR_PATTERN = Material.CRACKED_STONE_BRICKS;
    private static final Material FENCE = Material.OAK_FENCE;
    private static final Material PLATFORM_FLOOR = Material.OAK_PLANKS;
    private static final Material STAIRS = Material.OAK_STAIRS;

    public ArcherTrainingGrounds() {
        super("Campo de Treinamento de Arqueiros", 23, 12, 25);
    }

    @Override
    protected void build(Location center) {
        buildEntrance(center);
        buildMainFloor(center);
        buildCentralTower(center);
        buildTargets(center);
        buildShootingPlatforms(center);
        buildLighting(center);
        buildSupplies(center);
        buildPerimeter(center);
        buildRestArea(center);
        buildBowRacks(center);
        addDecorations(center);
    }

    private void buildEntrance(Location center) {
        // Caminho de entrada
        for (int z = -15; z < -10; z++) {
            for (int x = -1; x <= 1; x++) {
                setBlock(center, x, 0, z, Material.GRAVEL);
            }
            // Cercas laterais do caminho
            buildFenceColumn(center, -2, z);
            buildFenceColumn(center, 2, z);
        }

        // Portão de entrada
        buildGatePillar(center, -2, -10);
        buildGatePillar(center, 2, -10);

        // Lintel do portão
        setBlock(center, -1, 3, -10, Material.OAK_SLAB);
        setBlock(center, 0, 3, -10, Material.OAK_PLANKS);
        setBlock(center, 1, 3, -10, Material.OAK_SLAB);

        // Lanternas decorativas no portão
        setBlock(center, -2, 2, -11, Material.LANTERN);
        setBlock(center, 2, 2, -11, Material.LANTERN);
    }

    private void buildMainFloor(Location center) {
        for (int x = -FIELD_RADIUS; x <= FIELD_RADIUS; x++) {
            for (int z = -FIELD_RADIUS; z <= FIELD_RADIUS; z++) {
                Material floorMaterial = MAIN_FLOOR;

                // Área central da torre
                if (Math.abs(x) <= 2 && Math.abs(z) <= 2) {
                    floorMaterial = Material.STONE_BRICKS;
                }
                // Padrão decorativo
                else if (x % 3 == 0 || z % 3 == 0) {
                    floorMaterial = FLOOR_PATTERN;
                }

                setBlock(center, x, 0, z, floorMaterial);
            }
        }
    }

    private void buildCentralTower(Location center) {
        // Paredes da torre
        for (int y = 1; y <= TOWER_HEIGHT; y++) {
            buildTowerLevel(center, y);
        }

        // Entrada da torre
        setBlock(center, 0, 1, -2, Material.AIR);
        setBlock(center, 0, 2, -2, Material.AIR);

        // Escadas internas
        buildInternalStairs(center);

        // Janelas (seteiras)
        createArrowSlits(center);

        // Varandas nos níveis superiores
        buildBalconies(center);

        // Plataforma intermediária
        buildPlatform(center, -1, 1, 5, 3, 3, PLATFORM_FLOOR);

        // Topo da torre
        buildPlatform(center, -2, 2, PLATFORM_HEIGHT, TOWER_SIZE, TOWER_SIZE, PLATFORM_FLOOR);
        buildBattlements(center);
    }

    private void buildTowerLevel(Location center, int y) {
        int halfSize = TOWER_SIZE / 2;

        // Paredes norte e sul
        for (int x = -halfSize; x <= halfSize; x++) {
            setBlock(center, x, y, -halfSize, TOWER_WALL);
            setBlock(center, x, y, halfSize, TOWER_WALL);
        }

        // Paredes leste e oeste
        for (int z = -halfSize; z <= halfSize; z++) {
            setBlock(center, -halfSize, y, z, TOWER_WALL);
            setBlock(center, halfSize, y, z, TOWER_WALL);
        }

        // Reforço nos cantos a cada 2 níveis
        if (y % 2 == 0) {
            setBlock(center, -halfSize, y, -halfSize, TOWER_CORNER);
            setBlock(center, halfSize, y, -halfSize, TOWER_CORNER);
            setBlock(center, -halfSize, y, halfSize, TOWER_CORNER);
            setBlock(center, halfSize, y, halfSize, TOWER_CORNER);
        }
    }

    private void buildInternalStairs(Location center) {
        // Escada em espiral no canto noroeste
        int[] stairYLevels = {1, 2, 3, 4, 5, 6, 7, 8};
        BlockFace[] stairDirections = {
                BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST, BlockFace.NORTH,
                BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST, BlockFace.NORTH
        };

        int[][] stairPositions = {
                {-1, -1}, {-1, -1}, {-1, 0}, {-1, 1},
                {0, 1}, {1, 1}, {1, 0}, {1, -1}
        };

        for (int i = 0; i < stairYLevels.length; i++) {
            int x = stairPositions[i][0];
            int z = stairPositions[i][1];
            int y = stairYLevels[i];

            // Escada
            setBlock(center, x, y, z, STAIRS);
            // Bloco de suporte abaixo
            setBlock(center, x, y-1, z, Material.OAK_PLANKS);
        }

        // Abertura no topo da escada
        setBlock(center, 1, 9, -1, Material.AIR);
    }

    private void buildBalconies(Location center) {
        // Varanda no nível 4 (leste)
        buildBalcony(center, 2, 0, 4, 3, 1, BlockFace.EAST);

        // Varanda no nível 7 (oeste)
        buildBalcony(center, -2, 0, 7, 3, 1, BlockFace.WEST);
    }

    private void buildBalcony(Location center, int startX, int startZ, int y, int width, int depth, BlockFace direction) {
        // Plataforma da varanda
        for (int x = startX; x < startX + width * getDirectionMultiplierX(direction); x += getDirectionMultiplierX(direction)) {
            for (int z = startZ; z < startZ + depth * getDirectionMultiplierZ(direction); z += getDirectionMultiplierZ(direction)) {
                setBlock(center, x, y, z, PLATFORM_FLOOR);
                // Suportes abaixo
                setBlock(center, x, y-1, z, FENCE);
            }
        }

        // Cerca de proteção
        for (int i = 0; i < width; i++) {
            int x = startX + i * getDirectionMultiplierX(direction);
            int z = startZ + depth * getDirectionMultiplierZ(direction);
            setBlock(center, x, y+1, z, FENCE);
        }
    }

    private int getDirectionMultiplierX(BlockFace direction) {
        return direction == BlockFace.EAST ? 1 : direction == BlockFace.WEST ? -1 : 0;
    }

    private int getDirectionMultiplierZ(BlockFace direction) {
        return direction == BlockFace.SOUTH ? 1 : direction == BlockFace.NORTH ? -1 : 0;
    }

    private void createArrowSlits(Location center) {
        // Janelas no nível 3
        setBlock(center, 0, 3, -2, Material.AIR);
        setBlock(center, 0, 3, 2, Material.AIR);
        setBlock(center, -2, 3, 0, Material.AIR);
        setBlock(center, 2, 3, 0, Material.AIR);

        // Janelas no nível 6
        setBlock(center, 0, 6, -2, Material.AIR);
        setBlock(center, 0, 6, 2, Material.AIR);
        setBlock(center, -2, 6, 0, Material.AIR);
        setBlock(center, 2, 6, 0, Material.AIR);
    }

    private void buildBattlements(Location center) {
        int halfSize = TOWER_SIZE / 2;

        // Ameias norte e sul
        for (int x = -halfSize; x <= halfSize; x++) {
            if (x % 2 == 0) {
                setBlock(center, x, PLATFORM_HEIGHT + 1, -halfSize, Material.COBBLESTONE_WALL);
                setBlock(center, x, PLATFORM_HEIGHT + 1, halfSize, Material.COBBLESTONE_WALL);
            }
        }

        // Ameias leste e oeste
        for (int z = -halfSize; z <= halfSize; z++) {
            if (z % 2 == 0) {
                setBlock(center, -halfSize, PLATFORM_HEIGHT + 1, z, Material.COBBLESTONE_WALL);
                setBlock(center, halfSize, PLATFORM_HEIGHT + 1, z, Material.COBBLESTONE_WALL);
            }
        }

        // Mastro para bandeira
        setBlock(center, 0, PLATFORM_HEIGHT + 1, 0, Material.OAK_FENCE);
        setBlock(center, 0, PLATFORM_HEIGHT + 2, 0, Material.OAK_FENCE);
        setBlock(center, 0, PLATFORM_HEIGHT + 3, 0, Material.OAK_FENCE);
    }

    private void buildTargets(Location center) {
        // Alvos próximos (nível 1)
        buildTarget(center, -5, -5, 1);
        buildTarget(center, 5, -5, 1);
        buildTarget(center, -5, 5, 1);
        buildTarget(center, 5, 5, 1);

        // Alvos médios (nível 2)
        buildTarget(center, -8, 0, 2);
        buildTarget(center, 8, 0, 2);

        // Alvos distantes (nível 3)
        buildTarget(center, 0, -9, 3);
        buildTarget(center, 0, 9, 3);
    }

    private void buildTarget(Location center, int x, int z, int height) {
        // Base de palha
        for (int y = 1; y <= height; y++) {
            setBlock(center, x, y, z, Material.HAY_BLOCK);
        }
        // Alvo no topo
        setBlock(center, x, height + 1, z, Material.TARGET);
    }

    private void buildShootingPlatforms(Location center) {
        buildShootingPlatform(center, -8, -8); // NE
        buildShootingPlatform(center, 8, -8);  // NW
        buildShootingPlatform(center, -8, 8);  // SE
        buildShootingPlatform(center, 8, 8);   // SW
    }

    private void buildShootingPlatform(Location center, int centerX, int centerZ) {
        // Plataforma 3x3
        buildPlatform(center, centerX - 1, centerZ - 1, 1, 3, 3, Material.SPRUCE_PLANKS);

        // Cercas de proteção
        setBlock(center, centerX, 2, centerZ - 1, FENCE);
        setBlock(center, centerX, 2, centerZ + 1, FENCE);
        setBlock(center, centerX - 1, 2, centerZ, FENCE);
        setBlock(center, centerX + 1, 2, centerZ, FENCE);

        // Tocha central
        setBlock(center, centerX, 2, centerZ, Material.TORCH);
    }

    private void buildLighting(Location center) {
        // Lanternas nos cantos da torre (nível 3)
        setBlock(center, -3, 3, -3, Material.LANTERN);
        setBlock(center, 3, 3, -3, Material.LANTERN);
        setBlock(center, -3, 3, 3, Material.LANTERN);
        setBlock(center, 3, 3, 3, Material.LANTERN);

        // Lanternas nos cantos da torre (nível 7)
        setBlock(center, -3, 7, -3, Material.LANTERN);
        setBlock(center, 3, 7, -3, Material.LANTERN);
        setBlock(center, -3, 7, 3, Material.LANTERN);
        setBlock(center, 3, 7, 3, Material.LANTERN);

        // Iluminação adicional no perímetro
        for (int x = -FIELD_RADIUS + 2; x <= FIELD_RADIUS - 2; x += 4) {
            setBlock(center, x, 2, -FIELD_RADIUS + 1, Material.TORCH);
            setBlock(center, x, 2, FIELD_RADIUS - 1, Material.TORCH);
        }
        for (int z = -FIELD_RADIUS + 2; z <= FIELD_RADIUS - 2; z += 4) {
            setBlock(center, -FIELD_RADIUS + 1, 2, z, Material.TORCH);
            setBlock(center, FIELD_RADIUS - 1, 2, z, Material.TORCH);
        }
    }

    private void buildSupplies(Location center) {
        // Baús na torre
        setBlock(center, 0, 1, 1, Material.CHEST); // Térreo
        setBlock(center, 0, 6, 1, Material.CHEST); // Meio
        setBlock(center, 0, 10, 0, Material.CHEST); // Topo

        // Barris nos postos de tiro
        setBlock(center, -8, 1, -8, Material.BARREL);
        setBlock(center, 8, 1, -8, Material.BARREL);
        setBlock(center, -8, 1, 8, Material.BARREL);
        setBlock(center, 8, 1, 8, Material.BARREL);

        // Suportes de armadura (decoração)
        setBlock(center, -1, 1, 0, Material.ARMOR_STAND);
        setBlock(center, 1, 1, 0, Material.ARMOR_STAND);
    }

    private void buildPerimeter(Location center) {
        // Cerca norte e sul
        for (int x = -FIELD_RADIUS; x <= FIELD_RADIUS; x++) {
            if (x % 2 == 0 && Math.abs(x) > 2) {
                setBlock(center, x, 1, -FIELD_RADIUS, FENCE);
                setBlock(center, x, 1, FIELD_RADIUS, FENCE);
            }
        }

        // Cerca leste e oeste
        for (int z = -FIELD_RADIUS; z <= FIELD_RADIUS; z++) {
            if (z % 2 == 0 && Math.abs(z) > 2) {
                setBlock(center, -FIELD_RADIUS, 1, z, FENCE);
                setBlock(center, FIELD_RADIUS, 1, z, FENCE);
            }
        }
    }

    private void buildRestArea(Location center) {
        // Área de descanso no canto sudeste
        int startX = 6;
        int startZ = 6;

        // Banco 1 (leste-oeste)
        setBlock(center, startX, 1, startZ, Material.SPRUCE_STAIRS);
        setBlock(center, startX + 1, 1, startZ, Material.SPRUCE_STAIRS);
        setBlock(center, startX + 2, 1, startZ, Material.SPRUCE_STAIRS);

        // Banco 2 (norte-sul)
        setBlock(center, startX, 1, startZ + 1, Material.SPRUCE_STAIRS);

        // Mesa central
        setBlock(center, startX + 1, 1, startZ + 1, Material.OAK_PRESSURE_PLATE);
        setBlock(center, startX + 1, 2, startZ + 1, Material.TORCH);

        // Luminária suspensa
        setBlock(center, startX + 1, 4, startZ + 1, Material.OAK_FENCE);
        setBlock(center, startX + 1, 5, startZ + 1, Material.LANTERN);
    }

    private void buildBowRacks(Location center) {
        // Suportes para arcos nas paredes internas da torre
        int[][] rackPositions = {
                {-2, 0, 2},  // Parede oeste
                {2, 0, 2},   // Parede leste
                {0, 0, -2},  // Parede norte
                {0, 0, 2}    // Parede sul
        };

        for (int[] pos : rackPositions) {
            int x = pos[0];
            int y = pos[1];
            int z = pos[2];

            // Suporte inferior
            setBlock(center, x, y + 1, z, Material.OAK_FENCE);
            // Item frame para arco
            setBlock(center, x, y + 2, z, Material.ITEM_FRAME);
        }

        // Suportes adicionais nos postos de tiro
        setBlock(center, -8, 2, -7, Material.ITEM_FRAME);
        setBlock(center, 8, 2, -7, Material.ITEM_FRAME);
        setBlock(center, -8, 2, 7, Material.ITEM_FRAME);
        setBlock(center, 8, 2, 7, Material.ITEM_FRAME);
    }

    private void addDecorations(Location center) {
        // Bandeiras nos cantos do campo
        setBlock(center, -FIELD_RADIUS, 3, -FIELD_RADIUS, Material.OAK_FENCE);
        setBlock(center, -FIELD_RADIUS, 4, -FIELD_RADIUS, Material.WHITE_BANNER);

        setBlock(center, FIELD_RADIUS, 3, -FIELD_RADIUS, Material.OAK_FENCE);
        setBlock(center, FIELD_RADIUS, 4, -FIELD_RADIUS, Material.WHITE_BANNER);

        setBlock(center, -FIELD_RADIUS, 3, FIELD_RADIUS, Material.OAK_FENCE);
        setBlock(center, -FIELD_RADIUS, 4, FIELD_RADIUS, Material.WHITE_BANNER);

        setBlock(center, FIELD_RADIUS, 3, FIELD_RADIUS, Material.OAK_FENCE);
        setBlock(center, FIELD_RADIUS, 4, FIELD_RADIUS, Material.WHITE_BANNER);

        // Vasos com flores perto da entrada
        setBlock(center, -3, 1, -8, Material.FLOWER_POT);
        setBlock(center, 3, 1, -8, Material.FLOWER_POT);

        // Placas informativas
        setBlock(center, -1, 1, -9, Material.OAK_SIGN);
        setBlock(center, 1, 1, -9, Material.OAK_SIGN);
    }

    // ===== MÉTODOS AUXILIARES =====

    private void buildFenceColumn(Location center, int x, int z) {
        setBlock(center, x, 0, z, FENCE);
        setBlock(center, x, 1, z, FENCE);
    }

    private void buildGatePillar(Location center, int x, int z) {
        setBlock(center, x, 0, z, FENCE);
        setBlock(center, x, 1, z, FENCE);
        setBlock(center, x, 2, z, FENCE);
        setBlock(center, x, 3, z, Material.OAK_PLANKS);
    }

    private void buildPlatform(Location center, int startX, int startZ, int y, int width, int depth, Material material) {
        for (int x = startX; x < startX + width; x++) {
            for (int z = startZ; z < startZ + depth; z++) {
                setBlock(center, x, y, z, material);
            }
        }
    }
}