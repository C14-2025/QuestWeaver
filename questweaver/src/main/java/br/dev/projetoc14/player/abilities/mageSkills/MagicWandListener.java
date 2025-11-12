package br.dev.projetoc14.player.abilities.mageSkills;

import br.dev.projetoc14.QuestWeaver;
import br.dev.projetoc14.player.RPGPlayer;
import br.dev.projetoc14.player.abilities.Ability;
import br.dev.projetoc14.player.abilities.AbilityUtil;
import br.dev.projetoc14.player.classes.MagePlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class MagicWandListener implements Listener {

    private final QuestWeaver plugin;
    private final Map<UUID, Integer> abilityIndex = new HashMap<>();
    private final List<String> abilities = Arrays.asList("FIREBALL", "CURE");

    private final Map<String, Ability> abilityMap = new HashMap<>();

    public MagicWandListener(QuestWeaver plugin) {
        this.plugin = plugin;

        Fireball fireball = new Fireball(plugin);
        Healing healing = new Healing();

        fireball.setCooldownListener(plugin.getCooldownListener());
        healing.setCooldownListener(plugin.getCooldownListener());

        abilityMap.put("FIREBALL", fireball);
        abilityMap.put("CURE", healing);
    }

    @EventHandler
    public void onItemSwitch(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        Action action = e.getAction();

        if (!isWand(player.getInventory().getItemInMainHand())) return;
        if (!player.isSneaking()) return;
        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) return;

        MagePlayer mage = getMagePlayer(player);
        if (mage == null) return;

        AbilityUtil.switchAbility(player, e, abilityIndex, abilities, this::formatName);
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        Action a = e.getAction();

        if (a != Action.RIGHT_CLICK_AIR && a != Action.RIGHT_CLICK_BLOCK) return;
        if (!isWand(p.getInventory().getItemInMainHand())) return;
        if (p.isSneaking()) return;

        MagePlayer mage = getMagePlayer(p);
        if (mage == null) {
            p.sendActionBar(Component.text("❌ Apenas magos podem usar este cajado!")
                .color(NamedTextColor.RED));
            return;
        }

        AbilityUtil.executeAbility(p, e, abilityIndex, abilities, abilityMap, mage);
    }

    private MagePlayer getMagePlayer(Player p) {
        RPGPlayer rpgPlayer = plugin.getRPGPlayer(p);
        if (rpgPlayer instanceof MagePlayer mage) return mage;
        return null;
    }

    private boolean isWand(ItemStack item) {
        if (item == null || item.getType() != Material.BLAZE_ROD) return false;
        if (!item.hasItemMeta()) return false;
        ItemMeta meta = item.getItemMeta();
        if (meta.displayName() == null) return false;
        String displayName = PlainTextComponentSerializer.plainText()
                .serialize(Objects.requireNonNull(meta.displayName()));
        return displayName.equalsIgnoreCase("Cajado Mágico");
    }

    public String formatName(String nome) {
        return switch (nome.toUpperCase()) {
            case "FIREBALL" -> "Bola de Fogo 🔥";
            case "CURE" -> "Cura 💚";
            default -> nome;
        };
    }
}