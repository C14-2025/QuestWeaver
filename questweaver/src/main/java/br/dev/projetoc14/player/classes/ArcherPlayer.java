package br.dev.projetoc14.player.classes;

import br.dev.projetoc14.player.PlayerClass;
import br.dev.projetoc14.player.RPGPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ArcherPlayer extends RPGPlayer {

    public ArcherPlayer(Player player) {
        super(player, PlayerClass.ARCHER);
    }

    @Override
    protected void initializeClass() {
        // Atributos iniciais do Arqueiro
        stats.setStrength(1);
        stats.setAgility(1);
        stats.setIntelligence(7);
        stats.setHealth(30);
        stats.setMana(60);
    }

    @Override
    public void levelUp() {
        level++;
        stats.setAgility(stats.getAgility() + 3);
        stats.setStrength(stats.getStrength() + 2);
        stats.setHealth(stats.getHealth() + 10);
        stats.setMana(stats.getMana() + 5);
        player.sendMessage(ChatColor.GREEN + "🏹 Arqueiro subiu para o nível " + ChatColor.GOLD + level + ChatColor.GREEN + "!");
    }

    @Override
    public void getStartingEquipment() {
        // Cria o arco mágico
        ItemStack magicBow = createMagicBow();

        ClassUtil.equipPlayer(this, magicBow, Color.fromRGB(0, 150, 0)); // Verde floresta
        // Adiciona flechas iniciais
        player.getInventory().addItem(new ItemStack(Material.ARROW, 16));
    }

    // Arco mágico do arqueiro com habilidades especiais
    private ItemStack createMagicBow() {
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
