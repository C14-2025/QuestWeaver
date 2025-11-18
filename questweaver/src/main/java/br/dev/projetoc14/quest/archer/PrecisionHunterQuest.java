package br.dev.projetoc14.quest.archer;

import br.dev.projetoc14.quest.HitQuest;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;

import java.util.ArrayList;

/**
 * Quest média do arqueiro: Acertar tiros críticos em mobs
 */
public class PrecisionHunterQuest extends HitQuest {

    public PrecisionHunterQuest(Location spawnLocation) {
        super("precision_hunter_quest",
                "Caçador Preciso",
                "Acerte 8 tiros críticos em zumbis (flechas devem ser críticas)",
                200,
                "ZOMBIE",
                1,
                0,
                spawnLocation,
                new ArrayList<>());
    }

    @Override
    protected boolean isValidProjectile(Arrow arrow) {
        // Valida se a flecha é crítica (partículas especiais no Minecraft)
        return arrow.isCritical();
    }

    @Override
    public String getProgressText() {
        return String.format("%d/%d tiros críticos acertados",
                getCurrentCount(), getTargetCount());
    }
}