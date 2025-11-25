package br.dev.projetoc14.quest.warrior;

import br.dev.projetoc14.QuestWeaver;
import br.dev.projetoc14.quest.KillQuest;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

import java.util.List;

public class BerserkerRageQuest extends KillQuest {

    public BerserkerRageQuest(Location spawnLocation) {
        super("warrior_berserker",
                "Fúria do Berserker",
                "Estilhace 6 Creepers usando seu Machado de Ferro",
                250,
                "CREEPER",
                6,
                0,
                spawnLocation,
                List.of(Material.IRON_AXE) // Só aceita machado
        );
    }

    @Override
    public void spawnTargetEntities(Player player) {
        World world = player.getWorld();
        Location playerLoc = player.getLocation();

        // 1. Pega a direção que o jogador está olhando (ignorando altura Y)
        Vector direction = playerLoc.getDirection().setY(0).normalize();

        // Configuração de Distância Segura
        double minDistance = 10.0;
        double maxDistance = 15.0;

        for (int i = 0; i < targetCount; i++) {
            // 2. Calcula um ângulo aleatório num cone de 90º à frente (-45º a +45º)
            double angleOffset = Math.toRadians(Math.random() * 90 - 45);

            // Rotaciona o vetor de direção matematicamente
            double x = direction.getX() * Math.cos(angleOffset) - direction.getZ() * Math.sin(angleOffset);
            double z = direction.getX() * Math.sin(angleOffset) + direction.getZ() * Math.cos(angleOffset);
            Vector spawnDir = new Vector(x, 0, z);

            // 3. Define a distância aleatória
            double distance = minDistance + (Math.random() * (maxDistance - minDistance));

            // Calcula a posição final
            Location spawnLoc = playerLoc.clone().add(spawnDir.multiply(distance));

            // 4. Ajusta o Y para o chão (previne spawnar dentro da parede ou no ar)
            spawnLoc.setY(world.getHighestBlockYAt(spawnLoc) + 1);

            // 5. Spawna e Configura o Creeper
            Entity entity = world.spawnEntity(spawnLoc, EntityType.CREEPER);

            if (entity instanceof Creeper creeper) {
                creeper.setMaxFuseTicks(60); // Aumenta o tempo de explosão para 3s (padrão é 1.5s)
                creeper.setPowered(false);   // Garante que não é creeper carregado
            }

            // Marca como alvo da quest (Importante para não dropar itens e contar kills)
            NamespacedKey key = new NamespacedKey(QuestWeaver.getInstance(), "quest_target");
            entity.getPersistentDataContainer().set(key, PersistentDataType.BYTE, (byte) 1);
            entity.customName(Component.text("§cAlvo Berserker"));
            entity.setCustomNameVisible(true);
        }

        player.sendMessage("§c⚠ Creepers detectados à frente! Prepare-se!");
    }
}