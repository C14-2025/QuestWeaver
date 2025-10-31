package br.dev.projetoc14.player.classes;

import br.dev.projetoc14.player.PlayerClass;
import br.dev.projetoc14.player.RPGPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class ArcherPlayer extends RPGPlayer {

    public ArcherPlayer(Player player) {
        super(player, PlayerClass.ARCHER);
    }

    @Override
    protected void initializeClass() {
        // Stats iniciais do arqueiro
        stats.setStrength(10);
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
    public void getStartingEquipment() {
        PlayerInventory inv = this.getPlayer().getInventory();

        // Arco e flechas
        inv.addItem(new ItemStack(Material.BOW, 1));
        inv.addItem(new ItemStack(Material.ARROW, 32));

        // Armadura leve
        inv.setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));

        inv.addItem(createQuestBook());
    }
}