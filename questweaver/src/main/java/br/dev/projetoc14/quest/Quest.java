package br.dev.projetoc14.quest;

import br.dev.projetoc14.QuestWeaver;
import br.dev.projetoc14.items.ItemProtectionUtil;
import br.dev.projetoc14.player.RPGPlayer;
import br.dev.projetoc14.quest.utils.QuestCompletedEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public abstract class Quest {
    protected String id;
    protected String name;
    protected String description;
    protected int experienceReward;
    protected boolean completed;

    public Quest(String id, String name, String description, int experienceReward) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.experienceReward = experienceReward;
        this.completed = false;//A quest, obrigatoriamente, começa não completada
    }

    //Para checar se a quest foi completada
    public abstract boolean checkCompletion();

    /*
    Para checar o progresso da missão.
    Como cada missão pode ter diferentes parâmetros dependendo de o que ela pedir,
    "Object... params" define um número "n" de parâmetros de um tipo genérico,
    então na hora de verificar, basta saber a ordem que os parâmetros estão sendo
    passados e iterar sobre ele como se fosse um vetor

    IMPORTANTE:
    - Como é um tipo genérico e a quantidade é variável, é importante sempre usar
    "instaceof" e iterar dentro dele para saber o tamanho dele
     */
    public abstract void updateProgress(Object... params);

    public abstract ItemStack[] getRewardItems();

    public abstract void assignToPlayer(Player player);

    // Entrega as recompensas ao jogador
    public void giveRewards(Player player) {
        // Dá a experiência
        RPGPlayer rpgPlayer = ((QuestWeaver) QuestWeaver.getInstance()).getRPGPlayer(player);
        if (rpgPlayer != null) {
            rpgPlayer.addExperience(experienceReward);
        }

        // Dá os itens
        ItemStack[] rewards = ItemProtectionUtil.makeUndroppable(getRewardItems());
        if (rewards != null && rewards.length > 0) {
            HashMap<Integer, ItemStack> leftover = player.getInventory().addItem(rewards);

            // Se o inventário estiver cheio, dropa os itens no chão
            if (!leftover.isEmpty()) {
                for (ItemStack item : leftover.values()) {
                    player.getWorld().dropItem(player.getLocation(), item);
                }
                player.sendMessage("§cSeu inventário está cheio! Alguns itens foram dropados no chão!");
            }

            // Mensagem de recompensa
            player.sendMessage("§aVocê recebeu suas recompensas!");
            completed = true;
        }
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isCompleted() {
        return completed;
    }

    public String getDescription() {
        return description;
    }

    public int getExperienceReward() {
        return experienceReward;
    }
}