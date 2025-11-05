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

public class AssassinPlayer extends RPGPlayer {

    public AssassinPlayer(Player player) {
        super(player, PlayerClass.ASSASSIN);
    }

    @Override
    protected void initializeClass() {
        // Atributos iniciais do assassino
        stats.setStrength(2);      // Alto dano físico
        stats.setAgility(5);       // Muito ágil
        stats.setIntelligence(6);  // Inteligência razoável
        stats.setHealth(20);       // Vida baixa
        stats.setMana(60);         // Usa mais ataques físicos
    }

    @Override
    public void levelUp() {
        level++;
        stats.setStrength(stats.getStrength() + 3);
        stats.setAgility(stats.getAgility() + 2);
        stats.setHealth(stats.getHealth() + 12);
        stats.setMana(stats.getMana() + 5);
        player.sendMessage(ChatColor.DARK_GRAY + "☠ O Assassino subiu para o nível " + ChatColor.LIGHT_PURPLE + level + ChatColor.DARK_GRAY + "!");
    }

    @Override
    public void getStartingEquipment() {
        // Cria a arma do assassino
        ItemStack sword = new ItemStack(Material.IRON_SWORD);
        ItemMeta swordMeta = sword.getItemMeta();

        if (swordMeta != null) {
            swordMeta.setDisplayName(ChatColor.DARK_GRAY + "Lâmina Sombria");
            swordMeta.setLore(List.of(
                    ChatColor.GRAY + "Uma lâmina leve e precisa, feita para matar em silêncio.",
                    "",
                    ChatColor.DARK_GRAY + "Classe: " + ChatColor.LIGHT_PURPLE + "Assassino"
            ));
            swordMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            sword.setItemMeta(swordMeta);
        }

        // Usa a ClassUtil para equipar armadura e o livro
        ClassUtil.equipPlayer(this, sword, Color.fromRGB(25, 25, 25)); // Preto escuro

        // Adiciona a poção exclusiva do assassino
        player.getInventory().addItem(createAssassinPotion());
    }

    // Cria a poção exclusiva das habilidades do assassino
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
