package br.dev.projetoc14.quest.structures;

import org.bukkit.Location;
import org.bukkit.Material;

/**
 * Cripta abandonada para assassinos (versão expandida)
 */
public class AssassinCrypt extends QuestStructure {

    public AssassinCrypt() {
        super("Cripta dos Assassinos", 17, 8, 20);
    }

    @Override
    protected void build(Location center) {
        // ===== CORREDOR DE ENTRADA =====
        // Cria corredor de acesso (5 blocos pra fora)
        for (int z = -10; z < -6; z++) {
            for (int x = -2; x <= 2; x++) {
                // Limpa ar do corredor
                setBlock(center, x, -1, z, Material.AIR);
                setBlock(center, x, 0, z, Material.AIR);
                setBlock(center, x, 1, z, Material.AIR);
                setBlock(center, x, 2, z, Material.AIR);

                // Piso do corredor
                setBlock(center, x, -2, z, Material.POLISHED_ANDESITE);
            }

            // Paredes do corredor
            for (int y = -1; y <= 2; y++) {
                setBlock(center, -3, y, z, Material.STONE_BRICKS);
                setBlock(center, 3, y, z, Material.STONE_BRICKS);
            }

            // Teto do corredor
            setBlock(center, -3, 3, z, Material.STONE_BRICKS);
            setBlock(center, -2, 3, z, Material.STONE_BRICK_SLAB);
            setBlock(center, -1, 3, z, Material.AIR);
            setBlock(center, 0, 3, z, Material.AIR);
            setBlock(center, 1, 3, z, Material.AIR);
            setBlock(center, 2, 3, z, Material.STONE_BRICK_SLAB);
            setBlock(center, 3, 3, z, Material.STONE_BRICKS);
        }

        // Portal de entrada decorado
        setBlock(center, -3, -1, -6, Material.CHISELED_STONE_BRICKS);
        setBlock(center, -3, 0, -6, Material.CHISELED_STONE_BRICKS);
        setBlock(center, -3, 1, -6, Material.CHISELED_STONE_BRICKS);
        setBlock(center, -3, 2, -6, Material.CHISELED_STONE_BRICKS);

        setBlock(center, 3, -1, -6, Material.CHISELED_STONE_BRICKS);
        setBlock(center, 3, 0, -6, Material.CHISELED_STONE_BRICKS);
        setBlock(center, 3, 1, -6, Material.CHISELED_STONE_BRICKS);
        setBlock(center, 3, 2, -6, Material.CHISELED_STONE_BRICKS);

        // Arco superior da entrada
        setBlock(center, -2, 3, -6, Material.CHISELED_STONE_BRICKS);
        setBlock(center, -1, 3, -6, Material.CHISELED_STONE_BRICKS);
        setBlock(center, 0, 3, -6, Material.CHISELED_STONE_BRICKS);
        setBlock(center, 1, 3, -6, Material.CHISELED_STONE_BRICKS);
        setBlock(center, 2, 3, -6, Material.CHISELED_STONE_BRICKS);

        // Lanternas na entrada
        setBlock(center, -3, 1, -8, Material.SOUL_LANTERN);
        setBlock(center, 3, 1, -8, Material.SOUL_LANTERN);

        // ===== SALA PRINCIPAL (MAIOR) =====
        // Base subterrânea - escava 3 blocos pra baixo
        for (int x = -7; x <= 7; x++) {
            for (int z = -6; z <= 8; z++) {
                // Limpa o ar (mais alto)
                setBlock(center, x, -3, z, Material.AIR);
                setBlock(center, x, -2, z, Material.AIR);
                setBlock(center, x, -1, z, Material.AIR);

                // Piso variado
                if ((x + z) % 3 == 0) {
                    setBlock(center, x, 0, z, Material.CRACKED_STONE_BRICKS);
                } else {
                    setBlock(center, x, 0, z, Material.MOSSY_STONE_BRICKS);
                }
            }
        }

        // Paredes da cripta (mais altas)
        for (int y = 1; y <= 5; y++) {
            // Parede norte (com entrada)
            for (int x = -7; x <= 7; x++) {
                if (x >= -1 && x <= 1 && y <= 3) continue; // Entrada maior
                setBlock(center, x, y, -6, Material.STONE_BRICKS);
            }

            // Parede sul
            for (int x = -7; x <= 7; x++) {
                setBlock(center, x, y, 8, Material.STONE_BRICKS);
            }

            // Parede leste
            for (int z = -6; z <= 8; z++) {
                setBlock(center, 7, y, z, Material.STONE_BRICKS);
            }

            // Parede oeste
            for (int z = -6; z <= 8; z++) {
                setBlock(center, -7, y, z, Material.STONE_BRICKS);
            }
        }

        // Teto
        for (int x = -7; x <= 7; x++) {
            for (int z = -6; z <= 8; z++) {
                if ((x + z) % 4 == 0) {
                    setBlock(center, x, 6, z, Material.CRACKED_STONE_BRICKS);
                } else {
                    setBlock(center, x, 6, z, Material.STONE_BRICKS);
                }
            }
        }

        // Pilares internos (6 pilares)
        for (int y = 1; y <= 4; y++) {
            setBlock(center, -4, y, -3, Material.POLISHED_BLACKSTONE);
            setBlock(center, 4, y, -3, Material.POLISHED_BLACKSTONE);
            setBlock(center, -4, y, 2, Material.POLISHED_BLACKSTONE);
            setBlock(center, 4, y, 2, Material.POLISHED_BLACKSTONE);
            setBlock(center, -4, y, 6, Material.POLISHED_BLACKSTONE);
            setBlock(center, 4, y, 6, Material.POLISHED_BLACKSTONE);
        }

        // Topo dos pilares
        setBlock(center, -4, 5, -3, Material.CHISELED_POLISHED_BLACKSTONE);
        setBlock(center, 4, 5, -3, Material.CHISELED_POLISHED_BLACKSTONE);
        setBlock(center, -4, 5, 2, Material.CHISELED_POLISHED_BLACKSTONE);
        setBlock(center, 4, 5, 2, Material.CHISELED_POLISHED_BLACKSTONE);
        setBlock(center, -4, 5, 6, Material.CHISELED_POLISHED_BLACKSTONE);
        setBlock(center, 4, 5, 6, Material.CHISELED_POLISHED_BLACKSTONE);

        // Sarcófagos (4 unidades maiores)
        // Esquerda frente
        setBlock(center, -5, 1, -1, Material.POLISHED_BLACKSTONE_STAIRS);
        setBlock(center, -5, 1, 0, Material.CHISELED_STONE_BRICKS);
        setBlock(center, -5, 1, 1, Material.POLISHED_BLACKSTONE_STAIRS);

        // Direita frente
        setBlock(center, 5, 1, -1, Material.POLISHED_BLACKSTONE_STAIRS);
        setBlock(center, 5, 1, 0, Material.CHISELED_STONE_BRICKS);
        setBlock(center, 5, 1, 1, Material.POLISHED_BLACKSTONE_STAIRS);

        // Esquerda trás
        setBlock(center, -5, 1, 4, Material.POLISHED_BLACKSTONE_STAIRS);
        setBlock(center, -5, 1, 5, Material.CHISELED_STONE_BRICKS);
        setBlock(center, -5, 1, 6, Material.POLISHED_BLACKSTONE_STAIRS);

        // Direita trás
        setBlock(center, 5, 1, 4, Material.POLISHED_BLACKSTONE_STAIRS);
        setBlock(center, 5, 1, 5, Material.CHISELED_STONE_BRICKS);
        setBlock(center, 5, 1, 6, Material.POLISHED_BLACKSTONE_STAIRS);

        // Altar central (3x3 elevado)
        for (int x = -1; x <= 1; x++) {
            for (int z = 2; z <= 4; z++) {
                setBlock(center, x, 1, z, Material.POLISHED_BLACKSTONE_BRICKS);
            }
        }

        // Centro do altar (onde spawnam mobs)
        setBlock(center, 0, 2, 3, Material.CRYING_OBSIDIAN);

        // Fogueiras ao redor do altar
        setBlock(center, -1, 2, 2, Material.SOUL_CAMPFIRE);
        setBlock(center, 1, 2, 2, Material.SOUL_CAMPFIRE);
        setBlock(center, -1, 2, 4, Material.SOUL_CAMPFIRE);
        setBlock(center, 1, 2, 4, Material.SOUL_CAMPFIRE);

        // Iluminação sombria
        setBlock(center, -6, 3, -4, Material.SOUL_LANTERN);
        setBlock(center, 6, 3, -4, Material.SOUL_LANTERN);
        setBlock(center, -6, 3, 1, Material.SOUL_LANTERN);
        setBlock(center, 6, 3, 1, Material.SOUL_LANTERN);
        setBlock(center, -6, 3, 7, Material.SOUL_LANTERN);
        setBlock(center, 6, 3, 7, Material.SOUL_LANTERN);

        // Teias de aranha
        setBlock(center, -7, 5, -3, Material.COBWEB);
        setBlock(center, 7, 5, 3, Material.COBWEB);
        setBlock(center, -2, 5, 8, Material.COBWEB);
        setBlock(center, 2, 5, -6, Material.COBWEB);
        setBlock(center, -5, 4, 1, Material.COBWEB);
        setBlock(center, 5, 4, 5, Material.COBWEB);

        // Crânios decorativos
        setBlock(center, -6, 2, 0, Material.SKELETON_SKULL);
        setBlock(center, 6, 2, 0, Material.SKELETON_SKULL);
        setBlock(center, -6, 2, 6, Material.SKELETON_SKULL);
        setBlock(center, 6, 2, 6, Material.SKELETON_SKULL);

        // Baús
        setBlock(center, 0, 1, 7, Material.CHEST); // Principal
        setBlock(center, -6, 1, -4, Material.CHEST); // Escondido
        setBlock(center, 6, 1, 7, Material.BARREL); // Extra
    }
}