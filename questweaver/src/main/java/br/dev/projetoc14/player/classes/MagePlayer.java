package br.dev.projetoc14.player.classes;

import br.dev.projetoc14.player.PlayerClass;
import br.dev.projetoc14.player.RPGPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MagePlayer extends RPGPlayer {

    public MagePlayer(Player player) {
        super(player, PlayerClass.MAGE);
    }

    @Override
    protected void initializeClass() {
        // Stats iniciais do mago
        stats.setStrength(1);
        stats.setDefense(6);
        stats.setAgility(10);
        stats.setIntelligence(18);
        stats.setHealth(20);
        stats.setMana(120);
    }

    @Override
    public void levelUp() {
        level++;
        stats.setIntelligence(stats.getIntelligence() + 4);
        stats.setMana(stats.getMana() + 15);
        stats.setHealth(stats.getHealth() + 8);
        player.sendMessage("Mago subiu para nível " + level + "!");
    }

    @Override
    public ItemStack[] getStartingEquipment() {
        ItemStack wand = new ItemStack(Material.BLAZE_ROD, 1);

        ItemMeta wandMeta = wand.getItemMeta();
        if (wandMeta != null){
            // Definaindo o nome com cores para corresponder à checagem do Listener
            wandMeta.setDisplayName(ChatColor.AQUA + "Cajado Mágico");
            wand.setItemMeta(wandMeta);
        }
        return new ItemStack[] {
                wand, // Cajado customizado
                new ItemStack(Material.ENCHANTED_BOOK, 2),
                new ItemStack(Material.SPLASH_POTION, 3),
                new ItemStack(Material.CAKE, 6)
        };
    }
}