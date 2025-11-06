package br.dev.projetoc14.quest.types.archer;

import br.dev.projetoc14.quest.Quest;

public class RangedCombatQuest extends Quest {

    private int requiredHits;
    private int currentHits;
    private double minDistance;

    public RangedCombatQuest(String id, String name, String description,
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
        if (params.length == 0) return;

        if (params[0] instanceof Double distance) {
            if (distance >= minDistance) {
                currentHits++;
            }
        }
    }

    public int getRequiredHits() { return requiredHits; }
    public int getCurrentHits() { return currentHits; }
    public double getMinDistance() { return minDistance; }

    public void setCurrentHits(int hits) { this.currentHits = hits; }

    public String getProgressText() {
        return String.format("Acertos: %d/%d (distância mín: %.0f blocos)",
                currentHits, requiredHits, minDistance);
    }

    public double getRequiredDistance() {
        return minDistance;
    }

    public void incrementHits() {
        this.currentHits++;
    }

}
