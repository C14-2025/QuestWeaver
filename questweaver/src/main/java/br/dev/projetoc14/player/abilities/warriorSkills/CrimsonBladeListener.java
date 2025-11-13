package br.dev.projetoc14.player.abilities.warriorSkills;

import br.dev.projetoc14.QuestWeaver;
import br.dev.projetoc14.player.RPGPlayer;
import br.dev.projetoc14.player.abilities.Ability;
import br.dev.projetoc14.player.abilities.AbilityUtil;
import br.dev.projetoc14.player.classes.WarriorPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class CrimsonBladeListener implements Listener {

    private final QuestWeaver plugin;
    private final Map<UUID, Integer> abilityIndex = new HashMap<>();
    private final List<String> abilities = Arrays.asList("POWERCHARGE", "BLOODBLADE");

    private final Map<String, Ability> abilityMap = new HashMap<>();

    public CrimsonBladeListener(QuestWeaver plugin) {
        this.plugin = plugin;

        BloodBlade bloodBlade = new BloodBlade();
        PowerCharge powerCharge = new PowerCharge(plugin);

        bloodBlade.setCooldownListener(plugin.getCooldownListener());
        powerCharge.setCooldownListener(plugin.getCooldownListener());

        abilityMap.put("POWERCHARGE", powerCharge);
        abilityMap.put("BLOODBLADE", bloodBlade);
    }

    @EventHandler
    public void onItemSwitch(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        Action action = e.getAction();

        if (!isAxe(player.getInventory().getItemInMainHand())) return;
        if (!player.isSneaking()) return;
        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) return;

        WarriorPlayer warrior = getWarriorPlayer(player);
        if (warrior == null) return;

        AbilityUtil.switchAbility(player, e, abilityIndex, abilities, this::formatName);
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        Action a = e.getAction();

        if (a != Action.RIGHT_CLICK_AIR && a != Action.RIGHT_CLICK_BLOCK) return;
        if (!isAxe(p.getInventory().getItemInMainHand())) return;
        if (p.isSneaking()) return;

        WarriorPlayer warrior = getWarriorPlayer(p);
        if (warrior == null) {
            Component message = Component.text("❌ Apenas guerreiros podem usar este machado!", NamedTextColor.RED);
            p.sendActionBar(message);
            return;
        }

        AbilityUtil.executeAbility(p, e, abilityIndex, abilities, abilityMap, warrior);
    }

    private WarriorPlayer getWarriorPlayer(Player p) {
        RPGPlayer rpgPlayer = plugin.getRPGPlayer(p);
        if (rpgPlayer instanceof WarriorPlayer warrior) return warrior;
        return null;
    }

    private boolean isAxe(ItemStack item) {
        if (item == null) return false;
        Material type = item.getType();
        return type == Material.IRON_AXE ||
                type == Material.DIAMOND_AXE ||
                type == Material.NETHERITE_AXE;
    }

    public String formatName(String nome) {
        return switch (nome.toUpperCase()) {
            case "POWERCHARGE" -> "Carga Poderosa ⚡";
            case "BLOODBLADE" -> "Lâmina Sangrenta 🗡️";
            default -> nome;
        };
    }
}