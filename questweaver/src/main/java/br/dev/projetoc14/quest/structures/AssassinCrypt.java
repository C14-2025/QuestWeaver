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
 * Cripta abandonada para assassinos - Versão Final Revisada
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
        // ORDEM CORRIGIDA: Limpeza -> Cripta -> Exterior
        clearArea(center);
        buildMainCrypt(center);
        buildPillars(center);
        buildSarcophagi(center);
        buildAltar(center);
        buildSecretAreas(center);
        buildEntranceStructure(center);
        buildLighting(center);
        buildDecorations(center);
        buildTraps(center);
        buildStorage(center);
        buildExteriorStructure(center);

        debugStructure(center); // Remove depois de testar
    }

    private void clearArea(Location center) {
        // Limpa uma área maior que a cripta
        for (int x = -10; x <= 10; x++) {
            for (int z = -10; z <= 12; z++) {
                for (int y = -5; y <= 10; y++) {
                    setBlock(center, x, y, z, Material.AIR);
                }
                // Base sólida
                setBlock(center, x, -5, z, Material.STONE);
            }
        }
    }

    private void buildMainCrypt(Location center) {
        buildCryptFloor(center);
        buildCryptWalls(center);
        buildCryptCeiling(center);
    }

    private void buildCryptFloor(Location center) {
        for (int x = -7; x <= 7; x++) {
            for (int z = -6; z <= 8; z++) {
                // Base sólida
                setBlock(center, x, -3, z, Material.STONE);
                setBlock(center, x, -2, z, Material.STONE);
                setBlock(center, x, -1, z, Material.STONE);

                // Piso decorativo
                Material floorMat = (x + z) % 3 == 0 ? FLOOR_SECONDARY : FLOOR_PRIMARY;
                setBlock(center, x, 0, z, floorMat);
            }
        }
    }

    private void buildCryptWalls(Location center) {
        // Paredes principais
        for (int y = 1; y <= 5; y++) {
            // Parede norte (com entrada)
            for (int x = -7; x <= 7; x++) {
                if (x >= -2 && x <= 2 && y <= 3) continue; // Entrada
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

        // Conteúdo
        setBlock(center, x, 0, z, Material.BARREL);

        // Decoração
        if (random.nextBoolean()) {
            setBlock(center, x - 1, 1, z, Material.SKELETON_SKULL);
        }
    }

    private void buildAltar(Location center) {
        // Base do altar
        for (int x = -1; x <= 1; x++) {
            for (int z = 2; z <= 4; z++) {
                setBlock(center, x, 1, z, Material.POLISHED_BLACKSTONE_BRICKS);
            }
        }

        // Centro do altar
        setBlock(center, 0, 2, 3, Material.CRYING_OBSIDIAN);

        // Fogueiras rituais
        setBlock(center, -1, 2, 2, Material.SOUL_CAMPFIRE);
        setBlock(center, 1, 2, 2, Material.SOUL_CAMPFIRE);
        setBlock(center, -1, 2, 4, Material.SOUL_CAMPFIRE);
        setBlock(center, 1, 2, 4, Material.SOUL_CAMPFIRE);

        // Oferecimentos
        setBlock(center, -1, 2, 3, Material.IRON_SWORD);
        setBlock(center, 1, 2, 3, Material.BOW);

        // Velas
        setBlock(center, -2, 2, 2, Material.CANDLE);
        setBlock(center, 2, 2, 2, Material.CANDLE);
        setBlock(center, -2, 2, 4, Material.CANDLE);
        setBlock(center, 2, 2, 4, Material.CANDLE);
    }

    private void buildEntranceStructure(Location center) {
        // Escadaria de entrada
        buildEntranceStairs(center);

        // Portão de entrada
        buildEntranceGate(center);

        // Guardiões
        buildEntranceGuardians(center);
    }

    private void buildEntranceStairs(Location center) {
        // Escadas descendentes para a cripta
        for (int z = -6; z <= -3; z++) {
            int stepHeight = -2 - (-6 - z); // -2, -1, 0, 1

            for (int x = -4; x <= 4; x++) {
                setBlock(center, x, stepHeight, z, Material.STONE_BRICKS);

                // Corrimãos
                if (Math.abs(x) == 4) {
                    setBlock(center, x, stepHeight + 1, z, Material.STONE_BRICK_WALL);
                    setBlock(center, x, stepHeight + 2, z, Material.STONE_BRICK_WALL);
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
        for (int y = -2; y <= 3; y++) {
            setBlock(center, -3, y, -7, DECORATIVE_MATERIAL);
            setBlock(center, 3, y, -7, DECORATIVE_MATERIAL);
        }

        // Arco
        setBlock(center, -2, 3, -7, DECORATIVE_MATERIAL);
        setBlock(center, -1, 3, -7, DECORATIVE_MATERIAL);
        setBlock(center, 0, 3, -7, DECORATIVE_MATERIAL);
        setBlock(center, 1, 3, -7, DECORATIVE_MATERIAL);
        setBlock(center, 2, 3, -7, DECORATIVE_MATERIAL);

        // Portas
        setBlock(center, -1, 0, -7, Material.DARK_OAK_DOOR);
        setBlock(center, -1, 1, -7, Material.DARK_OAK_DOOR);
        setBlock(center, 1, 0, -7, Material.DARK_OAK_DOOR);
        setBlock(center, 1, 1, -7, Material.DARK_OAK_DOOR);

        // Decoração
        setBlock(center, -3, 2, -7, Material.SKELETON_SKULL);
        setBlock(center, 3, 2, -7, Material.SKELETON_SKULL);
    }

    private void buildEntranceGuardians(Location center) {
        // Estátuas guardiãs
        buildGuardianStatue(center, -6, -1, -5);
        buildGuardianStatue(center, 6, -1, -5);
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

        // Tesouro secreto
        setBlock(center, 0, 1, 10, Material.CHEST);
        setBlock(center, -1, 1, 10, Material.ANVIL);
        setBlock(center, 1, 1, 10, Material.SMITHING_TABLE);
        setBlock(center, 0, 3, 10, LIGHT_SOURCE);
    }

    private void buildLighting(Location center) {
        // Iluminação principal
        int[][] lightPositions = {
                {-6, 3, -4}, {6, 3, -4}, {-6, 3, 1}, {6, 3, 1},
                {-6, 3, 7}, {6, 3, 7}
        };

        for (int[] pos : lightPositions) {
            setBlock(center, pos[0], pos[1], pos[2], LIGHT_SOURCE);
        }

        // Iluminação do teto
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

    private void buildDecorations(Location center) {
        buildCobwebs(center);
        buildSkulls(center);
        buildWeaponRacks(center);
        buildBloodStains(center);
        buildChains(center);
        buildCandles(center);
        addCryptDetails(center);
    }

    private void buildCobwebs(Location center) {
        int[][] webPositions = {
                {-7, 5, -3}, {7, 5, 3}, {-2, 5, 8}, {2, 5, -6},
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
                {-6, 2, 0}, {6, 2, 0}, {-6, 2, 6}, {6, 2, 6},
                {-3, 1, -5}, {3, 1, -5}, {0, 1, 7}
        };

        for (int[] pos : skullPositions) {
            if (random.nextDouble() < 0.8) {
                setBlock(center, pos[0], pos[1], pos[2], Material.SKELETON_SKULL);
            }
        }
    }

    private void buildWeaponRacks(Location center) {
        int[][] rackPositions = {
                {-7, 2, -2}, {-7, 2, 3}, {7, 2, -2}, {7, 2, 3}
        };

        for (int[] pos : rackPositions) {
            setBlock(center, pos[0], pos[1], pos[2], Material.ITEM_FRAME);
            setBlock(center, pos[0], pos[1] - 1, pos[2], Material.STONE_BRICK_WALL);
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
                {-3, 2, 0}, {3, 2, 0}, {0, 2, -4}, {0, 2, 7}
        };

        for (int[] pos : candlePositions) {
            setBlock(center, pos[0], pos[1], pos[2], Material.CANDLE);
            setBlock(center, pos[0], pos[1] - 1, pos[2], Material.BLACKSTONE_SLAB);
        }
    }

    private void addCryptDetails(Location center) {
        // Goteiras
        for (int i = 0; i < 5; i++) {
            int x = random.nextInt(14) - 7;
            int z = random.nextInt(14) - 6;
            setBlock(center, x, 5, z, Material.DRIPSTONE_BLOCK);
            setBlock(center, x, 4, z, Material.POINTED_DRIPSTONE);
        }

        // Fungos
        for (int i = 0; i < 6; i++) {
            int x = random.nextInt(14) - 7;
            int z = random.nextInt(14) - 6;
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
        buildArrowChest(center, 0, 1, 7);
        buildArrowChest(center, -6, 1, -4);
        buildArrowChest(center, 6, 1, 7);

        // Baú secreto
        setBlock(center, -7, 1, 0, Material.ENDER_CHEST);

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

    private void buildExteriorStructure(Location center) {
        // Apenas decoração leve no exterior para não cobrir
        buildGravestones(center);
        buildDeadVegetation(center);
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

    private void buildDeadVegetation(Location center) {
        // Árvores mortas
        buildDeadTree(center, -13, 1, -2);
        buildDeadTree(center, 12, 1, -1);

        // Arbustos mortos
        for (int i = 0; i < 6; i++) {
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
        setBlock(center, x + 1, y + 3, z, Material.STRIPPED_OAK_LOG);
        setBlock(center, x - 1, y + 2, z, Material.STRIPPED_OAK_LOG);
    }

    private Location getRelativeLocation(Location center, int x, int y, int z) {
        return center.clone().add(x, y, z);
    }

    // Método de debug - REMOVA depois de testar
    private void debugStructure(Location center) {
        System.out.println("=== CRIPTA CONSTRUÍDA ===");
        System.out.println("Center: " + center);
        checkBlock(center, 0, 0, 0, "Centro da cripta");
        checkBlock(center, 0, 2, 3, "Altar");
        checkBlock(center, -5, 1, -1, "Sarcófago NW");
        checkBlock(center, 0, 1, 7, "Baú central");
        checkBlock(center, 0, 1, 10, "Baú secreto");
    }

    private void checkBlock(Location center, int x, int y, int z, String name) {
        Location loc = center.clone().add(x, y, z);
        Material material = loc.getBlock().getType();
        System.out.println(name + " (" + x + "," + y + "," + z + "): " + material);
    }
}