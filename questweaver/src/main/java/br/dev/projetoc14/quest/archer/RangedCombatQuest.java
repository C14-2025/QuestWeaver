package br.dev.projetoc14.quest.archer;

import br.dev.projetoc14.quest.KillQuest;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;

import java.util.List;

public class RangedCombatQuest extends KillQuest {
    public RangedCombatQuest(Location spawnLocation) {
        super("ranged_combat_quest",
                "Combate a Distância",
                "Acerte 5 flechas explosivas, com knockback ou com poison em zumbis",
                100,
                "ZOMBIE",
                5,
                0,
                spawnLocation,
                null);
    }

    @Override
    protected boolean isValidSpecialArrow(Arrow arrow) {
        // Verifica se a flecha tem uma das metadata das flechas especiais
        return arrow.hasMetadata("explosive_arrow") ||
                arrow.hasMetadata("knockback_arrow") ||
                arrow.hasMetadata("poison_arrow");
    }
}
