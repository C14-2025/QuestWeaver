package br.dev.projetoc14.items.artifacts.MageArtifact;

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

public class ArcaneAmulet extends Artifacts {

    public ArcaneAmulet() {
        super("Amuleto Arcano",
                Material.AMETHYST_SHARD,
                ArtifactRarity.LEGENDARY,
                ArtifactType.AMULET);
    }

    @Override
    public ItemStack createItem() {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        meta.displayName(Component.text(name)
                .color(NamedTextColor.LIGHT_PURPLE)
                .decorate(TextDecoration.BOLD)
                .decoration(TextDecoration.ITALIC, false));

        meta.lore(List.of(
                Component.text("Um amuleto que canaliza energias arcanas puras.")
                        .color(NamedTextColor.GRAY)
                        .decoration(TextDecoration.ITALIC, false),
                Component.empty(),
                Component.text("Efeitos:")
                        .color(NamedTextColor.YELLOW)
                        .decoration(TextDecoration.ITALIC, false),
                Component.text("• +15% de Dano Mágico")
                        .color(NamedTextColor.BLUE)
                        .decoration(TextDecoration.ITALIC, false),
                Component.text("• +20 de Mana Máximo")
                        .color(NamedTextColor.AQUA)
                        .decoration(TextDecoration.ITALIC, false),
                Component.text("• -10% de Custo de Mana")
                        .color(NamedTextColor.GREEN)
                        .decoration(TextDecoration.ITALIC, false),
                Component.empty(),
                Component.text("Classe: ")
                        .color(NamedTextColor.DARK_GRAY)
                        .append(Component.text("Mago").color(NamedTextColor.BLUE))
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

    }
}
