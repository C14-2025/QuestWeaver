package br.dev.projetoc14.quest.structures;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.Barrel;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Inventory;

import java.util.Random;

/**
 * Campo de treinamento para arqueiros - Versão melhorada com escadas na parede
 */
public class ArcherTrainingGrounds extends QuestStructure {

    // Constantes aumentadas
    private static final int TOWER_SIZE = 7;
    private static final int TOWER_HEIGHT = 15; // Aumentado para 15 andares
    private static final int PLATFORM_HEIGHT = 16;
    private static final int FIELD_RADIUS = 12; // Campo maior

    // Materiais
    private static final Material TOWER_WALL = Material.COBBLESTONE;
    private static final Material TOWER_CORNER = Material.STONE_BRICKS;
    private static final Material MAIN_FLOOR = Material.STONE_BRICKS;
    private static final Material FLOOR_PATTERN = Material.CRACKED_STONE_BRICKS;
    private static final Material FENCE = Material.OAK_FENCE;
    private static final Material PLATFORM_FLOOR = Material.OAK_PLANKS;
    private static final Material LADDER = Material.LADDER;

    private Random random = new Random();

    public ArcherTrainingGrounds() {
        super("Campo de Treinamento de Arqueiros", 25, 18, 27); // Dimensões aumentadas
    }

    @Override
    protected void build(Location center) {
        // Limpeza primeiro (novo método)
        clearArea(center);

        buildEntrance(center);
        buildMainFloor(center);
        buildCentralTower(center);
        buildWallLadders(center); // Escadas na parede
        buildTowerFloors(center); // Andares decorados
        buildTargets(center);
        buildShootingPlatforms(center);
        buildLighting(center);
        buildSupplies(center);
        buildPerimeter(center);
        buildRestArea(center);
        buildBowRacks(center);
        addDecorations(center);
        addNaturalDetails(center); // Adicionado aqui
        createArrowSlits(center); // Adicionado aqui
    }

    private void clearArea(Location center) {
        // Limpa uma área maior que a estrutura
        for (int x = -FIELD_RADIUS - 2; x <= FIELD_RADIUS + 2; x++) {
            for (int z = -FIELD_RADIUS - 2; z <= FIELD_RADIUS + 2; z++) {
                for (int y = -2; y <= TOWER_HEIGHT + 5; y++) {
                    setBlock(center, x, y, z, Material.AIR);
                }
                // Base sólida
                setBlock(center, x, -2, z, Material.STONE);
            }
        }
    }

    private void buildEntrance(Location center) {
        // Caminho de entrada
        for (int z = -17; z < -12; z++) {
            for (int x = -1; x <= 1; x++) {
                setBlock(center, x, 0, z, Material.GRAVEL);
            }
            // Cercas laterais
            buildFenceColumn(center, -2, z);
            buildFenceColumn(center, 2, z);
        }

        // Portão de entrada
        buildGatePillar(center, -2, -12);
        buildGatePillar(center, 2, -12);

        // Lintel do portão
        setBlock(center, -1, 3, -12, Material.OAK_SLAB);
        setBlock(center, 0, 3, -12, Material.OAK_PLANKS);
        setBlock(center, 1, 3, -12, Material.OAK_SLAB);

        // Lanternas decorativas
        setBlock(center, -2, 2, -13, Material.LANTERN);
        setBlock(center, 2, 2, -13, Material.LANTERN);
    }

    private void buildMainFloor(Location center) {
        for (int x = -FIELD_RADIUS; x <= FIELD_RADIUS; x++) {
            for (int z = -FIELD_RADIUS; z <= FIELD_RADIUS; z++) {
                Material floorMaterial = MAIN_FLOOR;

                // Área central da torre
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

        // Entrada da torre
        setBlock(center, -1, 1, -3, Material.AIR);
        setBlock(center, 0, 1, -3, Material.AIR);
        setBlock(center, 1, 1, -3, Material.AIR);
        setBlock(center, -1, 2, -3, Material.AIR);
        setBlock(center, 0, 2, -3, Material.AIR);
        setBlock(center, 1, 2, -3, Material.AIR);

        // Topo da torre
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

        // Reforço nos cantos
        if (y % 2 == 0) {
            setBlock(center, -halfSize, y, -halfSize, TOWER_CORNER);
            setBlock(center, halfSize, y, -halfSize, TOWER_CORNER);
            setBlock(center, -halfSize, y, halfSize, TOWER_CORNER);
            setBlock(center, halfSize, y, halfSize, TOWER_CORNER);
        }
    }

    private void buildWallLadders(Location center) {
        // Escada na parede leste (de -2 a 2 no eixo Z)
        for (int y = 1; y < TOWER_HEIGHT; y++) {
            for (int z = -2; z <= 2; z++) {
                // Escada principal
                setBlock(center, 3, y, z, LADDER);

                // Plataforma de descanso a cada 4 degraus
                if (y % 4 == 0) {
                    setBlock(center, 2, y, z, PLATFORM_FLOOR);
                    setBlock(center, 1, y, z, PLATFORM_FLOOR);

                    // Abertura para passar
                    setBlock(center, 2, y + 1, z, Material.AIR);
                    setBlock(center, 1, y + 1, z, Material.AIR);
                }
            }

            // Corrimão de segurança
            setBlock(center, 3, y, -3, FENCE);
            setBlock(center, 3, y, 3, FENCE);
        }

        // Aberturas entre os andares
        for (int y = 4; y < TOWER_HEIGHT; y += 4) {
            setBlock(center, 2, y, 0, Material.AIR);
            setBlock(center, 1, y, 0, Material.AIR);
        }
    }

    private void buildTowerFloors(Location center) {
        // 5 andares decorados (níveis 3, 6, 9, 12, 15)
        int[] floorLevels = {3, 6, 9, 12, 15};

        for (int i = 0; i < floorLevels.length; i++) {
            int floorY = floorLevels[i];
            buildPlatform(center, -2, 2, floorY, 5, 5, PLATFORM_FLOOR);
            decorateTowerFloor(center, floorY, i + 1);
        }
    }

    private void decorateTowerFloor(Location center, int floorY, int floorNumber) {
        switch (floorNumber) {
            case 1:
                decorateArcheryStorage(center, floorY); // Armazenamento
                break;
            case 2:
                decorateTargetPractice(center, floorY); // Prática de alvo
                break;
            case 3:
                decorateBowCrafting(center, floorY);   // Fabricação
                break;
            case 4:
                decorateStrategyRoom(center, floorY);  // Estratégia
                break;
            case 5:
                decorateObservationDeck(center, floorY); // Observação
                break;
        }
    }

    private void decorateArcheryStorage(Location center, int floorY) {
        // Armazenamento de flechas e equipamentos
        setBlock(center, -1, floorY, 0, Material.CHEST);
        setBlock(center, 0, floorY, 0, Material.BARREL);
        setBlock(center, 1, floorY, 0, Material.CHEST);

        // Suportes de arcos
        setBlock(center, -2, floorY + 1, 1, Material.ITEM_FRAME);
        setBlock(center, -2, floorY + 1, -1, Material.ITEM_FRAME);
        setBlock(center, 2, floorY + 1, 1, Material.ITEM_FRAME);
        setBlock(center, 2, floorY + 1, -1, Material.ITEM_FRAME);

        // Mesa de trabalho
        setBlock(center, 0, floorY, 2, Material.CRAFTING_TABLE);

        // Iluminação
        setBlock(center, 0, floorY + 1, 0, Material.LANTERN);
    }

    private void decorateTargetPractice(Location center, int floorY) {
        // Área de prática interna
        setBlock(center, -2, floorY, 0, Material.TARGET);
        setBlock(center, 2, floorY, 0, Material.TARGET);
        setBlock(center, 0, floorY, -2, Material.TARGET);
        setBlock(center, 0, floorY, 2, Material.TARGET);

        // Marcadores de posição
        for (int x = -1; x <= 1; x += 2) {
            for (int z = -1; z <= 1; z += 2) {
                setBlock(center, x, floorY, z, Material.OAK_PRESSURE_PLATE);
            }
        }

        // Painel de pontuação
        setBlock(center, 0, floorY + 1, -1, Material.OAK_SIGN);
    }

    private void decorateBowCrafting(Location center, int floorY) {
        // Área de fabricação de arcos
        setBlock(center, -1, floorY, 1, Material.FLETCHING_TABLE);
        setBlock(center, 1, floorY, 1, Material.SMITHING_TABLE);
        setBlock(center, 0, floorY, 1, Material.CARTOGRAPHY_TABLE);

        // Estante de materiais
        setBlock(center, -2, floorY, 2, Material.BOOKSHELF);
        setBlock(center, 2, floorY, 2, Material.BOOKSHELF);

        // Baú de suprimentos
        setBlock(center, 0, floorY, -1, Material.CHEST);

        // Lâmpada de trabalho
        setBlock(center, 0, floorY + 2, 0, Material.LANTERN);
    }

    private void decorateStrategyRoom(Location center, int floorY) {
        // Sala de estratégia e mapas
        setBlock(center, 0, floorY, 0, Material.OAK_PRESSURE_PLATE); // Mesa central

        // Mapas na parede
        for (int x = -2; x <= 2; x += 4) {
            setBlock(center, x, floorY + 1, 0, Material.ITEM_FRAME);
        }

        // Estantes de livros
        setBlock(center, -1, floorY, 2, Material.BOOKSHELF);
        setBlock(center, 1, floorY, 2, Material.BOOKSHELF);
        setBlock(center, -1, floorY, -2, Material.BOOKSHELF);
        setBlock(center, 1, floorY, -2, Material.BOOKSHELF);

        // Cadeiras
        setBlock(center, -1, floorY, 1, Material.OAK_STAIRS);
        setBlock(center, 1, floorY, 1, Material.OAK_STAIRS);
        setBlock(center, -1, floorY, -1, Material.OAK_STAIRS);
        setBlock(center, 1, floorY, -1, Material.OAK_STAIRS);
    }

    private void decorateObservationDeck(Location center, int floorY) {
        // Deck de observação (penúltimo andar)
        // Varanda de observação
        for (int x = -3; x <= 3; x++) {
            setBlock(center, x, floorY, 3, PLATFORM_FLOOR);
            setBlock(center, x, floorY + 1, 3, FENCE);
        }

        // Telescópio/binóculo
        setBlock(center, 0, floorY, 2, Material.OBSERVER);

        // Mesa de observação
        setBlock(center, -1, floorY, 1, Material.CARTOGRAPHY_TABLE);
        setBlock(center, 1, floorY, 1, Material.COMPOSTER); // Suporte para mapas

        // Estante de anotações
        setBlock(center, 2, floorY, 0, Material.BOOKSHELF);
    }

    private void buildBattlements(Location center) {
        int halfSize = TOWER_SIZE / 2;

        // Ameias elaboradas
        for (int x = -halfSize; x <= halfSize; x++) {
            for (int z = -halfSize; z <= halfSize; z++) {
                if (Math.abs(x) == halfSize || Math.abs(z) == halfSize) {
                    if ((x + z) % 2 == 0) {
                        setBlock(center, x, PLATFORM_HEIGHT + 1, z, Material.COBBLESTONE_WALL);
                    } else {
                        setBlock(center, x, PLATFORM_HEIGHT + 1, z, Material.STONE_BRICK_SLAB);
                    }
                }
            }
        }

        // Mastro para bandeira
        for (int y = PLATFORM_HEIGHT + 1; y <= PLATFORM_HEIGHT + 4; y++) {
            setBlock(center, 0, y, 0, FENCE);
        }
        setBlock(center, 0, PLATFORM_HEIGHT + 5, 0, Material.WHITE_BANNER);
    }

    private void buildTargets(Location center) {
        // Alvos próximos
        buildTarget(center, -6, -6, 1);
        buildTarget(center, 6, -6, 1);
        buildTarget(center, -6, 6, 1);
        buildTarget(center, 6, 6, 1);

        // Alvos médios
        buildTarget(center, -9, 0, 2);
        buildTarget(center, 9, 0, 2);

        // Alvos distantes
        buildTarget(center, 0, -11, 3);
        buildTarget(center, 0, 11, 3);

        // Alvos extras devido à área maior
        buildTarget(center, -11, -11, 2);
        buildTarget(center, 11, -11, 2);
        buildTarget(center, -11, 11, 2);
        buildTarget(center, 11, 11, 2);
    }

    private void buildTarget(Location center, int x, int z, int height) {
        // Base de palha
        for (int y = 1; y <= height; y++) {
            setBlock(center, x, y, z, Material.HAY_BLOCK);
        }
        // Alvo no topo
        setBlock(center, x, height + 1, z, Material.TARGET);

        // Plataforma de acesso
        setBlock(center, x, 0, z, Material.OAK_PLANKS);
        setBlock(center, x + 1, 0, z, Material.OAK_PLANKS);
        setBlock(center, x - 1, 0, z, Material.OAK_PLANKS);
        setBlock(center, x, 0, z + 1, Material.OAK_PLANKS);
        setBlock(center, x, 0, z - 1, Material.OAK_PLANKS);
    }

    private void buildSupplies(Location center) {
        // Baús na torre com flechas aleatórias
        buildArrowChest(center, -1, 3, 0); // Primeiro andar
        buildArrowChest(center, 0, 6, 0);  // Segundo andar
        buildArrowChest(center, 1, 12, 0); // Quarto andar
        buildArrowChest(center, 0, 15, 0); // Quinto andar

        // Barris nos postos de tiro
        buildArrowBarrel(center, -9, 1, -9);
        buildArrowBarrel(center, 9, 1, -9);
        buildArrowBarrel(center, -9, 1, 9);
        buildArrowBarrel(center, 9, 1, 9);

        // Suportes de armadura
        setBlock(center, -1, 1, 0, Material.ARMOR_STAND);
        setBlock(center, 1, 1, 0, Material.ARMOR_STAND);
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

    private void buildArrowBarrel(Location center, int x, int y, int z) {
        setBlock(center, x, y, z, Material.BARREL);

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

        // Chance de arcos e flechas especiais
        if (random.nextDouble() < 0.3) {
            ItemStack bow = new ItemStack(Material.BOW, 1);
            int slot = getRandomEmptySlot(inventory);
            if (slot != -1) {
                inventory.setItem(slot, bow);
            }
        }

        if (random.nextDouble() < 0.2) {
            ItemStack spectralArrow = new ItemStack(Material.SPECTRAL_ARROW, random.nextInt(8) + 4);
            int slot = getRandomEmptySlot(inventory);
            if (slot != -1) {
                inventory.setItem(slot, spectralArrow);
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

    private void buildShootingPlatforms(Location center) {
        buildShootingPlatform(center, -9, -9); // NE
        buildShootingPlatform(center, 9, -9);  // NW
        buildShootingPlatform(center, -9, 9);  // SE
        buildShootingPlatform(center, 9, 9);   // SW

        // Novas plataformas devido à área maior
        buildShootingPlatform(center, -6, -9); // Centro norte
        buildShootingPlatform(center, 6, -9);  // Centro norte
        buildShootingPlatform(center, -9, -6); // Centro oeste
        buildShootingPlatform(center, -9, 6);  // Centro oeste
    }

    private void buildShootingPlatform(Location center, int centerX, int centerZ) {
        // Plataforma 3x3
        buildPlatform(center, centerX - 1, centerZ - 1, 1, 3, 3, Material.SPRUCE_PLANKS);

        // Cercas de proteção
        setBlock(center, centerX, 2, centerZ - 1, FENCE);
        setBlock(center, centerX, 2, centerZ + 1, FENCE);
        setBlock(center, centerX - 1, 2, centerZ, FENCE);
        setBlock(center, centerX + 1, 2, centerZ, FENCE);

        // Suporte para arco
        setBlock(center, centerX, 2, centerZ, Material.ITEM_FRAME);

        // Iluminação
        setBlock(center, centerX, 2, centerZ, Material.TORCH);
    }

    private void buildLighting(Location center) {
        // Lanternas nos cantos da torre em múltiplos níveis
        int[] lanternLevels = {3, 6, 9, 12, 15};
        for (int level : lanternLevels) {
            setBlock(center, -4, level, -4, Material.LANTERN);
            setBlock(center, 4, level, -4, Material.LANTERN);
            setBlock(center, -4, level, 4, Material.LANTERN);
            setBlock(center, 4, level, 4, Material.LANTERN);
        }

        // Iluminação adicional no perímetro
        for (int x = -FIELD_RADIUS + 2; x <= FIELD_RADIUS - 2; x += 4) {
            setBlock(center, x, 2, -FIELD_RADIUS + 1, Material.TORCH);
            setBlock(center, x, 2, FIELD_RADIUS - 1, Material.TORCH);
        }
        for (int z = -FIELD_RADIUS + 2; z <= FIELD_RADIUS - 2; z += 4) {
            setBlock(center, -FIELD_RADIUS + 1, 2, z, Material.TORCH);
            setBlock(center, FIELD_RADIUS - 1, 2, z, Material.TORCH);
        }

        // Iluminação nas escadas
        for (int y = 2; y <= TOWER_HEIGHT; y += 4) {
            setBlock(center, 3, y, 0, Material.TORCH);
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
        int startX = 8;
        int startZ = 8;

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

        // Vasos com flores
        setBlock(center, startX, 1, startZ + 3, Material.FLOWER_POT);
        setBlock(center, startX + 2, 1, startZ + 3, Material.FLOWER_POT);
    }

    private void buildBowRacks(Location center) {
        // Suportes para arcos nas paredes internas da torre
        int[][] rackPositions = {
                {-3, 1, 2},  // Parede oeste - andar 1
                {3, 1, 2},   // Parede leste - andar 1
                {0, 1, -3},  // Parede norte - andar 1
                {0, 1, 3},   // Parede sul - andar 1

                {-3, 4, 2},  // Parede oeste - andar 2
                {3, 4, 2},   // Parede leste - andar 2
                {-3, 7, 2},  // Parede oeste - andar 3
                {3, 7, 2},   // Parede leste - andar 3
        };

        for (int[] pos : rackPositions) {
            int x = pos[0];
            int y = pos[1];
            int z = pos[2];

            // Suporte inferior
            setBlock(center, x, y, z, Material.OAK_FENCE);
            // Item frame para arco
            setBlock(center, x, y + 1, z, Material.ITEM_FRAME);
        }

        // Suportes adicionais nos postos de tiro
        setBlock(center, -9, 2, -8, Material.ITEM_FRAME);
        setBlock(center, 9, 2, -8, Material.ITEM_FRAME);
        setBlock(center, -9, 2, 8, Material.ITEM_FRAME);
        setBlock(center, 9, 2, 8, Material.ITEM_FRAME);

        // Suportes nas plataformas de tiro
        setBlock(center, -6, 2, -9, Material.ITEM_FRAME);
        setBlock(center, 6, 2, -9, Material.ITEM_FRAME);
        setBlock(center, -9, 2, -6, Material.ITEM_FRAME);
        setBlock(center, -9, 2, 6, Material.ITEM_FRAME);
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
        setBlock(center, -3, 1, -10, Material.FLOWER_POT);
        setBlock(center, 3, 1, -10, Material.FLOWER_POT);

        // Placas informativas
        setBlock(center, -1, 1, -11, Material.OAK_SIGN);
        setBlock(center, 1, 1, -11, Material.OAK_SIGN);

        // Estátuas decorativas de arqueiros nos cantos
        buildArcherStatue(center, -11, 1, -11);
        buildArcherStatue(center, 11, 1, -11);

        // Tabela de pontuação central
        setBlock(center, 0, 1, -10, Material.OAK_WALL_SIGN);

        // Dummies de treino adicionais
        buildTrainingDummy(center, -4, 1, -8);
        buildTrainingDummy(center, 4, 1, -8);

        // Barris de água para limpar flechas
        setBlock(center, -8, 1, 0, Material.WATER_CAULDRON);
        setBlock(center, 8, 1, 0, Material.WATER_CAULDRON);
    }

    private void buildArcherStatue(Location center, int x, int y, int z) {
        // Base da estátua
        setBlock(center, x, y, z, Material.STONE_BRICKS);
        setBlock(center, x, y + 1, z, Material.STONE_BRICK_SLAB);
        // Corpo (armor stand)
        setBlock(center, x, y + 2, z, Material.ARMOR_STAND);
        // Arco na mão
        setBlock(center, x, y + 2, z + 1, Material.BOW);
    }

    private void buildTrainingDummy(Location center, int x, int y, int z) {
        // Base do dummy
        setBlock(center, x, y, z, Material.OAK_FENCE);
        setBlock(center, x, y + 1, z, Material.OAK_FENCE);
        // "Cabeça" do dummy
        setBlock(center, x, y + 2, z, Material.HAY_BLOCK);
        // Alvo no peito
        setBlock(center, x, y + 1, z + 1, Material.TARGET);
    }

    // Método auxiliar para criar janelas (seteiras)
    private void createArrowSlits(Location center) {
        int halfSize = TOWER_SIZE / 2;

        // Janelas em múltiplos níveis
        int[] windowLevels = {2, 5, 8, 11, 14};

        for (int level : windowLevels) {
            // Norte e Sul - janelas estreitas
            for (int x = -1; x <= 1; x++) {
                if (x != 0) {
                    setBlock(center, x, level, -halfSize, Material.AIR);
                    setBlock(center, x, level, halfSize, Material.AIR);
                }
            }
            // Leste e Oeste - janelas estreitas
            for (int z = -1; z <= 1; z++) {
                if (z != 0) {
                    setBlock(center, -halfSize, level, z, Material.AIR);
                    setBlock(center, halfSize, level, z, Material.AIR);
                }
            }
        }
    }

    // Método para adicionar detalhes naturais ao redor
    private void addNaturalDetails(Location center) {
        // Algumas árvores ao redor do campo
        buildSmallTree(center, -8, 1, 5);
        buildSmallTree(center, 8, 1, 5);
        buildSmallTree(center, -8, 1, -5);
        buildSmallTree(center, 8, 1, -5);

        // Arbustos decorativos
        for (int i = 0; i < 6; i++) {
            int x = random.nextInt(20) - 10;
            int z = random.nextInt(20) - 10;
            if (Math.abs(x) > 8 || Math.abs(z) > 8) {
                setBlock(center, x, 1, z, Material.OAK_LEAVES);
                setBlock(center, x, 0, z, Material.OAK_LOG);
            }
        }
    }

    private void buildSmallTree(Location center, int x, int y, int z) {
        // Tronco
        for (int i = 0; i < 4; i++) {
            setBlock(center, x, y + i, z, Material.OAK_LOG);
        }
        // Copa
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                for (int dy = 2; dy <= 4; dy++) {
                    if (!(dx == 0 && dz == 0 && dy == 2)) {
                        setBlock(center, x + dx, y + dy, z + dz, Material.OAK_LEAVES);
                    }
                }
            }
        }
    }
}