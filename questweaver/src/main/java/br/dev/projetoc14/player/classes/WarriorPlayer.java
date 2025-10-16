package br.dev.projetoc14.player.classes;

import br.dev.projetoc14.player.PlayerClass;
import br.dev.projetoc14.player.RPGPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class WarriorPlayer extends RPGPlayer {

    public WarriorPlayer(Player player) {
        super(player, PlayerClass.WARRIOR);
    }

    @Override
    protected void initializeClass() {
        stats.setStrength(15);
        stats.setDefense(12);
        stats.setAgility(8);
        stats.setIntelligence(5);
        stats.setHealth(120);
        stats.setMana(30);
    }

    @Override
    public void levelUp() {
        level++;
        stats.setStrength(stats.getStrength() + 3);
        stats.setDefense(stats.getDefense() + 2);
        stats.setHealth(stats.getHealth() + 15);
        player.sendMessage("Guerreiro subiu para nível " + level + "!");
    }

    // Méthod: Equipamento Inicial da classe de Guerreiro
    @Override
    public ItemStack[] getStartingEquipment() {
        return new ItemStack[] {
                new ItemStack(Material.IRON_AXE, 1),
                new ItemStack(Material.IRON_CHESTPLATE, 1),
                new ItemStack(Material.IRON_LEGGINGS, 1),
                new ItemStack(Material.IRON_BOOTS, 1),
                new  ItemStack(Material.IRON_HELMET, 1),
                new ItemStack(Material.APPLE, 10)
        };
    }
}
