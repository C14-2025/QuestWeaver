package br.dev.projetoc14.quest.types.archer;

import br.dev.projetoc14.quest.Quest;

public class EagleEyeQuest extends Quest {

    private final int requiredHits;
    private int currentHits;
    private final double minDistance;

    public EagleEyeQuest(String id, String name, String description,
                         int expReward, int requiredHits, double minDistance) {
        super(id, name, description, expReward);
        this.requiredHits = requiredHits;
        this.currentHits = 0;
        this.minDistance = minDistance;
    }

    @Override
    public boolean checkCompletion() {
        return currentHits >= requiredHits;
    }

    @Override
    public void updateProgress(Object... params) {
        // params[0] = distância
        if (params.length > 0 && params[0] instanceof Double distance) {
            if (distance >= minDistance) {
                currentHits++;
                completed = checkCompletion();
            }
        }
    }

    public void resetProgress() {
        this.currentHits = 0;
    }

    public int getCurrentHits() { return currentHits; }
    public int getRequiredHits() { return requiredHits; }
    public double getMinDistance() { return minDistance; }

    public String getProgressText() {
        return String.format("Acertos válidos: %d/%d (mín: %.0f blocos)",
                currentHits, requiredHits, minDistance);
    }
}
