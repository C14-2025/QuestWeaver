package br.dev.projetoc14.quest.structures;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Inventory;

import java.util.Random;

/**
 * Torre do Arqueiro - Versão Melhorada com Entrada e Conexões
 */
public class ArcherTower extends QuestStructure {

    private static final Material TOWER_MATERIAL = Material.STONE_BRICKS;
    private static final Material ROOF_MATERIAL = Material.SPRUCE_PLANKS;
    private static final Material FLOOR_MATERIAL = Material.OAK_PLANKS;
    private static final Material STAIR_MATERIAL = Material.OAK_STAIRS;

    private Random random = new Random();

    public ArcherTower() {
        super("Torre do Arqueiro", 9, 15, 9);
    }

    @Override
    protected void build(Location center) {
        clearArea(center);
        buildFoundation(center);
        buildTowerStructure(center);
        buildEntrance(center); // NOVA: Entrada real
        buildInternalStairs(center); // NOVA: Escadas internas
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

    private void buildEntrance(Location center) {
        // ENTRADA REAL - Substitui uma parede por porta
        for (int y = 1; y <= 3; y++) {
            setBlock(center, 0, y, -2, Material.AIR); // Entrada sul
        }

        // Porta de entrada
        setBlock(center, 0, 1, -2, Material.OAK_DOOR);
        setBlock(center, 0, 2, -2, Material.OAK_DOOR);

        // Degraus de entrada
        setBlock(center, 0, 0, -3, Material.STONE_BRICK_STAIRS);
        setBlock(center, -1, 0, -3, Material.STONE_BRICKS);
        setBlock(center, 1, 0, -3, Material.STONE_BRICKS);
    }

    private void buildInternalStairs(Location center) {
        // ESCADAS INTERNAS conectando todos os andares

        // Andar 1 para 2 (0 → 3)
        buildStairSection(center, -1, 1, 0, 3, 1); // Oeste
        buildStairSection(center, 1, 1, 0, 3, -1); // Leste

        // Andar 2 para 3 (3 → 6)
        buildStairSection(center, -1, 4, 3, 6, 1); // Oeste
        buildStairSection(center, 1, 4, 3, 6, -1); // Leste

        // Andar 3 para 4 (6 → 9)
        buildStairSection(center, -1, 7, 6, 9, 1); // Oeste
        buildStairSection(center, 1, 7, 6, 9, -1); // Leste

        // Plataforma de descanso no topo (nível 9)
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                setBlock(center, x, 9, z, FLOOR_MATERIAL);
            }
        }
    }

    private void buildStairSection(Location center, int startX, int startY, int fromLevel, int toLevel, int direction) {
        int currentY = startY;
        int currentX = startX;

        for (int y = fromLevel; y <= toLevel; y++) {
            setBlock(center, currentX, currentY, 0, STAIR_MATERIAL);
            currentY++;

            // Alterna entre leste e oeste
            if (y % 2 == 0) {
                currentX += direction;
            }
        }
    }

    private void buildRoof(Location center) {
        // Telhado melhorado
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

        // Plataforma de tiro no topo
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                setBlock(center, x, 10, z, Material.OAK_PLANKS);
            }
        }

        // Parapeito de proteção
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                if (Math.abs(x) == 2 || Math.abs(z) == 2) {
                    setBlock(center, x, 11, z, Material.OAK_FENCE);
                }
            }
        }
    }

    private void buildInterior(Location center) {
        // Pisos internos em todos os níveis
        int[] floorLevels = {3, 6, 9};

        for (int level : floorLevels) {
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    setBlock(center, x, level, z, FLOOR_MATERIAL);
                }
            }
        }

        // Nível 3 - Área de trabalho
        setBlock(center, 1, 4, 0, Material.CRAFTING_TABLE);
        setBlock(center, -1, 4, 0, Material.FLETCHING_TABLE);
        setBlock(center, 0, 4, 1, Material.SMITHING_TABLE);

        // Nível 6 - Armazenamento
        setBlock(center, 0, 7, 1, Material.CHEST);
        setupArcherChest(center, 0, 7, 1);
        setBlock(center, 1, 7, 0, Material.BARREL);
        setBlock(center, -1, 7, 0, Material.BARREL);

        // Nível 9 - Área de descanso
        setBlock(center, 0, 10, 0, Material.LANTERN);
        setBlock(center, 1, 10, 1, Material.OAK_SLAB);
        setBlock(center, -1, 10, 1, Material.OAK_SLAB);
    }

    private void buildExteriorDecorations(Location center) {
        // Postes de iluminação
        int[][] torchPosts = {
                {4, 1, 4}, {-4, 1, 4}, {4, 1, -4}, {-4, 1, -4},
                {5, 1, 0}, {-5, 1, 0}, {0, 1, 5}, {0, 1, -5} // MAIS POSTES
        };

        for (int[] post : torchPosts) {
            setBlock(center, post[0], post[1], post[2], Material.OAK_FENCE);
            setBlock(center, post[0], post[1] + 1, post[2], Material.TORCH);
        }

        // Bancos de madeira
        setBlock(center, 5, 1, 2, Material.OAK_SLAB);
        setBlock(center, -5, 1, 2, Material.OAK_SLAB);
        setBlock(center, 5, 1, -2, Material.OAK_SLAB);
        setBlock(center, -5, 1, -2, Material.OAK_SLAB);

        // MAIS ALVOS DE PRÁTICA
        buildArcheryTarget(center, 7, 1, 0);  // Norte principal
        buildArcheryTarget(center, -7, 1, 0); // Sul
        buildArcheryTarget(center, 0, 1, 7);  // Leste
        buildArcheryTarget(center, 0, 1, -7); // Oeste
        buildArcheryTarget(center, 6, 1, 6);  // Noroeste
        buildArcheryTarget(center, -6, 1, 6); // Nordeste
        buildArcheryTarget(center, 6, 1, -6); // Sudoeste
        buildArcheryTarget(center, -6, 1, -6);// Sudeste

        // Barris de suprimentos
        setBlock(center, 4, 1, -3, Material.BARREL);
        setBlock(center, -4, 1, -3, Material.BARREL);
        setBlock(center, 4, 1, 3, Material.BARREL);
        setBlock(center, -4, 1, 3, Material.BARREL);

        // Área de prática com grama
        int[][] grassPatches = {
                {5, 1, 3}, {-5, 1, 3}, {5, 1, -3}, {-5, 1, -3},
                {6, 1, 1}, {-6, 1, 1}, {6, 1, -1}, {-6, 1, -1}
        };

        for (int[] patch : grassPatches) {
            setBlock(center, patch[0], patch[1], patch[2], Material.GRASS_BLOCK);
            if (random.nextDouble() < 0.7) {
                setBlock(center, patch[0], patch[1] + 1, patch[2], Material.SHORT_GRASS);
            }
        }

        // Flores decorativas
        Material[] flowers = {Material.DANDELION, Material.POPPY, Material.BLUE_ORCHID, Material.ALLIUM};
        for (int i = 0; i < 8; i++) {
            int x = random.nextInt(7) - 3;
            int z = random.nextInt(7) - 3;
            if (Math.abs(x) > 3 || Math.abs(z) > 3) {
                Material flower = flowers[random.nextInt(flowers.length)];
                setBlock(center, x + 4, 2, z + 4, flower);
            }
        }

        // Caminho de pedra até a entrada
        int[] pathX = {0, 0, 0, 0, 0};
        int[] pathZ = {-4, -5, -6, -7, -8};

        for (int i = 0; i < pathX.length; i++) {
            setBlock(center, pathX[i], 0, pathZ[i], Material.STONE_BRICKS);
            setBlock(center, pathX[i] - 1, 0, pathZ[i], Material.COBBLESTONE);
            setBlock(center, pathX[i] + 1, 0, pathZ[i], Material.COBBLESTONE);
        }
    }

    private void buildArcheryTarget(Location center, int x, int y, int z) {
        // Suporte do alvo
        setBlock(center, x, y, z, Material.OAK_FENCE);
        setBlock(center, x, y + 1, z, Material.OAK_FENCE);

        // Alvo (usando blocos coloridos para visualização)
        setBlock(center, x, y + 2, z, Material.HAY_BLOCK);
        setBlock(center, x, y + 3, z, Material.TARGET);

        // Plataforma de tiro
        setBlock(center, x - 2, y, z, Material.OAK_PLANKS);
        setBlock(center, x + 2, y, z, Material.OAK_PLANKS);
        setBlock(center, x, y, z - 2, Material.OAK_PLANKS);
        setBlock(center, x, y, z + 2, Material.OAK_PLANKS);
    }

    private void addArcherEquipment(Location center) {
        // Suportes de arcos nas paredes
        setBlock(center, 2, 4, 1, Material.ITEM_FRAME);
        setBlock(center, 2, 4, -1, Material.ITEM_FRAME);
        setBlock(center, -2, 5, 1, Material.ITEM_FRAME);
        setBlock(center, -2, 5, -1, Material.ITEM_FRAME);

        // Flechas em mesas
        setBlock(center, 0, 4, 0, Material.ARROW);
        setBlock(center, 1, 7, 1, Material.ARROW);
        setBlock(center, -1, 10, 0, Material.ARROW);

        // Estantes de flechas
        setBlock(center, -1, 4, 1, Material.CHEST);
        setupArrowChest(center, -1, 4, 1);

        setBlock(center, 1, 7, -1, Material.CHEST);
        setupArrowChest(center, 1, 7, -1);
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
            addItemToChest(inv, Material.TIPPED_ARROW, random.nextInt(12) + 6);

            if (random.nextDouble() < 0.4) {
                addItemToChest(inv, Material.CROSSBOW, 1);
            }

            // Itens extras
            addItemToChest(inv, Material.FEATHER, random.nextInt(8) + 4);
            addItemToChest(inv, Material.FLINT, random.nextInt(5) + 3);
            addItemToChest(inv, Material.STICK, random.nextInt(12) + 6);

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

            for (int i = 0; i < 6; i++) {
                addItemToChest(inv, Material.ARROW, random.nextInt(32) + 16);
            }

            // Adiciona alguns tipos especiais de flechas
            addItemToChest(inv, Material.SPECTRAL_ARROW, random.nextInt(8) + 4);
            addItemToChest(inv, Material.TIPPED_ARROW, random.nextInt(6) + 3);

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
        System.out.println("=== TORRE DO ARQUEIRO MELHORADA ===");
        System.out.println("Center: " + center);

        checkBlock(center, 0, 0, 0, "Centro da fundação");
        checkBlock(center, 0, 1, -2, "Porta de entrada");
        checkBlock(center, -1, 1, 0, "Primeira escada interna");
        checkBlock(center, 2, 5, 0, "Abertura leste");
        checkBlock(center, 7, 1, 0, "Alvo norte principal");
        checkBlock(center, 0, 7, 1, "Baú principal nível 6");
        checkBlock(center, 0, 10, 0, "Área de descanso topo");
    }

    private void checkBlock(Location center, int x, int y, int z, String name) {
        Location loc = center.clone().add(x, y, z);
        Material material = loc.getBlock().getType();
        System.out.println(name + " (" + x + "," + y + "," + z + "): " + material);
    }
}