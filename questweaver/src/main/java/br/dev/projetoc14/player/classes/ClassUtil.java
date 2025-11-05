package br.dev.projetoc14.player.classes;

import br.dev.projetoc14.player.RPGPlayer;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class ClassUtil {

    public static void equipPlayer(RPGPlayer rpgPlayer, ItemStack weapon, Color color) {
        PlayerInventory inv = rpgPlayer.getPlayer().getInventory();

        // Adiciona a arma principal
        inv.addItem(weapon);

        // Cria e equipa armadura colorida
        inv.setHelmet(coloredArmor(Material.LEATHER_HELMET, color));
        inv.setChestplate(coloredArmor(Material.LEATHER_CHESTPLATE, color));
        inv.setLeggings(coloredArmor(Material.LEATHER_LEGGINGS, color));
        inv.setBoots(coloredArmor(Material.LEATHER_BOOTS, color));

        // Adiciona o livro de quests (precisa ser público no RPGPlayer)
        inv.addItem(rpgPlayer.createQuestBook());

        // Atualiza vida do jogador para cheia
        rpgPlayer.refreshHealth();
    }

    private static ItemStack coloredArmor(Material material, Color color) {
        ItemStack item = new ItemStack(material);
        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        meta.setColor(color);
        item.setItemMeta(meta);
        return item;
    }
}
