package br.dev.projetoc14.quest.types.archer;

import br.dev.projetoc14.quest.Quest;

/*
 * Quest para acertar projéteis em mobs a uma distância mínima
 */
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
        if (params.length > 0 && params[0] instanceof Double) {
            double distance = (Double) params[0];

            if (distance >= minDistance) {
                currentHits++;
                completed = checkCompletion();
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
}