package br.dev.projetoc14.player.classes;

import br.dev.projetoc14.player.PlayerClass;
import br.dev.projetoc14.player.RPGPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;

public class MagePlayer extends RPGPlayer {

    public MagePlayer(Player player) {
        super(player, PlayerClass.MAGE);
    }

    @Override
    protected void initializeClass() {
        // Stats iniciais do mago
        stats.setStrength(1);
        stats.setAgility(1);
        stats.setIntelligence(18);
        stats.setHealth(15);
        stats.setMana(120);
    }

    @Override
    public void levelUp() {
        level++;
        stats.setIntelligence(stats.getIntelligence() + 4);
        stats.setMana(stats.getMana() + 15);
        stats.setHealth(stats.getHealth() + 8);
        refreshHealth();
        player.sendMessage("§9✦ Seu poder mágico aumenta! Nível " + level + " alcançado!");
    }

    @Override
    public void getStartingEquipment() {
        // Cria o cajado
        ItemStack wand = new ItemStack(Material.BLAZE_ROD);
        ItemMeta wandMeta = wand.getItemMeta();
        if (wandMeta != null) {
            wandMeta.setDisplayName(ChatColor.AQUA + "Cajado Mágico");
            wand.setItemMeta(wandMeta);
        }

        // Cria poções de resistência ao fogo
        ItemStack potionItem = new ItemStack(Material.SPLASH_POTION, 2);
        PotionMeta potionMeta = (PotionMeta) potionItem.getItemMeta();
        if (potionMeta != null) {
            potionMeta.setBasePotionType(PotionType.FIRE_RESISTANCE);
            potionItem.setItemMeta(potionMeta);
        }

        // Cor da armadura
        Color mageColor = Color.BLUE;

        ClassUtil.equipPlayer(this, wand, mageColor);

        // Adiciona as poções extras
        player.getInventory().addItem(potionItem);
    }
}
