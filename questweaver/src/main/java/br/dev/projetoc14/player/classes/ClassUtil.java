package br.dev.projetoc14.player.classes;

import br.dev.projetoc14.QuestWeaver;
import br.dev.projetoc14.player.RPGPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.Objects;

public class ClassUtil {

    public static void equipPlayer(RPGPlayer rpgPlayer, ItemStack weapon, Color color) {
        Player player = rpgPlayer.getPlayer();
        PlayerInventory inv = player.getInventory();

        // Limpa inventário antes de equipar
        inv.clear();

        // Adiciona a arma principal
        inv.addItem(weapon);

        // Cria e equipa armadura colorida
        inv.setHelmet(coloredArmor(Material.LEATHER_HELMET, color));
        inv.setChestplate(coloredArmor(Material.LEATHER_CHESTPLATE, color));
        inv.setLeggings(coloredArmor(Material.LEATHER_LEGGINGS, color));
        inv.setBoots(coloredArmor(Material.LEATHER_BOOTS, color));

        // Adiciona o livro de quests
        inv.addItem(rpgPlayer.createQuestBook());

        // Atualiza vida com delay para garantir aplicação
        Bukkit.getScheduler().runTaskLater(QuestWeaver.getInstance(), () -> {
            // Define vida máxima do Bukkit
            int maxHealth = rpgPlayer.getMaxHealth();
            Objects.requireNonNull(player.getAttribute(Attribute.MAX_HEALTH)).setBaseValue(maxHealth);

            // Define vida atual - cheia
            player.setHealth(maxHealth);
            rpgPlayer.setCurrentHealth(maxHealth);

            // Garante fome e saturação
            player.setFoodLevel(20);
            player.setSaturation(20f);
            player.setExhaustion(0f);
        }, 2L); // 2 ticks de delay (0.1 segundo)
    }

    private static ItemStack coloredArmor(Material material, Color color) {
        ItemStack item = new ItemStack(material);
        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        meta.setColor(color);
        item.setItemMeta(meta);
        return item;
    }
}