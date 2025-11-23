package br.dev.projetoc14.items.artifacts.ArcherArtifact;

import br.dev.projetoc14.items.artifacts.ArtifactRarity;
import br.dev.projetoc14.items.artifacts.ArtifactType;
import br.dev.projetoc14.items.artifacts.Artifacts;
import br.dev.projetoc14.player.RPGPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class EaglesEyeAmulet extends Artifacts {
    public EaglesEyeAmulet() {
        super("Olho de Águia",
                Material.ENDER_EYE,
                ArtifactRarity.EPIC,
                ArtifactType.AMULET);
    }

    @Override
    public ItemStack createItem() {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        meta.displayName(Component.text(name)
                .color(NamedTextColor.YELLOW)
                .decorate(TextDecoration.BOLD)
                .decoration(TextDecoration.ITALIC, false));

        meta.lore(List.of(
                Component.text("Um amuleto que concede visão aguçada e precisão mortal.")
                        .color(NamedTextColor.GRAY)
                        .decoration(TextDecoration.ITALIC, false),
                Component.empty(),
                Component.text("Efeitos:")
                        .color(NamedTextColor.YELLOW)
                        .decoration(TextDecoration.ITALIC, false),
                Component.text("• +25% de Dano em Flechas à Distância")
                        .color(NamedTextColor.RED)
                        .decoration(TextDecoration.ITALIC, false),
                Component.text("• +15% de Chance de Crítico")
                        .color(NamedTextColor.GOLD)
                        .decoration(TextDecoration.ITALIC, false),
                Component.text("• Alcance de Visão Aumentado")
                        .color(NamedTextColor.GREEN)
                        .decoration(TextDecoration.ITALIC, false),
                Component.empty(),
                Component.text("Classe: ")
                        .color(NamedTextColor.DARK_GRAY)
                        .append(Component.text("Arqueiro").color(NamedTextColor.GREEN))
                        .decoration(TextDecoration.ITALIC, false),
                Component.text("Raridade: ")
                        .color(NamedTextColor.DARK_GRAY)
                        .append(Component.text("Épico").color(NamedTextColor.DARK_PURPLE))
                        .decoration(TextDecoration.ITALIC, false)
        ));

        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public void applyEffects(RPGPlayer player) {
        // Efeitos aplicados no sistema de combate
        // Bônus de dano, crítico e zoom
    }
}