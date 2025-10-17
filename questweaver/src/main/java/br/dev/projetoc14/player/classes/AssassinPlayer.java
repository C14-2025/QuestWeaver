package br.dev.projetoc14.player.classes;

import br.dev.projetoc14.player.PlayerClass;
import br.dev.projetoc14.player.RPGPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AssassinPlayer extends RPGPlayer {

    public AssassinPlayer(Player player) {
        super(player, PlayerClass.ASSASSIN); // Corrigido para a classe Assassino
    }

    @Override
    protected void initializeClass() {
        // Stats iniciais do assassino
        stats.setStrength(14);      // Alto dano físico
        stats.setDefense(6);        // Defesa baixa
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
    public ItemStack[] getStartingEquipment() {
        return new ItemStack[]{
                new ItemStack(Material.IRON_SWORD, 1),    // Lâmina leve
                new ItemStack(Material.LEATHER_CHESTPLATE, 1), // Armadura leve
                new ItemStack(Material.LEATHER_BOOTS, 1),
                new ItemStack(Material.COOKED_BEEF, 4)         // Suprimentos básicos
        };
    }
}