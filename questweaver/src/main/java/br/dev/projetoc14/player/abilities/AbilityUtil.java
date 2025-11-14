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
        player.sendActionBar(Component.text("✨ Habilidade: ")
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

        // Só executamos a habilidade se o resultado for SUCCESS
        switch (result) {
            case SUCCESS -> ability.cast(rpgPlayer);
            case COOLDOWN, NO_MANA -> {
                // Feedback já foi dado por canCast().
            }
        }
    }

    @FunctionalInterface
    public interface FormatFunction {
        String format(String name);
    }
}