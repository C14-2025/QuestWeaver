package br.dev.projetoc14.quest.warrior;

import br.dev.projetoc14.quest.KillQuest;
import br.dev.projetoc14.quest.utils.QuestCompletedEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import java.util.List;

public class IronWillQuest extends KillQuest {

    public IronWillQuest(Location spawnLocation) {
        super("warrior_iron_will",
                "Vontade de Ferro",
                "Mate 5 Esqueletos empunhando um Escudo na mão secundária",
                150,
                "SKELETON",
                5,
                0,
                spawnLocation,
                List.of(Material.IRON_SWORD, Material.IRON_AXE, Material.DIAMOND_SWORD)
        );
    }

    @Override
    public void updateProgress(Object... params) {
        // params: mobType, weapon, player, entity
        if (params.length >= 3 && params[2] instanceof Player player) {
            String mobType = (String) params[0];
            Material weapon = (Material) params[1];

            // Verifica mob, arma e SE TEM ESCUDO na offhand
            if (mobType.equalsIgnoreCase(targetMob) && isValidWeapon(weapon)) {
                if (player.getInventory().getItemInOffHand().getType() == Material.SHIELD) {
                    currentCount++;

                    player.sendMessage("§7 Defensor implacável! (" + currentCount + "/" + targetCount + ")");

                    if (checkCompletion()) {
                        QuestCompletedEvent event = new QuestCompletedEvent(player, this);
                        Bukkit.getServer().getPluginManager().callEvent(event);
                    }
                } else {
                    player.sendMessage("§cVocê precisa estar segurando um escudo na mão secundária!");
                }
            }
        }
    }
}