package br.dev.projetoc14.player.classes;

import br.dev.projetoc14.player.PlayerClass;
import br.dev.projetoc14.player.RPGPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ArcherPlayer extends RPGPlayer {

    public ArcherPlayer(Player player) {
        super(player, PlayerClass.ARCHER);
    }

    @Override
    protected void initializeClass() {
        // Stats iniciais do arqueiro
        stats.setStrength(10);
        stats.setAgility(15);
        stats.setIntelligence(7);
        stats.setHealth(100);
        stats.setMana(50);
    }

    @Override
    public void levelUp() {
        level++;
        stats.setAgility(stats.getAgility() + 3);
        stats.setStrength(stats.getStrength() + 2);
        stats.setHealth(stats.getHealth() + 10);
        stats.setMana(stats.getMana() + 5);
        player.sendMessage("Arqueiro subiu para nível " + level + "!");
    }

    @Override
    public void getStartingEquipment() {
        PlayerInventory inv = this.getPlayer().getInventory();

        inv.addItem(createMagicBow());
        inv.addItem(new ItemStack(Material.ARROW, 64));

        // Armadura leve
        inv.setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
        inv.setBoots(new ItemStack(Material.LEATHER_BOOTS));

        inv.addItem(createQuestBook());
    }

    private ItemStack createMagicBow() {
        ItemStack bow = new ItemStack(Material.BOW, 1);
        ItemMeta meta = bow.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ChatColor.GOLD + "Arco Mágico");
            meta.setLore(List.of(
                    ChatColor.GRAY + "Clique esquerdo no ar para alternar o tipo de flecha.",
                    ChatColor.GRAY + "Clique direito para disparar a habilidade atual.",
                    "",
                    ChatColor.DARK_GRAY + "Classe: " + ChatColor.YELLOW + "Arqueiro"
            ));
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            bow.setItemMeta(meta);
        }

        return bow;
    }
}