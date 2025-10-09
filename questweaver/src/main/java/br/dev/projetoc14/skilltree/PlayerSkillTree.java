package br.dev.projetoc14.skilltree;

import java.util.HashMap;
import java.util.Map;

public class PlayerSkillTree {
    private final Map<String, Integer> levels = new HashMap<>();
    private final Map<String, SkillDefinition> definitions;

    public PlayerSkillTree(Map<String, SkillDefinition> definitions) {
        this.definitions = definitions;
    }

    public int getLevel(String skillId) {
        return levels.getOrDefault(skillId, 0);
    }

    public Map<String, SkillDefinition> getDefinitions() {
        return definitions;
    }

    public boolean isMaxed(String skillId) {
        SkillDefinition def = definitions.get(skillId);
        if (def == null) return true;
        return getLevel(skillId) >= def.getMaxLevel();
    }

    // custo do próximo nível
    public int nextLevelCost(String skillId) {
        SkillDefinition def = definitions.get(skillId);
        if (def == null) return Integer.MAX_VALUE;
        int next = getLevel(skillId) + 1;
        if (next > def.getMaxLevel()) return Integer.MAX_VALUE;
        return def.costForLevel(next); // custo em "níveis"
    }

    // incrementa o nível localmente
    public boolean incrementLevel(String skillId) {
        SkillDefinition def = definitions.get(skillId);
        if (def == null) return false;
        int next = getLevel(skillId) + 1;
        if (next > def.getMaxLevel()) return false;
        levels.put(skillId, next);
        return true;
    }

    public void setLevel(String skillId, int level) {
        SkillDefinition def = definitions.get(skillId);
        if (def == null) return;
        int clamped = Math.max(0, Math.min(level, def.getMaxLevel()));
        levels.put(skillId, clamped);
    }

    // serialização simples para salvar
    public Map<String, Integer> serialize() {
        return new HashMap<>(levels);
    }

    // desserializa de um Map<String, Object>
    public void deserialize(Map<String, Object> map) {
        levels.clear();
        if (map == null) return;
        for (Map.Entry<String, Object> e : map.entrySet()) {
            try {
                int lvl = Integer.parseInt(String.valueOf(e.getValue()));
                setLevel(e.getKey(), lvl);
            } catch (Exception ignored) {}
        }
    }
}
