package br.dev.projetoc14.player;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ArcherPlayer extends RPGPlayer {

    public ArcherPlayer(Player player) {
        super(player, PlayerClass.ARCHER);
    }

    @Override
    protected void initializeClass() {
        // Stats iniciais do arqueiro
        stats.setStrength(10);
        stats.setDefense(8);
        stats.setAgility(15);
        stats.setIntelligence(7);
        stats.setHealth(100);
        stats.setMana(50);
    }

    @Override
    public void levelUp() {
        level++;
        stats.setAgility(stats.getAgility() + 3);
        stats.setStrength(stats.getStrength() + 2);
        stats.setHealth(stats.getHealth() + 10);
        stats.setMana(stats.getMana() + 5);
        player.sendMessage("Arqueiro subiu para nível " + level + "!");
    }

    @Override
    public ItemStack[] getStartingEquipment() {
        return new ItemStack[] {
                new ItemStack(Material.BOW, 1),
                new ItemStack(Material.ARROW, 32),
                new ItemStack(Material.LEATHER_CHESTPLATE, 1),
                new ItemStack(Material.COOKED_CHICKEN, 5)
        };
    }
}