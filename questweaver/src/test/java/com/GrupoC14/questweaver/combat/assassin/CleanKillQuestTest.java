package com.GrupoC14.questweaver.combat.assassin;

import br.dev.projetoc14.QuestWeaver;
import br.dev.projetoc14.quest.assassin.CleanKillQuest;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;
import org.mockbukkit.mockbukkit.entity.PlayerMock;

import static org.junit.jupiter.api.Assertions.*;

public class CleanKillQuestTest {

    private CleanKillQuest quest;
    private ServerMock server;
    private Location spawnLocation;

    @BeforeEach
    void setUp() {
        server = MockBukkit.mock();
        QuestWeaver plugin = MockBukkit.load(QuestWeaver.class);

        World world = server.addSimpleWorld("world");
        spawnLocation = new Location(world, 100, 64, 100);
        quest = new CleanKillQuest(spawnLocation);
    }

    @AfterEach
    void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    public void testCleanKillIncrementsProgress() {
        PlayerMock player = server.addPlayer();
        player.setHealth(20.0); // Vida cheia

        // Primeiro kill
        quest.updateProgress("ZOMBIE", Material.IRON_SWORD, player);
        assertEquals(1, quest.getCurrentCount());

        // Segundo kill (ainda sem dano)
        quest.updateProgress("ZOMBIE", Material.IRON_SWORD, player);
        assertEquals(2, quest.getCurrentCount());
    }

    @Test
    public void testDirtyKillDoesNotCount() {
        PlayerMock player = server.addPlayer();

        // Primeiro kill com vida cheia
        player.setHealth(20.0);
        quest.updateProgress("ZOMBIE", Material.IRON_SWORD, player);
        assertEquals(1, quest.getCurrentCount());

        // Segundo kill com dano (não deve contar)
        player.setHealth(15.0); // Dano tomado
        quest.updateProgress("ZOMBIE", Material.IRON_SWORD, player);

        assertEquals(1, quest.getCurrentCount(), "Kill com dano não deve contar");
    }

    @Test
    public void testMixedKills() {
        PlayerMock player = server.addPlayer();

        // Primeiro kill com vida cheia (deve contar)
        player.setHealth(20.0);
        quest.updateProgress("ZOMBIE", Material.IRON_SWORD, player);
        assertEquals(1, quest.getCurrentCount());

        // Segundo kill com dano (não deve contar)
        player.setHealth(18.0);
        quest.updateProgress("ZOMBIE", Material.STONE_SWORD, player);
        assertEquals(1, quest.getCurrentCount(), "Kill com dano não deve contar");

        // Terceiro kill com vida cheia novamente (deve contar)
        player.setHealth(20.0);
        quest.updateProgress("ZOMBIE", Material.WOODEN_SWORD, player);
        assertEquals(2, quest.getCurrentCount());
    }

    @Test
    public void testHealthTracking() {
        PlayerMock player = server.addPlayer();

        // Primeiro kill - vida cheia
        player.setHealth(20.0);
        quest.updateProgress("ZOMBIE", Material.IRON_SWORD, player);
        assertEquals(1, quest.getCurrentCount());

        // Tomou dano mas não matou nada
        player.setHealth(15.0);

        // Tentativa de kill com dano (não deve contar)
        quest.updateProgress("ZOMBIE", Material.IRON_SWORD, player);
        assertEquals(1, quest.getCurrentCount(), "Kill com dano não deve contar");

        // Cura e tenta novamente
        player.setHealth(20.0);
        quest.updateProgress("ZOMBIE", Material.IRON_SWORD, player);
        assertEquals(2, quest.getCurrentCount());
    }

    @Test
    public void testQuestCompletion() {
        PlayerMock player = server.addPlayer();
        player.setHealth(20.0);

        // 3 kills limpos consecutivos
        quest.updateProgress("ZOMBIE", Material.IRON_SWORD, player);
        quest.updateProgress("ZOMBIE", Material.STONE_SWORD, player);
        quest.updateProgress("ZOMBIE", Material.WOODEN_SWORD, player);

        assertTrue(quest.checkCompletion());
        assertEquals(3, quest.getCurrentCount());
    }

    @Test
    public void testWrongMobDoesNotCount() {
        PlayerMock player = server.addPlayer();
        player.setHealth(20.0);

        quest.updateProgress("SKELETON", Material.IRON_SWORD, player);

        assertEquals(0, quest.getCurrentCount());
    }

    @Test
    public void testWrongWeaponDoesNotCount() {
        PlayerMock player = server.addPlayer();
        player.setHealth(20.0);

        quest.updateProgress("ZOMBIE", Material.DIAMOND_AXE, player);

        assertEquals(0, quest.getCurrentCount());
    }

    @Test
    public void testOnPlayerDamagedFeedback() {
        PlayerMock player = server.addPlayer();

        // Simula progresso parcial
        player.setHealth(20.0);
        quest.updateProgress("ZOMBIE", Material.IRON_SWORD, player);

        // Chama o método de dano
        assertDoesNotThrow(() -> quest.onPlayerDamaged(player));
    }

}