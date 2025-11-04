package com.GrupoC14.questweaver.combat;

import br.dev.projetoc14.player.abilities.mageSkills.Healing;
import br.dev.projetoc14.player.classes.MagePlayer;
import br.dev.projetoc14.player.RPGPlayer;
import br.dev.projetoc14.player.PlayerStatsManager;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class HealingTest {

    private Healing heal;
    private RPGPlayer player;
    private Player mockBukkitPlayer;
    private PlayerStatsManager statsManager;

    @BeforeEach
    public void setUp() {
        heal = new Healing();

        // Mock do Player do Bukkit
        mockBukkitPlayer = mock(Player.class);

        // Configura comportamento de health
        when(mockBukkitPlayer.getHealth()).thenReturn(50.0);

        // MagePlayer inicializado com HP máximo = 100
        player = new MagePlayer(mockBukkitPlayer);
        player.getStats().setHealth(100);
        player.setCurrentHealth(50);

        // Configura PlayerStatsManager (para evitar NPE na barra de mana)
        statsManager = mock(PlayerStatsManager.class);
        player.setStatsManager(statsManager);
    }

    @Test
    public void testAbilityProperties() {
        assertEquals("Cura", heal.getName());
        assertEquals(15, heal.getManaCost());
        assertEquals(3, heal.getCooldown());
        assertEquals(30, heal.getHealAmount());
    }

    @Test
    public void testHealRestoresHealth() {
        heal.cast(player);

        // Verifica que a cura foi aplicada corretamente
        verify(mockBukkitPlayer).setHealth(80.0);
    }
}