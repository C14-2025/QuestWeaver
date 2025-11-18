package br.dev.projetoc14.quest.archer;

import br.dev.projetoc14.quest.HitQuest;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;

import java.util.ArrayList;

public class RangedCombatQuest extends HitQuest {
    private static final double MIN_DISTANCE = 15.0; // Distância mínima em blocos

    public RangedCombatQuest(Location spawnLocation) {
        super("ranged_combat_quest",
                "Combate a Distância",
                "Acerte 5 flechas em esqueletos a uma distância de pelo menos " + (int)MIN_DISTANCE + " blocos",
                100,
                "SKELETON",
                5,
                0,
                spawnLocation,
                new ArrayList<>()); // Lista vazia, não usamos validWeapons
    }

    @Override
    protected boolean isValidProjectile(Arrow arrow) {
        // Agora valida pela distância, não por metadata
        if (arrow.getShooter() instanceof org.bukkit.entity.Player shooter) {
            Location shooterLoc = shooter.getLocation();
            Location arrowLoc = arrow.getLocation();

            double distance = shooterLoc.distance(arrowLoc);

            return distance >= MIN_DISTANCE;
        }
        return false;
    }

    @Override
    public String getProgressText() {
        return String.format("%d/%d acertos a longa distância (mín. %d blocos)",
                getCurrentCount(), getTargetCount(), (int)MIN_DISTANCE);
    }

    public static double getMinDistance() {
        return MIN_DISTANCE;
    }
}