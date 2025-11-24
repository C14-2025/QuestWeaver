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
 * Quest Fácil: Combate a Distância - VERSÃO SUPER SIMPLIFICADA
 */
public class RangedCombatQuest extends HitQuest {
    private static final double MIN_DISTANCE = 15.0;
    private Location skeletonPlatform;

    public RangedCombatQuest(Location spawnLocation) {
        super("ranged_combat_quest",
                "Combate a Distância",
                "Derrote 5 esqueletos a pelo menos " + (int)MIN_DISTANCE + " blocos",
                100,
                "SKELETON",
                5,
                0,
                spawnLocation,
                new ArrayList<>());

        // Plataforma fica a 20 blocos na frente do spawn
        this.skeletonPlatform = spawnLocation.clone().add(20, 0, 0);
    }

    @Override
    public void buildQuestEnvironment(Player player) {
        if (environmentBuilt) return;

        World world = player.getWorld();
        Location center = skeletonPlatform;

        player.sendMessage("§a🔨 Criando plataforma dos esqueletos...");

        // **PLATAFORMA BEM SIMPLES** - 5x5 blocos
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                setBlockSafe(world, center.clone().add(x, 0, z), Material.COBBLESTONE);
            }
        }

        // **PAREDINHA BAIXA** - só para não cairem
        for (int x = -3; x <= 3; x++) {
            for (int z = -3; z <= 3; z++) {
                if (Math.abs(x) == 3 || Math.abs(z) == 3) {
                    setBlockSafe(world, center.clone().add(x, 1, z), Material.COBBLESTONE_WALL);
                }
            }
        }

        environmentBuilt = true;
        player.sendMessage("§a✅ Plataforma pronta!");
    }

    @Override
    public void spawnStrategicEntities(Player player) {
        if (skeletonPlatform == null) {
            player.sendMessage("§c❌ Erro: Plataforma não existe!");
            return;
        }

        World world = player.getWorld();

        player.sendMessage("§e👻 Spawnando esqueletos...");

        // **SPAWN EM POSIÇÕES FIXAS** na plataforma
        Location[] spawnSpots = {
                skeletonPlatform.clone().add(-1, 1, -1),
                skeletonPlatform.clone().add(-1, 1, 1),
                skeletonPlatform.clone().add(0, 1, 0),
                skeletonPlatform.clone().add(1, 1, -1),
                skeletonPlatform.clone().add(1, 1, 1)
        };

        for (int i = 0; i < spawnSpots.length; i++) {
            Location spawnLoc = spawnSpots[i];

            player.sendMessage("§7📍 Spawn " + (i + 1) + ": " +
                    String.format("%.0f, %.0f, %.0f", spawnLoc.getX(), spawnLoc.getY(), spawnLoc.getZ()));

            try {
                // **USA O MÉTODO DA SUPERCLASSE** para spawnar
                Skeleton skeleton = (Skeleton) spawnQuestEntity(world, spawnLoc,
                        org.bukkit.entity.EntityType.SKELETON, "§7Alvo " + (i + 1));

                // **DAR ARCO PARA REVIDAR**
                skeleton.getEquipment().setItemInMainHand(new ItemStack(Material.BOW));

                player.sendMessage("§a✅ Esqueleto " + (i + 1) + " pronto!");

            } catch (Exception e) {
                player.sendMessage("§c❌ Erro no esqueleto " + (i + 1));
            }
        }

        player.sendMessage("§e🎯 5 esqueletos spawnados! Eles vão revidar!");
        player.sendMessage("§6⚡ Mínimo: " + (int)MIN_DISTANCE + " blocos de distância!");
    }

    @Override
    protected boolean isValidProjectile(Arrow arrow) {
        if (!(arrow.getShooter() instanceof Player shooter)) {
            return false;
        }

        // **VERIFICA DISTÂNCIA**
        Location shooterLoc = shooter.getLocation();
        Location arrowLoc = arrow.getLocation();
        double distance = shooterLoc.distance(arrowLoc);

        if (distance >= MIN_DISTANCE) {
            shooter.sendMessage(String.format("§a✓ Distância boa! %.1f blocos", distance));
            return true;
        } else {
            shooter.sendMessage(String.format("§c✗ Muito perto! %.1f/%d blocos", distance, (int)MIN_DISTANCE));
            return false;
        }
    }

    @Override
    public void updateProgress(Object... params) {
        // **DEBUG BÁSICO**
        if (params.length >= 3 && params[2] instanceof Player player) {
            player.sendMessage("§7🎯 Processando acerto em esqueleto...");
        }

        // **CHAMA A LÓGICA DA SUPERCLASSE** - ela já faz a contagem
        if (params.length >= 3 &&
                params[0] instanceof String &&
                params[1] instanceof Material &&
                params[2] instanceof Player) {

            super.updateProgress(params);
        }
    }

    @Override
    public String getProgressText() {
        return String.format("%d/%d esqueletos (mín. %d blocos)",
                getCurrentCount(), getTargetCount(), (int)MIN_DISTANCE);
    }

    @Override
    public void assignToPlayer(Player player) {
        player.sendMessage("§6🏹 Missão: Combate a Distância");
        player.sendMessage("§7" + getDescription());

        // **USA O assignToPlayer DA SUPERCLASSE** - que já chama build e spawn
        super.assignToPlayer(player);
    }

    @Override
    public ItemStack[] getRewardItems() {
        // Recompensas mínimas por enquanto
        return new ItemStack[]{
                new ItemStack(Material.ARROW, 5),
                new ItemStack(Material.EXPERIENCE_BOTTLE, 1)
        };
    }
}