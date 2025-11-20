package br.dev.projetoc14.quest.structures;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.Barrel;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Inventory;

import java.util.Random;

/**
 * Cripta dos Assassinos - Versão Definitiva Mesclada
 * Combina a estrutura sólida da primeira versão com a estética dark da segunda
 */
public class AssassinCrypt extends QuestStructure {

    // Dimensões otimizadas
    private static final int CRYPT_WIDTH = 21;
    private static final int CRYPT_HEIGHT = 12;
    private static final int CRYPT_DEPTH = 25;
    private static final int CRYPT_CEILING = 7;
    private static final int CRYPT_FLOOR = 0;

    // Materiais mesclados - estética dark com variedade
    private static final Material WALL_MATERIAL = Material.DEEPSLATE_BRICKS;
    private static final Material FLOOR_PRIMARY = Material.DEEPSLATE_TILES;
    private static final Material FLOOR_SECONDARY = Material.CRACKED_DEEPSLATE_BRICKS;
    private static final Material PILLAR_MATERIAL = Material.POLISHED_BLACKSTONE;
    private static final Material DECORATIVE_MATERIAL = Material.CHISELED_DEEPSLATE;
    private static final Material LIGHT_SOURCE = Material.SOUL_LANTERN;
    private static final Material ROOF_MATERIAL = Material.BLACKSTONE;
    private static final Material EXTERIOR_MATERIAL = Material.DEEPSLATE;

    private Random random = new Random();

    public AssassinCrypt() {
        super("Cripta dos Assassinos", CRYPT_WIDTH, CRYPT_HEIGHT, CRYPT_DEPTH);
    }

    @Override
    protected void build(Location center) {
        // ORDEM OTIMIZADA: Exterior primeiro, depois interior
        clearArea(center);
        buildExteriorStructure(center);      // Colina e muros
        buildMainCrypt(center);              // Estrutura principal
        buildEnhancedPillars(center);        // Pilares detalhados
        buildSarcophagi(center);
        buildDarkAltar(center);              // Altar sombrio
        buildSecretAreas(center);
        buildEntranceStructure(center);      // Entrada elaborada
        buildAtmosphericLighting(center);    // Iluminação atmosférica
        buildEnhancedDecorations(center);    // Decoração completa
        buildTraps(center);
        buildStorage(center);

        debugStructure(center); // Remove depois de testar
    }

    private void clearArea(Location center) {
        // Limpa uma área maior para o exterior
        for (int x = -12; x <= 12; x++) {
            for (int z = -12; z <= 13; z++) {
                for (int y = -5; y <= 12; y++) {
                    setBlock(center, x, y, z, Material.AIR);
                }
                // Base sólida
                setBlock(center, x, -5, z, Material.DEEPSLATE);
            }
        }
    }

    private void buildExteriorStructure(Location center) {
        buildCryptMound(center);              // Colina natural
        buildRuinedWalls(center);             // Muros em ruínas
        buildDarkGardens(center);             // Jardins sombrios
        buildProcessionalPath(center);        // Caminho de entrada
    }

    private void buildCryptMound(Location center) {
        // Colina natural escura sobre a cripta
        for (int x = -10; x <= 10; x++) {
            for (int z = -9; z <= 11; z++) {
                double distance = Math.sqrt(x * x / 36.0 + z * z / 30.0);
                if (distance <= 1.2) {
                    int height = (int) ((1.2 - distance) * 5) + 1;
                    for (int y = 1; y <= height; y++) {
                        // Camadas da colina
                        if (y == height) {
                            setBlock(center, x, y, z, Material.COARSE_DIRT);
                        } else if (y >= height - 2) {
                            setBlock(center, x, y, z, Material.DIRT);
                        } else {
                            setBlock(center, x, y, z, Material.DEEPSLATE);
                        }
                    }

                    // Vegetação morta no topo
                    if (height > 3 && random.nextDouble() < 0.3) {
                        setBlock(center, x, height + 1, z, Material.DEAD_BUSH);
                    }
                }
            }
        }
    }

    private void buildRuinedWalls(Location center) {
        // Muros parcialmente destruídos ao redor
        buildCrumblingWall(center, -12, 1, -8, 16, true);  // Norte
        buildCrumblingWall(center, -12, 1, 10, 16, true);  // Sul
        buildCrumblingWall(center, -12, 1, -8, 18, false); // Oeste
        buildCrumblingWall(center, 12, 1, -8, 18, false);  // Leste

        // Torres de vigilância arruinadas
        buildRuinedTower(center, -11, 1, -7);
        buildRuinedTower(center, 11, 1, -7);
        buildRuinedTower(center, -11, 1, 9);
        buildRuinedTower(center, 11, 1, 9);
    }

    private void buildCrumblingWall(Location center, int startX, int startY, int startZ, int length, boolean horizontal) {
        for (int i = 0; i < length; i++) {
            int x = horizontal ? startX + i : startX;
            int z = horizontal ? startZ : startZ + i;

            // Parede com padrão de destruição
            if (random.nextDouble() > 0.2) { // 80% de chance de ter bloco
                setBlock(center, x, startY, z, Material.DEEPSLATE_BRICKS);
                if (random.nextDouble() > 0.4) { // 60% de chance de ter segundo nível
                    setBlock(center, x, startY + 1, z, Material.DEEPSLATE_BRICKS);
                    if (random.nextDouble() > 0.6) { // 40% de chance de ter terceiro nível
                        setBlock(center, x, startY + 2, z, Material.DEEPSLATE_BRICK_WALL);
                    }
                }
            }

            // Detalhes de destruição
            if (random.nextDouble() < 0.1) {
                setBlock(center, x, startY, z, Material.COBBLED_DEEPSLATE);
            }
        }
    }

    private void buildRuinedTower(Location center, int x, int y, int z) {
        // Base da torre
        for (int towerY = y; towerY <= y + 4; towerY++) {
            for (int dx = -1; dx <= 1; dx++) {
                for (int dz = -1; dz <= 1; dz++) {
                    if (Math.abs(dx) == 1 && Math.abs(dz) == 1) continue;
                    if (random.nextDouble() > 0.3) { // 70% de chance de ter bloco
                        setBlock(center, x + dx, towerY, z + dz, Material.DEEPSLATE_BRICKS);
                    }
                }
            }
        }

        // Topo arruinado
        if (random.nextBoolean()) {
            setBlock(center, x, y + 5, z, Material.DEEPSLATE_BRICK_WALL);
        }
    }

    private void buildDarkGardens(Location center) {
        // Árvores mortas e retorcidas
        buildGnarledTree(center, -8, 1, 5);
        buildGnarledTree(center, 8, 1, 5);
        buildGnarledTree(center, -8, 1, -5);
        buildGnarledTree(center, 8, 1, -5);

        // Arbustos mortos
        for (int i = 0; i < 12; i++) {
            int x = random.nextInt(24) - 12;
            int z = random.nextInt(22) - 11;
            if (Math.abs(x) > 8 || Math.abs(z) > 6) {
                setBlock(center, x, 1, z, Material.DEAD_BUSH);
            }
        }

        // Fungos sombrios
        for (int i = 0; i < 8; i++) {
            int x = random.nextInt(20) - 10;
            int z = random.nextInt(18) - 9;
            setBlock(center, x, 1, z, random.nextBoolean() ? Material.BROWN_MUSHROOM : Material.CRIMSON_FUNGUS);
        }
    }

    private void buildGnarledTree(Location center, int x, int y, int z) {
        // Tronco retorcido
        int height = 5 + random.nextInt(3);
        for (int i = 0; i < height; i++) {
            setBlock(center, x, y + i, z, Material.STRIPPED_DARK_OAK_LOG);
        }

        // Galhos retorcidos
        for (int i = 0; i < 3; i++) {
            int branchHeight = height - 2 + i;
            int branchLength = 2 + random.nextInt(2);

            for (int j = 1; j <= branchLength; j++) {
                int offsetX = random.nextInt(3) - 1;
                int offsetZ = random.nextInt(3) - 1;
                if (offsetX != 0 || offsetZ != 0) {
                    setBlock(center, x + offsetX * j, y + branchHeight, z + offsetZ * j,
                            Material.STRIPPED_DARK_OAK_LOG);
                }
            }
        }
    }

    private void buildProcessionalPath(Location center) {
        // Caminho sinuoso até a entrada
        int[] pathX = {0, 1, -1, 0, 1, -1, 0, 0, -1, 1, 0};
        int[] pathZ = {-15, -14, -13, -12, -11, -10, -9, -8, -7, -6, -5};

        for (int i = 0; i < pathX.length; i++) {
            int x = pathX[i];
            int z = pathZ[i];

            // Caminho principal
            setBlock(center, x, 0, z, Material.DEEPSLATE_BRICKS);
            setBlock(center, x, 0, z - 1, Material.COBBLED_DEEPSLATE);
            setBlock(center, x, 0, z + 1, Material.COBBLED_DEEPSLATE);

            // Postes de iluminação sinistros
            if (i % 3 == 0) {
                setBlock(center, x - 2, 1, z, Material.DEEPSLATE_BRICK_WALL);
                setBlock(center, x - 2, 2, z, Material.SOUL_LANTERN);
                setBlock(center, x + 2, 1, z, Material.DEEPSLATE_BRICK_WALL);
                setBlock(center, x + 2, 2, z, Material.SOUL_LANTERN);
            }
        }
    }

    private void buildMainCrypt(Location center) {
        buildCryptFloor(center);
        buildDetailedWalls(center);
        buildOrnateCeiling(center);
    }

    private void buildCryptFloor(Location center) {
        for (int x = -8; x <= 8; x++) {
            for (int z = -7; z <= 9; z++) {
                // Base sólida
                setBlock(center, x, -2, z, Material.DEEPSLATE);
                setBlock(center, x, -1, z, Material.DEEPSLATE);

                // Piso decorativo com padrão
                Material floorMat = (x + z) % 3 == 0 ? FLOOR_SECONDARY : FLOOR_PRIMARY;
                setBlock(center, x, 0, z, floorMat);
            }
        }
    }

    private void buildDetailedWalls(Location center) {
        // Padrão de paredes com detalhes
        for (int y = 1; y <= 6; y++) {
            for (int x = -8; x <= 8; x++) {
                for (int z = -7; z <= 9; z++) {
                    // Apenas nas bordas
                    if (Math.abs(x) == 8 || Math.abs(z) == 7 || z == 9) {
                        Material wallMaterial = WALL_MATERIAL;

                        // Padrão decorativo
                        if ((x + y) % 4 == 0) {
                            wallMaterial = Material.DEEPSLATE_TILES;
                        }
                        if ((z + y) % 3 == 0 && random.nextDouble() < 0.3) {
                            wallMaterial = Material.CRACKED_DEEPSLATE_BRICKS;
                        }

                        // Nichos para velas
                        if (y == 3 && random.nextDouble() < 0.1) {
                            setBlock(center, x, y, z, Material.AIR);
                            setBlock(center, x, y - 1, z, Material.BLACKSTONE_SLAB);
                            setBlock(center, x, y, z, Material.CANDLE);
                        } else {
                            setBlock(center, x, y, z, wallMaterial);
                        }
                    }
                }
            }
        }

        // Entrada principal
        for (int y = 1; y <= 4; y++) {
            for (int x = -2; x <= 2; x++) {
                setBlock(center, x, y, -7, Material.AIR);
            }
        }
    }

    private void buildOrnateCeiling(Location center) {
        // Teto com padrão ornamental
        for (int x = -8; x <= 8; x++) {
            for (int z = -7; z <= 9; z++) {
                Material ceilingMaterial = ROOF_MATERIAL;

                // Padrão de xadrez
                if ((x + z) % 3 == 0) {
                    ceilingMaterial = Material.POLISHED_BLACKSTONE;
                }
                // Bordas decorativas
                if (Math.abs(x) == 8 || Math.abs(z) == 7 || z == 9) {
                    ceilingMaterial = Material.CHISELED_DEEPSLATE;
                }

                setBlock(center, x, 7, z, ceilingMaterial);
            }
        }

        // Lustre central
        setBlock(center, 0, 7, 1, Material.CHAIN);
        setBlock(center, 0, 6, 1, Material.CHAIN);
        setBlock(center, 0, 5, 1, Material.SOUL_LANTERN);
    }

    private void buildEnhancedPillars(Location center) {
        int[][] pillarPositions = {
                {-6, -5}, {6, -5}, {-6, 3}, {6, 3}, {-6, 7}, {6, 7}
        };

        for (int[] pos : pillarPositions) {
            int x = pos[0];
            int z = pos[1];

            // Pilar decorado
            for (int y = 1; y <= 6; y++) {
                Material pillarMaterial = PILLAR_MATERIAL;

                // Padrão no pilar
                if (y % 2 == 0) {
                    pillarMaterial = Material.POLISHED_BLACKSTONE_BRICKS;
                }
                if (y == 3 || y == 5) {
                    setBlock(center, x, y, z, Material.CHAIN);
                } else {
                    setBlock(center, x, y, z, pillarMaterial);
                }
            }

            // Base e topo ornamentados
            setBlock(center, x, 0, z, Material.POLISHED_BLACKSTONE_BRICKS);
            setBlock(center, x, 7, z, Material.CHISELED_POLISHED_BLACKSTONE);

            // Braços de suporte
            setBlock(center, x - 1, 4, z, Material.POLISHED_BLACKSTONE_STAIRS);
            setBlock(center, x + 1, 4, z, Material.POLISHED_BLACKSTONE_STAIRS);
            setBlock(center, x, 4, z - 1, Material.POLISHED_BLACKSTONE_STAIRS);
            setBlock(center, x, 4, z + 1, Material.POLISHED_BLACKSTONE_STAIRS);
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
        // Sarcófago dark
        setBlock(center, x, 1, z - 1, Material.POLISHED_BLACKSTONE_STAIRS);
        setBlock(center, x, 1, z, Material.CHISELED_DEEPSLATE);
        setBlock(center, x, 1, z + 1, Material.POLISHED_BLACKSTONE_STAIRS);
        setBlock(center, x, 2, z, Material.BLACKSTONE_SLAB);
        setBlock(center, x, 0, z, Material.BARREL);

        // Decoração sinistra
        if (random.nextBoolean()) {
            setBlock(center, x - 1, 1, z, Material.SKELETON_SKULL);
        }
        if (random.nextBoolean()) {
            setBlock(center, x + 1, 1, z, Material.CANDLE);
        }
    }

    private void buildDarkAltar(Location center) {
        // Base do altar com degraus
        for (int step = 0; step < 3; step++) {
            int size = 5 - step;
            for (int x = -size; x <= size; x++) {
                for (int z = 1 + step; z <= 5 - step; z++) {
                    setBlock(center, x, step, z, Material.POLISHED_BLACKSTONE_BRICKS);
                }
            }
        }

        // Altar principal
        for (int x = -2; x <= 2; x++) {
            for (int z = 2; z <= 4; z++) {
                setBlock(center, x, 3, z, Material.BLACKSTONE);
            }
        }

        // Centro do altar
        setBlock(center, 0, 4, 3, Material.CRYING_OBSIDIAN);

        // Runas ao redor
        setBlock(center, -1, 3, 2, Material.END_STONE_BRICK_SLAB);
        setBlock(center, 1, 3, 2, Material.END_STONE_BRICK_SLAB);
        setBlock(center, -1, 3, 4, Material.END_STONE_BRICK_SLAB);
        setBlock(center, 1, 3, 4, Material.END_STONE_BRICK_SLAB);

        // Fogueiras rituais
        setBlock(center, -2, 4, 2, Material.SOUL_CAMPFIRE);
        setBlock(center, 2, 4, 2, Material.SOUL_CAMPFIRE);
        setBlock(center, -2, 4, 4, Material.SOUL_CAMPFIRE);
        setBlock(center, 2, 4, 4, Material.SOUL_CAMPFIRE);

        // Oferecimentos sombrios
        setBlock(center, -1, 4, 3, Material.IRON_SWORD);
        setBlock(center, 1, 4, 3, Material.BOW);
        setBlock(center, 0, 4, 2, Material.POTION);
        setBlock(center, 0, 4, 4, Material.ENCHANTED_BOOK);

        // Velas ritualísticas
        for (int x = -3; x <= 3; x += 6) {
            for (int z = 1; z <= 5; z += 4) {
                setBlock(center, x, 4, z, Material.CANDLE);
                setBlock(center, x, 3, z, Material.BLACKSTONE_SLAB);
            }
        }
    }

    private void buildEntranceStructure(Location center) {
        buildEntranceStairs(center);
        buildEntranceGate(center);
        buildEntranceGuardians(center);
    }

    private void buildEntranceStairs(Location center) {
        // Escadas descendentes para a cripta
        for (int z = -7; z <= -4; z++) {
            int stepHeight = -1 - (-7 - z); // -1, 0, 1, 2

            for (int x = -4; x <= 4; x++) {
                setBlock(center, x, stepHeight, z, Material.DEEPSLATE_BRICKS);

                // Corrimãos
                if (Math.abs(x) == 4) {
                    setBlock(center, x, stepHeight + 1, z, Material.DEEPSLATE_BRICK_WALL);
                    if (stepHeight > 0) {
                        setBlock(center, x, stepHeight + 2, z, Material.DEEPSLATE_BRICK_WALL);
                    }
                }
            }

            // Iluminação
            if (z % 2 == 0) {
                setBlock(center, -3, stepHeight + 1, z, LIGHT_SOURCE);
                setBlock(center, 3, stepHeight + 1, z, LIGHT_SOURCE);
            }
        }
    }

    private void buildEntranceGate(Location center) {
        // Pilares do portão
        for (int y = -1; y <= 4; y++) {
            setBlock(center, -3, y, -8, DECORATIVE_MATERIAL);
            setBlock(center, 3, y, -8, DECORATIVE_MATERIAL);
        }

        // Arco
        setBlock(center, -2, 4, -8, DECORATIVE_MATERIAL);
        setBlock(center, -1, 4, -8, DECORATIVE_MATERIAL);
        setBlock(center, 0, 4, -8, DECORATIVE_MATERIAL);
        setBlock(center, 1, 4, -8, DECORATIVE_MATERIAL);
        setBlock(center, 2, 4, -8, DECORATIVE_MATERIAL);

        // Portas
        setBlock(center, -1, 1, -8, Material.DARK_OAK_DOOR);
        setBlock(center, -1, 2, -8, Material.DARK_OAK_DOOR);
        setBlock(center, 1, 1, -8, Material.DARK_OAK_DOOR);
        setBlock(center, 1, 2, -8, Material.DARK_OAK_DOOR);

        // Decoração
        setBlock(center, -3, 3, -8, Material.SKELETON_SKULL);
        setBlock(center, 3, 3, -8, Material.SKELETON_SKULL);
    }

    private void buildEntranceGuardians(Location center) {
        // Estátuas guardiãs
        buildGuardianStatue(center, -6, 0, -6);
        buildGuardianStatue(center, 6, 0, -6);
    }

    private void buildGuardianStatue(Location center, int x, int y, int z) {
        setBlock(center, x, y, z, Material.POLISHED_BLACKSTONE_BRICKS);
        setBlock(center, x, y + 1, z, Material.POLISHED_BLACKSTONE);
        setBlock(center, x, y + 2, z, Material.POLISHED_BLACKSTONE);
        setBlock(center, x, y + 3, z, Material.SKELETON_SKULL);
        setBlock(center, x, y + 2, z + 1, Material.IRON_SWORD);
    }

    private void buildSecretAreas(Location center) {
        // Passagem secreta sul
        setBlock(center, 0, 1, 9, Material.AIR);
        setBlock(center, 0, 2, 9, Material.AIR);

        // Sala secreta
        for (int x = -1; x <= 1; x++) {
            for (int z = 10; z <= 12; z++) {
                for (int y = 1; y <= 3; y++) {
                    setBlock(center, x, y, z, Material.AIR);
                }
                setBlock(center, x, 0, z, Material.POLISHED_BLACKSTONE_BRICKS);
            }
        }

        // Tesouro secreto
        setBlock(center, 0, 1, 11, Material.CHEST);
        setBlock(center, -1, 1, 11, Material.ANVIL);
        setBlock(center, 1, 1, 11, Material.SMITHING_TABLE);
        setBlock(center, 0, 3, 11, LIGHT_SOURCE);
    }

    private void buildAtmosphericLighting(Location center) {
        // Iluminação estratégica para criar sombras
        int[][] lightPositions = {
                {-7, 4, -6}, {7, 4, -6}, {-7, 4, 8}, {7, 4, 8},
                {-7, 2, 1}, {7, 2, 1}, {0, 2, -6}, {0, 2, 8}
        };

        for (int[] pos : lightPositions) {
            setBlock(center, pos[0], pos[1], pos[2], LIGHT_SOURCE);
        }

        // Correntes com lanternas no teto
        for (int x = -6; x <= 6; x += 4) {
            for (int z = -4; z <= 6; z += 3) {
                for (int y = 6; y >= 4; y--) {
                    setBlock(center, x, y, z, Material.CHAIN);
                }
                setBlock(center, x, 7, z, LIGHT_SOURCE);
            }
        }

        // Tochas escondidas criando sombras
        for (int z = -6; z <= 8; z += 4) {
            setBlock(center, -8, 2, z, Material.SOUL_TORCH);
            setBlock(center, 8, 2, z, Material.SOUL_TORCH);
        }
    }

    private void buildEnhancedDecorations(Location center) {
        buildCobwebs(center);
        buildSkulls(center);
        buildWeaponRacks(center);
        buildBloodStains(center);
        buildChains(center);
        buildCandles(center);
        addCryptDetails(center);
        buildWallCarvings(center);
        buildBonePiles(center);
    }

    private void buildCobwebs(Location center) {
        int[][] webPositions = {
                {-8, 5, -3}, {8, 5, 3}, {-2, 5, 9}, {2, 5, -7},
                {-5, 4, 1}, {5, 4, 5}, {0, 4, 0}, {0, 4, 6}
        };

        for (int[] pos : webPositions) {
            if (random.nextDouble() < 0.7) {
                setBlock(center, pos[0], pos[1], pos[2], Material.COBWEB);
            }
        }
    }

    private void buildSkulls(Location center) {
        int[][] skullPositions = {
                {-7, 2, 0}, {7, 2, 0}, {-7, 2, 6}, {7, 2, 6},
                {-3, 1, -6}, {3, 1, -6}, {0, 1, 8}
        };

        for (int[] pos : skullPositions) {
            if (random.nextDouble() < 0.8) {
                setBlock(center, pos[0], pos[1], pos[2], Material.SKELETON_SKULL);
            }
        }
    }

    private void buildWeaponRacks(Location center) {
        int[][] rackPositions = {
                {-8, 2, -2}, {-8, 2, 3}, {8, 2, -2}, {8, 2, 3}
        };

        for (int[] pos : rackPositions) {
            setBlock(center, pos[0], pos[1], pos[2], Material.ITEM_FRAME);
            setBlock(center, pos[0], pos[1] - 1, pos[2], Material.DEEPSLATE_BRICK_WALL);
        }
    }

    private void buildBloodStains(Location center) {
        int[][] bloodPositions = {
                {-3, 0, 1}, {2, 0, -2}, {4, 0, 5}, {-5, 0, 3}
        };

        for (int[] pos : bloodPositions) {
            if (random.nextDouble() < 0.6) {
                setBlock(center, pos[0], pos[1], pos[2], Material.REDSTONE_BLOCK);
            }
        }
    }

    private void buildChains(Location center) {
        int[][] chainPositions = {
                {-5, 5, -3}, {5, 5, -3}, {-5, 5, 6}, {5, 5, 6}
        };

        for (int[] pos : chainPositions) {
            for (int y = pos[1]; y >= 3; y--) {
                setBlock(center, pos[0], y, pos[2], Material.CHAIN);
            }
            setBlock(center, pos[0], 2, pos[2], LIGHT_SOURCE);
        }
    }

    private void buildCandles(Location center) {
        int[][] candlePositions = {
                {-3, 2, 0}, {3, 2, 0}, {0, 2, -4}, {0, 2, 8}
        };

        for (int[] pos : candlePositions) {
            setBlock(center, pos[0], pos[1], pos[2], Material.CANDLE);
            setBlock(center, pos[0], pos[1] - 1, pos[2], Material.BLACKSTONE_SLAB);
        }
    }

    private void buildWallCarvings(Location center) {
        // Esculturas nas paredes
        int[][] carvingPositions = {
                {-8, 3, -3}, {-8, 3, 3}, {8, 3, -3}, {8, 3, 3},
                {-5, 3, -7}, {5, 3, -7}, {-5, 3, 9}, {5, 3, 9}
        };

        for (int[] pos : carvingPositions) {
            setBlock(center, pos[0], pos[1], pos[2], Material.ITEM_FRAME);
        }
    }

    private void buildBonePiles(Location center) {
        // Pilhas de ossos espalhadas
        int[][] bonePiles = {
                {-5, 1, -4}, {5, 1, -4}, {-5, 1, 6}, {5, 1, 6},
                {-3, 1, 0}, {3, 1, 0}, {0, 1, -2}, {0, 1, 6}
        };

        for (int[] pos : bonePiles) {
            if (random.nextDouble() < 0.7) {
                setBlock(center, pos[0], pos[1], pos[2], Material.BONE_BLOCK);
                if (random.nextBoolean()) {
                    setBlock(center, pos[0], pos[1] + 1, pos[2], Material.SKELETON_SKULL);
                }
            }
        }
    }

    private void addCryptDetails(Location center) {
        // Goteiras
        for (int i = 0; i < 5; i++) {
            int x = random.nextInt(16) - 8;
            int z = random.nextInt(16) - 7;
            setBlock(center, x, 7, z, Material.DRIPSTONE_BLOCK);
            setBlock(center, x, 6, z, Material.POINTED_DRIPSTONE);
        }

        // Fungos
        for (int i = 0; i < 6; i++) {
            int x = random.nextInt(16) - 8;
            int z = random.nextInt(16) - 7;
            setBlock(center, x, 1, z, random.nextBoolean() ? Material.BROWN_MUSHROOM : Material.RED_MUSHROOM);
        }
    }

    private void buildTraps(Location center) {
        // Armadilha de pressão
        setBlock(center, -3, 0, 4, Material.STONE_PRESSURE_PLATE);
        setBlock(center, -3, -1, 4, Material.REDSTONE_BLOCK);

        // Armadilha de poço
        for (int y = 0; y >= -3; y--) {
            setBlock(center, 2, y, 1, Material.AIR);
        }
        setBlock(center, 2, -4, 1, Material.CACTUS);
        setBlock(center, 2, -3, 1, Material.COBWEB);
    }

    private void buildStorage(Location center) {
        // Baús principais
        buildArrowChest(center, 0, 1, 8);
        buildArrowChest(center, -7, 1, -4);
        buildArrowChest(center, 7, 1, 8);

        // Baú secreto
        setBlock(center, -8, 1, 0, Material.ENDER_CHEST);

        // Barris
        setBlock(center, -5, 1, -4, Material.BARREL);
        setBlock(center, 5, 1, -4, Material.BARREL);
    }

    private void buildArrowChest(Location center, int x, int y, int z) {
        setBlock(center, x, y, z, Material.CHEST);

        Location chestLocation = getRelativeLocation(center, x, y, z);
        BlockState blockState = chestLocation.getBlock().getState();

        if (blockState instanceof Chest) {
            Chest chest = (Chest) blockState;
            Inventory inventory = chest.getInventory();
            addRandomArrowsToInventory(inventory);
            chest.update(true);
        }
    }

    private void addRandomArrowsToInventory(Inventory inventory) {
        inventory.clear();

        int arrowCount = getRandomArrowCount();
        int stacks = (int) Math.ceil(arrowCount / 64.0);

        for (int i = 0; i < stacks; i++) {
            int stackSize = Math.min(64, arrowCount - (i * 64));
            if (stackSize <= 0) break;

            ItemStack arrows = new ItemStack(Material.ARROW, stackSize);
            int slot = getRandomEmptySlot(inventory);
            if (slot != -1) {
                inventory.setItem(slot, arrows);
            }
        }

        // Itens extras
        if (random.nextDouble() < 0.4) {
            ItemStack poisonPotion = new ItemStack(Material.POTION, 1);
            int slot = getRandomEmptySlot(inventory);
            if (slot != -1) {
                inventory.setItem(slot, poisonPotion);
            }
        }
    }

    private int getRandomArrowCount() {
        double chance = random.nextDouble();
        if (chance < 0.5) return random.nextInt(9) + 8;
        else if (chance < 0.8) return random.nextInt(17) + 16;
        else if (chance < 0.95) return random.nextInt(17) + 32;
        else return random.nextInt(17) + 48;
    }

    private int getRandomEmptySlot(Inventory inventory) {
        for (int i = 0; i < 3; i++) {
            int slot = random.nextInt(inventory.getSize());
            if (inventory.getItem(slot) == null) {
                return slot;
            }
        }
        return -1;
    }

    private Location getRelativeLocation(Location center, int x, int y, int z) {
        return center.clone().add(x, y, z);
    }

    // Método de debug - REMOVA depois de testar
    private void debugStructure(Location center) {
        System.out.println("=== CRIPTA DEFINITIVA CONSTRUÍDA ===");
        System.out.println("Center: " + center);
        checkBlock(center, 0, 0, 0, "Centro da cripta");
        checkBlock(center, 0, 4, 3, "Altar");
        checkBlock(center, -5, 1, -1, "Sarcófago NW");
        checkBlock(center, 0, 1, 8, "Baú central");
        checkBlock(center, 0, 1, 11, "Baú secreto");
    }

    private void checkBlock(Location center, int x, int y, int z, String name) {
        Location loc = center.clone().add(x, y, z);
        Material material = loc.getBlock().getType();
        System.out.println(name + " (" + x + "," + y + "," + z + "): " + material);
    }
}