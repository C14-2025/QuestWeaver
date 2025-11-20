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
 * Cripta abandonada para assassinos (versão compatível com 1.21.8)
 */
public class AssassinCrypt extends QuestStructure {

    // Constantes para configuração
    private static final int CRYPT_WIDTH = 17;
    private static final int CRYPT_HEIGHT = 10;
    private static final int CRYPT_DEPTH = 22;
    private static final int CRYPT_CEILING = 6;
    private static final int CRYPT_FLOOR = -3;

    // Materiais compatíveis com 1.21.8
    private static final Material WALL_MATERIAL = Material.STONE_BRICKS;
    private static final Material FLOOR_PRIMARY = Material.STONE_BRICKS;
    private static final Material FLOOR_SECONDARY = Material.CRACKED_STONE_BRICKS;
    private static final Material PILLAR_MATERIAL = Material.POLISHED_BLACKSTONE;
    private static final Material DECORATIVE_MATERIAL = Material.CHISELED_STONE_BRICKS;
    private static final Material LIGHT_SOURCE = Material.SOUL_LANTERN;
    private static final Material EXTERIOR_MATERIAL = Material.STONE;

    private Random random = new Random();

    public AssassinCrypt() {
        super("Cripta dos Assassinos", CRYPT_WIDTH, CRYPT_HEIGHT, CRYPT_DEPTH);
    }

    @Override
    protected void build(Location center) {
        buildExteriorStructure(center);
        buildEntrance(center);
        buildMainCrypt(center);
        buildPillars(center);
        buildSarcophagi(center);
        buildAltar(center);
        buildSecretAreas(center);
        buildLighting(center);
        buildDecorations(center);
        buildTraps(center);
        buildStorage(center);
    }

    private void buildExteriorStructure(Location center) {
        // Colina natural cobrindo a cripta
        buildBurialMound(center);

        // Ruínas antigas no exterior
        buildExteriorRuins(center);

        // Caminho sinuoso até a entrada
        buildPathToEntrance(center);

        // Árvores mortas e vegetação sombria
        buildDeadVegetation(center);
    }

    private void buildBurialMound(Location center) {
        // Cria uma colina natural sobre a cripta
        for (int x = -9; x <= 9; x++) {
            for (int z = -8; z <= 10; z++) {
                // Forma ovalada da colina
                double distance = Math.sqrt(x * x / 25.0 + z * z / 20.0);
                if (distance <= 1.0) {
                    int height = (int) ((1.0 - distance) * 4) + 1;
                    for (int y = 1; y <= height; y++) {
                        setBlock(center, x, y, z, Material.GRASS_BLOCK);
                        if (y == height) {
                            // Topo com terra e pedra
                            setBlock(center, x, y, z, random.nextBoolean() ? Material.GRASS_BLOCK : Material.COARSE_DIRT);
                        } else if (y == height - 1) {
                            setBlock(center, x, y, z, Material.DIRT);
                        } else {
                            setBlock(center, x, y, z, Material.STONE);
                        }
                    }
                }
            }
        }
    }

    private void buildExteriorRuins(Location center) {
        // Pilares caídos
        buildFallenPillar(center, -12, 1, -5);
        buildFallenPillar(center, 11, 1, 8);
        buildFallenPillar(center, -8, 1, 12);

        // Muros parcialmente destruídos
        buildCrumblingWall(center, -10, 1, -7, 5, BlockFace.EAST);
        buildCrumblingWall(center, 9, 1, 3, 4, BlockFace.WEST);

        // Lápides e monumentos funerários
        buildGravestones(center);
    }

    private void buildFallenPillar(Location center, int x, int y, int z) {
        // Base do pilar
        setBlock(center, x, y, z, PILLAR_MATERIAL);
        setBlock(center, x, y+1, z, PILLAR_MATERIAL);

        // Pilar caído
        for (int i = 0; i < 3; i++) {
            setBlock(center, x + i + 1, y, z + i, PILLAR_MATERIAL);
        }
    }

    private void buildCrumblingWall(Location center, int startX, int startY, int startZ, int length, BlockFace direction) {
        for (int i = 0; i < length; i++) {
            if (random.nextDouble() > 0.3) { // 70% de chance de ter bloco (parede danificada)
                int x = startX + (direction == BlockFace.EAST ? i : 0);
                int z = startZ + (direction == BlockFace.SOUTH ? i : 0);
                setBlock(center, x, startY, z, WALL_MATERIAL);
                if (random.nextBoolean()) {
                    setBlock(center, x, startY + 1, z, WALL_MATERIAL);
                }
            }
        }
    }

    private void buildGravestones(Location center) {
        int[][] graves = {
                {-11, 1, -3}, {-9, 1, 11}, {8, 1, 11}, {10, 1, -4}
        };

        for (int[] grave : graves) {
            setBlock(center, grave[0], grave[1], grave[2], Material.STONE_BRICK_WALL);
            setBlock(center, grave[0], grave[1] + 1, grave[2], Material.SKELETON_SKULL);
        }
    }

    private void buildPathToEntrance(Location center) {
        // Caminho sinuoso de pedra quebrada
        int[] pathZ = {-15, -14, -13, -12, -11, -10, -9, -8};
        int[] pathX = {0, 1, 0, -1, 0, 1, 0, 0}; // Padrão sinuoso

        for (int i = 0; i < pathZ.length; i++) {
            int x = pathX[i];
            int z = pathZ[i];

            setBlock(center, x, 0, z, Material.COBBLESTONE);
            setBlock(center, x-1, 0, z, Material.GRAVEL);
            setBlock(center, x+1, 0, z, Material.GRAVEL);

            // Postes de iluminação ao longo do caminho
            if (i % 2 == 0) {
                setBlock(center, x, 1, z, Material.COBBLESTONE_WALL);
                setBlock(center, x, 2, z, LIGHT_SOURCE);
            }
        }
    }

    private void buildDeadVegetation(Location center) {
        // Árvores mortas
        buildDeadTree(center, -13, 1, -2);
        buildDeadTree(center, 12, 1, -1);
        buildDeadTree(center, -10, 1, 13);
        buildDeadTree(center, 9, 1, 14);

        // Arbustos e vegetação rasteira morta
        for (int i = 0; i < 8; i++) {
            int x = random.nextInt(20) - 10;
            int z = random.nextInt(20) - 8;
            if (Math.abs(x) > 8 || Math.abs(z) > 6) {
                setBlock(center, x, 1, z, Material.DEAD_BUSH);
            }
        }
    }

    private void buildDeadTree(Location center, int x, int y, int z) {
        // Tronco
        for (int i = 0; i < 4; i++) {
            setBlock(center, x, y + i, z, Material.STRIPPED_OAK_LOG);
        }

        // Galhos
        setBlock(center, x+1, y+3, z, Material.STRIPPED_OAK_LOG);
        setBlock(center, x-1, y+2, z, Material.STRIPPED_OAK_LOG);
        setBlock(center, x, y+3, z+1, Material.STRIPPED_OAK_LOG);
    }

    private void buildEntrance(Location center) {
        // Entrada reformulada - Templo subterrâneo em ruínas
        buildGrandEntrance(center);
        buildEntranceStairs(center);
        buildEntranceGuardians(center);
    }

    private void buildGrandEntrance(Location center) {
        // Estrutura monumental da entrada
        int entranceWidth = 5;
        int entranceHeight = 5;

        // Pilares laterais maciços
        for (int y = 0; y <= entranceHeight; y++) {
            for (int x = -entranceWidth - 1; x <= entranceWidth + 1; x++) {
                if (Math.abs(x) == entranceWidth + 1 || Math.abs(x) == entranceWidth) {
                    setBlock(center, x, y, -7, Material.STONE_BRICKS);
                }
            }
        }

        // Arco de entrada elaborado
        for (int x = -entranceWidth + 1; x <= entranceWidth - 1; x++) {
            setBlock(center, x, entranceHeight, -7, Material.STONE_BRICK_SLAB);
            setBlock(center, x, entranceHeight + 1, -7, Material.CHISELED_STONE_BRICKS);
        }

        // Portas duplas de madeira escura
        setBlock(center, -1, 1, -7, Material.DARK_OAK_DOOR);
        setBlock(center, -1, 2, -7, Material.DARK_OAK_DOOR);
        setBlock(center, 1, 1, -7, Material.DARK_OAK_DOOR);
        setBlock(center, 1, 2, -7, Material.DARK_OAK_DOOR);

        // Detalhes decorativos na entrada
        setBlock(center, -entranceWidth, 3, -7, Material.SKELETON_SKULL);
        setBlock(center, entranceWidth, 3, -7, Material.SKELETON_SKULL);
    }

    private void buildEntranceStairs(Location center) {
        // Escadaria descendente dramática
        for (int z = -6; z >= -3; z++) {
            int stepHeight = -6 + z; // -6, -5, -4, -3
            for (int x = -4; x <= 4; x++) {
                setBlock(center, x, stepHeight, z, Material.STONE_BRICKS);

                // Corrimãos
                if (Math.abs(x) == 4) {
                    for (int y = stepHeight + 1; y <= stepHeight + 2; y++) {
                        setBlock(center, x, y, z, Material.STONE_BRICK_WALL);
                    }
                }
            }
        }

        // Iluminação dramática nas escadas
        for (int z = -6; z <= -3; z += 2) {
            setBlock(center, -3, -5, z, LIGHT_SOURCE);
            setBlock(center, 3, -5, z, LIGHT_SOURCE);
        }
    }

    private void buildEntranceGuardians(Location center) {
        // Estátuas de guardiões na entrada
        buildGuardianStatue(center, -6, 0, -5);
        buildGuardianStatue(center, 6, 0, -5);
    }

    private void buildGuardianStatue(Location center, int x, int y, int z) {
        // Base da estátua
        setBlock(center, x, y, z, Material.POLISHED_BLACKSTONE_BRICKS);
        setBlock(center, x, y+1, z, Material.POLISHED_BLACKSTONE);
        setBlock(center, x, y+2, z, Material.POLISHED_BLACKSTONE);

        // Detalhes da estátua
        setBlock(center, x, y+3, z, Material.SKELETON_SKULL);
        setBlock(center, x, y+2, z+1, Material.IRON_SWORD);
    }

    private void buildMainCrypt(Location center) {
        buildCryptFloor(center);
        buildCryptWalls(center);
        buildCryptCeiling(center);
        addCryptDetails(center);
    }

    private void addCryptDetails(Location center) {
        // Goteiras e umidade
        for (int i = 0; i < 10; i++) {
            int x = random.nextInt(14) - 7;
            int z = random.nextInt(14) - 6;
            setBlock(center, x, 5, z, Material.DRIPSTONE_BLOCK);
            setBlock(center, x, 4, z, Material.POINTED_DRIPSTONE);
        }

        // Fungos e cogumelos
        for (int i = 0; i < 8; i++) {
            int x = random.nextInt(14) - 7;
            int z = random.nextInt(14) - 6;
            setBlock(center, x, 1, z, random.nextBoolean() ? Material.BROWN_MUSHROOM : Material.RED_MUSHROOM);
        }

        // Vasos quebrados
        for (int i = 0; i < 5; i++) {
            int x = random.nextInt(12) - 6;
            int z = random.nextInt(12) - 6;
            setBlock(center, x, 1, z, Material.FLOWER_POT);
        }
    }

    private void buildDecorations(Location center) {
        buildCobwebs(center);
        buildSkulls(center);
        buildWeaponRacks(center);
        buildBloodStains(center);
        buildChains(center);
        buildCandles(center);
    }

    private void buildChains(Location center) {
        // Correntes penduradas no teto
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
        // Velas espalhadas pela cripta
        int[][] candlePositions = {
                {-3, 2, 0}, {3, 2, 0}, {0, 2, -4}, {0, 2, 7},
                {-6, 2, 3}, {6, 2, 3}, {-6, 2, -2}, {6, 2, -2}
        };

        for (int[] pos : candlePositions) {
            setBlock(center, pos[0], pos[1], pos[2], Material.CANDLE);
            setBlock(center, pos[0], pos[1] - 1, pos[2], Material.BLACKSTONE_SLAB);
        }
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

                // Adiciona detalhes de poças e manchas
                if (random.nextDouble() < 0.1) {
                    setBlock(center, x, 1, z, Material.WATER);
                }
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

            // Correntes decorativas nos pilares
            if (random.nextBoolean()) {
                setBlock(center, x, 3, z, Material.CHAIN);
            }
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

        // Conteúdo dos sarcófagos
        setBlock(center, x, 0, z, Material.BARREL);

        // Decoração ao redor
        if (random.nextBoolean()) {
            setBlock(center, x - 1, 1, z, Material.SKELETON_SKULL);
        }
        if (random.nextBoolean()) {
            setBlock(center, x + 1, 1, z, Material.CANDLE);
        }
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
        setBlock(center, -1, 2, 3, Material.IRON_SWORD);
        setBlock(center, 1, 2, 3, Material.BOW);

        // Velas ao redor do altar
        setBlock(center, -2, 2, 2, Material.CANDLE);
        setBlock(center, 2, 2, 2, Material.CANDLE);
        setBlock(center, -2, 2, 4, Material.CANDLE);
        setBlock(center, 2, 2, 4, Material.CANDLE);
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

        // Decoração da sala secreta
        setBlock(center, -1, 1, 10, Material.ANVIL);
        setBlock(center, 1, 1, 10, Material.SMITHING_TABLE);
    }

    private void buildLighting(Location center) {
        int[][] lightPositions = {
                {-6, -4}, {6, -4}, {-6, 1}, {6, 1}, {-6, 7}, {6, 7}
        };

        for (int[] pos : lightPositions) {
            setBlock(center, pos[0], 3, pos[1], LIGHT_SOURCE);
        }

        // Iluminação adicional no teto com correntes
        for (int x = -6; x <= 6; x += 4) {
            for (int z = -4; z <= 6; z += 3) {
                setBlock(center, x, CRYPT_CEILING - 1, z, Material.CHAIN);
                setBlock(center, x, CRYPT_CEILING, z, LIGHT_SOURCE);
            }
        }

        // Tochas nas paredes
        for (int z = -5; z <= 7; z += 4) {
            setBlock(center, -7, 2, z, Material.SOUL_TORCH);
            setBlock(center, 7, 2, z, Material.SOUL_TORCH);
        }
    }

    private void buildCobwebs(Location center) {
        int[][] webPositions = {
                {-7, 5, -3}, {7, 5, 3}, {-2, 5, 8}, {2, 5, -6},
                {-5, 4, 1}, {5, 4, 5}, {0, 4, 0}, {0, 4, 6},
                {-3, 3, -5}, {3, 3, -5}, {-6, 2, 2}, {6, 2, -3}
        };

        for (int[] pos : webPositions) {
            if (random.nextDouble() < 0.7) { // 70% de chance de ter teia
                setBlock(center, pos[0], pos[1], pos[2], Material.COBWEB);
            }
        }
    }

    private void buildSkulls(Location center) {
        int[][] skullPositions = {
                {-6, 2, 0}, {6, 2, 0}, {-6, 2, 6}, {6, 2, 6},
                {-3, 1, -5}, {3, 1, -5}, {0, 1, 7}, {-7, 3, 4},
                {7, 3, -2}, {-4, 1, 7}, {4, 1, -4}
        };

        for (int[] pos : skullPositions) {
            if (random.nextDouble() < 0.8) { // 80% de chance de ter caveira
                setBlock(center, pos[0], pos[1], pos[2], Material.SKELETON_SKULL);
            }
        }
    }

    private void buildWeaponRacks(Location center) {
        // Suportes de armas nas paredes
        int[][] rackPositions = {
                {-7, 2, -2}, {-7, 2, 3}, {7, 2, -2}, {7, 2, 3},
                {-7, 3, 0}, {7, 3, 5}, {-5, 2, 8}, {5, 2, 8}
        };

        for (int[] pos : rackPositions) {
            setBlock(center, pos[0], pos[1], pos[2], Material.ITEM_FRAME);
            setBlock(center, pos[0], pos[1] - 1, pos[2], Material.STONE_BRICK_WALL);

            // Adiciona armas decorativas próximas
            if (random.nextBoolean()) {
                setBlock(center, pos[0], pos[1] - 1, pos[2] + 1, Material.IRON_SWORD);
            }
        }
    }

    private void buildBloodStains(Location center) {
        // Manchas de "sangue" no chão
        int[][] bloodPositions = {
                {-3, 0, 1}, {2, 0, -2}, {4, 0, 5}, {-5, 0, 3},
                {1, 0, -4}, {-2, 0, 6}, {5, 0, 0}, {-4, 0, -1}
        };

        for (int[] pos : bloodPositions) {
            if (random.nextDouble() < 0.6) { // 60% de chance de ter mancha
                setBlock(center, pos[0], pos[1], pos[2], Material.REDSTONE_BLOCK);
                // Redstone wire para efeito de respingo
                if (random.nextBoolean()) {
                    setBlock(center, pos[0] + 1, pos[1], pos[2], Material.REDSTONE_WIRE);
                }
                if (random.nextBoolean()) {
                    setBlock(center, pos[0] - 1, pos[1], pos[2], Material.REDSTONE_WIRE);
                }
            }
        }
    }

    private void buildTraps(Location center) {
        buildPressurePlateTrap(center, -3, 0, 4);
        buildPressurePlateTrap(center, 3, 0, 4);
        buildArrowTrap(center, 0, 3, -5);
        buildPitTrap(center, 2, 0, 1);
    }

    private void buildPressurePlateTrap(Location center, int x, int y, int z) {
        setBlock(center, x, y, z, Material.STONE_PRESSURE_PLATE);
        setBlock(center, x, y - 1, z, Material.REDSTONE_BLOCK);
        // Armadilha conectada
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

    private void buildPitTrap(Location center, int x, int y, int z) {
        // Poço com cactos no fundo
        for (int pitY = y; pitY >= y - 3; pitY--) {
            setBlock(center, x, pitY, z, Material.AIR);
        }
        setBlock(center, x, y - 4, z, Material.CACTUS);
        setBlock(center, x, y - 3, z, Material.COBWEB); // Para prender a vítima
    }

    private void buildStorage(Location center) {
        // Baús principais
        buildArrowChest(center, 0, 1, 7);
        buildArrowChest(center, -6, 1, -4);
        buildArrowChest(center, 6, 1, 7);

        // Baú secreto adicional
        setBlock(center, -7, 1, 0, Material.ENDER_CHEST);

        // Barris de suprimentos
        setBlock(center, -5, 1, -4, Material.BARREL);
        setBlock(center, 5, 1, -4, Material.BARREL);
    }

    private void buildArrowChest(Location center, int x, int y, int z) {
        setBlock(center, x, y, z, Material.CHEST);

        // Adiciona flechas aleatórias ao baú
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

        // Chance de adicionar outros itens de assassino
        if (random.nextDouble() < 0.4) {
            ItemStack poisonPotion = new ItemStack(Material.POTION, 1);
            int slot = getRandomEmptySlot(inventory);
            if (slot != -1) {
                inventory.setItem(slot, poisonPotion);
            }
        }

        if (random.nextDouble() < 0.3) {
            ItemStack dagger = new ItemStack(Material.IRON_SWORD, 1);
            int slot = getRandomEmptySlot(inventory);
            if (slot != -1) {
                inventory.setItem(slot, dagger);
            }
        }
    }

    private int getRandomArrowCount() {
        double chance = random.nextDouble();
        if (chance < 0.5) {
            return random.nextInt(9) + 8;
        } else if (chance < 0.8) {
            return random.nextInt(17) + 16;
        } else if (chance < 0.95) {
            return random.nextInt(17) + 32;
        } else {
            return random.nextInt(17) + 48;
        }
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
}