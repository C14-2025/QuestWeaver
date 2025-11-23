package br.dev.projetoc14.player.classes;

import br.dev.projetoc14.items.ItemProtectionUtil;
import br.dev.projetoc14.player.PlayerClass;
import br.dev.projetoc14.player.RPGPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;

import java.util.List;

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

        player.sendActionBar(
                Component.text("✦ Seu poder mágico aumenta! Nível ")
                        .color(NamedTextColor.BLUE)
                            .append(Component.text(level)
                                    .color(NamedTextColor.AQUA))
                                        .append(Component.text(" alcançado!")
                                                .color(NamedTextColor.BLUE))
        );
    }

    @Override
    public void getStartingEquipment() {
        // Cria o cajado
        ItemStack wand = new ItemStack(Material.BLAZE_ROD);
        ItemMeta wandMeta = wand.getItemMeta();

        if (wandMeta != null) {
            wandMeta.displayName(
                    Component.text("Cajado Mágico")
                            .color(NamedTextColor.AQUA)
                            .decoration(TextDecoration.ITALIC, false)
            );

            wandMeta.lore(List.of(
                    Component.text("Um cajado imbuído de energia arcana.")
                            .color(NamedTextColor.GRAY)
                            .decoration(TextDecoration.ITALIC, false),
                    Component.empty(),
                    Component.text("Classe: ")
                            .color(NamedTextColor.DARK_GRAY)
                            .append(Component.text("Mago").color(NamedTextColor.BLUE))
                            .decoration(TextDecoration.ITALIC, false)
            ));

            wandMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            wand.setItemMeta(wandMeta);
        }

        // Cria poções de resistência ao fogo
        ItemStack potionItem = new ItemStack(Material.SPLASH_POTION, 2);
        PotionMeta potionMeta = (PotionMeta) potionItem.getItemMeta();

        if (potionMeta != null) {
            potionMeta.setBasePotionType(PotionType.FIRE_RESISTANCE);

            potionMeta.displayName(
                    Component.text("Poção de Resistência ao Fogo")
                            .color(NamedTextColor.GOLD)
                            .decoration(TextDecoration.ITALIC, false)
            );

            potionItem.setItemMeta(potionMeta);
        }

        // Cor da armadura
        Color mageColor = Color.BLUE;

        ClassUtil.equipPlayer(this, wand, mageColor);

        // Adiciona as poções extras
        player.getInventory().addItem(ItemProtectionUtil.makeUndroppable(potionItem));
    }
}