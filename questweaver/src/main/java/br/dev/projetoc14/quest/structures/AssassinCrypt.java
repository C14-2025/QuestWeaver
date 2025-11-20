package br.dev.projetoc14.quest.structures;

import org.bukkit.Location;
import org.bukkit.Material;

/**
 * Cripta abandonada para assassinos (alternativa mais gótica)
 */
public class AssassinCrypt extends QuestStructure {

    public AssassinCrypt() {
        super("Cripta dos Assassinos", 11, 6, 11);
    }

    @Override
    protected void build(Location center) {
        // Base subterrânea - escava 2 blocos pra baixo
        for (int x = -5; x <= 5; x++) {
            for (int z = -5; z <= 5; z++) {
                // Limpa o ar
                setBlock(center, x, -2, z, Material.AIR);
                setBlock(center, x, -1, z, Material.AIR);

                // Piso de stone bricks
                setBlock(center, x, 0, z, Material.MOSSY_STONE_BRICKS);
            }
        }

        // Paredes da cripta
        for (int y = 1; y <= 4; y++) {
            // Parede norte
            for (int x = -5; x <= 5; x++) {
                if (x == 0 && y <= 2) continue; // Entrada
                setBlock(center, x, y, -5, Material.STONE_BRICKS);
            }

            // Parede sul
            for (int x = -5; x <= 5; x++) {
                setBlock(center, x, y, 5, Material.STONE_BRICKS);
            }

            // Parede leste
            for (int z = -5; z <= 5; z++) {
                setBlock(center, 5, y, z, Material.STONE_BRICKS);
            }

            // Parede oeste
            for (int z = -5; z <= 5; z++) {
                setBlock(center, -5, y, z, Material.STONE_BRICKS);
            }
        }

        // Teto
        for (int x = -5; x <= 5; x++) {
            for (int z = -5; z <= 5; z++) {
                setBlock(center, x, 5, z, Material.STONE_BRICKS);
            }
        }

        // Pilares internos (4 cantos)
        for (int y = 1; y <= 3; y++) {
            setBlock(center, -3, y, -3, Material.CHISELED_STONE_BRICKS);
            setBlock(center, 3, y, -3, Material.CHISELED_STONE_BRICKS);
            setBlock(center, -3, y, 3, Material.CHISELED_STONE_BRICKS);
            setBlock(center, 3, y, 3, Material.CHISELED_STONE_BRICKS);
        }

        // Sarcófagos (decoração)
        setBlock(center, -3, 1, 0, Material.CHISELED_STONE_BRICKS);
        setBlock(center, 3, 1, 0, Material.CHISELED_STONE_BRICKS);

        // Altar central (3x3)
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                setBlock(center, x, 1, z, Material.POLISHED_BLACKSTONE_SLAB);
            }
        }

        // Centro do altar (onde spawnam mobs)
        setBlock(center, 0, 1, 0, Material.CRYING_OBSIDIAN);

        // Iluminação sombria (tochas soul)
        setBlock(center, -4, 2, -4, Material.SOUL_TORCH);
        setBlock(center, 4, 2, -4, Material.SOUL_TORCH);
        setBlock(center, -4, 2, 4, Material.SOUL_TORCH);
        setBlock(center, 4, 2, 4, Material.SOUL_TORCH);

        // Teias de aranha (atmosfera)
        setBlock(center, -5, 4, -3, Material.COBWEB);
        setBlock(center, 5, 4, 3, Material.COBWEB);
        setBlock(center, -2, 4, 5, Material.COBWEB);
        setBlock(center, 2, 4, -5, Material.COBWEB);

        // Baú escondido atrás do pilar
        setBlock(center, -3, 1, -4, Material.CHEST);

        // Escadas de entrada (subindo pra fora)
        setBlock(center, 0, 1, -5, Material.STONE_BRICK_STAIRS);
        setBlock(center, 0, 2, -6, Material.STONE_BRICK_STAIRS);
    }
}