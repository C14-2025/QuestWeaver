package br.dev.projetoc14.items.artifacts.AssassinArtifact;

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

public class BloodthirstAmulet extends Artifacts {
    public BloodthirstAmulet() {
        super("Amuleto Sanguinário",
                Material.RED_DYE,
                ArtifactRarity.LEGENDARY,
                ArtifactType.AMULET);
    }

    @Override
    public ItemStack createItem() {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        meta.displayName(Component.text(name)
                .color(NamedTextColor.DARK_RED)
                .decorate(TextDecoration.BOLD)
                .decoration(TextDecoration.ITALIC, false));

        meta.lore(List.of(
                Component.text("Um amuleto que pulsa com energia vampírica, sedento por sangue.")
                        .color(NamedTextColor.GRAY)
                        .decoration(TextDecoration.ITALIC, false),
                Component.empty(),
                Component.text("Efeitos:")
                        .color(NamedTextColor.YELLOW)
                        .decoration(TextDecoration.ITALIC, false),
                Component.text("• +40% de Dano contra Alvos Sangrando")
                        .color(NamedTextColor.GOLD)
                        .decoration(TextDecoration.ITALIC, false),
                Component.text("• +25% de Chance de Crítico em Ataques Rápidos")
                        .color(NamedTextColor.GOLD)
                        .decoration(TextDecoration.ITALIC, false),
                Component.text("• Reduz Cura Recebida por Inimigos Atingidos")
                        .color(NamedTextColor.DARK_PURPLE)
                        .decoration(TextDecoration.ITALIC, false),
                Component.empty(),
                Component.text("Classe: ")
                        .color(NamedTextColor.DARK_GRAY)
                        .append(Component.text("Assassino").color(NamedTextColor.LIGHT_PURPLE))
                        .decoration(TextDecoration.ITALIC, false),
                Component.text("Raridade: ")
                        .color(NamedTextColor.DARK_GRAY)
                        .append(Component.text("Lendário").color(NamedTextColor.LIGHT_PURPLE))
                        .decoration(TextDecoration.ITALIC, false)
        ));

        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public void applyEffects(RPGPlayer player) {
        // Efeitos aplicados no sistema de combate
    }
}