package br.dev.projetoc14.skilltree.inventories;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class AssassinSkillTree {

    public static void assassinSkillTree(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 27, "Árvore de habilidades");

        ItemStack primaryWeapon = new ItemStack(Material.GOLDEN_SWORD);
        ItemMeta mMeta = primaryWeapon.getItemMeta();
        mMeta.setDisplayName(ChatColor.DARK_PURPLE + "ARMA PRINCIPAL");
        mMeta.setLore(Arrays.asList(
                ChatColor.GRAY + "♦ Melhora o dano"
        ));
        primaryWeapon.setItemMeta(mMeta);
        inventory.setItem(10, primaryWeapon);

        ItemStack armor = new ItemStack(Material.GOLDEN_CHESTPLATE);
        ItemMeta aMeta = armor.getItemMeta();
        aMeta.setDisplayName(ChatColor.GREEN + "ARMADURA");
        aMeta.setLore(Arrays.asList(
                ChatColor.GRAY + "♦ Melhora a armadura",
                ChatColor.GRAY + "♦ Incluindo encantamentos e melhoria de material"
        ));
        armor.setItemMeta(aMeta);
        inventory.setItem(12, armor);

        ItemStack health = new ItemStack(Material.GOLDEN_APPLE);
        ItemMeta wMeta = health.getItemMeta();
        wMeta.setDisplayName(ChatColor.RED + "VIDA");
        wMeta.setLore(Arrays.asList(
                ChatColor.GRAY + "♦ Aumenta a vida"
        ));
        health.setItemMeta(wMeta);
        inventory.setItem(14, health);

        /*
        TODO: descrição do item especial
         */
        ItemStack especialItem = new ItemStack(Material.GOLDEN_BOOTS);
        ItemMeta assMeta = especialItem.getItemMeta();
        assMeta.setDisplayName(ChatColor.BLUE + "ITEM ESPECIAL");
        assMeta.setLore(Arrays.asList(
                ChatColor.GRAY + "♦ Melhora a eficiência do especial"
        ));
        especialItem.setItemMeta(assMeta);
        inventory.setItem(16, especialItem);

        player.openInventory(inventory);
    }
}
