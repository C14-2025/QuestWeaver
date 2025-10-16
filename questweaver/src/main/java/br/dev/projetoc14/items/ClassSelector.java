package br.dev.projetoc14.items;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ClassSelector {

    private static final Material ITEM_MATERIAL = Material.CHEST;
    private static final String DISPLAY_NAME = "§eSeletor de Classe";
    private static final List<String> LORE = List.of("§7Clique para escolher sua classe!");

    /*
        Cria e retorna o item configurado do class Selector

        TODO: change deprecated methods
     */
    public static ItemStack create() {
        ItemStack item = new ItemStack(ITEM_MATERIAL);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(DISPLAY_NAME);
            meta.setLore(LORE);
            item.setItemMeta(meta);
        }

        return item;
    }

    private ClassSelector() {}
}
