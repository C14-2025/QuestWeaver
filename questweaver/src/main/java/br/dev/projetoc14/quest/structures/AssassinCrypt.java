package br.dev.projetoc14.quest.structures;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Inventory;

import java.util.Random;

/**
 * Cripta dos Assassinos - Versão Simplificada
 * Exterior mantido, interior simplificado e dark
 */
public class AssassinCrypt extends QuestStructure {

    // Dimensões reduzidas
    private static final int CRYPT_WIDTH = 15;
    private static final int CRYPT_HEIGHT = 8;
    private static final int CRYPT_DEPTH = 15;

    // Materiais dark simplificados
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
        buildExteriorStructure(center);  // Mantemos o exterior que está bom
        buildSimpleCrypt(center);        // Cripta interna simplificada
        buildLighting(center);
        buildDecorations(center);
        buildStorage(center);

        debugStructure(center);
    }

    private void clearArea(Location center) {
        // Limpa área menor e mais simples
        for (int x = -7; x <= 7; x++) {
            for (int z = -7; z <= 7; z++) {
                for (int y = -1; y <= 6; y++) {
                    setBlock(center, x, y, z, Material.AIR);
                }
                // Base sólida simples
                setBlock(center, x, -1, z, Material.DEEPSLATE);
            }
        }
    }

    private void buildExteriorStructure(Location center) {
        // Mantemos apenas o essencial do exterior
        buildSimpleWalls(center);
        buildEntrance(center);
        buildExteriorDecorations(center);
    }

    private void buildSimpleWalls(Location center) {
        // Muros simples ao redor
        for (int i = 0; i < 16; i++) {
            int x = -8 + i;
            setBlock(center, x, 1, -8, Material.DEEPSLATE_BRICKS);
            setBlock(center, x, 1, 8, Material.DEEPSLATE_BRICKS);
            setBlock(center, -8, 1, -8 + i, Material.DEEPSLATE_BRICKS);
            setBlock(center, 8, 1, -8 + i, Material.DEEPSLATE_BRICKS);
        }

        // Torres simples nos cantos
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
        // Entrada simples
        for (int y = 1; y <= 3; y++) {
            setBlock(center, -1, y, -7, Material.AIR);
            setBlock(center, 0, y, -7, Material.AIR);
            setBlock(center, 1, y, -7, Material.AIR);
        }

        // Portas
        setBlock(center, -1, 1, -7, Material.DARK_OAK_DOOR);
        setBlock(center, -1, 2, -7, Material.DARK_OAK_DOOR);
        setBlock(center, 1, 1, -7, Material.DARK_OAK_DOOR);
        setBlock(center, 1, 2, -7, Material.DARK_OAK_DOOR);

        // Escadas de entrada simples
        for (int z = -6; z <= -4; z++) {
            int height = -6 - z;
            for (int x = -2; x <= 2; x++) {
                setBlock(center, x, height, z, Material.DEEPSLATE_BRICKS);
            }
        }
    }

    private void buildExteriorDecorations(Location center) {
        // Decorações externas simples
        setBlock(center, -6, 1, -6, Material.DEAD_BUSH);
        setBlock(center, 6, 1, -6, Material.DEAD_BUSH);
        setBlock(center, -6, 1, 6, Material.DEAD_BUSH);
        setBlock(center, 6, 1, 6, Material.DEAD_BUSH);

        // Tochas de entrada
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
        // Piso simples
        for (int x = -5; x <= 5; x++) {
            for (int z = -5; z <= 5; z++) {
                setBlock(center, x, 0, z, FLOOR_MATERIAL);
            }
        }
    }

    private void buildCryptWalls(Location center) {
        // Paredes internas simples
        for (int y = 1; y <= 4; y++) {
            for (int x = -5; x <= 5; x++) {
                for (int z = -5; z <= 5; z++) {
                    if (Math.abs(x) == 5 || Math.abs(z) == 5) {
                        setBlock(center, x, y, z, WALL_MATERIAL);
                    }
                }
            }
        }

        // Abertura da entrada
        for (int y = 1; y <= 3; y++) {
            setBlock(center, -1, y, -5, Material.AIR);
            setBlock(center, 0, y, -5, Material.AIR);
            setBlock(center, 1, y, -5, Material.AIR);
        }
    }

    private void buildCryptCeiling(Location center) {
        // Teto simples
        for (int x = -5; x <= 5; x++) {
            for (int z = -5; z <= 5; z++) {
                setBlock(center, x, 5, z, CEILING_MATERIAL);
            }
        }
    }

    private void buildPillars(Location center) {
        // Pilares simples nos cantos
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
        // Altar simples
        for (int x = -1; x <= 1; x++) {
            for (int z = 1; z <= 3; z++) {
                setBlock(center, x, 1, z, Material.BLACKSTONE);
            }
        }

        setBlock(center, 0, 2, 2, Material.CRYING_OBSIDIAN);
        setBlock(center, -1, 2, 2, Material.SOUL_CAMPFIRE);
        setBlock(center, 1, 2, 2, Material.SOUL_CAMPFIRE);

        // Velas
        setBlock(center, -2, 2, 1, Material.CANDLE);
        setBlock(center, 2, 2, 1, Material.CANDLE);
        setBlock(center, -2, 2, 3, Material.CANDLE);
        setBlock(center, 2, 2, 3, Material.CANDLE);
    }

    private void buildSarcophagi(Location center) {
        // Sarcófagos simplificados
        buildSarcophagus(center, -3, -2);
        buildSarcophagus(center, 3, -2);
        buildSarcophagus(center, -3, 2);
        buildSarcophagus(center, 3, 2);
    }

    private void buildSarcophagus(Location center, int x, int z) {
        setBlock(center, x, 1, z, Material.CHISELED_DEEPSLATE);
        setBlock(center, x, 2, z, Material.BLACKSTONE_SLAB);
        setBlock(center, x, 0, z, Material.BARREL);

        if (random.nextBoolean()) {
            setBlock(center, x - 1, 1, z, Material.SKELETON_SKULL);
        }
    }

    private void buildLighting(Location center) {
        // Iluminação simples e funcional
        int[][] lightPositions = {
                {-4, 3, -4}, {4, 3, -4}, {-4, 3, 4}, {4, 3, 4},
                {0, 3, 0}, {-4, 3, 0}, {4, 3, 0}, {0, 3, -4}, {0, 3, 4}
        };

        for (int[] pos : lightPositions) {
            setBlock(center, pos[0], pos[1], pos[2], LIGHT_SOURCE);
        }

        // Correntes no teto
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

    private void buildStorage(Location center) {
        // Baús simples
        buildChest(center, -4, 1, 0, "assassin");
        buildChest(center, 4, 1, 0, "arrows");
        buildChest(center, 0, 1, -4, "tools");

        // Barrel central
        setBlock(center, 0, 1, 0, Material.BARREL);
        setupBarrel(center, 0, 1, 0);
    }

    private void buildChest(Location center, int x, int y, int z, String type) {
        setBlock(center, x, y, z, Material.CHEST);

        Location chestLoc = getRelativeLocation(center, x, y, z);
        BlockState blockState = chestLoc.getBlock().getState();

        if (blockState instanceof Chest) {
            Chest chest = (Chest) blockState;
            Inventory inv = chest.getInventory();
            inv.clear();

            switch (type) {
                case "assassin":
                    addItemToChest(inv, Material.IRON_SWORD, 1);
                    addItemToChest(inv, Material.LEATHER_CHESTPLATE, 1);
                    addItemToChest(inv, Material.ARROW, random.nextInt(16) + 8);
                    break;
                case "arrows":
                    addItemToChest(inv, Material.ARROW, random.nextInt(32) + 16);
                    addItemToChest(inv, Material.SPECTRAL_ARROW, random.nextInt(8) + 4);
                    if (random.nextDouble() < 0.3) {
                        addItemToChest(inv, Material.BOW, 1);
                    }
                    break;
                case "tools":
                    addItemToChest(inv, Material.IRON_PICKAXE, 1);
                    addItemToChest(inv, Material.TORCH, random.nextInt(8) + 4);
                    addItemToChest(inv, Material.BREAD, random.nextInt(5) + 3);
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

            // Itens aleatórios no barrel
            addItemToChest(inv, Material.BONE, random.nextInt(8) + 4);
            addItemToChest(inv, Material.ROTTEN_FLESH, random.nextInt(5) + 2);
            addItemToChest(inv, Material.STRING, random.nextInt(6) + 3);

            if (random.nextDouble() < 0.4) {
                addItemToChest(inv, Material.EMERALD, random.nextInt(3) + 1);
            }

            barrel.update(true);
        }
    }

    private void addItemToChest(Inventory inventory, Material material, int amount) {
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
        System.out.println("=== CRIPTA SIMPLIFICADA CONSTRUÍDA ===");
        System.out.println("Center: " + center);
        checkBlock(center, 0, 0, 0, "Centro da cripta");
        checkBlock(center, 0, 2, 2, "Altar");
        checkBlock(center, -3, 1, -2, "Sarcófago NW");
        checkBlock(center, -4, 1, 0, "Baú assassino");
        checkBlock(center, 0, 1, 0, "Barrel central");
    }

    private void checkBlock(Location center, int x, int y, int z, String name) {
        Location loc = center.clone().add(x, y, z);
        Material material = loc.getBlock().getType();
        System.out.println(name + " (" + x + "," + y + "," + z + "): " + material);
    }
}