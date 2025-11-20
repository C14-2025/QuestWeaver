package br.dev.projetoc14.quest.archer;

import br.dev.projetoc14.quest.HitQuest;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;

import java.util.ArrayList;

/**
 * Quest Média: Campo de Tiro ao Alvo - Adaptada para nova HitQuest
 */
public class PrecisionHunterQuest extends HitQuest {
    private Location shootingRange;

    public PrecisionHunterQuest(Location spawnLocation) {
        super("precision_hunter_quest",
                "Caçador Preciso",
                "Acerte 8 tiros críticos em zumbis móveis no campo de tiro",
                200,
                "ZOMBIE",
                8,
                0,
                spawnLocation,
                new ArrayList<>());
        this.shootingRange = spawnLocation.clone().add(15, 0, 0);
    }

    @Override
    public void buildQuestEnvironment(Player player) {
        buildShootingRange(player);
    }

    @Override
    public void spawnStrategicEntities(Player player) {
        spawnMovingZombies(player);
    }

    private void buildShootingRange(Player player) {
        if (environmentBuilt) return;

        World world = player.getWorld();
        Location center = shootingRange;

        // Campo de tiro longo (30 blocos de comprimento)
        for (int z = 0; z <= 30; z++) {
            for (int x = -2; x <= 2; x++) {
                setBlockSafe(world, center.clone().add(x, -1, z), Material.OAK_PLANKS);
            }
        }

        // Plataformas móveis para zumbis
        buildMovingPlatform(world, center.clone().add(0, 0, 10));
        buildMovingPlatform(world, center.clone().add(0, 0, 20));

        // Cabine de tiro do jogador
        Location shootingBooth = player.getLocation().clone();
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                setBlockSafe(world, shootingBooth.clone().add(x, -1, z), Material.STONE_BRICKS);
            }
        }

        // Alvos de prática estáticos
        buildPracticeTargets(world, center);

        environmentBuilt = true;
        player.sendMessage("§a🎯 Campo de tiro construído! Os zumbis se moverão em plataformas!");
    }

    private void buildMovingPlatform(World world, Location center) {
        // Plataforma 3x3 que se move
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                setBlockSafe(world, center.clone().add(x, -1, z), Material.SPRUCE_PLANKS);
            }
        }

        // Fences laterais
        setBlockSafe(world, center.clone().add(-2, 0, 0), Material.OAK_FENCE);
        setBlockSafe(world, center.clone().add(2, 0, 0), Material.OAK_FENCE);
    }

    private void buildPracticeTargets(World world, Location center) {
        // Alvos estáticos para prática
        int[] targetDistances = {5, 15, 25};
        for (int distance : targetDistances) {
            Location targetLoc = center.clone().add(3, 1, distance);
            setBlockSafe(world, targetLoc, Material.HAY_BLOCK);
            setBlockSafe(world, targetLoc.clone().add(0, 1, 0), Material.TARGET);
        }
    }

    private void spawnMovingZombies(Player player) {
        if (shootingRange == null) return;

        World world = player.getWorld();

        // Spawna zumbis em plataformas móveis
        int[] platformDistances = {10, 15, 20, 25};

        for (int i = 0; i < getTargetCount(); i++) {
            int distance = platformDistances[i % platformDistances.length];
            double offset = (i % 2 == 0) ? -1.5 : 1.5; // Alterna entre lados

            Location spawnLoc = shootingRange.clone().add(offset, 1, distance);

            // Usa o método utilitário da superclasse
            spawnQuestEntity(world, spawnLoc, org.bukkit.entity.EntityType.ZOMBIE, "§2Alvo Móvel");
        }

        player.sendMessage("§e🎯 " + getTargetCount() + " zumbis móveis apareceram!");
        player.sendMessage("§6💡 Dica: Puxe o arco completamente para tiros críticos!");
    }

    @Override
    protected boolean isValidProjectile(Arrow arrow) {
        // Só conta se for tiro crítico (arco totalmente puxado)
        if (!arrow.isCritical()) {
            if (arrow.getShooter() instanceof Player shooter) {
                shooter.sendMessage("§c✗ Não foi crítico! Puxe o arco completamente.");
            }
            return false;
        }
        return true;
    }

    @Override
    public void updateProgress(Object... params) {
        // Delega para a superclasse
        super.updateProgress(params);

        // Feedback específico para tiros críticos
        if (params.length >= 3 && params[2] instanceof Player player) {
            int current = getCurrentCount();
            if (current > 0) {
                player.sendMessage("§e⚡ Tiro Crítico! " + getProgressText());
            }
        }
    }

    @Override
    public String getProgressText() {
        return String.format("%d/%d tiros críticos em zumbis móveis",
                getCurrentCount(), getTargetCount());
    }

    @Override
    public void assignToPlayer(Player player) {
        super.assignToPlayer(player);
    }
}