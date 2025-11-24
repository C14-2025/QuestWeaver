package br.dev.projetoc14.player.classes;

import br.dev.projetoc14.player.PlayerClass;
import br.dev.projetoc14.player.RPGPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class WarriorPlayer extends RPGPlayer {

    public WarriorPlayer(Player player) {
        super(player, PlayerClass.WARRIOR);
    }

    @Override
    protected void initializeClass() {
        // Atributos iniciais do Guerreiro
        stats.setStrength(1);
        stats.setAgility(1);
        stats.setIntelligence(5);
        stats.setHealth(24);
        stats.setMana(40);
    }

    @Override
    public void levelUp() {
        level++;
        stats.setStrength(stats.getStrength() + 3);
        stats.setDefense(stats.getDefense() + 2);
        stats.setHealth(stats.getHealth() + 15);
        refreshHealth();
        player.sendActionBar(
                Component.text("⚔ Guerreiro subiu para o nível ")
                        .color(NamedTextColor.RED)
                            .append(Component.text(level)
                                .color(NamedTextColor.GOLD))
                                    .append(Component.text("!")
                                         .color(NamedTextColor.RED))
        );

    }

    @Override
    public void getStartingEquipment() {
        ItemStack axe = new ItemStack(Material.IRON_AXE);
        ClassUtil.equipPlayer(this, axe, Color.RED);
    }
}
