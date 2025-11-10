package br.dev.projetoc14.player.abilities;

import br.dev.projetoc14.player.RPGPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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
        player.sendActionBar(Component.text( "✨ Habilidade: ")
                .color(NamedTextColor.AQUA)
                    .append(Component.text(formatter.format(nova))
                            .color(NamedTextColor.GOLD)));
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
        CastResult result = ability.canCast(rpgPlayer);
        switch(result) {
            case SUCCESS -> ability.cast(rpgPlayer);
            case COOLDOWN -> sendCooldownMessage(player);
            case NO_MANA -> {} // canCast já mostra o resultado da falta de mana.
        }
    }

    private static void sendCooldownMessage(Player player) {
        player.sendActionBar(Component.text("⏳ Habilidade em cooldown!").color(NamedTextColor.DARK_GREEN));
        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
    }

    @FunctionalInterface
    public interface FormatFunction {
        String format(String name);
    }
}
