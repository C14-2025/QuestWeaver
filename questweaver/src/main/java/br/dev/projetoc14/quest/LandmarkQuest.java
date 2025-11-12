package br.dev.projetoc14.quest;


/*
 * Quest para encontrar um landmark específico.
 * O jogador precisa encontrar um tipo de landmark (vila, templo, etc).
 * Quando encontra, a quest é marcada como completa.
 */

import br.dev.projetoc14.quest.utils.LandmarkType;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class LandmarkQuest extends Quest {
    private final LandmarkType targetType;
    private final double detectionRadius;
    private boolean found;

    public LandmarkQuest(String id, String name, String description,
                         int expReward, LandmarkType targetType, double radius) {
        super(id, name, description, expReward);
        this.targetType = targetType;
        this.detectionRadius = radius;
        this.found = false;
    }

    @Override
    public boolean checkCompletion() {
        return found;
    }

    /*
    Para essa quest, precisamos que o player esteja no local que a missão pede para ele ir
     */
    @Override
    public void updateProgress(Object... params) {
        // Esperado: LandmarkType e Location
        if (params.length >= 2 &&
                params[0] instanceof LandmarkType &&
                params[1] instanceof Location) {

            LandmarkType foundType = (LandmarkType) params[0];
            Location playerLocation = (Location) params[1];

            // Só marca como encontrado se for o tipo certo
            if (foundType == targetType) {
                found = true;
                completed = true;
            }
        }
    }

    @Override
    public ItemStack[] getRewardItems() {
        return new ItemStack[0];
    }

    @Override
    public void assignToPlayer(Player player) {
        // TODO: Adicionar esse metodo para esse tipo de quest
        return;
    }

    public LandmarkType getTargetType() {
        return targetType;
    }

    public boolean isFound() {
        return found;
    }

    public String getProgressText() {
        if (found) {
            return "✅ " + targetType.getDisplayName() + " encontrado!";
        } else {
            return "🔍 Procurando por: " + targetType.getDisplayName();
        }
    }
}