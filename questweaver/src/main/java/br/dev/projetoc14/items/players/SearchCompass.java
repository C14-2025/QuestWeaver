package br.dev.projetoc14.items.players;

import br.dev.projetoc14.items.Item;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class SearchCompass {

    private static final Material ITEM_MATERIAL = Material.COMPASS;
    private static final String DISPLAY_NAME = "§eTeleportar para jogador";
    private static final List<String> LORE = List.of("§7Clique clique em um jogador para se teleportar para ele.");

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

    private SearchCompass() {}
}
