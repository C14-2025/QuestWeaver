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

public class QuiverOfTheHunt extends Artifacts {
    public QuiverOfTheHunt() {
        super("Aljava da Caça",
                Material.BUNDLE,
                ArtifactRarity.LEGENDARY,
                ArtifactType.OFFHAND);
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
                Component.text("Uma aljava ancestral que nunca se esvazia e fortalece flechas especiais.")
                        .color(NamedTextColor.GRAY)
                        .decoration(TextDecoration.ITALIC, false),
                Component.empty(),
                Component.text("Efeitos:")
                        .color(NamedTextColor.YELLOW)
                        .decoration(TextDecoration.ITALIC, false),
                Component.text("• Recarga Automática de Flechas")
                        .color(NamedTextColor.RED)
                        .decoration(TextDecoration.ITALIC, false),
                Component.text("• -40% de Recarga em Habilidades de Flecha")
                        .color(NamedTextColor.AQUA)
                        .decoration(TextDecoration.ITALIC, false),
                Component.text("• Flechas Especiais +50% de Raio de Efeito")
                        .color(NamedTextColor.GOLD)
                        .decoration(TextDecoration.ITALIC, false),
                Component.text("• +2 Flechas Adicionais em Rajadas")
                        .color(NamedTextColor.GREEN)
                        .decoration(TextDecoration.ITALIC, false),
                Component.empty(),
                Component.text("Classe: ")
                        .color(NamedTextColor.DARK_GRAY)
                        .append(Component.text("Arqueiro").color(NamedTextColor.GREEN))
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
        // Efeitos aplicados no sistema de habilidades
        // Recarga automática, redução de cooldown e bônus em flechas especiais
    }
}