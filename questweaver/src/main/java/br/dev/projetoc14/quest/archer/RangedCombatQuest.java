package br.dev.projetoc14.quest.archer;

import br.dev.projetoc14.quest.HitQuest;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

/**
 * Quest Fácil: Arena de Treinamento Controlada - COM ACESSO FÁCIL E MOBS CONTIDOS
 */
public class RangedCombatQuest extends HitQuest {
    private static final double MIN_DISTANCE = 15.0;
    private Location arenaCenter;

    public RangedCombatQuest(Location spawnLocation) {
        super("ranged_combat_quest",
                "Combate a Distância",
                "Derrote 5 esqueletos na arena de treinamento a pelo menos " + (int)MIN_DISTANCE + " blocos",
                100,
                "SKELETON",
                5,
                0,
                spawnLocation,
                new ArrayList<>());
        this.arenaCenter = spawnLocation.clone().add(0, 0, 20);
    }

    @Override
    public void buildQuestEnvironment(Player player) {
        buildTrainingArena(player);
    }

    @Override
    public void spawnStrategicEntities(Player player) {
        spawnContainedSkeletons(player);
    }

    private void buildTrainingArena(Player player) {
        if (environmentBuilt) return;

        World world = player.getWorld();
        Location center = arenaCenter;

        // **PLATAFORMA DOS ESQUELETOS - CONTIDA**
        // Poço dos esqueletos (10x10) com paredes altas
        for (int x = -5; x <= 5; x++) {
            for (int z = -5; z <= 5; z++) {
                setBlockSafe(world, center.clone().add(x, 0, z), Material.OAK_PLANKS);
            }
        }

        // **PAREDES ALTAS** - esqueletos não escapam (3 blocos de altura)
        for (int y = 1; y <= 3; y++) {
            for (int x = -6; x <= 6; x++) {
                for (int z = -6; z <= 6; z++) {
                    if (Math.abs(x) == 6 || Math.abs(z) == 6) {
                        setBlockSafe(world, center.clone().add(x, y, z), Material.OAK_FENCE);
                    }
                }
            }
        }

        // **PLATAFORMA DO JOGADOR ELEVADA E ACESSÍVEL**
        Location playerPlatform = player.getLocation().clone();

        // **CORREÇÃO DA RAMPA**: Primeiro construímos a torre sólida
        // Base da torre (5x5)
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                setBlockSafe(world, playerPlatform.clone().add(x, 0, z), Material.STONE_BRICKS);
            }
        }

        // Torre sólida (3x3)
        for (int y = 1; y <= 4; y++) {
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    setBlockSafe(world, playerPlatform.clone().add(x, y, z), Material.STONE_BRICKS);
                }
            }
        }

        // **PLATAFORMA NO TOPO**
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                setBlockSafe(world, playerPlatform.clone().add(x, 5, z), Material.STONE_BRICKS);
            }
        }

        // **PARAPEITO COM ABERTURAS**
        for (int x = -3; x <= 3; x++) {
            for (int z = -3; z <= 3; z++) {
                if ((Math.abs(x) == 3 || Math.abs(z) == 3) && !(Math.abs(x) == 3 && Math.abs(z) == 3)) {
                    setBlockSafe(world, playerPlatform.clone().add(x, 6, z), Material.STONE_BRICK_WALL);
                }
            }
        }

        // **CORREÇÃO DA RAMPA**: Agora construímos a rampa conectando ao topo
        buildAccessRamp(world, playerPlatform);

        environmentBuilt = true;
        player.sendMessage("§a⚔ Arena de treinamento construída!");
        player.sendMessage("§e🎯 Use a rampa para subir na plataforma de tiro!");
    }

    private void buildAccessRamp(World world, Location platformBase) {
        // **CORREÇÃO**: Rampa começa no chão e sobe até o topo da plataforma (altura 5)
        for (int i = 0; i < 12; i++) { // Rampa mais longa para subir suavemente
            double progress = (double) i / 11; // 0.0 a 1.0
            int height = (int) (progress * 5); // Sobe de 0 até 5

            // Piso da rampa (3 blocos de largura)
            for (int x = 0; x < 3; x++) {
                setBlockSafe(world, platformBase.clone().add(3 + i, height, x - 1), Material.STONE_BRICKS);
            }

            // Corrimãos opcionais
            if (i % 2 == 0) { // Corrimãos a cada 2 blocos
                setBlockSafe(world, platformBase.clone().add(3 + i, height + 1, -2), Material.STONE_BRICK_WALL);
                setBlockSafe(world, platformBase.clone().add(3 + i, height + 1, 2), Material.STONE_BRICK_WALL);
            }
        }

        // **CORREÇÃO**: Plataforma de conexão no topo
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                setBlockSafe(world, platformBase.clone().add(15, 5, z), Material.STONE_BRICKS);
            }
        }
    }

    private void spawnContainedSkeletons(Player player) {
        if (arenaCenter == null) return;

        World world = player.getWorld();

        // **CORREÇÃO DO SPAWN**: Usar método correto para spawnar entidades
        int targetCount = getTargetCount();
        int spawnedCount = 0;

        for (int i = 0; i < targetCount; i++) {
            double angle = (2 * Math.PI * i) / targetCount;
            double distance = 2 + (i % 3); // Distâncias variadas dentro do poço
            double x = arenaCenter.getX() + distance * Math.cos(angle);
            double z = arenaCenter.getZ() + distance * Math.sin(angle);

            // Spawn no chão do poço (Y + 1 para ficar em cima dos blocos)
            Location spawnLoc = new Location(world, x, arenaCenter.getY() + 1, z);

            if (isSafeSpawnLocation(world, spawnLoc)) {
                // **CORREÇÃO**: Usar o método spawnQuestEntity corretamente
                try {
                    Skeleton skeleton = (Skeleton) spawnQuestEntity(world, spawnLoc,
                            org.bukkit.entity.EntityType.SKELETON, "§7Alvo de Treinamento");

                    if (skeleton != null) {
                        spawnedCount++;
                        // Configuração para ficar mais contido mas ainda atirar
                        skeleton.setAI(true);
                        skeleton.setRemoveWhenFarAway(false);
                    }
                } catch (Exception e) {
                    player.sendMessage("§cErro ao spawnar esqueleto: " + e.getMessage());
                }
            }
        }

        if (spawnedCount > 0) {
            player.sendMessage("§e🎯 " + spawnedCount + " esqueletos apareceram no poço de treinamento!");
            player.sendMessage("§6🎯 Acerte-os a pelo menos " + (int)MIN_DISTANCE + " blocos de distância!");
        } else {
            player.sendMessage("§c❌ Erro: Nenhum esqueleto foi spawnado!");
        }
    }

    /** Verifica se o local de spawn é seguro (não dentro de blocos) */
    private boolean isSafeSpawnLocation(World world, Location location) {
        // Verifica se o bloco na posição é ar e o bloco abaixo é sólido
        Location groundCheck = location.clone().add(0, -1, 0);
        return world.getBlockAt(location).getType() == Material.AIR &&
                world.getBlockAt(groundCheck).getType().isSolid() &&
                world.getBlockAt(groundCheck).getType() != Material.LAVA &&
                world.getBlockAt(groundCheck).getType() != Material.WATER;
    }

    @Override
    protected boolean isValidProjectile(Arrow arrow) {
        if (!(arrow.getShooter() instanceof Player shooter)) {
            return false;
        }

        Location shooterLoc = shooter.getLocation();
        Location arrowLoc = arrow.getLocation();
        double distance = shooterLoc.distance(arrowLoc);

        if (distance >= MIN_DISTANCE) {
            shooter.sendMessage(String.format("§a✓ Boa! %.1f blocos de distância!", distance));
            return true;
        } else {
            shooter.sendMessage(String.format("§c✗ Muito perto! (%.1f/%.0f blocos)", distance, MIN_DISTANCE));
            return false;
        }
    }

    @Override
    public void updateProgress(Object... params) {
        // **CORREÇÃO**: Verificar se temos os parâmetros mínimos antes de processar
        if (params.length >= 3 &&
                params[0] instanceof String &&
                params[1] instanceof Material &&
                params[2] instanceof Player) {

            String mobType = (String) params[0];
            Player player = (Player) params[2];

            // Verificar se é o mob correto antes de chamar super
            if (mobType.equalsIgnoreCase(targetMob)) {
                super.updateProgress(params);

                // Feedback adicional
                int current = getCurrentCount();
                int target = getTargetCount();

                if (current > 0 && current <= target) {
                    player.sendMessage("§a✓ " + getProgressText());
                }
            }
        }
    }

    @Override
    public String getProgressText() {
        return String.format("%d/%d esqueletos (mín. %d blocos)",
                getCurrentCount(), getTargetCount(), (int)MIN_DISTANCE);
    }

    @Override
    public void assignToPlayer(Player player) {
        // **CORREÇÃO**: Garantir que o ambiente seja construído antes de spawnar entidades
        super.assignToPlayer(player);

        // Spawnar esqueletos após um pequeno delay para garantir que a arena está construída
        try {
            // Pequeno delay para garantir que tudo foi construído
            Thread.sleep(100);
            spawnContainedSkeletons(player);
        } catch (InterruptedException e) {
            spawnContainedSkeletons(player);
        }
    }

    public static double getMinDistance() {
        return MIN_DISTANCE;
    }

    @Override
    public ItemStack[] getRewardItems() {
        // Recompensas para quest fácil
        return new ItemStack[]{
                new ItemStack(Material.ARROW, 16),
                new ItemStack(Material.EXPERIENCE_BOTTLE, 2),
                new ItemStack(Material.BREAD, 3)
        };
    }
}