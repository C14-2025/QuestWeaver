package br.dev.projetoc14.quest.listeners;

import br.dev.projetoc14.QuestWeaver;
import br.dev.projetoc14.quest.utils.PlayerQuestData;
import br.dev.projetoc14.quest.Quest;
import br.dev.projetoc14.quest.utils.QuestManager;
import br.dev.projetoc14.quest.KillQuest;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

/*
 * Atualiza progresso das KillQuests quando jogador mata mobs
 */
public class MobKillQuestListener implements Listener {

    private final QuestManager questManager;
    private final NamespacedKey noDropsKey = new NamespacedKey(QuestWeaver.getInstance(), "quest_target");

    public MobKillQuestListener(QuestManager questManager) {
        this.questManager = questManager;
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        PersistentDataContainer container = entity.getPersistentDataContainer();

        if (container.has(noDropsKey, PersistentDataType.BYTE)) {
            event.getDrops().clear();
            event.setDroppedExp(0);
        }

        Player killer = entity.getKiller();

        if (killer == null) return;

        // Pega a arma usada para matar
        Material weaponUsed = killer.getInventory().getItemInMainHand().getType();

        // Nome da entidade morta (ex: "ZOMBIE")
        String entityType = entity.getType().name();

        // Pega os dados de quest do jogador
        PlayerQuestData questData = questManager.getPlayerQuests(killer);
        if (questData == null) return;

        // Atualiza todas as KillQuests ativas do jogador
        for (Quest quest : questData.getActiveQuests().values()) {
            if (quest instanceof KillQuest killQuest) {
                killQuest.updateProgress(entityType, weaponUsed, killer, entity);
            }
        }

    }
}