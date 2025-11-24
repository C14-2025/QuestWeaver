package com.GrupoC14.questweaver.combat.assassin;

import br.dev.projetoc14.QuestWeaver;
import br.dev.projetoc14.quest.assassin.PerfectAssassinationQuest;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.LivingEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;
import org.mockbukkit.mockbukkit.entity.PlayerMock;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PerfectAssassinationQuestTest {

    private PerfectAssassinationQuest quest;
    private ServerMock server;
    private Location spawnLocation;

    @BeforeEach
    void setUp() {
        server = MockBukkit.mock();
        QuestWeaver plugin = MockBukkit.load(QuestWeaver.class);

        World world = server.addSimpleWorld("world");
        spawnLocation = new Location(world, 100, 64, 100);
        quest = new PerfectAssassinationQuest(spawnLocation);
    }

    @AfterEach
    void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    public void testQuestProperties() {
        assertEquals("perfect_assassination_quest", quest.getId());
        assertEquals("Assassinato Perfeito", quest.getName());
        assertEquals("Mate 4 creepers sem tomar dano e sem deixá-los explodir", quest.getDescription());
        assertEquals(350, quest.getExperienceReward());
        assertEquals("CREEPER", quest.targetMob);
        assertEquals(4, quest.getTargetCount());
    }

    @Test
    public void testValidWeapons() {
        assertTrue(quest.isValidWeapon(Material.IRON_SWORD));
        assertTrue(quest.isValidWeapon(Material.DIAMOND_SWORD));
        assertTrue(quest.isValidWeapon(Material.NETHERITE_SWORD));
        assertFalse(quest.isValidWeapon(Material.WOODEN_SWORD));
    }

    @Test
    public void testPerfectKillIncrementsProgress() {
        PlayerMock player = server.addPlayer();
        player.setHealth(20.0); // Vida cheia

        // Usamos um mock do Creeper
        Creeper creeper = mock(Creeper.class);
        when(creeper.getUniqueId()).thenReturn(UUID.randomUUID());

        quest.updateProgress("CREEPER", Material.DIAMOND_SWORD, player, creeper);

        assertEquals(1, quest.getCurrentCount());
    }

    @Test
    public void testDamagedPlayerResetsProgress() {
        PlayerMock player = server.addPlayer();
        Creeper creeper = mock(Creeper.class);
        when(creeper.getUniqueId()).thenReturn(UUID.randomUUID());

        // Primeiro kill perfeito
        player.setHealth(20.0);
        quest.updateProgress("CREEPER", Material.DIAMOND_SWORD, player, creeper);
        assertEquals(1, quest.getCurrentCount());

        // Simula que jogador tomou dano
        player.setHealth(15.0);
        quest.onPlayerDamaged(player);

        assertEquals(0, quest.getCurrentCount(), "Dano deve resetar progresso");
    }

    @Test
    public void testExplodedCreeperDoesNotCount() {
        PlayerMock player = server.addPlayer();
        player.setHealth(20.0);

        // Cria um creeper mock e marca como explodido
        Creeper creeper = mock(Creeper.class);
        UUID creeperId = UUID.randomUUID();
        when(creeper.getUniqueId()).thenReturn(creeperId);

        // Marca creeper como explodido
        quest.onCreeperExplode(creeper);

        // Tenta matar creeper explodido
        quest.updateProgress("CREEPER", Material.DIAMOND_SWORD, player, creeper);

        assertEquals(0, quest.getCurrentCount(), "Creeper explodido não deve contar");
    }

    @Test
    public void testQuestCompletion() {
        PlayerMock player = server.addPlayer();
        player.setHealth(20.0);

        // 4 kills perfeitos
        for (int i = 0; i < 4; i++) {
            Creeper creeper = mock(Creeper.class);
            when(creeper.getUniqueId()).thenReturn(UUID.randomUUID());
            quest.updateProgress("CREEPER", Material.DIAMOND_SWORD, player, creeper);
        }

        assertTrue(quest.checkCompletion());
        assertEquals(4, quest.getCurrentCount());
    }

    @Test
    public void testWrongMobDoesNotCount() {
        PlayerMock player = server.addPlayer();
        player.setHealth(20.0);

        Creeper creeper = mock(Creeper.class);
        when(creeper.getUniqueId()).thenReturn(UUID.randomUUID());

        quest.updateProgress("ZOMBIE", Material.DIAMOND_SWORD, player, creeper);

        assertEquals(0, quest.getCurrentCount());
    }

    @Test
    public void testWrongWeaponDoesNotCount() {
        PlayerMock player = server.addPlayer();
        player.setHealth(20.0);

        Creeper creeper = mock(Creeper.class);
        when(creeper.getUniqueId()).thenReturn(UUID.randomUUID());

        quest.updateProgress("CREEPER", Material.WOODEN_SWORD, player, creeper);

        assertEquals(0, quest.getCurrentCount());
    }

    @Test
    public void testOnCreeperExplode() {
        Creeper creeper = mock(Creeper.class);
        when(creeper.getUniqueId()).thenReturn(UUID.randomUUID());

        assertDoesNotThrow(() -> quest.onCreeperExplode(creeper));
    }

    @Test
    public void testProgressText() {
        PlayerMock player = server.addPlayer();
        player.setHealth(20.0);

        Creeper creeper = mock(Creeper.class);
        when(creeper.getUniqueId()).thenReturn(UUID.randomUUID());

        quest.updateProgress("CREEPER", Material.DIAMOND_SWORD, player, creeper);

        String progress = quest.getProgressText();
        assertTrue(progress.contains("1/4"));
        assertTrue(progress.contains("execuções perfeitas"));
    }

    @Test
    public void testMixedScenario() {
        PlayerMock player = server.addPlayer();

        // Primeiro kill perfeito
        player.setHealth(20.0);
        Creeper creeper1 = mock(Creeper.class);
        when(creeper1.getUniqueId()).thenReturn(UUID.randomUUID());
        quest.updateProgress("CREEPER", Material.DIAMOND_SWORD, player, creeper1);
        assertEquals(1, quest.getCurrentCount());

        // Segundo kill com dano (deve resetar)
        player.setHealth(15.0);
        Creeper creeper2 = mock(Creeper.class);
        when(creeper2.getUniqueId()).thenReturn(UUID.randomUUID());
        quest.updateProgress("CREEPER", Material.DIAMOND_SWORD, player, creeper2);
        assertEquals(0, quest.getCurrentCount(), "Dano deve resetar progresso");

        // Terceiro kill perfeito novamente
        player.setHealth(20.0);
        Creeper creeper3 = mock(Creeper.class);
        when(creeper3.getUniqueId()).thenReturn(UUID.randomUUID());
        quest.updateProgress("CREEPER", Material.DIAMOND_SWORD, player, creeper3);
        assertEquals(1, quest.getCurrentCount());
    }

    @Test
    public void testOnPlayerDamagedFeedback() {
        PlayerMock player = server.addPlayer();

        // Simula progresso parcial
        player.setHealth(20.0);
        Creeper creeper = mock(Creeper.class);
        when(creeper.getUniqueId()).thenReturn(UUID.randomUUID());
        quest.updateProgress("CREEPER", Material.DIAMOND_SWORD, player, creeper);

        // Chama o método de dano
        assertDoesNotThrow(() -> quest.onPlayerDamaged(player));
    }

    @Test
    public void testMultipleCreepersExploded() {
        PlayerMock player = server.addPlayer();
        player.setHealth(20.0);

        // Primeiro creeper explode
        Creeper creeper1 = mock(Creeper.class);
        UUID creeper1Id = UUID.randomUUID();
        when(creeper1.getUniqueId()).thenReturn(creeper1Id);
        quest.onCreeperExplode(creeper1);
        quest.updateProgress("CREEPER", Material.DIAMOND_SWORD, player, creeper1);
        assertEquals(0, quest.getCurrentCount(), "Creeper explodido não conta");

        // Segundo creeper normal
        Creeper creeper2 = mock(Creeper.class);
        when(creeper2.getUniqueId()).thenReturn(UUID.randomUUID());
        quest.updateProgress("CREEPER", Material.DIAMOND_SWORD, player, creeper2);
        assertEquals(1, quest.getCurrentCount(), "Creeper normal conta");

        // Terceiro creeper explode
        Creeper creeper3 = mock(Creeper.class);
        UUID creeper3Id = UUID.randomUUID();
        when(creeper3.getUniqueId()).thenReturn(creeper3Id);
        quest.onCreeperExplode(creeper3);
        quest.updateProgress("CREEPER", Material.DIAMOND_SWORD, player, creeper3);
        assertEquals(1, quest.getCurrentCount(), "Creeper explodido não conta");
    }

    @Test
    public void testHealthTrackingBetweenKills() {
        PlayerMock player = server.addPlayer();

        // Primeiro kill - vida cheia
        player.setHealth(20.0);
        Creeper creeper1 = mock(Creeper.class);
        when(creeper1.getUniqueId()).thenReturn(UUID.randomUUID());
        quest.updateProgress("CREEPER", Material.DIAMOND_SWORD, player, creeper1);
        assertEquals(1, quest.getCurrentCount());

        // Tomou dano mas não matou nada
        player.setHealth(15.0);

        // Tentativa de kill com dano (não deve contar)
        Creeper creeper2 = mock(Creeper.class);
        when(creeper2.getUniqueId()).thenReturn(UUID.randomUUID());
        quest.updateProgress("CREEPER", Material.DIAMOND_SWORD, player, creeper2);
        assertEquals(0, quest.getCurrentCount(), "Kill com dano não deve contar");

        // Cura e tenta novamente
        player.setHealth(20.0);
        Creeper creeper3 = mock(Creeper.class);
        when(creeper3.getUniqueId()).thenReturn(UUID.randomUUID());
        quest.updateProgress("CREEPER", Material.DIAMOND_SWORD, player, creeper3);
        assertEquals(1, quest.getCurrentCount());
    }

    @Test
    public void testNoProgressAfterCompletion() {
        PlayerMock player = server.addPlayer();
        player.setHealth(20.0);

        // Completa a quest
        for (int i = 0; i < 4; i++) {
            Creeper creeper = mock(Creeper.class);
            when(creeper.getUniqueId()).thenReturn(UUID.randomUUID());
            quest.updateProgress("CREEPER", Material.DIAMOND_SWORD, player, creeper);
        }

        assertTrue(quest.checkCompletion());
        assertEquals(4, quest.getCurrentCount());

        // Tentativa de kill adicional não deve alterar nada
        Creeper extraCreeper = mock(Creeper.class);
        when(extraCreeper.getUniqueId()).thenReturn(UUID.randomUUID());
        quest.updateProgress("CREEPER", Material.DIAMOND_SWORD, player, extraCreeper);
        assertEquals(4, quest.getCurrentCount(), "Não deve alterar após completion");
    }

    @Test
    public void testLivingEntityInsteadOfCreeper() {
        PlayerMock player = server.addPlayer();
        player.setHealth(20.0);

        // Testa com LivingEntity genérico (não deve contar como creeper explodido)
        LivingEntity entity = mock(LivingEntity.class);
        when(entity.getUniqueId()).thenReturn(UUID.randomUUID());

        quest.updateProgress("CREEPER", Material.DIAMOND_SWORD, player, entity);
        assertEquals(1, quest.getCurrentCount(), "LivingEntity genérico deve contar");
    }
}