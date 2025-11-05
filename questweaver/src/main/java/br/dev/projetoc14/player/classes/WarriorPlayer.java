package br.dev.projetoc14.player.classes;

import br.dev.projetoc14.player.PlayerClass;
import br.dev.projetoc14.player.RPGPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.Objects;

public class WarriorPlayer extends RPGPlayer {

    public WarriorPlayer(Player player) {
        super(player, PlayerClass.WARRIOR);
    }

    @Override
    protected void initializeClass() {
        // Atributos iniciais do Guerreiro
        stats.setStrength(1);
        stats.setAgility(1);
        stats.setIntelligence(5);
        stats.setHealth(40);
        stats.setMana(40);
    }

    @Override
    public void levelUp() {
        level++;
        stats.setStrength(stats.getStrength() + 3);
        stats.setDefense(stats.getDefense() + 2);
        stats.setHealth(stats.getHealth() + 15);
        player.sendMessage(ChatColor.RED + "⚔ Guerreiro subiu para o nível " + level + "!");
    }

    @Override
    public void getStartingEquipment() {
        ItemStack axe = new ItemStack(Material.IRON_AXE);
        ClassUtil.equipPlayer(this, axe, Color.RED);
    }
}
