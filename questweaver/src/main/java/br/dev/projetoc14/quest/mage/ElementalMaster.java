package br.dev.projetoc14.quest.mage;

import br.dev.projetoc14.QuestWeaver;
import br.dev.projetoc14.quest.KillQuest;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

public final class ElementalMaster extends KillQuest {
    private final NamespacedKey magicDamageKey;

    public ElementalMaster (Location spawnLocation) {
        super("mage_elemental_master",
                "Dominando os elementos",
                "Mate 5 zumbis usando magia",
                100,
                "ZOMBIE",
                5,
                0,
                spawnLocation,
                null
        );
        this.magicDamageKey = new NamespacedKey(QuestWeaver.getInstance(), "magic_damage");
    }

    @Override
    public void updateProgress(Object... params) {
        if (params.length >= 2 && params[0] instanceof String mobType && params[1] instanceof Material weapon) {
            if (mobType.equalsIgnoreCase(targetMob) && (validWeapons.isEmpty() || validWeapons.contains(weapon)) && params[3] instanceof LivingEntity entity) {
                if (entity.getPersistentDataContainer().has(magicDamageKey, PersistentDataType.BOOLEAN)) {
                    currentCount++;
                    completed = checkCompletion();
                }

                if (completed && params[2] instanceof Player player) {
                    giveRewards(player);
                }
            }
        }
    }
}
