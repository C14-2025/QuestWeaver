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

public class AssassinPlayer extends RPGPlayer {

    public AssassinPlayer(Player player) {
        super(player, PlayerClass.ASSASSIN); // Corrigido para a classe Assassino
    }

    @Override
    protected void initializeClass() {
        // Stats iniciais do assassino
        stats.setStrength(14);      // Alto dano físico
        stats.setAgility(18);       // Muito ágil
        stats.setIntelligence(6);   // Pouca inteligência
        stats.setHealth(80);       // Vida baixa
        stats.setMana(60);          // Pouco mana, usa mais ataques físicos
    }

    @Override
    public void levelUp() {
        level++;
        stats.setStrength(stats.getStrength() + 3);    // Aumenta dano físico
        stats.setAgility(stats.getAgility() + 2);      // Aumenta velocidade/esquiva
        stats.setHealth(stats.getHealth() + 12);       // Ganha um pouco mais de vida
        stats.setMana(stats.getMana() + 5);            // Pequeno ganho de mana
        player.sendMessage("§7O Assassino subiu para o nível §a" + level + "§7!");
    }

    @Override
    public void getStartingEquipment() {
        PlayerInventory inv = this.getPlayer().getInventory();
        // Arma
        inv.addItem(new ItemStack(Material.IRON_SWORD)); // Lâmina leve

        // Armadura
        inv.setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE)); // Armadura leve
        inv.setBoots(new ItemStack(Material.LEATHER_BOOTS));

        inv.addItem(createQuestBook());
        
        // poçao das habilidades
        inv.addItem(createAssassinPotion());
    }


    //  Cria a poção mágica exclusiva do assassino para ativar habilidades.
    private ItemStack createAssassinPotion() {
        ItemStack potion = new ItemStack(Material.POTION, 1);
        ItemMeta meta = potion.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ChatColor.DARK_PURPLE + "Poção das Sombras");
            meta.setLore(List.of(
                    ChatColor.GRAY + "Use enquanto agachado (Shift + botão direito)",
                    ChatColor.GRAY + "para alternar entre habilidades.",
                    ChatColor.GRAY + "Use normalmente para ativar a habilidade atual.",
                    "",
                    ChatColor.DARK_GRAY + "Classe: " + ChatColor.LIGHT_PURPLE + "Assassino"
            ));
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            potion.setItemMeta(meta);
        }

        return potion;
    }
}