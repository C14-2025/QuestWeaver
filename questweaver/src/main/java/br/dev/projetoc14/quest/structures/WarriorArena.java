package br.dev.projetoc14.quest.structures;

import br.dev.projetoc14.items.ItemProtectionUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class WarriorArena extends QuestStructure {

    public WarriorArena() {
        super("Arena de Ferro", 15, 6, 15);
    }

    @Override
    protected void build(Location center) {
        // Limpa a área
        clear(center);

        // Chão da arena
        for (int x = -6; x <= 6; x++) {
            for (int z = -6; z <= 6; z++) {
                setBlock(center, x, 0, z, Material.COARSE_DIRT);
            }
        }

        // Pilares e cercas (Grades da Prisão)
        for (int x = -7; x <= 7; x++) {
            for (int z = -7; z <= 7; z++) {
                if (Math.abs(x) == 7 || Math.abs(z) == 7) {
                    // Base sólida
                    setBlock(center, x, 0, z, Material.STONE_BRICKS);

                    // Barras de ferro (paredes)
                    setBlock(center, x, 1, z, Material.IRON_BARS);
                    setBlock(center, x, 2, z, Material.IRON_BARS);
                    setBlock(center, x, 3, z, Material.IRON_BARS); // Aumentei a altura para ficar mais imponente

                    // Detalhes nos cantos
                    if (Math.abs(x) == 7 && Math.abs(z) == 7) {
                        setBlock(center, x, 1, z, Material.POLISHED_ANDESITE);
                        setBlock(center, x, 2, z, Material.POLISHED_ANDESITE);
                        setBlock(center, x, 3, z, Material.POLISHED_ANDESITE);
                        setBlock(center, x, 4, z, Material.CAMPFIRE); // Fogo nos cantos
                    }
                }
            }
        }

        // === ENTRADA DA ARENA ===
        // Removemos as barras de ferro em um dos lados para criar a entrada
        for (int x = -1; x <= 1; x++) {
            setBlock(center, x, 1, -7, Material.AIR);
            setBlock(center, x, 2, -7, Material.AIR);
            setBlock(center, x, 3, -7, Material.AIR);
            // Escada de pedra para subir na base
            setBlock(center, x, 0, -8, Material.STONE_BRICK_STAIRS);
        }

        // Centro com Bau de Loot
        setBlock(center, 0, 0, 0, Material.GILDED_BLACKSTONE);
        setBlock(center, 0, 1, 0, Material.CHEST);
        setupLootChest(center, 0, 1, 0);
    }

    private void setupLootChest(Location center, int x, int y, int z) {
        Location chestLoc = center.clone().add(x, y, z);
        BlockState blockState = chestLoc.getBlock().getState();

        if (blockState instanceof Chest chest) {
            Inventory inv = chest.getInventory();
            inv.clear();

            // Loot extra para o guerreiro
            inv.setItem(2, ItemProtectionUtil.makeDroppable(new ItemStack(Material.COOKED_BEEF, 4)));

            chest.update(true);
        }
    }
}