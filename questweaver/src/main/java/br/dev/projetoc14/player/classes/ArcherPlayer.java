package br.dev.projetoc14.player.classes;

import br.dev.projetoc14.items.MagicBow;
import br.dev.projetoc14.player.PlayerClass;
import br.dev.projetoc14.player.RPGPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


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
        refreshHealth();
        player.sendActionBar(Component.text("🏹 Arqueiro subiu para o nível ")
                .color(NamedTextColor.RED)
                    .append(Component.text(level)
                            .color(NamedTextColor.GOLD)
                                .append(Component.text("!")
                                    .color(NamedTextColor.GREEN))));
    }

    @Override
    public void getStartingEquipment() {
        // Cria o arco mágico
        MagicBow bow = new MagicBow();
        ItemStack magicBow = bow.createMagicBow();

        ClassUtil.equipPlayer(this, magicBow, Color.fromRGB(0, 150, 0)); // Verde floresta
        // Adiciona flechas iniciais
        player.getInventory().addItem(new ItemStack(Material.ARROW, 16));
    }
}
