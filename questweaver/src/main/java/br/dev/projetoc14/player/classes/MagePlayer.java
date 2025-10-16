package br.dev.projetoc14.player.classes;

import br.dev.projetoc14.player.PlayerClass;
import br.dev.projetoc14.player.RPGPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MagePlayer extends RPGPlayer {

    public MagePlayer(Player player) {
        super(player, PlayerClass.MAGE);
    }

    @Override
    protected void initializeClass() {
        // Stats iniciais do mago
        stats.setStrength(1);
        stats.setDefense(6);
        stats.setAgility(10);
        stats.setIntelligence(18);
        stats.setHealth(20);
        stats.setMana(120);
    }

    @Override
    public void levelUp() {
        level++;
        stats.setIntelligence(stats.getIntelligence() + 4);
        stats.setMana(stats.getMana() + 15);
        stats.setHealth(stats.getHealth() + 8);
        player.sendMessage("Mago subiu para nível " + level + "!");
    }

    @Override
    public ItemStack[] getStartingEquipment() {
        return new ItemStack[] {
                new ItemStack(Material.BLAZE_ROD, 1), // Cajado
                new ItemStack(Material.ENCHANTED_BOOK, 2),
                new ItemStack(Material.SPLASH_POTION, 3),
                new ItemStack(Material.CAKE, 6)
        };
    }
}