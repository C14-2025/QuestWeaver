package br.dev.projetoc14.quest.structures;

import org.bukkit.Location;
import org.bukkit.Material;

public class MageTower extends QuestStructure {

    public MageTower() {
        super("Torre Arcana", 9, 15, 9);
    }

    @Override
    protected void build(Location center) {
        // Base flutuante
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                setBlock(center, x, 5, z, Material.OBSIDIAN);
            }
        }

        // Pilares de cristal
        for(int y = 0; y < 12; y++) {
            setBlock(center, -3, y, -3, Material.AMETHYST_BLOCK);
            setBlock(center, 3, y, 3, Material.AMETHYST_BLOCK);
            setBlock(center, -3, y, 3, Material.AMETHYST_BLOCK);
            setBlock(center, 3, y, -3, Material.AMETHYST_BLOCK);
        }

        // O núcleo mágico
        setBlock(center, 0, 7, 0, Material.BEACON);
        setBlock(center, 0, 6, 0, Material.DIAMOND_BLOCK); // Para ativar o beacon visualmente

        // Vidro colorido
        for(int y = 6; y < 10; y++) {
            setBlock(center, 1, y, 0, Material.MAGENTA_STAINED_GLASS);
            setBlock(center, -1, y, 0, Material.MAGENTA_STAINED_GLASS);
            setBlock(center, 0, y, 1, Material.MAGENTA_STAINED_GLASS);
            setBlock(center, 0, y, -1, Material.MAGENTA_STAINED_GLASS);
        }
    }
}