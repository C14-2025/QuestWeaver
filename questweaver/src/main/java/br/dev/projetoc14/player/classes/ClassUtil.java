package br.dev.projetoc14.player.classes;

import br.dev.projetoc14.QuestWeaver;
import br.dev.projetoc14.items.ItemProtectionUtil;
import br.dev.projetoc14.player.RPGPlayer;
import br.dev.projetoc14.quest.utils.QuestBook;
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

        // Protege a arma principal
        ItemProtectionUtil.makeUndroppable(weapon);
        inv.addItem(weapon);

        // Cria armadura colorida e protege
        ItemStack helmet = ItemProtectionUtil.makeUndroppable(coloredArmor(Material.LEATHER_HELMET, color));
        ItemStack chestplate = ItemProtectionUtil.makeUndroppable(coloredArmor(Material.LEATHER_CHESTPLATE, color));
        ItemStack leggings = ItemProtectionUtil.makeUndroppable(coloredArmor(Material.LEATHER_LEGGINGS, color));
        ItemStack boots = ItemProtectionUtil.makeUndroppable(coloredArmor(Material.LEATHER_BOOTS, color));

        inv.setHelmet(helmet);
        inv.setChestplate(chestplate);
        inv.setLeggings(leggings);
        inv.setBoots(boots);

        // Adiciona o livro de quests (já é protegido internamente)
        QuestWeaver plugin = (QuestWeaver) QuestWeaver.getInstance();
        QuestBook questBook = plugin.getQuestBook();
        questBook.giveQuestBookToPlayer(player);

        // resto do código permanece igual...
        Bukkit.getScheduler().runTaskLater(QuestWeaver.getInstance(), () -> {
            int maxHealth = rpgPlayer.getMaxHealth();
            Objects.requireNonNull(player.getAttribute(Attribute.MAX_HEALTH)).setBaseValue(maxHealth);

            // Define vida atual - cheia
            player.setHealth(maxHealth);
            rpgPlayer.setCurrentHealth(maxHealth);
            player.setFoodLevel(20);
            player.setSaturation(20f);
            player.setExhaustion(0f);
        }, 2L);
    }

    private static ItemStack coloredArmor(Material material, Color color) {
        ItemStack item = new ItemStack(material);
        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        meta.setColor(color);
        item.setItemMeta(meta);
        return item;
    }
}