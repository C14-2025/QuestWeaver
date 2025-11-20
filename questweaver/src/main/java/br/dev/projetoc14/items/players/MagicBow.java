package br.dev.projetoc14.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class MagicBow implements Item {

    // Arco mágico do arqueiro com habilidades especiais
    public ItemStack create() {
        ItemStack bow = new ItemStack(Material.BOW, 1);
        ItemMeta meta = bow.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ChatColor.GOLD + "Arco Mágico");
            meta.setLore(List.of(
                    ChatColor.GRAY + "Clique esquerdo no ar para alternar o tipo de flecha.",
                    ChatColor.GRAY + "Clique direito para disparar a habilidade atual.",
                    "",
                    ChatColor.DARK_GRAY + "Classe: " + ChatColor.GREEN + "Arqueiro"
            ));
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            bow.setItemMeta(meta);
        }

        return bow;
    }
}
