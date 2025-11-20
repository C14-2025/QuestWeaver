package br.dev.projetoc14.quest.structures;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Inventory;

import java.util.Random;

/**
 * Torre do Arqueiro - Versão 1.21.8 Compatível
 */
public class ArcherTower extends QuestStructure {

    private static final Material TOWER_MATERIAL = Material.STONE_BRICKS;
    private static final Material ROOF_MATERIAL = Material.SPRUCE_PLANKS;
    private static final Material FLOOR_MATERIAL = Material.OAK_PLANKS;

    private Random random = new Random();

    public ArcherTower() {
        super("Torre do Arqueiro", 9, 15, 9);
    }

    @Override
    protected void build(Location center) {
        clearArea(center);
        buildFoundation(center);
        buildTowerStructure(center);
        buildSpiralStaircase(center);
        buildRoof(center);
        buildInterior(center);
        buildExteriorDecorations(center);
        addArcherEquipment(center);

        debugStructure(center);
    }

    private void clearArea(Location center) {
        for (int x = -4; x <= 4; x++) {
            for (int z = -4; z <= 4; z++) {
                for (int y = 0; y <= 14; y++) {
                    setBlock(center, x, y, z, Material.AIR);
                }
                setBlock(center, x, -1, z, Material.STONE);
            }
        }
    }

    private void buildFoundation(Location center) {
        for (int x = -3; x <= 3; x++) {
            for (int z = -3; z <= 3; z++) {
                setBlock(center, x, 0, z, Material.STONE_BRICKS);
            }
        }
    }

    private void buildTowerStructure(Location center) {
        for (int y = 1; y <= 10; y++) {
            for (int x = -2; x <= 2; x++) {
                for (int z = -2; z <= 2; z++) {
                    if (Math.abs(x) == 2 || Math.abs(z) == 2) {
                        setBlock(center, x, y, z, TOWER_MATERIAL);
                    } else {
                        setBlock(center, x, y, z, Material.AIR);
                    }
                }
            }

            // Aberturas para arqueiros
            if (y % 2 == 0 && y >= 3) {
                setBlock(center, 2, y, 0, Material.AIR);
                setBlock(center, -2, y, 0, Material.AIR);
                setBlock(center, 0, y, 2, Material.AIR);
                setBlock(center, 0, y, -2, Material.AIR);
            }
        }
    }

    private void buildSpiralStaircase(Location center) {
        // Escada em espiral externa - apenas blocos sólidos
        int[][] staircase = {
                {3, 1, 0}, {3, 2, 1}, {2, 3, 2}, {1, 4, 3}, {0, 5, 3},
                {-1, 6, 2}, {-2, 7, 1}, {-3, 8, 0}, {-3, 9, -1}, {-2, 10, -2}
        };

        for (int[] step : staircase) {
            setBlock(center, step[0], step[2], step[1], Material.COBBLESTONE);
        }

        // Plataformas de descanso
        setBlock(center, 3, 0, 1, Material.OAK_SLAB);
        setBlock(center, 0, 0, 5, Material.OAK_SLAB);
        setBlock(center, -3, 0, 9, Material.OAK_SLAB);
    }

    private void buildRoof(Location center) {
        // Telhado simples
        for (int y = 11; y <= 13; y++) {
            int radius = 13 - y;
            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    if (Math.abs(x) == radius || Math.abs(z) == radius) {
                        setBlock(center, x, y, z, ROOF_MATERIAL);
                    }
                }
            }
        }

        setBlock(center, 0, 14, 0, Material.OAK_PLANKS);
    }

    private void buildInterior(Location center) {
        // Pisos internos
        for (int y = 3; y <= 9; y += 3) {
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    setBlock(center, x, y, z, FLOOR_MATERIAL);
                }
            }
        }

        // Mesa de trabalho
        setBlock(center, 1, 4, 0, Material.CRAFTING_TABLE);
        setBlock(center, -1, 4, 0, Material.FLETCHING_TABLE);

        // Baú de suprimentos
        setBlock(center, 0, 7, 1, Material.CHEST);
        setupArcherChest(center, 0, 7, 1);
    }

    private void buildExteriorDecorations(Location center) {
        // CORREÇÃO PARA 1.21.8 - usando materiais compatíveis

        // Postes de iluminação
        int[][] torchPosts = {
                {4, 1, 4}, {-4, 1, 4}, {4, 1, -4}, {-4, 1, -4}
        };

        for (int[] post : torchPosts) {
            setBlock(center, post[0], post[1], post[2], Material.OAK_FENCE);
            setBlock(center, post[0], post[1] + 1, post[2], Material.TORCH);
        }

        // Bancos de madeira
        setBlock(center, 5, 1, 0, Material.OAK_SLAB);
        setBlock(center, -5, 1, 0, Material.OAK_SLAB);
        setBlock(center, 0, 1, 5, Material.OAK_SLAB);
        setBlock(center, 0, 1, -5, Material.OAK_SLAB);

        // Alvo de prática
        buildArcheryTarget(center, 6, 1, 2);

        // Barris
        setBlock(center, 4, 1, -3, Material.BARREL);
        setBlock(center, -4, 1, -3, Material.BARREL);

        // CORREÇÃO: Usando GRASS_BLOCK (bloco de grama) em vez de GRASS
        // e plantas compatíveis com 1.21.8
        setBlock(center, 5, 1, 3, Material.GRASS_BLOCK);
        setBlock(center, -5, 1, 3, Material.GRASS_BLOCK);

        // Plantas compatíveis
        setBlock(center, 5, 2, 3, Material.SHORT_GRASS); // Grama alta
        setBlock(center, -5, 2, 3, Material.SHORT_GRASS);

        setBlock(center, 5, 2, -3, Material.FERN);
        setBlock(center, -5, 2, -3, Material.FERN);

        // Adicionando algumas flores simples
        if (random.nextBoolean()) {
            setBlock(center, 5, 2, 2, Material.DANDELION);
        }
        if (random.nextBoolean()) {
            setBlock(center, -5, 2, 2, Material.POPPY);
        }
    }

    private void buildArcheryTarget(Location center, int x, int y, int z) {
        setBlock(center, x, y, z, Material.OAK_FENCE);
        setBlock(center, x, y + 1, z, Material.OAK_FENCE);
        setBlock(center, x, y + 2, z, Material.HAY_BLOCK);
        setBlock(center, x, y + 3, z, Material.TARGET);
    }

    private void addArcherEquipment(Location center) {
        setBlock(center, 2, 5, 1, Material.ITEM_FRAME);
        setBlock(center, 2, 5, -1, Material.ITEM_FRAME);
        setBlock(center, 0, 5, 0, Material.ARROW);

        // Estante de flechas
        setBlock(center, -1, 4, 1, Material.CHEST);
        setupArrowChest(center, -1, 4, 1);
    }

    private void setupArcherChest(Location center, int x, int y, int z) {
        Location chestLoc = getRelativeLocation(center, x, y, z);
        BlockState blockState = chestLoc.getBlock().getState();

        if (blockState instanceof Chest) {
            Chest chest = (Chest) blockState;
            Inventory inv = chest.getInventory();
            inv.clear();

            addItemToChest(inv, Material.BOW, 1);
            addItemToChest(inv, Material.ARROW, random.nextInt(16) + 8);
            addItemToChest(inv, Material.SPECTRAL_ARROW, random.nextInt(8) + 4);

            if (random.nextDouble() < 0.3) {
                addItemToChest(inv, Material.CROSSBOW, 1);
            }

            // Adicionando alguns itens extras da 1.21.8
            if (random.nextDouble() < 0.2) {
                addItemToChest(inv, Material.WIND_CHARGE, random.nextInt(3) + 1);
            }

            chest.update(true);
        }
    }

    private void setupArrowChest(Location center, int x, int y, int z) {
        Location chestLoc = getRelativeLocation(center, x, y, z);
        BlockState blockState = chestLoc.getBlock().getState();

        if (blockState instanceof Chest) {
            Chest chest = (Chest) blockState;
            Inventory inv = chest.getInventory();
            inv.clear();

            for (int i = 0; i < 5; i++) {
                addItemToChest(inv, Material.ARROW, random.nextInt(32) + 16);
            }

            chest.update(true);
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
        System.out.println("=== TORRE DO ARQUEIRO 1.21.8 CONSTRUÍDA ===");
        System.out.println("Center: " + center);

        checkBlock(center, 0, 0, 0, "Centro da fundação");
        checkBlock(center, 2, 5, 0, "Abertura leste");
        checkBlock(center, 3, 1, 0, "Primeiro degrau");
        checkBlock(center, 4, 1, 4, "Poste tocha NE");
        checkBlock(center, 5, 1, 3, "Bloco grama teste");
        checkBlock(center, 6, 1, 2, "Alvo prática");
    }

    private void checkBlock(Location center, int x, int y, int z, String name) {
        Location loc = center.clone().add(x, y, z);
        Material material = loc.getBlock().getType();
        System.out.println(name + " (" + x + "," + y + "," + z + "): " + material);
    }
}