package com.GrupoC14.questweaver.combat.assassin;

import br.dev.projetoc14.QuestWeaver;
import br.dev.projetoc14.player.RPGPlayer;
import br.dev.projetoc14.player.abilities.assassinSkills.VampireKnives;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;
import org.mockbukkit.mockbukkit.entity.PlayerMock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class VampireKnivesTest {

    private VampireKnives vampireKnives;
    private ServerMock server;

    @BeforeEach
    void setUp() {
        server = MockBukkit.mock();
        QuestWeaver plugin = MockBukkit.load(QuestWeaver.class);
        vampireKnives = new VampireKnives();
        server.getPluginManager().registerEvents(vampireKnives, plugin);
    }

    @AfterEach
    void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    public void testAbilityProperties() {
        assertEquals("Vampire Knives", vampireKnives.getName());
        assertEquals(20, vampireKnives.getManaCost());
        assertEquals(30, vampireKnives.getCooldown());
    }

    @Test
    public void testOnCastDoesNotThrowException() {
        PlayerMock player = server.addPlayer();
        RPGPlayer rpgPlayer = mock(RPGPlayer.class);
        when(rpgPlayer.getPlayer()).thenReturn(player);

        assertDoesNotThrow(() -> vampireKnives.onCast(rpgPlayer));
    }

    @Test
    public void testOnCastWithValidPlayer() {
        PlayerMock player = server.addPlayer();
        RPGPlayer rpgPlayer = mock(RPGPlayer.class);
        when(rpgPlayer.getPlayer()).thenReturn(player);

        vampireKnives.onCast(rpgPlayer);

        assertTrue(player.isOnline());
        assertNotNull(player.getUniqueId());
    }

    @Test
    public void testSchedulerAdvances() {
        PlayerMock player = server.addPlayer();
        RPGPlayer rpgPlayer = mock(RPGPlayer.class);
        when(rpgPlayer.getPlayer()).thenReturn(player);

        vampireKnives.onCast(rpgPlayer);

        server.getScheduler().performTicks(50);

        assertTrue(player.isOnline());
    }

    @Test
    public void testEffectCompleteDuration() {
        PlayerMock player = server.addPlayer();
        RPGPlayer rpgPlayer = mock(RPGPlayer.class);
        when(rpgPlayer.getPlayer()).thenReturn(player);

        vampireKnives.onCast(rpgPlayer);

        server.getScheduler().performTicks(210);

        assertTrue(player.isOnline());
    }

    @Test
    public void testMultipleCasts() {
        PlayerMock player = server.addPlayer();
        RPGPlayer rpgPlayer = mock(RPGPlayer.class);
        when(rpgPlayer.getPlayer()).thenReturn(player);

        assertDoesNotThrow(() -> {
            vampireKnives.onCast(rpgPlayer);
            vampireKnives.onCast(rpgPlayer);
            vampireKnives.onCast(rpgPlayer);
        });
    }
}