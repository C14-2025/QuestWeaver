package br.dev.projetoc14.quest.mage;

import br.dev.projetoc14.QuestWeaver;
import br.dev.projetoc14.quest.KillQuest;
import br.dev.projetoc14.quest.utils.QuestCompletedEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

public class PyromancerQuest extends KillQuest {

    private final NamespacedKey magicDamageKey;

    public PyromancerQuest(Location spawnLocation) {
        super("mage_pyromancer",
                "Prova do Piromante",
                "Use Bolas de Fogo para eliminar 6 Aranhas",
                250,
                "SPIDER",
                6,
                0,
                spawnLocation,
                null
        );
        this.magicDamageKey = new NamespacedKey(QuestWeaver.getInstance(), "magic_damage");
    }

    @Override
    public void updateProgress(Object... params) {
        if (params.length >= 4 && params[3] instanceof LivingEntity entity) {
            String mobType = (String) params[0];

            if (mobType.equalsIgnoreCase(targetMob)) {
                // Verifica se morreu por dano mágico (Fireball.java aplica essa tag)
                if (entity.getPersistentDataContainer().has(magicDamageKey, PersistentDataType.BOOLEAN)) {
                    currentCount++;

                    if (params[2] instanceof Player player) {
                        player.sendMessage("§6 Aranha incinerada! (" + currentCount + "/" + targetCount + ")");
                    }

                    if (checkCompletion() && params[2] instanceof Player player) {
                        QuestCompletedEvent event = new QuestCompletedEvent(player, this);
                        Bukkit.getServer().getPluginManager().callEvent(event);
                    }
                }
            }
        }
    }
}