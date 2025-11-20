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
 * Campo de treinamento para arqueiros (versão melhorada com torre maior e loot de flechas)
 */
public class ArcherTrainingGrounds extends QuestStructure {

    // Constantes para melhor manutenção - TORRE AUMENTADA
    private static final int TOWER_SIZE = 7; // Aumentado de 5 para 7
    private static final int TOWER_HEIGHT = 12; // Aumentado de 9 para 12
    private static final int PLATFORM_HEIGHT = 13; // Aumentado de 10 para 13
    private static final int FIELD_RADIUS = 10;

    // Materiais reutilizáveis
    private static final Material TOWER_WALL = Material.COBBLESTONE;
    private static final Material TOWER_CORNER = Material.STONE_BRICKS;
    private static final Material MAIN_FLOOR = Material.STONE_BRICKS;
    private static final Material FLOOR_PATTERN = Material.CRACKED_STONE_BRICKS;
    private static final Material FENCE = Material.OAK_FENCE;
    private static final Material PLATFORM_FLOOR = Material.OAK_PLANKS;
    private static final Material STAIRS = Material.OAK_STAIRS;

    private Random random = new Random();

    public ArcherTrainingGrounds() {
        super("Campo de Treinamento de Arqueiros", 23, 15, 25); // Altura aumentada para 15
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

                // Área central da torre (agora maior)
                if (Math.abs(x) <= 3 && Math.abs(z) <= 3) {
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
        // Paredes da torre (mais alta)
        for (int y = 1; y <= TOWER_HEIGHT; y++) {
            buildTowerLevel(center, y);
        }

        // Entrada da torre (agora mais larga)
        setBlock(center, -1, 1, -3, Material.AIR);
        setBlock(center, 0, 1, -3, Material.AIR);
        setBlock(center, 1, 1, -3, Material.AIR);
        setBlock(center, -1, 2, -3, Material.AIR);
        setBlock(center, 0, 2, -3, Material.AIR);
        setBlock(center, 1, 2, -3, Material.AIR);

        // Escadas internas melhoradas
        buildImprovedInternalStairs(center);

        // Janelas (seteiras) - mais níveis devido à torre mais alta
        createArrowSlits(center);

        // Plataformas intermediárias adicionais
        buildPlatform(center, -2, 2, 6, TOWER_SIZE - 2, TOWER_SIZE - 2, PLATFORM_FLOOR);
        buildPlatform(center, -2, 2, 9, TOWER_SIZE - 2, TOWER_SIZE - 2, PLATFORM_FLOOR);

        // Topo da torre (maior)
        buildPlatform(center, -3, 3, PLATFORM_HEIGHT, TOWER_SIZE, TOWER_SIZE, PLATFORM_FLOOR);
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

    private void buildImprovedInternalStairs(Location center) {
        // Escada em espiral mais larga e confortável
        int[][] stairPositions = {
                // Nível 1-2
                {-2, -2}, {-1, -2}, {0, -2}, {1, -2},
                // Nível 3-4
                {1, -1}, {1, 0}, {1, 1},
                // Nível 5-6
                {0, 1}, {-1, 1}, {-2, 1},
                // Nível 7-8
                {-2, 0}, {-2, -1}, {-2, -2},
                // Nível 9-10 (nova escada para torre mais alta)
                {-1, -2}, {0, -2}, {1, -2},
                // Nível 11-12
                {1, -1}, {1, 0}, {1, 1}
        };

        BlockFace[] stairDirections = {
                BlockFace.EAST, BlockFace.EAST, BlockFace.EAST, BlockFace.EAST,
                BlockFace.SOUTH, BlockFace.SOUTH, BlockFace.SOUTH,
                BlockFace.WEST, BlockFace.WEST, BlockFace.WEST,
                BlockFace.NORTH, BlockFace.NORTH, BlockFace.NORTH,
                BlockFace.EAST, BlockFace.EAST, BlockFace.EAST,
                BlockFace.SOUTH, BlockFace.SOUTH, BlockFace.SOUTH
        };

        for (int i = 0; i < stairPositions.length; i++) {
            int x = stairPositions[i][0];
            int z = stairPositions[i][1];
            int y = (i / 3) + 1; // 3 degraus por nível

            // Escada
            setBlock(center, x, y, z, STAIRS);
            // Bloco de suporte abaixo
            setBlock(center, x, y-1, z, Material.OAK_PLANKS);

            // Corrimão nas laterais
            if (i % 3 == 0) { // A cada 3 degraus, coloca corrimão
                setBlock(center, x + 1, y, z, FENCE);
            }
        }

        // Aberturas entre os níveis
        for (int y = 3; y <= TOWER_HEIGHT; y += 3) {
            setBlock(center, -2, y, -2, Material.AIR);
        }
    }

    private void createArrowSlits(Location center) {
        int halfSize = TOWER_SIZE / 2;

        // Janelas em 4 níveis diferentes (devido à torre mais alta)
        int[] windowLevels = {3, 5, 8, 11};

        for (int level : windowLevels) {
            // Norte e Sul
            for (int x = -1; x <= 1; x++) {
                if (x != 0) { // Deixa espaço no meio
                    setBlock(center, x, level, -halfSize, Material.AIR);
                    setBlock(center, x, level, halfSize, Material.AIR);
                }
            }
            // Leste e Oeste
            for (int z = -1; z <= 1; z++) {
                if (z != 0) { // Deixa espaço no meio
                    setBlock(center, -halfSize, level, z, Material.AIR);
                    setBlock(center, halfSize, level, z, Material.AIR);
                }
            }
        }
    }

    private void buildBattlements(Location center) {
        int halfSize = TOWER_SIZE / 2;

        // Ameias mais elaboradas
        for (int x = -halfSize; x <= halfSize; x++) {
            for (int z = -halfSize; z <= halfSize; z++) {
                // Apenas nas bordas
                if (Math.abs(x) == halfSize || Math.abs(z) == halfSize) {
                    if ((x + z) % 2 == 0) { // Padrão xadrez
                        setBlock(center, x, PLATFORM_HEIGHT + 1, z, Material.COBBLESTONE_WALL);
                    } else {
                        setBlock(center, x, PLATFORM_HEIGHT + 1, z, Material.STONE_BRICK_SLAB);
                    }
                }
            }
        }

        // Mastro para bandeira central mais alto
        for (int y = PLATFORM_HEIGHT + 1; y <= PLATFORM_HEIGHT + 4; y++) {
            setBlock(center, 0, y, 0, FENCE);
        }
        setBlock(center, 0, PLATFORM_HEIGHT + 5, 0, Material.WHITE_BANNER);
    }

    private void buildSupplies(Location center) {
        // Baús na torre com flechas aleatórias
        buildArrowChest(center, 1, 1, 2); // Torre térreo
        buildArrowChest(center, -2, 6, 2); // Torre meio
        buildArrowChest(center, 2, 10, 0); // Torre topo

        // Barris nos postos de tiro também com flechas
        buildArrowBarrel(center, -8, 1, -8);
        buildArrowBarrel(center, 8, 1, -8);
        buildArrowBarrel(center, -8, 1, 8);
        buildArrowBarrel(center, 8, 1, 8);

        // Suportes de armadura (decoração)
        setBlock(center, -1, 1, 0, Material.ARMOR_STAND);
        setBlock(center, 1, 1, 0, Material.ARMOR_STAND);
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

    private void buildArrowBarrel(Location center, int x, int y, int z) {
        setBlock(center, x, y, z, Material.BARREL);

        // Adiciona flechas aleatórias ao barril
        Location barrelLocation = getRelativeLocation(center, x, y, z);
        BlockState blockState = barrelLocation.getBlock().getState();

        if (blockState instanceof Barrel) {
            Barrel barrel = (Barrel) blockState;
            Inventory inventory = barrel.getInventory();
            addRandomArrowsToInventory(inventory);
            barrel.update(true);
        }
    }

    private void addRandomArrowsToInventory(Inventory inventory) {
        inventory.clear(); // Limpa o inventário primeiro

        int arrowCount = getRandomArrowCount();
        int stacks = (int) Math.ceil(arrowCount / 64.0);

        for (int i = 0; i < stacks; i++) {
            int stackSize = Math.min(64, arrowCount - (i * 64));
            if (stackSize <= 0) break;

            ItemStack arrows = new ItemStack(Material.ARROW, stackSize);

            // Tenta colocar em slots aleatórios
            int slot = random.nextInt(inventory.getSize());
            int attempts = 0;
            while (inventory.getItem(slot) != null && attempts < inventory.getSize()) {
                slot = random.nextInt(inventory.getSize());
                attempts++;
            }

            if (attempts < inventory.getSize()) {
                inventory.setItem(slot, arrows);
            }
        }

        // Chance de adicionar outros itens relacionados a arqueiro
        if (random.nextDouble() < 0.3) { // 30% de chance
            ItemStack bow = new ItemStack(Material.BOW, 1);
            int slot = getRandomEmptySlot(inventory);
            if (slot != -1) {
                inventory.setItem(slot, bow);
            }
        }

        if (random.nextDouble() < 0.2) { // 20% de chance
            ItemStack spectralArrow = new ItemStack(Material.SPECTRAL_ARROW, random.nextInt(8) + 4);
            int slot = getRandomEmptySlot(inventory);
            if (slot != -1) {
                inventory.setItem(slot, spectralArrow);
            }
        }
    }

    private int getRandomEmptySlot(Inventory inventory) {
        for (int i = 0; i < 3; i++) { // Tenta 3 vezes
            int slot = random.nextInt(inventory.getSize());
            if (inventory.getItem(slot) == null) {
                return slot;
            }
        }
        return -1; // Não encontrou slot vazio
    }

    private int getRandomArrowCount() {
        // Retorna entre 8-64 flechas, com distribuição:
        // 50% chance: 8-16 flechas (poucas)
        // 30% chance: 16-32 flechas (médias)
        // 15% chance: 32-48 flechas (muitas)
        // 5% chance: 48-64 flechas (cheio)

        double chance = random.nextDouble();
        if (chance < 0.5) {
            return random.nextInt(9) + 8; // 8-16
        } else if (chance < 0.8) {
            return random.nextInt(17) + 16; // 16-32
        } else if (chance < 0.95) {
            return random.nextInt(17) + 32; // 32-48
        } else {
            return random.nextInt(17) + 48; // 48-64
        }
    }

    private Location getRelativeLocation(Location center, int x, int y, int z) {
        return center.clone().add(x, y, z);
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

        // Novos alvos devido à área maior
        buildTarget(center, -9, -9, 2);
        buildTarget(center, 9, -9, 2);
        buildTarget(center, -9, 9, 2);
        buildTarget(center, 9, 9, 2);
    }

    private void buildTarget(Location center, int x, int z, int height) {
        // Base de palha
        for (int y = 1; y <= height; y++) {
            setBlock(center, x, y, z, Material.HAY_BLOCK);
        }
        // Alvo no topo
        setBlock(center, x, height + 1, z, Material.TARGET);

        // Plataforma de acesso ao alvo
        setBlock(center, x, 0, z, Material.OAK_PLANKS);
        setBlock(center, x + 1, 0, z, Material.OAK_PLANKS);
        setBlock(center, x - 1, 0, z, Material.OAK_PLANKS);
        setBlock(center, x, 0, z + 1, Material.OAK_PLANKS);
        setBlock(center, x, 0, z - 1, Material.OAK_PLANKS);
    }

    private void buildShootingPlatforms(Location center) {
        buildShootingPlatform(center, -8, -8); // NE
        buildShootingPlatform(center, 8, -8);  // NW
        buildShootingPlatform(center, -8, 8);  // SE
        buildShootingPlatform(center, 8, 8);   // SW

        // Novas plataformas devido à área maior
        buildShootingPlatform(center, -5, -8); // Centro norte
        buildShootingPlatform(center, 5, -8);  // Centro norte
        buildShootingPlatform(center, -8, -5); // Centro oeste
        buildShootingPlatform(center, -8, 5);  // Centro oeste
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

        // Suporte para arco
        setBlock(center, centerX, 2, centerZ, Material.ITEM_FRAME);
    }

    private void buildLighting(Location center) {
        // Lanternas nos cantos da torre (nível 3)
        setBlock(center, -4, 3, -4, Material.LANTERN);
        setBlock(center, 4, 3, -4, Material.LANTERN);
        setBlock(center, -4, 3, 4, Material.LANTERN);
        setBlock(center, 4, 3, 4, Material.LANTERN);

        // Lanternas nos cantos da torre (nível 7)
        setBlock(center, -4, 7, -4, Material.LANTERN);
        setBlock(center, 4, 7, -4, Material.LANTERN);
        setBlock(center, -4, 7, 4, Material.LANTERN);
        setBlock(center, 4, 7, 4, Material.LANTERN);

        // Lanternas nos cantos da torre (nível 11)
        setBlock(center, -4, 11, -4, Material.LANTERN);
        setBlock(center, 4, 11, -4, Material.LANTERN);
        setBlock(center, -4, 11, 4, Material.LANTERN);
        setBlock(center, 4, 11, 4, Material.LANTERN);

        // Iluminação adicional no perímetro
        for (int x = -FIELD_RADIUS + 2; x <= FIELD_RADIUS - 2; x += 4) {
            setBlock(center, x, 2, -FIELD_RADIUS + 1, Material.TORCH);
            setBlock(center, x, 2, FIELD_RADIUS - 1, Material.TORCH);
        }
        for (int z = -FIELD_RADIUS + 2; z <= FIELD_RADIUS - 2; z += 4) {
            setBlock(center, -FIELD_RADIUS + 1, 2, z, Material.TORCH);
            setBlock(center, FIELD_RADIUS - 1, 2, z, Material.TORCH);
        }

        // Iluminação nas escadas internas
        for (int y = 2; y <= TOWER_HEIGHT; y += 3) {
            setBlock(center, -2, y, -2, Material.TORCH);
        }
    }

    private void buildPerimeter(Location center) {
        // Cerca norte e sul
        for (int x = -FIELD_RADIUS; x <= FIELD_RADIUS; x++) {
            if (x % 2 == 0 && Math.abs(x) > 3) {
                setBlock(center, x, 1, -FIELD_RADIUS, FENCE);
                setBlock(center, x, 1, FIELD_RADIUS, FENCE);
            }
        }

        // Cerca leste e oeste
        for (int z = -FIELD_RADIUS; z <= FIELD_RADIUS; z++) {
            if (z % 2 == 0 && Math.abs(z) > 3) {
                setBlock(center, -FIELD_RADIUS, 1, z, FENCE);
                setBlock(center, FIELD_RADIUS, 1, z, FENCE);
            }
        }

        // Portões de acesso aos alvos
        setBlock(center, -3, 1, -FIELD_RADIUS, Material.AIR);
        setBlock(center, -3, 2, -FIELD_RADIUS, Material.AIR);
        setBlock(center, 3, 1, -FIELD_RADIUS, Material.AIR);
        setBlock(center, 3, 2, -FIELD_RADIUS, Material.AIR);
        setBlock(center, -FIELD_RADIUS, 1, -3, Material.AIR);
        setBlock(center, -FIELD_RADIUS, 2, -3, Material.AIR);
        setBlock(center, FIELD_RADIUS, 1, -3, Material.AIR);
        setBlock(center, FIELD_RADIUS, 2, -3, Material.AIR);
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
        setBlock(center, startX, 1, startZ + 2, Material.SPRUCE_STAIRS);

        // Mesa central
        setBlock(center, startX + 1, 1, startZ + 1, Material.OAK_PRESSURE_PLATE);
        setBlock(center, startX + 1, 2, startZ + 1, Material.TORCH);

        // Luminária suspensa
        setBlock(center, startX + 1, 4, startZ + 1, Material.OAK_FENCE);
        setBlock(center, startX + 1, 5, startZ + 1, Material.LANTERN);

        // Baú de suprimentos na área de descanso
        setBlock(center, startX + 2, 1, startZ + 2, Material.CHEST);

        // Tapete decorativo
        setBlock(center, startX + 1, 1, startZ, Material.WHITE_CARPET);
        setBlock(center, startX + 2, 1, startZ, Material.WHITE_CARPET);
    }

    private void buildBowRacks(Location center) {
        // Suportes para arcos nas paredes internas da torre
        int[][] rackPositions = {
                {-3, 0, 2},  // Parede oeste
                {3, 0, 2},   // Parede leste
                {0, 0, -3},  // Parede norte
                {0, 0, 3}    // Parede sul
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

        // Suportes nas plataformas de tiro
        setBlock(center, -5, 2, -8, Material.ITEM_FRAME);
        setBlock(center, 5, 2, -8, Material.ITEM_FRAME);
        setBlock(center, -8, 2, -5, Material.ITEM_FRAME);
        setBlock(center, -8, 2, 5, Material.ITEM_FRAME);
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

        // Estátuas decorativas de arqueiros nos cantos
        buildArcherStatue(center, -9, 1, -9);
        buildArcherStatue(center, 9, 1, -9);

        // Tabela de pontuação
        setBlock(center, 0, 1, -8, Material.OAK_WALL_SIGN);
    }

    private void buildArcherStatue(Location center, int x, int y, int z) {
        // Base da estátua
        setBlock(center, x, y, z, Material.STONE_BRICKS);
        setBlock(center, x, y + 1, z, Material.STONE_BRICK_SLAB);
        // Corpo
        setBlock(center, x, y + 2, z, Material.ARMOR_STAND);
    }
}