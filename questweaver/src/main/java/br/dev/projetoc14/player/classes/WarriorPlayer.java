package br.dev.projetoc14.player.classes;

import br.dev.projetoc14.player.PlayerClass;
import br.dev.projetoc14.player.RPGPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class WarriorPlayer extends RPGPlayer {

    public WarriorPlayer(Player player) {
        super(player, PlayerClass.WARRIOR);
    }

    @Override
    protected void initializeClass() {
        stats.setStrength(15);
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
    public void getStartingEquipment() {
        PlayerInventory inv = this.getPlayer().getInventory();

        // Arma
        inv.addItem(new ItemStack(Material.IRON_AXE));

        // Armadura
        inv.setHelmet(new ItemStack(Material.IRON_HELMET));
        inv.setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
        inv.setLeggings(new ItemStack(Material.IRON_LEGGINGS));
        inv.setBoots(new ItemStack(Material.IRON_BOOTS));

        inv.addItem(createQuestBook());
    }

    /* TODO: Refactor code
    @Override
    public void onStart(Player player) {
        PlayerInventory inv = player.getInventory();

        // Arma
        inv.addItem(new ItemStack(Material.IRON_AXE));

        // Armadura
        inv.setHelmet(new ItemStack(Material.IRON_HELMET));
        inv.setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
        inv.setLeggings(new ItemStack(Material.IRON_LEGGINGS));
        inv.setBoots(new ItemStack(Material.IRON_BOOTS));
    }*/
}
