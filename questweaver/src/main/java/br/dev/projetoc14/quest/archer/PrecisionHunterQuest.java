package br.dev.projetoc14.quest.archer;

import br.dev.projetoc14.quest.HitQuest;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

/**
 * Quest Média: Campo de Tiro ao Alvo - COM ZUMBIS CONTIDOS E ACESSO FÁCIL
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
        buildContainedShootingRange(player);
    }

    @Override
    public void spawnStrategicEntities(Player player) {
        spawnContainedZombies(player);
    }

    private void buildContainedShootingRange(Player player) {
        if (environmentBuilt) return;

        World world = player.getWorld();
        Location center = shootingRange;

        // **CORREDOR DE TIRO** com zumbis contidos
        // Corredor longo (25 blocos) mas estreito (5 blocos de largura)
        for (int z = 0; z <= 25; z++) {
            for (int x = -2; x <= 2; x++) {
                setBlockSafe(world, center.clone().add(x, -1, z), Material.OAK_PLANKS);
            }
        }

        // **PAREDES LATERAIS ALTAS** - zumbis não escapam (3 blocos de altura)
        for (int z = 0; z <= 25; z++) {
            for (int y = 0; y <= 3; y++) {
                setBlockSafe(world, center.clone().add(-3, y, z), Material.OAK_FENCE);
                setBlockSafe(world, center.clone().add(3, y, z), Material.OAK_FENCE);
            }
        }

        // **PLATAFORMAS DOS ZUMBIS** - áreas contidas em posições estratégicas
        buildZombiePlatforms(world, center);

        // **PLATAFORMA DO JOGADOR ELEVADA** - com rampa de acesso
        buildPlayerShootingPlatform(world, player.getLocation());

        environmentBuilt = true;
        player.sendMessage("§a🎯 Campo de tiro construído!");
        player.sendMessage("§e💡 Os zumbis estão contidos no corredor!");
        player.sendMessage("§6🎯 Puxe o arco completamente para tiros críticos!");
    }

    private void buildZombiePlatforms(World world, Location center) {
        // Plataformas fixas para zumbis em distâncias variadas
        int[] platformDistances = {5, 8, 11, 14, 17, 20, 23};

        for (int distance : platformDistances) {
            // Plataforma 3x3 para zumbi
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    setBlockSafe(world, center.clone().add(x, 0, distance), Material.SPRUCE_PLANKS);
                }
            }

            // Barreiras laterais pequenas para conter zumbis
            setBlockSafe(world, center.clone().add(-2, 1, distance), Material.OAK_FENCE);
            setBlockSafe(world, center.clone().add(2, 1, distance), Material.OAK_FENCE);
            setBlockSafe(world, center.clone().add(-2, 2, distance), Material.OAK_FENCE);
            setBlockSafe(world, center.clone().add(2, 2, distance), Material.OAK_FENCE);
        }

        // **BARREIRA NO FINAL** - zumbis não passam do final
        for (int x = -3; x <= 3; x++) {
            for (int y = 0; y <= 3; y++) {
                setBlockSafe(world, center.clone().add(x, y, 26), Material.OAK_FENCE);
            }
        }
    }

    private void buildPlayerShootingPlatform(World world, Location playerLoc) {
        // **PLATAFORMA ELEVADA** para o jogador (3 blocos de altura)
        Location platformBase = playerLoc.clone();

        // Base da plataforma 5x5
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                for (int y = 0; y <= 3; y++) {
                    setBlockSafe(world, platformBase.clone().add(x, y, z), Material.STONE_BRICKS);
                }
            }
        }

        // Plataforma no topo
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                setBlockSafe(world, platformBase.clone().add(x, 4, z), Material.STONE_BRICKS);
            }
        }

        // **PARAPEITO COM ABERTURAS** para atirar
        for (int x = -3; x <= 3; x++) {
            for (int z = -3; z <= 3; z++) {
                if (Math.abs(x) == 3 || Math.abs(z) == 3) {
                    // Deixa aberturas estratégicas
                    if (!(x % 2 == 0 && z % 2 == 0)) {
                        setBlockSafe(world, platformBase.clone().add(x, 5, z), Material.STONE_BRICK_WALL);
                    }
                }
            }
        }

        // **RAMPA DE ACESSO** fácil
        buildPlatformRamp(world, platformBase);
    }

    private void buildPlatformRamp(World world, Location platformBase) {
        // Rampa suave de 6 blocos de comprimento
        for (int i = 0; i < 6; i++) {
            int height = i / 2; // Sobe gradualmente
            for (int x = 0; x <= 2; x++) {
                for (int z = 0; z <= 2; z++) {
                    setBlockSafe(world, platformBase.clone().add(3 + i, height, z - 1), Material.STONE_BRICKS);
                }
            }
        }

        // Corrimãos da rampa
        for (int i = 0; i < 6; i++) {
            int height = i / 2;
            setBlockSafe(world, platformBase.clone().add(3 + i, height + 1, -2), Material.STONE_BRICK_WALL);
            setBlockSafe(world, platformBase.clone().add(3 + i, height + 1, 2), Material.STONE_BRICK_WALL);
        }
    }

    private void spawnContainedZombies(Player player) {
        if (shootingRange == null) return;

        World world = player.getWorld();

        // **ZUMBIS EM PLATAFORMAS FIXAS** - não se movem muito
        int[] platformDistances = {5, 8, 11, 14, 17, 20, 23};
        int zombiesPerPlatform = (int) Math.ceil((double) getTargetCount() / platformDistances.length);

        int spawned = 0;
        for (int distance : platformDistances) {
            for (int i = 0; i < zombiesPerPlatform && spawned < getTargetCount(); i++) {
                double offset = (i % 2 == 0) ? -0.8 : 0.8; // Alterna entre lados do corredor

                Location spawnLoc = shootingRange.clone().add(offset, 1, distance);

                if (isSafeSpawnLocation(world, spawnLoc)) {
                    // Spawna zumbi e configura para ficar contido
                    Zombie zombie = (Zombie) spawnQuestEntity(world, spawnLoc,
                            org.bukkit.entity.EntityType.ZOMBIE, "§2Alvo de Tiro");

                    // Configurações para zumbi mais contido
                    zombie.setAI(true);

                    spawned++;
                }
            }
        }

        player.sendMessage("§e🎯 " + spawned + " zumbis apareceram no corredor!");
        player.sendMessage("§6💡 Puxe o arco completamente para tiros críticos!");
    }

    /** Verifica se o local de spawn é seguro */
    private boolean isSafeSpawnLocation(World world, Location location) {
        Location groundLevel = location.clone().add(0, -1, 0);
        return world.getBlockAt(location).getType() == Material.AIR &&
                world.getBlockAt(groundLevel).getType().isSolid();
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

                // Feedback especial a cada 3 acertos
                if (current % 3 == 0) {
                    player.sendMessage("§6✦ Continue assim! " + (getTargetCount() - current) + " críticos restantes!");
                }
            }
        }
    }

    @Override
    public String getProgressText() {
        return String.format("%d/%d tiros críticos em zumbis",
                getCurrentCount(), getTargetCount());
    }

    @Override
    public void assignToPlayer(Player player) {
        super.assignToPlayer(player);
    }

    @Override
    public ItemStack[] getRewardItems() {
        // Recompensas para quest média
        return new ItemStack[]{
                new ItemStack(Material.ARROW, 24),
                new ItemStack(Material.SPECTRAL_ARROW, 4),
                new ItemStack(Material.EXPERIENCE_BOTTLE, 3),
                new ItemStack(Material.GOLDEN_CARROT, 2)
        };
    }

    @Override
    public void cleanupEnvironment(Player player) {
        // Limpeza específica se necessário
        super.cleanupEnvironment(player);
    }
}