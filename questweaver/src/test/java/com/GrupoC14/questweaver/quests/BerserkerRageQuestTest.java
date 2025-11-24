package com.GrupoC14.questweaver.quests;

import br.dev.projetoc14.QuestWeaver;
import br.dev.projetoc14.quest.warrior.BerserkerRageQuest;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;
import org.mockbukkit.mockbukkit.entity.PlayerMock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BerserkerRageQuestTest {

    private BerserkerRageQuest quest;
    private ServerMock server;
    private PlayerMock player;
    private World world;

    @BeforeEach
    void setUp() {
        server = MockBukkit.mock();
        MockBukkit.load(QuestWeaver.class);
        world = server.addSimpleWorld("world");
        player = server.addPlayer();
        quest = new BerserkerRageQuest(new Location(world, 0, 64, 0));
    }

    @AfterEach
    void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    public void testSpawnCreepers() {
        // Garante que o mundo tem chão para o spawn
        world.getBlockAt(0, 63, 0).setType(Material.GRASS_BLOCK);

        // Executa o spawn
        quest.spawnTargetEntities(player);

        // Verifica se entidades foram spawnadas
        List<Entity> entities = world.getEntities();
        long creeperCount = entities.stream()
                .filter(e -> e.getType() == EntityType.CREEPER)
                .count();

        assertEquals(6, creeperCount, "Devem ser spawnados 6 Creepers");

        // Verifica se o fuse time foi alterado (MockBukkit pode não simular perfeitamente NBT,
        // mas podemos verificar se o objeto foi criado corretamente)
        Creeper creeper = (Creeper) entities.stream().filter(e -> e.getType() == EntityType.CREEPER).findFirst().orElse(null);
        assertNotNull(creeper);
        assertEquals(60, creeper.getMaxFuseTicks());
    }

    @Test
    public void testAxeKillCounts() {
        quest.updateProgress("CREEPER", Material.IRON_AXE, player);
        assertEquals(1, quest.getCurrentCount());
    }

    @Test
    public void testSwordKillDoesNotCount() {
        quest.updateProgress("CREEPER", Material.IRON_SWORD, player);
        assertEquals(0, quest.getCurrentCount(), "Espada não deve contar, apenas machado");
    }
}