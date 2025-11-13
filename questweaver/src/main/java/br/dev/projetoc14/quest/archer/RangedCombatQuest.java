package br.dev.projetoc14.quest.archer;

import br.dev.projetoc14.quest.HitQuest;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;

import java.util.ArrayList;

public class RangedCombatQuest extends HitQuest {
    public RangedCombatQuest(Location spawnLocation) {
        super("ranged_combat_quest",
                "Combate a Distância",
                "Acerte 5 flechas especiais (explosiva, knockback ou venenosa) em esqueletos",
                100,
                "SKELETON",
                5,
                0,
                spawnLocation,
                new ArrayList<>()); // Lista vazia, não usamos validWeapons
    }

    @Override
    protected boolean isValidProjectile(Arrow arrow) {
        // Valida se a flecha tem uma das metadata das flechas especiais
        return arrow.hasMetadata("explosive_arrow") ||
                arrow.hasMetadata("knockback_arrow") ||
                arrow.hasMetadata("poison_arrow");
    }

    @Override
    public String getProgressText() {
        return String.format("%d/%d acertos com flechas especiais", getCurrentCount(), getTargetCount());
    }
}