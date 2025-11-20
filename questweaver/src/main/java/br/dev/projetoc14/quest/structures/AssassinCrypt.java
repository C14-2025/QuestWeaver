package br.dev.projetoc14.quest.structures;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Inventory;

import java.util.Random;

/**
 * Cripta dos Assassinos - Versão Completa com Baús Escondidos
 */
public class AssassinCrypt extends QuestStructure {

    private static final int CRYPT_WIDTH = 15;
    private static final int CRYPT_HEIGHT = 8;
    private static final int CRYPT_DEPTH = 15;

    private static final Material WALL_MATERIAL = Material.DEEPSLATE_BRICKS;
    private static final Material FLOOR_MATERIAL = Material.DEEPSLATE_TILES;
    private static final Material CEILING_MATERIAL = Material.BLACKSTONE;
    private static final Material LIGHT_SOURCE = Material.SOUL_LANTERN;

    private Random random = new Random();

    public AssassinCrypt() {
        super("Cripta dos Assassinos", CRYPT_WIDTH, CRYPT_HEIGHT, CRYPT_DEPTH);
    }

    @Override
    protected void build(Location center) {
        clearArea(center);
        buildExteriorStructure(center);
        buildSimpleCrypt(center);
        buildLighting(center);
        buildDecorations(center);
        buildHiddenStorage(center);

        debugStructure(center);
    }

    private void clearArea(Location center) {
        for (int x = -7; x <= 7; x++) {
            for (int z = -7; z <= 7; z++) {
                for (int y = -1; y <= 6; y++) {
                    setBlock(center, x, y, z, Material.AIR);
                }
                setBlock(center, x, -1, z, Material.DEEPSLATE);
            }
        }
    }

    private void buildExteriorStructure(Location center) {
        buildSimpleWalls(center);
        buildEntrance(center);
        buildExteriorDecorations(center);
    }

    private void buildSimpleWalls(Location center) {
        for (int i = 0; i < 16; i++) {
            int x = -8 + i;
            setBlock(center, x, 1, -8, Material.DEEPSLATE_BRICKS);
            setBlock(center, x, 1, 8, Material.DEEPSLATE_BRICKS);
            setBlock(center, -8, 1, -8 + i, Material.DEEPSLATE_BRICKS);
            setBlock(center, 8, 1, -8 + i, Material.DEEPSLATE_BRICKS);
        }

        setBlock(center, -8, 2, -8, Material.DEEPSLATE_BRICKS);
        setBlock(center, -8, 3, -8, Material.DEEPSLATE_BRICKS);
        setBlock(center, 8, 2, -8, Material.DEEPSLATE_BRICKS);
        setBlock(center, 8, 3, -8, Material.DEEPSLATE_BRICKS);
        setBlock(center, -8, 2, 8, Material.DEEPSLATE_BRICKS);
        setBlock(center, -8, 3, 8, Material.DEEPSLATE_BRICKS);
        setBlock(center, 8, 2, 8, Material.DEEPSLATE_BRICKS);
        setBlock(center, 8, 3, 8, Material.DEEPSLATE_BRICKS);
    }

    private void buildEntrance(Location center) {
        for (int y = 1; y <= 3; y++) {
            setBlock(center, -1, y, -7, Material.AIR);
            setBlock(center, 0, y, -7, Material.AIR);
            setBlock(center, 1, y, -7, Material.AIR);
        }

        setBlock(center, -1, 1, -7, Material.DARK_OAK_DOOR);
        setBlock(center, -1, 2, -7, Material.DARK_OAK_DOOR);
        setBlock(center, 1, 1, -7, Material.DARK_OAK_DOOR);
        setBlock(center, 1, 2, -7, Material.DARK_OAK_DOOR);

        for (int z = -6; z <= -4; z++) {
            int height = -6 - z;
            for (int x = -2; x <= 2; x++) {
                setBlock(center, x, height, z, Material.DEEPSLATE_BRICKS);
            }
        }
    }

    private void buildExteriorDecorations(Location center) {
        setBlock(center, -6, 1, -6, Material.DEAD_BUSH);
        setBlock(center, 6, 1, -6, Material.DEAD_BUSH);
        setBlock(center, -6, 1, 6, Material.DEAD_BUSH);
        setBlock(center, 6, 1, 6, Material.DEAD_BUSH);

        setBlock(center, -3, 2, -7, Material.SOUL_TORCH);
        setBlock(center, 3, 2, -7, Material.SOUL_TORCH);
    }

    private void buildSimpleCrypt(Location center) {
        buildCryptFloor(center);
        buildCryptWalls(center);
        buildCryptCeiling(center);
        buildPillars(center);
        buildAltar(center);
        buildSarcophagi(center);
    }

    private void buildCryptFloor(Location center) {
        for (int x = -5; x <= 5; x++) {
            for (int z = -5; z <= 5; z++) {
                setBlock(center, x, 0, z, FLOOR_MATERIAL);
            }
        }
    }

    private void buildCryptWalls(Location center) {
        for (int y = 1; y <= 4; y++) {
            for (int x = -5; x <= 5; x++) {
                for (int z = -5; z <= 5; z++) {
                    if (Math.abs(x) == 5 || Math.abs(z) == 5) {
                        setBlock(center, x, y, z, WALL_MATERIAL);
                    }
                }
            }
        }

        for (int y = 1; y <= 3; y++) {
            setBlock(center, -1, y, -5, Material.AIR);
            setBlock(center, 0, y, -5, Material.AIR);
            setBlock(center, 1, y, -5, Material.AIR);
        }
    }

    private void buildCryptCeiling(Location center) {
        for (int x = -5; x <= 5; x++) {
            for (int z = -5; z <= 5; z++) {
                setBlock(center, x, 5, z, CEILING_MATERIAL);
            }
        }
    }

    private void buildPillars(Location center) {
        int[][] pillarPositions = {
                {-4, -4}, {4, -4}, {-4, 4}, {4, 4}
        };

        for (int[] pos : pillarPositions) {
            int x = pos[0];
            int z = pos[1];
            for (int y = 1; y <= 4; y++) {
                setBlock(center, x, y, z, Material.POLISHED_BLACKSTONE);
            }
        }
    }

    private void buildAltar(Location center) {
        for (int x = -1; x <= 1; x++) {
            for (int z = 1; z <= 3; z++) {
                setBlock(center, x, 1, z, Material.BLACKSTONE);
            }
        }

        setBlock(center, 0, 2, 2, Material.CRYING_OBSIDIAN);
        setBlock(center, -1, 2, 2, Material.SOUL_CAMPFIRE);
        setBlock(center, 1, 2, 2, Material.SOUL_CAMPFIRE);

        setBlock(center, -2, 2, 1, Material.CANDLE);
        setBlock(center, 2, 2, 1, Material.CANDLE);
        setBlock(center, -2, 2, 3, Material.CANDLE);
        setBlock(center, 2, 2, 3, Material.CANDLE);
    }

    private void buildSarcophagi(Location center) {
        buildSarcophagus(center, -3, -2);
        buildSarcophagus(center, 3, -2);
        buildSarcophagus(center, -3, 2);
        buildSarcophagus(center, 3, 2);
    }

    private void buildSarcophagus(Location center, int x, int z) {
        setBlock(center, x, 1, z, Material.CHISELED_DEEPSLATE);
        setBlock(center, x, 2, z, Material.BLACKSTONE_SLAB);

        if (random.nextBoolean()) {
            setBlock(center, x - 1, 1, z, Material.SKELETON_SKULL);
        }
    }

    private void buildLighting(Location center) {
        int[][] lightPositions = {
                {-4, 3, -4}, {4, 3, -4}, {-4, 3, 4}, {4, 3, 4},
                {0, 3, 0}, {-4, 3, 0}, {4, 3, 0}, {0, 3, -4}, {0, 3, 4}
        };

        for (int[] pos : lightPositions) {
            setBlock(center, pos[0], pos[1], pos[2], LIGHT_SOURCE);
        }

        setBlock(center, 0, 5, 0, Material.CHAIN);
        setBlock(center, 0, 4, 0, LIGHT_SOURCE);
    }

    private void buildDecorations(Location center) {
        buildCobwebs(center);
        buildSkulls(center);
        buildBonePiles(center);
    }

    private void buildCobwebs(Location center) {
        int[][] webPositions = {
                {-4, 3, 0}, {4, 3, 0}, {0, 3, -4}, {0, 3, 4},
                {-3, 2, -3}, {3, 2, 3}
        };

        for (int[] pos : webPositions) {
            if (random.nextDouble() < 0.6) {
                setBlock(center, pos[0], pos[1], pos[2], Material.COBWEB);
            }
        }
    }

    private void buildSkulls(Location center) {
        int[][] skullPositions = {
                {-5, 2, -3}, {-5, 2, 3}, {5, 2, -3}, {5, 2, 3},
                {-3, 2, -5}, {3, 2, -5}, {-3, 2, 5}, {3, 2, 5}
        };

        for (int[] pos : skullPositions) {
            if (random.nextDouble() < 0.7) {
                setBlock(center, pos[0], pos[1], pos[2], Material.SKELETON_SKULL);
            }
        }
    }

    private void buildBonePiles(Location center) {
        int[][] bonePositions = {
                {-2, 1, -2}, {2, 1, -2}, {-2, 1, 2}, {2, 1, 2}
        };

        for (int[] pos : bonePositions) {
            if (random.nextDouble() < 0.5) {
                setBlock(center, pos[0], pos[1], pos[2], Material.BONE_BLOCK);
            }
        }
    }

    private void buildHiddenStorage(Location center) {
        buildHiddenWallChest(center);    // Baú do Assassino
        buildUnderfloorChest(center);     // Baú de Flechas
        buildPillarChest(center);         // Baú de Ferramentas
        buildAltarSecret(center);         // Baú do Tesouro
        buildSarcophagusSecrets(center);  // Baús dos Sarcófagos
        buildSecretRoomChest(center);     // Baú de Armas
        buildCeilingChest(center);        // Baú Raro

        // Barrel central como distração
        setBlock(center, 0, 1, 0, Material.BARREL);
        setupBarrel(center, 0, 1, 0);
    }

    private void buildHiddenWallChest(Location center) {
        // Baú do Assassino - atrás da parede do fundo
        int x = 0;
        int y = 1;
        int z = 4; // Parede do fundo

        // Cria uma passagem secreta
        setBlock(center, x, y, z, Material.AIR);
        setBlock(center, x, y + 1, z, Material.AIR);

        // Sala secreta pequena atrás
        setBlock(center, x, y, z + 1, Material.AIR);
        setBlock(center, x, y + 1, z + 1, Material.AIR);
        setBlock(center, x, y, z + 2, Material.AIR);

        // Baú escondido
        setBlock(center, x, y, z + 2, Material.CHEST);
        buildChest(center, x, y, z + 2, "assassin");

        // Fecha a passagem com um bloco que parece normal
        setBlock(center, x, y, z, Material.DEEPSLATE_BRICKS);
        setBlock(center, x, y + 1, z, Material.DEEPSLATE_BRICKS);
    }

    private void buildUnderfloorChest(Location center) {
        // Baú de Flechas - sob o piso
        int x = -3;
        int y = 0;
        int z = -3;

        // Cria um alçapão no piso
        setBlock(center, x, y, z, Material.AIR);
        setBlock(center, x, y - 1, z, Material.CHEST);

        buildChest(center, x, y - 1, z, "arrows");

        // Cobre com uma placa de pressão que parece normal
        setBlock(center, x, y, z, Material.STONE_PRESSURE_PLATE);
    }

    private void buildPillarChest(Location center) {
        // Baú de Ferramentas - dentro do pilar
        int x = -4;
        int y = 1;
        int z = -4;

        // Remove bloco do pilar e coloca baú
        setBlock(center, x, y, z, Material.CHEST);
        buildChest(center, x, y, z, "tools");

        // Esconde com um bloco que parece parte do pilar
        setBlock(center, x, y, z, Material.POLISHED_BLACKSTONE);
    }

    private void buildAltarSecret(Location center) {
        // Baú do Tesouro - dentro do altar
        int x = 0;
        int y = 0;
        int z = 2;

        // Baú sob o altar
        setBlock(center, x, y, z, Material.CHEST);
        buildChest(center, x, y, z, "treasure");

        // Cobre com o bloco do altar
        setBlock(center, x, y + 1, z, Material.BLACKSTONE);
    }

    private void buildSarcophagusSecrets(Location center) {
        // Baús escondidos nos sarcófagos
        int[][] sarcophagusPositions = {
                {-3, -2}, {3, -2}, {-3, 2}, {3, 2}
        };

        for (int[] pos : sarcophagusPositions) {
            int x = pos[0];
            int z = pos[1];

            // Baú escondido sob cada sarcófago
            setBlock(center, x, 0, z, Material.CHEST);
            buildChest(center, x, 0, z, "sarcophagus");
        }
    }

    private void buildSecretRoomChest(Location center) {
        // Baú de Armas - sala secreta leste
        int x = 4;
        int y = 1;
        int z = 0;

        // Cria passagem secreta
        setBlock(center, x, y, z, Material.AIR);
        setBlock(center, x, y + 1, z, Material.AIR);
        setBlock(center, x + 1, y, z, Material.AIR);
        setBlock(center, x + 1, y + 1, z, Material.AIR);

        // Baú na sala secreta
        setBlock(center, x + 1, y, z, Material.CHEST);
        buildChest(center, x + 1, y, z, "weapons");

        // Fecha a passagem
        setBlock(center, x, y, z, Material.DEEPSLATE_BRICKS);
        setBlock(center, x, y + 1, z, Material.DEEPSLATE_BRICKS);
    }

    private void buildCeilingChest(Location center) {
        // Baú Raro - no teto
        int x = -2;
        int y = 4;
        int z = 2;

        // Cria espaço no teto
        setBlock(center, x, y, z, Material.AIR);
        setBlock(center, x, y + 1, z, Material.CHEST);

        buildChest(center, x, y + 1, z, "rare");

        // Cobre com corrente
        setBlock(center, x, y, z, Material.CHAIN);
    }

    private void buildChest(Location center, int x, int y, int z, String type) {
        Location chestLoc = getRelativeLocation(center, x, y, z);
        BlockState blockState = chestLoc.getBlock().getState();

        if (blockState instanceof Chest) {
            Chest chest = (Chest) blockState;
            Inventory inv = chest.getInventory();
            inv.clear();

            switch (type) {
                case "assassin":
                    // Equipamento completo de assassino
                    addItemToChest(inv, Material.IRON_SWORD, 1);
                    addItemToChest(inv, Material.LEATHER_CHESTPLATE, 1);
                    addItemToChest(inv, Material.LEATHER_LEGGINGS, 1);
                    addItemToChest(inv, Material.LEATHER_BOOTS, 1);
                    addItemToChest(inv, Material.LEATHER_HELMET, 1);
                    addItemToChest(inv, Material.ARROW, random.nextInt(16) + 8);
                    addItemToChest(inv, Material.POTION, 2);
                    addItemToChest(inv, Material.GOLDEN_CARROT, 3);
                    addItemToChest(inv, Material.SPECTRAL_ARROW, random.nextInt(4) + 2);
                    break;

                case "arrows":
                    // Munição variada
                    addItemToChest(inv, Material.ARROW, random.nextInt(32) + 16);
                    addItemToChest(inv, Material.SPECTRAL_ARROW, random.nextInt(8) + 4);
                    addItemToChest(inv, Material.TIPPED_ARROW, random.nextInt(12) + 6);
                    addItemToChest(inv, Material.FLINT, random.nextInt(5) + 3);
                    addItemToChest(inv, Material.FEATHER, random.nextInt(8) + 4);
                    addItemToChest(inv, Material.STICK, random.nextInt(12) + 6);
                    if (random.nextDouble() < 0.4) {
                        addItemToChest(inv, Material.BOW, 1);
                    }
                    if (random.nextDouble() < 0.2) {
                        addItemToChest(inv, Material.CROSSBOW, 1);
                    }
                    break;

                case "tools":
                    // Ferramentas e utilidades
                    addItemToChest(inv, Material.IRON_PICKAXE, 1);
                    addItemToChest(inv, Material.IRON_AXE, 1);
                    addItemToChest(inv, Material.IRON_SHOVEL, 1);
                    addItemToChest(inv, Material.TORCH, random.nextInt(8) + 4);
                    addItemToChest(inv, Material.BREAD, random.nextInt(5) + 3);
                    addItemToChest(inv, Material.IRON_INGOT, random.nextInt(3) + 2);
                    addItemToChest(inv, Material.COOKED_BEEF, random.nextInt(4) + 2);
                    addItemToChest(inv, Material.WATER_BUCKET, 1);
                    break;

                case "treasure":
                    // Gemas e recursos raros
                    addItemToChest(inv, Material.EMERALD, random.nextInt(5) + 3);
                    addItemToChest(inv, Material.GOLD_INGOT, random.nextInt(8) + 4);
                    addItemToChest(inv, Material.DIAMOND, random.nextInt(2) + 1);
                    addItemToChest(inv, Material.LAPIS_LAZULI, random.nextInt(12) + 8);
                    addItemToChest(inv, Material.REDSTONE, random.nextInt(16) + 8);
                    addItemToChest(inv, Material.GOLD_NUGGET, random.nextInt(10) + 5);
                    if (random.nextDouble() < 0.3) {
                        addItemToChest(inv, Material.ENCHANTED_BOOK, 1);
                    }
                    if (random.nextDouble() < 0.1) {
                        addItemToChest(inv, Material.MUSIC_DISC_13, 1);
                    }
                    break;

                case "sarcophagus":
                    // Recursos de monstros e itens diversos
                    addItemToChest(inv, Material.BONE, random.nextInt(8) + 4);
                    addItemToChest(inv, Material.ROTTEN_FLESH, random.nextInt(5) + 2);
                    addItemToChest(inv, Material.STRING, random.nextInt(6) + 3);
                    addItemToChest(inv, Material.GUNPOWDER, random.nextInt(4) + 2);
                    addItemToChest(inv, Material.SPIDER_EYE, random.nextInt(3) + 1);
                    addItemToChest(inv, Material.ENDER_PEARL, random.nextDouble() < 0.3 ? random.nextInt(2) + 1 : 0);
                    if (random.nextDouble() < 0.2) {
                        addItemToChest(inv, Material.GOLD_NUGGET, random.nextInt(10) + 5);
                    }
                    if (random.nextDouble() < 0.1) {
                        addItemToChest(inv, Material.NAME_TAG, 1);
                    }
                    break;

                case "weapons":
                    // Armas e equipamento de combate
                    addItemToChest(inv, Material.IRON_SWORD, 1);
                    addItemToChest(inv, Material.SHIELD, 1);
                    addItemToChest(inv, Material.IRON_AXE, 1);
                    addItemToChest(inv, Material.IRON_HELMET, 1);
                    addItemToChest(inv, Material.IRON_INGOT, random.nextInt(4) + 2);
                    addItemToChest(inv, Material.GOLDEN_APPLE, random.nextDouble() < 0.3 ? 1 : 0);
                    addItemToChest(inv, Material.TRIDENT, random.nextDouble() < 0.1 ? 1 : 0);
                    addItemToChest(inv, Material.ARROW, random.nextInt(12) + 6);
                    break;

                case "rare":
                    // Itens ultra raros e valiosos
                    addItemToChest(inv, Material.DIAMOND, random.nextInt(3) + 1);
                    addItemToChest(inv, Material.EMERALD, random.nextInt(6) + 3);
                    addItemToChest(inv, Material.NETHERITE_INGOT, random.nextDouble() < 0.05 ? 1 : 0);
                    addItemToChest(inv, Material.ENCHANTED_GOLDEN_APPLE, random.nextDouble() < 0.1 ? 1 : 0);
                    addItemToChest(inv, Material.EXPERIENCE_BOTTLE, random.nextInt(3) + 1);
                    addItemToChest(inv, Material.HEART_OF_THE_SEA, random.nextDouble() < 0.02 ? 1 : 0);
                    if (random.nextDouble() < 0.15) {
                        addItemToChest(inv, Material.ENCHANTED_BOOK, 1);
                    }
                    if (random.nextDouble() < 0.08) {
                        addItemToChest(inv, Material.MUSIC_DISC_CAT, 1);
                    }
                    if (random.nextDouble() < 0.05) {
                        addItemToChest(inv, Material.TOTEM_OF_UNDYING, 1);
                    }
                    break;
            }

            chest.update(true);
        }
    }

    private void setupBarrel(Location center, int x, int y, int z) {
        Location barrelLoc = getRelativeLocation(center, x, y, z);
        BlockState blockState = barrelLoc.getBlock().getState();

        if (blockState instanceof org.bukkit.block.Barrel) {
            org.bukkit.block.Barrel barrel = (org.bukkit.block.Barrel) blockState;
            Inventory inv = barrel.getInventory();
            inv.clear();

            // Itens comuns como distração
            addItemToChest(inv, Material.BONE, random.nextInt(5) + 2);
            addItemToChest(inv, Material.ROTTEN_FLESH, random.nextInt(3) + 1);
            addItemToChest(inv, Material.STRING, random.nextInt(4) + 2);
            addItemToChest(inv, Material.COAL, random.nextInt(6) + 3);
            addItemToChest(inv, Material.WHEAT_SEEDS, random.nextInt(8) + 4);
            addItemToChest(inv, Material.PAPER, random.nextInt(5) + 2);
            addItemToChest(inv, Material.CLAY_BALL, random.nextInt(4) + 2);

            barrel.update(true);
        }
    }

    private void addItemToChest(Inventory inventory, Material material, int amount) {
        if (amount <= 0) return;

        ItemStack item = new ItemStack(material, amount);
        for (int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, item);
                break;
            }
        }
    }

    private Location getRelativeLocation(Location center, int x, int y, int z) {
        return center.clone().add(x, y, z);
    }

    private void debugStructure(Location center) {
        System.out.println("=== CRIPTA DOS ASSASSINOS CONSTRUÍDA ===");
        System.out.println("Center: " + center);
        System.out.println("=== 7 BAÚS ESCONDIDOS COLOCADOS ===");

        // Verifica pontos importantes
        checkBlock(center, 0, 0, 0, "Barrel central (distração)");
        checkBlock(center, 0, 1, 4, "Parede falsa - Baú do Assassino");
        checkBlock(center, -3, 0, -3, "Alçapão - Baú de Flechas");
        checkBlock(center, -4, 1, -4, "Pilar falso - Baú de Ferramentas");
        checkBlock(center, 0, 0, 2, "Altar secreto - Baú do Tesouro");
        checkBlock(center, -3, 0, -2, "Sarcófago NW - Baú secreto");
        checkBlock(center, 4, 1, 0, "Sala secreta - Baú de Armas");
        checkBlock(center, -2, 4, 2, "Teto - Baú Raro");
    }

    private void checkBlock(Location center, int x, int y, int z, String name) {
        Location loc = center.clone().add(x, y, z);
        Material material = loc.getBlock().getType();
        System.out.println(name + " (" + x + "," + y + "," + z + "): " + material);
    }
}