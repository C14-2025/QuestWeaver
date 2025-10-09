package br.dev.projetoc14.skilltree;

import org.bukkit.Material;

public class SkillDefinition {
    private final String id;
    private final String displayName;
    private final int baseCost;
    private final int maxLevel;
    private final Material icon;

    public SkillDefinition(String id, String displayName, int baseCost, int maxLevel, Material icon) {
        this.id = id;
        this.displayName = displayName;
        this.baseCost = baseCost;
        this.maxLevel = maxLevel;
        this.icon = icon;
    }

    public String getId() { return id; }
    public String getDisplayName() { return displayName; }
    public int getBaseCost() { return baseCost; }
    public int getMaxLevel() { return maxLevel; }
    public Material getIcon() { return icon; }

    // custo: baseCost * nívelDesejado
    public int costForLevel(int level) {
        if (level <= 0) return 0;
        return baseCost * level;
    }
}
