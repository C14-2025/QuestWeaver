package br.dev.projetoc14.quest.archer;

import br.dev.projetoc14.quest.HitQuest;
import br.dev.projetoc14.quest.utils.QuestCompletedEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Quest Difícil: Desafio do Mestre Flecheiro - Adaptada para nova HitQuest
 */
public class WindMasterQuest extends HitQuest {
    private static final int MAX_COMBO = 10;
    private static final long COMBO_TIMEOUT = 7000; // 7 segundos
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
        this.challengeArena = spawnLocation.clone().add(-15, 0, 0);
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

        // Arena circular para desafio
        int radius = 12;
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                double distance = Math.sqrt(x*x + z*z);
                if (distance <= radius) {
                    setBlockSafe(world, center.clone().add(x, -1, z), Material.DARK_OAK_PLANKS);
                }
            }
        }

        // Obstáculos e cobertura
        buildObstacles(world, center);

        // Torres de tiro em diferentes alturas
        buildSniperTower(world, center.clone().add(-18, 0, 0), 5); // Torre baixa
        buildSniperTower(world, center.clone().add(18, 0, 0), 8);  // Torre média
        buildSniperTower(world, player.getLocation(), 12);         // Torre alta do jogador

        environmentBuilt = true;
        player.sendMessage("§6⚡ Desafio do Mestre Flecheiro iniciado!");
        player.sendMessage("§e🎯 Use as torres para obter vantagem estratégica!");
    }

    private void buildObstacles(World world, Location center) {
        // Pilares no centro
        for (int i = 0; i < 4; i++) {
            double angle = (Math.PI / 2) * i;
            int x = (int) (6 * Math.cos(angle));
            int z = (int) (6 * Math.sin(angle));

            for (int y = 0; y < 4; y++) {
                setBlockSafe(world, center.clone().add(x, y, z), Material.COBBLESTONE);
            }
        }

        // Muros baixos
        for (int x = -8; x <= 8; x += 16) {
            for (int z = -3; z <= 3; z++) {
                setBlockSafe(world, center.clone().add(x, 0, z), Material.STONE_BRICK_WALL);
            }
        }
    }

    private void buildSniperTower(World world, Location base, int height) {
        // Torre 3x3
        for (int y = 0; y < height; y++) {
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    if (Math.abs(x) == 1 || Math.abs(z) == 1) {
                        setBlockSafe(world, base.clone().add(x, y, z), Material.STONE_BRICKS);
                    }
                }
            }
        }

        // Plataforma no topo
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                setBlockSafe(world, base.clone().add(x, height, z), Material.OAK_PLANKS);
            }
        }

        // Escada interna
        setBlockSafe(world, base.clone().add(1, 0, 0), Material.LADDER);
        setBlockSafe(world, base.clone().add(1, 1, 0), Material.LADDER);
        setBlockSafe(world, base.clone().add(1, 2, 0), Material.LADDER);
    }

    private void spawnChallengeCreepers(Player player) {
        if (challengeArena == null) return;

        World world = player.getWorld();

        // Spawna creepers em posições estratégicas
        for (int i = 0; i < 15; i++) { // Spawna extras para desafio
            double angle = (2 * Math.PI * i) / 15;
            double distance = 8 + (i % 3); // Distâncias variadas
            double x = challengeArena.getX() + distance * Math.cos(angle);
            double z = challengeArena.getZ() + distance * Math.sin(angle);

            Location spawnLoc = new Location(world, x, challengeArena.getY(), z);

            // Usa o método utilitário da superclasse
            spawnQuestEntity(world, spawnLoc, org.bukkit.entity.EntityType.CREEPER, "§cDesafio Creeper");
        }

        player.sendMessage("§c💣 " + "15 creepers apareceram no desafio!");
        player.sendMessage("§6🎯 Acerte 10 em sequência sem errar!");
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
}