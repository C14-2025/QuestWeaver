package br.dev.projetoc14.quest.structures;

import org.bukkit.Location;
import org.bukkit.Material;

/**
 * Campo de treinamento para arqueiros
 * Estrutura: Torre de observação com alvos
 */
public class ArcherTrainingGrounds extends QuestStructure {

    public ArcherTrainingGrounds() {
        super("Campo de Treinamento de Arqueiros", 15, 8, 15);
    }

    @Override
    protected void build(Location center) {
        // Piso de pedra
        for (int x = -7; x <= 7; x++) {
            for (int z = -7; z <= 7; z++) {
                setBlock(center, x, 0, z, Material.STONE_BRICKS);
            }
        }

        // Torre central (3x3)
        for (int y = 1; y <= 6; y++) {
            // Paredes da torre
            setBlock(center, -1, y, -1, Material.COBBLESTONE);
            setBlock(center, -1, y, 0, Material.COBBLESTONE);
            setBlock(center, -1, y, 1, Material.COBBLESTONE);
            setBlock(center, 1, y, -1, Material.COBBLESTONE);
            setBlock(center, 1, y, 0, Material.COBBLESTONE);
            setBlock(center, 1, y, 1, Material.COBBLESTONE);
            setBlock(center, 0, y, -1, Material.COBBLESTONE);
            setBlock(center, 0, y, 1, Material.COBBLESTONE);
        }

        // Topo da torre (plataforma)
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                setBlock(center, x, 7, z, Material.OAK_PLANKS);
            }
        }

        // Ameias (proteção)
        setBlock(center, -1, 8, -1, Material.COBBLESTONE_WALL);
        setBlock(center, 1, 8, -1, Material.COBBLESTONE_WALL);
        setBlock(center, -1, 8, 1, Material.COBBLESTONE_WALL);
        setBlock(center, 1, 8, 1, Material.COBBLESTONE_WALL);

        // Alvos de prática (fardos de feno)
        setBlock(center, -6, 1, -6, Material.HAY_BLOCK);
        setBlock(center, 6, 1, -6, Material.HAY_BLOCK);
        setBlock(center, -6, 1, 6, Material.HAY_BLOCK);
        setBlock(center, 6, 1, 6, Material.HAY_BLOCK);

        // Decoração: tochas
        setBlock(center, -2, 2, 0, Material.TORCH);
        setBlock(center, 2, 2, 0, Material.TORCH);
        setBlock(center, 0, 2, -2, Material.TORCH);
        setBlock(center, 0, 2, 2, Material.TORCH);

        // Baú com suprimentos (centro do piso térreo)
        setBlock(center, 0, 1, 0, Material.CHEST);
    }
}