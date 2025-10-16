package br.dev.projetoc14.items;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class SkillTree {

    private static final Material ITEM_MATERIAL = Material.ENCHANTED_BOOK;
    private static final String DISPLAY_NAME = "§eÁrvore de habilidades";
    private static final List<String> LORE = List.of("§7Clique para aumentar o nível de suas habilidades!");

    /*
        Cria e retorna o item configurado

        TODO: fazer abrir uma tela diferente de habilidades para cada classe
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

    private SkillTree() {}
}
