package br.dev.projetoc14.player.classes;

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
        refreshHealth();
        player.sendActionBar(
                Component.text("☠ O Assassino subiu para o nível ")
                        .color(NamedTextColor.DARK_GRAY)
                            .append(Component.text(level)
                                .color(NamedTextColor.LIGHT_PURPLE))
                                    .append(Component.text("!")
                                        .color(NamedTextColor.DARK_GRAY))
        );
    }

    @Override
    public void getStartingEquipment() {
        // Cria a arma do assassino
        ItemStack dagger = new ItemStack(Material.IRON_SWORD);
        ItemMeta daggerMeta = dagger.getItemMeta();

        if (daggerMeta != null) {
            daggerMeta.displayName(
                    Component.text("Punhal Sombrio")
                            .color(NamedTextColor.DARK_GRAY)
                            .decoration(TextDecoration.ITALIC, false)
            );

            daggerMeta.lore(List.of(
                    Component.text("Uma lâmina leve e precisa, feita para matar em silêncio.")
                            .color(NamedTextColor.GRAY)
                            .decoration(TextDecoration.ITALIC, false),
                    Component.empty(),
                    Component.text("Classe: ")
                            .color(NamedTextColor.DARK_GRAY)
                            .append(Component.text("Assassino").color(NamedTextColor.LIGHT_PURPLE))
                            .decoration(TextDecoration.ITALIC, false)
            ));

            daggerMeta.setCustomModelData(1001); // Modelo Modificado

            daggerMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            dagger.setItemMeta(daggerMeta);
        }

        // Usa a ClassUtil para equipar armadura e o livro
        ClassUtil.equipPlayer(this, dagger, Color.fromRGB(25, 25, 25)); // Preto escuro

        // Adiciona a poção exclusiva do assassino
        player.getInventory().addItem(createAssassinPotion());
    }

    // Cria a poção exclusiva das habilidades do assassino
    private ItemStack createAssassinPotion() {
        ItemStack potion = new ItemStack(Material.POTION, 1);
        ItemMeta meta = potion.getItemMeta();

        if (meta != null) {
            meta.displayName(
                    Component.text("Poção das Sombras")
                            .color(NamedTextColor.DARK_PURPLE)
                            .decoration(TextDecoration.ITALIC, false)
            );

            meta.lore(List.of(
                    Component.text("Use enquanto agachado (Shift + botão direito)")
                            .color(NamedTextColor.GRAY)
                            .decoration(TextDecoration.ITALIC, false),
                    Component.text("para alternar entre habilidades.")
                            .color(NamedTextColor.GRAY)
                            .decoration(TextDecoration.ITALIC, false),
                    Component.text("Use normalmente para ativar a habilidade atual.")
                            .color(NamedTextColor.GRAY)
                            .decoration(TextDecoration.ITALIC, false),
                    Component.empty(),
                    Component.text("Classe: ")
                            .color(NamedTextColor.DARK_GRAY)
                            .append(Component.text("Assassino").color(NamedTextColor.LIGHT_PURPLE))
                            .decoration(TextDecoration.ITALIC, false)
            ));

            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            potion.setItemMeta(meta);
        }

        return potion;
    }
}
