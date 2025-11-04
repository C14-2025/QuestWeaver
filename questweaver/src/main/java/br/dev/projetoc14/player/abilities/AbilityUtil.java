package br.dev.projetoc14.player.abilities;

import br.dev.projetoc14.player.RPGPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AbilityUtil {

    public static void switchAbility(Player player, PlayerInteractEvent e,
                                     Map<UUID, Integer> abilityIndex,
                                     List<String> abilities,
                                     FormatFunction formatter) {
        e.setCancelled(true);

        int index = abilityIndex.getOrDefault(player.getUniqueId(), 0);
        index = (index + 1) % abilities.size();
        abilityIndex.put(player.getUniqueId(), index);

        String nova = abilities.get(index);
        player.sendActionBar(ChatColor.AQUA + "✨ Habilidade: " + ChatColor.GOLD + formatter.format(nova));
        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1.5f);
    }

    public static void executeAbility(Player player, PlayerInteractEvent e,
                                      Map<UUID, Integer> abilityIndex,
                                      List<String> abilities,
                                      Map<String, Ability> abilityMap,
                                      RPGPlayer rpgPlayer) {
        e.setCancelled(true);

        int index = abilityIndex.getOrDefault(player.getUniqueId(), 0);
        String abilityName = abilities.get(index);

        Ability ability = abilityMap.get(abilityName);

        if (ability != null) {
            if (ability.canCast(rpgPlayer)) {
                ability.cast(rpgPlayer);
            } else {
                sendCooldownMessage(player);
            }
        }
    }

    private static void sendCooldownMessage(Player player) {
        player.sendActionBar(ChatColor.RED + "⏳ Habilidade em cooldown!");
        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
    }

    @FunctionalInterface
    public interface FormatFunction {
        String format(String name);
    }
}
