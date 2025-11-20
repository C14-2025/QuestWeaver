package br.dev.projetoc14.quest.archer;

import br.dev.projetoc14.quest.HitQuest;
import br.dev.projetoc14.quest.utils.QuestCompletedEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Sign;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Quest Difícil: Desafio do Mestre Flecheiro - COMPLETO E CORRIGIDO
 */
public class WindMasterQuest extends HitQuest {
    private static final int MAX_COMBO = 10;
    private static final long COMBO_TIMEOUT = 7000;
    private static final double MIN_DISTANCE = 20.0;

    private Location challengeArena;
    private final Map<UUID, Integer> playerCombos = new HashMap<>();
    private final Map<UUID, Long> lastHitTime = new HashMap<>();

    public WindMasterQuest(Location spawnLocation) {
        super("wind_master_quest",
                "Mestre dos Ventos",
                "Acerte 10 creepers em sequência perfeita no desafio do flecheiro",
                350,
                "CREEPER",
                10,
                0,
                spawnLocation,
                new ArrayList<>());
        this.challengeArena = spawnLocation.clone().add(25, 0, 0); // Mais distante
    }

    @Override
    public void buildQuestEnvironment(Player player) {
        buildChallengeArena(player);
    }

    @Override
    public void spawnStrategicEntities(Player player) {
        spawnChallengeCreepers(player);
    }

    private void buildChallengeArena(Player player) {
        if (environmentBuilt) return;

        World world = player.getWorld();
        Location center = challengeArena;

        // **ARENA DOS CREEPERS - PLANA E SEGURA**
        // Plataforma grande e plana (25x25)
        for (int x = -12; x <= 12; x++) {
            for (int z = -12; z <= 12; z++) {
                setBlockSafe(world, center.clone().add(x, 0, z), Material.DARK_OAK_PLANKS);
            }
        }

        // **BORDAS SEGURAS** - creepers não escapam
        for (int x = -13; x <= 13; x++) {
            for (int z = -13; z <= 13; z++) {
                if (Math.abs(x) == 13 || Math.abs(z) == 13) {
                    setBlockSafe(world, center.clone().add(x, 1, z), Material.DARK_OAK_FENCE);
                }
            }
        }

        // **TORRES DE TIRO MELHORADAS** - sem buracos, com acesso fácil
        buildSniperTower(world, player.getLocation().clone().add(-8, 0, 0), 8, "Torre Principal"); // Torre do jogador
        buildSniperTower(world, center.clone().add(0, 0, -18), 6, "Torre Sul");
        buildSniperTower(world, center.clone().add(-18, 0, 0), 4, "Torre Leste");

        // **OBSTÁCULOS ESTRATÉGICOS** - para os creepers se esconderem
        buildStrategicObstacles(world, center);

        environmentBuilt = true;
        player.sendMessage("§6⚡ Desafio do Mestre Flecheiro!");
        player.sendMessage("§e🏹 Use as torres para obter vantagem sobre os creepers!");
        player.sendMessage("§6🎯 Acerte 10 em sequência sem errar!");
    }

    private void buildSniperTower(World world, Location base, int height, String name) {
        // **BASE SÓLIDA** 5x5
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                setBlockSafe(world, base.clone().add(x, 0, z), Material.STONE_BRICKS);
            }
        }

        // **TORRE SÓLIDA** 3x3 - sem buracos
        for (int y = 1; y <= height; y++) {
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    setBlockSafe(world, base.clone().add(x, y, z), Material.STONE_BRICKS);
                }
            }
        }

        // **PLATAFORMA NO TOPO** com parapeito
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                setBlockSafe(world, base.clone().add(x, height + 1, z), Material.OAK_PLANKS);
            }
        }

        // **PARAPEITO SEGURO** - com aberturas
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                if ((Math.abs(x) == 2 || Math.abs(z) == 2) && !(Math.abs(x) == 2 && Math.abs(z) == 2)) {
                    setBlockSafe(world, base.clone().add(x, height + 2, z), Material.STONE_BRICK_WALL);
                }
            }
        }

        // **ESCADA SEGURA** - sempre na mesma posição (leste)
        for (int y = 0; y < height; y++) {
            setBlockSafe(world, base.clone().add(2, y, 0), Material.LADDER);
        }

        // **PLATAFORMA DE ACESSO** na base da escada
        setBlockSafe(world, base.clone().add(3, 0, 0), Material.OAK_PLANKS);
        setBlockSafe(world, base.clone().add(3, 0, 1), Material.OAK_PLANKS);
        setBlockSafe(world, base.clone().add(3, 0, -1), Material.OAK_PLANKS);
    }

    private void buildStrategicObstacles(World world, Location center) {
        // **PILARES ESTRATÉGICOS** - para creepers se moverem
        int[][] obstaclePositions = {
                {-8, -8}, {-8, 8}, {8, -8}, {8, 8},  // Cantos
                {0, -8}, {0, 8}, {-8, 0}, {8, 0}     // Centros
        };

        for (int[] pos : obstaclePositions) {
            for (int y = 1; y <= 3; y++) {
                setBlockSafe(world, center.clone().add(pos[0], y, pos[1]), Material.COBBLESTONE);
            }
        }

        // **MUROS BAIXOS** - para criar rotas
        for (int x = -10; x <= 10; x += 5) {
            if (x != 0) { // Deixa passagem no centro
                for (int z = -2; z <= 2; z++) {
                    setBlockSafe(world, center.clone().add(x, 1, z), Material.STONE_BRICK_WALL);
                }
            }
        }
    }

    private void spawnChallengeCreepers(Player player) {
        if (challengeArena == null) return;

        World world = player.getWorld();

        // **SPAWN ESTRATÉGICO DE CREEPERS** - bem distribuídos
        int totalCreepers = 12; // Pouco mais que o necessário

        for (int i = 0; i < totalCreepers; i++) {
            double angle = (2 * Math.PI * i) / totalCreepers;
            double distance = 5 + (i % 6); // Distâncias variadas de 5 a 10 blocos
            double x = challengeArena.getX() + distance * Math.cos(angle);
            double z = challengeArena.getZ() + distance * Math.sin(angle);

            // **LOCAL SEGURO** - sempre no chão da arena
            Location spawnLoc = new Location(world, x, challengeArena.getY() + 1, z);

            // **VERIFICA SEGURANÇA** antes de spawnar
            if (isSafeSpawnLocation(world, spawnLoc)) {
                spawnQuestEntity(world, spawnLoc, org.bukkit.entity.EntityType.CREEPER, "§cDesafio Creeper");
            }
        }

        player.sendMessage("§c💣 " + totalCreepers + " creepers apareceram no desafio!");
        player.sendMessage("§6⚡ Use as torres para manter distância e acertar em sequência!");
    }

    /** Verifica se o local de spawn é seguro */
    private boolean isSafeSpawnLocation(World world, Location location) {
        Location groundLevel = location.clone().add(0, -1, 0);
        return world.getBlockAt(location).getType() == Material.AIR &&
                world.getBlockAt(groundLevel).getType().isSolid() &&
                world.getBlockAt(groundLevel).getType() != Material.LAVA &&
                world.getBlockAt(groundLevel).getType() != Material.WATER;
    }

    @Override
    protected boolean isValidProjectile(Arrow arrow) {
        if (!(arrow.getShooter() instanceof Player shooter)) {
            return false;
        }

        UUID playerId = shooter.getUniqueId();
        long currentTime = System.currentTimeMillis();

        // Verifica timeout do combo
        if (lastHitTime.containsKey(playerId)) {
            long timeSinceLastHit = currentTime - lastHitTime.get(playerId);
            if (timeSinceLastHit > COMBO_TIMEOUT) {
                resetCombo(shooter, "§c✗ Combo perdido! Você demorou mais de 7 segundos.");
                return false;
            }
        }

        // Verifica distância
        Location shooterLoc = shooter.getLocation();
        Location arrowLoc = arrow.getLocation();
        double distance = shooterLoc.distance(arrowLoc);

        if (distance < MIN_DISTANCE) {
            shooter.sendMessage(String.format("§c✗ Muito perto! (%.1f/%.0f blocos)", distance, MIN_DISTANCE));
            return false;
        }

        // Atualiza combo
        int combo = playerCombos.getOrDefault(playerId, 0) + 1;
        playerCombos.put(playerId, combo);
        lastHitTime.put(playerId, currentTime);

        // Feedback do combo
        if (combo >= 3) {
            String color = combo >= 8 ? "§6" : combo >= 5 ? "§e" : "§a";
            shooter.sendMessage(String.format("%s⚡ COMBO x%d §7(%.1f blocos)", color, combo, distance));
        }

        return true;
    }

    @Override
    public void updateProgress(Object... params) {
        if (params.length >= 3 &&
                params[0] instanceof String mobType &&
                params[1] instanceof Material weapon &&
                params[2] instanceof Player player) {

            UUID playerId = player.getUniqueId();

            // Se tiver arrow como 4º parâmetro
            if (params.length >= 4 && params[3] instanceof Arrow arrow) {
                if (mobType.equalsIgnoreCase(targetMob) && isValidProjectile(arrow)) {
                    currentCount++;

                    // Feedback de progresso
                    int combo = playerCombos.getOrDefault(playerId, 0);
                    player.sendMessage("§a✓ Acerto! " + getProgressText());

                    if (checkCompletion()) {
                        resetCombo(player, "§6✦ §e§lCOMBO PERFEITO! §6Quest completada!");
                        QuestCompletedEvent customEvent = new QuestCompletedEvent(player, this);
                        Bukkit.getServer().getPluginManager().callEvent(customEvent);
                    }
                }
            }
        }
    }

    public void onMissedShot(Player player) {
        resetCombo(player, "§c✗ Errou! Combo perdido.");
    }

    private void resetCombo(Player player, String message) {
        UUID playerId = player.getUniqueId();
        int lostCombo = playerCombos.getOrDefault(playerId, 0);

        playerCombos.remove(playerId);
        lastHitTime.remove(playerId);

        if (lostCombo > 0) {
            player.sendMessage(message + " §7(Era " + lostCombo + "/10)");

            // Reseta progresso se perdeu combo alto
            if (lostCombo >= 3) {
                currentCount = 0; // Reseta para 0
                player.sendMessage("§c⚠ Progresso resetado! Mantenha o foco!");
            }
        }
    }

    @Override
    public String getProgressText() {
        int currentCombo = 0;
        if (!playerCombos.isEmpty()) {
            // Pega o combo do jogador atual (assumindo um jogador por instância)
            currentCombo = playerCombos.values().iterator().next();
        }
        return String.format("%d/%d acertos (Combo: %d/10)",
                getCurrentCount(), getTargetCount(), currentCombo);
    }

    @Override
    public void assignToPlayer(Player player) {
        super.assignToPlayer(player);
    }

    public int getCombo(Player player) {
        return playerCombos.getOrDefault(player.getUniqueId(), 0);
    }

    @Override
    public void cleanupEnvironment(Player player) {
        // Limpa os combos do jogador
        UUID playerId = player.getUniqueId();
        playerCombos.remove(playerId);
        lastHitTime.remove(playerId);

        // Chama cleanup da superclasse
        super.cleanupEnvironment(player);
    }

    @Override
    public ItemStack[] getRewardItems() {
        // Recompensas especiais para quest difícil
        return new ItemStack[]{
                new ItemStack(Material.ARROW, 32),
                new ItemStack(Material.SPECTRAL_ARROW, 8),
                new ItemStack(Material.EXPERIENCE_BOTTLE, 5),
                new ItemStack(Material.GOLDEN_APPLE, 1)
        };
    }
}