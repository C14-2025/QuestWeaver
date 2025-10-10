package br.dev.projetoc14.skilltree;

import org.bukkit.Material;
import java.util.LinkedHashMap;
import java.util.Map;

public class SkillTreeFactory {
    // skills a serem definidas pela equipe de Player

    public static Map<String, SkillDefinition> criarGuerreiro() {
        Map<String, SkillDefinition> map = new LinkedHashMap<>();
        map.put("forca", new SkillDefinition("forca", "Força", 50, 5, Material.IRON_SWORD));
        map.put("resistencia", new SkillDefinition("resistencia", "Resistência", 40, 5, Material.SHIELD));
        return map;
    }

    public static Map<String, SkillDefinition> criarMago() {
        Map<String, SkillDefinition> map = new LinkedHashMap<>();
        map.put("mana", new SkillDefinition("mana", "Mana Máxima", 40, 5, Material.BLAZE_POWDER));
        map.put("poder", new SkillDefinition("poder", "Poder Arcano", 60, 5, Material.BLAZE_ROD));
        return map;
    }

    public static Map<String, SkillDefinition> criarArqueiro() {
        Map<String, SkillDefinition> map = new LinkedHashMap<>();
        map.put("precisao", new SkillDefinition("precisao", "Precisão", 35, 5, Material.BOW));
        map.put("tiros", new SkillDefinition("tiros", "Tiros Rápidos", 45, 5, Material.ARROW));
        return map;
    }
}
