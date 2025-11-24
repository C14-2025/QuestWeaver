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

public class FrostbiteQuest extends KillQuest {

    private final NamespacedKey frostKey;

    public FrostbiteQuest(Location spawnLocation) {
        super("mage_frostbite",
                "Toque Gélido",
                "Mate 5 inimigos congelados pelo Raio Gélido",
                150,
                "Inimigo", // Nome genérico para exibição
                5,
                0,
                spawnLocation,
                null
        );
        this.frostKey = new NamespacedKey(QuestWeaver.getInstance(), "frost_ray");
    }

    @Override
    public void updateProgress(Object... params) {
        // params: String mobType, Material weapon, Player player, LivingEntity deadEntity
        if (params.length >= 4 && params[3] instanceof LivingEntity entity) {

            // Não verificamos mais mobType.equals("SKELETON")
            // Apenas verificamos se a entidade tinha a marca de gelo ao morrer

            if (entity.getPersistentDataContainer().has(frostKey, PersistentDataType.INTEGER)) {

                // Verifica se o jogador é o dono da quest
                if (params[2] instanceof Player player) {
                    currentCount++;

                    player.sendMessage("§b❄ Inimigo congelado eliminado! (" + currentCount + "/" + targetCount + ")");

                    if (checkCompletion()) {
                        QuestCompletedEvent event = new QuestCompletedEvent(player, this);
                        Bukkit.getServer().getPluginManager().callEvent(event);
                    }
                }
            }
        }
    }
}