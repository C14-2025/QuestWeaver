package com.GrupoC14.questweaver.combat;

import br.dev.projetoc14.QuestWeaver;
import br.dev.projetoc14.player.RPGPlayer;
import br.dev.projetoc14.player.abilities.assassinSkills.ShadowMove;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;
import org.mockbukkit.mockbukkit.entity.PlayerMock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ShadowMoveTest {

    private ShadowMove shadowMove;
    private ServerMock server;

    @BeforeEach
    void setUp() {
        server = MockBukkit.mock();
        QuestWeaver plugin = MockBukkit.load(QuestWeaver.class);
        shadowMove = new ShadowMove();
        server.getPluginManager().registerEvents(shadowMove, plugin);
    }

    @AfterEach
    void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    public void testAbilityProperties() {
        assertEquals("Shadow Move", shadowMove.getName());
        assertEquals(12, shadowMove.getManaCost());
        assertEquals(15, shadowMove.getCooldown());
    }

    @Test
    public void testOnCastDoesNotThrowException() {
        PlayerMock player = server.addPlayer();
        RPGPlayer rpgPlayer = mock(RPGPlayer.class);
        when(rpgPlayer.getPlayer()).thenReturn(player);

        assertDoesNotThrow(() -> shadowMove.onCast(rpgPlayer));
    }

    @Test
    public void testOnCastAppliesEffects() {
        PlayerMock player = server.addPlayer();
        RPGPlayer rpgPlayer = mock(RPGPlayer.class);
        when(rpgPlayer.getPlayer()).thenReturn(player);

        shadowMove.onCast(rpgPlayer);

        // Verifica os efeitos aplicados
        PotionEffect speed = player.getPotionEffect(PotionEffectType.SPEED);
        PotionEffect invis = player.getPotionEffect(PotionEffectType.INVISIBILITY);

        assertNotNull(speed, "O efeito de SPEED deve ser aplicado");
        assertNotNull(invis, "O efeito de INVISIBILITY deve ser aplicado");

        assertEquals(20 * 10, speed.getDuration());
        assertEquals(20 * 10, invis.getDuration());

        assertEquals(1, speed.getAmplifier());
        assertEquals(1, invis.getAmplifier());
    }

    @Test
    public void testMultipleCasts() {
        PlayerMock player = server.addPlayer();
        RPGPlayer rpgPlayer = mock(RPGPlayer.class);
        when(rpgPlayer.getPlayer()).thenReturn(player);

        assertDoesNotThrow(() -> {
            shadowMove.onCast(rpgPlayer);
            shadowMove.onCast(rpgPlayer);
            shadowMove.onCast(rpgPlayer);
        });

        assertTrue(player.isOnline());
    }
}
