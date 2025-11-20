package br.dev.projetoc14.quest.structures;

import org.bukkit.Location;
import org.bukkit.Material;

/**
 * Campo de treinamento para arqueiros (versão expandida)
 * Estrutura: Torre de observação alta com campo de tiro completo
 */
public class ArcherTrainingGrounds extends QuestStructure {

    public ArcherTrainingGrounds() {
        super("Campo de Treinamento de Arqueiros", 23, 12, 25);
    }

    @Override
    protected void build(Location center) {
        // ===== ENTRADA DEFINIDA COM PORTÃO =====
        // Caminho de entrada (5 blocos)
        for (int z = -15; z < -10; z++) {
            for (int x = -1; x <= 1; x++) {
                setBlock(center, x, 0, z, Material.GRAVEL);
            }
            // Cercas laterais
            setBlock(center, -2, 0, z, Material.OAK_FENCE);
            setBlock(center, 2, 0, z, Material.OAK_FENCE);
            setBlock(center, -2, 1, z, Material.OAK_FENCE);
            setBlock(center, 2, 1, z, Material.OAK_FENCE);
        }

        // Portão de entrada
        setBlock(center, -2, 0, -10, Material.OAK_FENCE);
        setBlock(center, -2, 1, -10, Material.OAK_FENCE);
        setBlock(center, -2, 2, -10, Material.OAK_FENCE);
        setBlock(center, -2, 3, -10, Material.OAK_PLANKS);

        setBlock(center, 2, 0, -10, Material.OAK_FENCE);
        setBlock(center, 2, 1, -10, Material.OAK_FENCE);
        setBlock(center, 2, 2, -10, Material.OAK_FENCE);
        setBlock(center, 2, 3, -10, Material.OAK_PLANKS);

        setBlock(center, -1, 3, -10, Material.OAK_SLAB);
        setBlock(center, 0, 3, -10, Material.OAK_PLANKS);
        setBlock(center, 1, 3, -10, Material.OAK_SLAB);

        // Placas decorativas
        setBlock(center, -2, 2, -11, Material.LANTERN);
        setBlock(center, 2, 2, -11, Material.LANTERN);

        // ===== PISO PRINCIPAL (MAIOR E VARIADO) =====
        for (int x = -10; x <= 10; x++) {
            for (int z = -10; z <= 10; z++) {
                // Padrão de piso
                if (Math.abs(x) <= 2 && Math.abs(z) <= 2) {
                    // Área central (torre)
                    setBlock(center, x, 0, z, Material.STONE_BRICKS);
                } else if (x % 3 == 0 || z % 3 == 0) {
                    setBlock(center, x, 0, z, Material.CRACKED_STONE_BRICKS);
                } else {
                    setBlock(center, x, 0, z, Material.STONE_BRICKS);
                }
            }
        }

        // ===== TORRE CENTRAL (MAIOR - 5x5 e mais alta) =====
        for (int y = 1; y <= 9; y++) {
            // Paredes externas (5x5)
            for (int x = -2; x <= 2; x++) {
                setBlock(center, x, y, -2, Material.COBBLESTONE);
                setBlock(center, x, y, 2, Material.COBBLESTONE);
            }
            for (int z = -2; z <= 2; z++) {
                setBlock(center, -2, y, z, Material.COBBLESTONE);
                setBlock(center, 2, y, z, Material.COBBLESTONE);
            }

            // Cantos reforçados
            if (y % 2 == 0) {
                setBlock(center, -2, y, -2, Material.STONE_BRICKS);
                setBlock(center, 2, y, -2, Material.STONE_BRICKS);
                setBlock(center, -2, y, 2, Material.STONE_BRICKS);
                setBlock(center, 2, y, 2, Material.STONE_BRICKS);
            }
        }

        // Entrada da torre
        setBlock(center, 0, 1, -2, Material.AIR);
        setBlock(center, 0, 2, -2, Material.AIR);

        // Janelas (seteiras para arqueiros)
        setBlock(center, 0, 3, -2, Material.AIR);
        setBlock(center, 0, 3, 2, Material.AIR);
        setBlock(center, -2, 3, 0, Material.AIR);
        setBlock(center, 2, 3, 0, Material.AIR);

        setBlock(center, 0, 6, -2, Material.AIR);
        setBlock(center, 0, 6, 2, Material.AIR);
        setBlock(center, -2, 6, 0, Material.AIR);
        setBlock(center, 2, 6, 0, Material.AIR);

        // Plataforma intermediária (piso nível 5)
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                setBlock(center, x, 5, z, Material.OAK_PLANKS);
            }
        }

        // Topo da torre (plataforma de observação - nível 10)
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                setBlock(center, x, 10, z, Material.OAK_PLANKS);
            }
        }

        // Ameias no topo (proteção completa)
        for (int x = -2; x <= 2; x++) {
            if (x % 2 == 0) {
                setBlock(center, x, 11, -2, Material.COBBLESTONE_WALL);
                setBlock(center, x, 11, 2, Material.COBBLESTONE_WALL);
            }
        }
        for (int z = -2; z <= 2; z++) {
            if (z % 2 == 0) {
                setBlock(center, -2, 11, z, Material.COBBLESTONE_WALL);
                setBlock(center, 2, 11, z, Material.COBBLESTONE_WALL);
            }
        }

        // ===== ALVOS DE PRÁTICA (8 ALVOS EM DISTÂNCIAS VARIADAS) =====
        // Alvos próximos (nível 1)
        setBlock(center, -5, 1, -5, Material.HAY_BLOCK);
        setBlock(center, -5, 2, -5, Material.TARGET);

        setBlock(center, 5, 1, -5, Material.HAY_BLOCK);
        setBlock(center, 5, 2, -5, Material.TARGET);

        setBlock(center, -5, 1, 5, Material.HAY_BLOCK);
        setBlock(center, -5, 2, 5, Material.TARGET);

        setBlock(center, 5, 1, 5, Material.HAY_BLOCK);
        setBlock(center, 5, 2, 5, Material.TARGET);

        // Alvos médios (nível 2)
        setBlock(center, -8, 1, 0, Material.HAY_BLOCK);
        setBlock(center, -8, 2, 0, Material.HAY_BLOCK);
        setBlock(center, -8, 3, 0, Material.TARGET);

        setBlock(center, 8, 1, 0, Material.HAY_BLOCK);
        setBlock(center, 8, 2, 0, Material.HAY_BLOCK);
        setBlock(center, 8, 3, 0, Material.TARGET);

        // Alvos distantes (nível 3)
        setBlock(center, 0, 1, -9, Material.HAY_BLOCK);
        setBlock(center, 0, 2, -9, Material.HAY_BLOCK);
        setBlock(center, 0, 3, -9, Material.HAY_BLOCK);
        setBlock(center, 0, 4, -9, Material.TARGET);

        setBlock(center, 0, 1, 9, Material.HAY_BLOCK);
        setBlock(center, 0, 2, 9, Material.HAY_BLOCK);
        setBlock(center, 0, 3, 9, Material.HAY_BLOCK);
        setBlock(center, 0, 4, 9, Material.TARGET);

        // ===== POSTOS DE TIRO (4 PLATAFORMAS) =====
        // Posto nordeste
        for (int x = -9; x <= -7; x++) {
            for (int z = -9; z <= -7; z++) {
                setBlock(center, x, 1, z, Material.SPRUCE_PLANKS);
            }
        }
        setBlock(center, -8, 2, -9, Material.OAK_FENCE);
        setBlock(center, -8, 2, -7, Material.OAK_FENCE);
        setBlock(center, -9, 2, -8, Material.OAK_FENCE);
        setBlock(center, -7, 2, -8, Material.OAK_FENCE);

        // Posto noroeste
        for (int x = 7; x <= 9; x++) {
            for (int z = -9; z <= -7; z++) {
                setBlock(center, x, 1, z, Material.SPRUCE_PLANKS);
            }
        }
        setBlock(center, 8, 2, -9, Material.OAK_FENCE);
        setBlock(center, 8, 2, -7, Material.OAK_FENCE);
        setBlock(center, 7, 2, -8, Material.OAK_FENCE);
        setBlock(center, 9, 2, -8, Material.OAK_FENCE);

        // Posto sudeste
        for (int x = -9; x <= -7; x++) {
            for (int z = 7; z <= 9; z++) {
                setBlock(center, x, 1, z, Material.SPRUCE_PLANKS);
            }
        }
        setBlock(center, -8, 2, 7, Material.OAK_FENCE);
        setBlock(center, -8, 2, 9, Material.OAK_FENCE);
        setBlock(center, -9, 2, 8, Material.OAK_FENCE);
        setBlock(center, -7, 2, 8, Material.OAK_FENCE);

        // Posto sudoeste
        for (int x = 7; x <= 9; x++) {
            for (int z = 7; z <= 9; z++) {
                setBlock(center, x, 1, z, Material.SPRUCE_PLANKS);
            }
        }
        setBlock(center, 8, 2, 7, Material.OAK_FENCE);
        setBlock(center, 8, 2, 9, Material.OAK_FENCE);
        setBlock(center, 7, 2, 8, Material.OAK_FENCE);
        setBlock(center, 9, 2, 8, Material.OAK_FENCE);

        // ===== ILUMINAÇÃO =====
        // Lanternas nos cantos da torre
        setBlock(center, -3, 3, -3, Material.LANTERN);
        setBlock(center, 3, 3, -3, Material.LANTERN);
        setBlock(center, -3, 3, 3, Material.LANTERN);
        setBlock(center, 3, 3, 3, Material.LANTERN);

        setBlock(center, -3, 7, -3, Material.LANTERN);
        setBlock(center, 3, 7, -3, Material.LANTERN);
        setBlock(center, -3, 7, 3, Material.LANTERN);
        setBlock(center, 3, 7, 3, Material.LANTERN);

        // Tochas nos postos
        setBlock(center, -8, 2, -8, Material.TORCH);
        setBlock(center, 8, 2, -8, Material.TORCH);
        setBlock(center, -8, 2, 8, Material.TORCH);
        setBlock(center, 8, 2, 8, Material.TORCH);

        // ===== SUPORTES DE ARMAS =====
        // Suportes de armadura (decoração)
        setBlock(center, -1, 1, 0, Material.ARMOR_STAND);
        setBlock(center, 1, 1, 0, Material.ARMOR_STAND);

        // ===== BAÚS DE SUPRIMENTOS =====
        setBlock(center, 0, 1, 1, Material.CHEST); // Torre térreo
        setBlock(center, 0, 6, 1, Material.CHEST); // Torre meio
        setBlock(center, -8, 1, -8, Material.BARREL); // Posto 1
        setBlock(center, 8, 1, -8, Material.BARREL); // Posto 2
        setBlock(center, -8, 1, 8, Material.BARREL); // Posto 3
        setBlock(center, 8, 1, 8, Material.BARREL); // Posto 4

        // ===== CERCA PERÍMETRO =====
        for (int x = -10; x <= 10; x++) {
            if (x % 2 == 0 && Math.abs(x) > 2) {
                setBlock(center, x, 1, -10, Material.OAK_FENCE);
                setBlock(center, x, 1, 10, Material.OAK_FENCE);
            }
        }
        for (int z = -10; z <= 10; z++) {
            if (z % 2 == 0 && Math.abs(z) > 2) {
                setBlock(center, -10, 1, z, Material.OAK_FENCE);
                setBlock(center, 10, 1, z, Material.OAK_FENCE);
            }
        }
    }
}