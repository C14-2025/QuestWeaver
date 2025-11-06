package com.GrupoC14.questweaver.combat;

import br.dev.projetoc14.player.abilities.mageSkills.Healing;
import br.dev.projetoc14.player.classes.MagePlayer;
import br.dev.projetoc14.player.RPGPlayer;
import br.dev.projetoc14.player.PlayerStatsManager;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
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

        // Mock do AttributeInstance
        AttributeInstance maxHealthAttr = mock(AttributeInstance.class);
        when(maxHealthAttr.getValue()).thenReturn(20.0);
        when(mockBukkitPlayer.getAttribute(Attribute.GENERIC_MAX_HEALTH)).thenReturn(maxHealthAttr);

        // Configura comportamento de health
        when(mockBukkitPlayer.getHealth()).thenReturn(10.0);

        // MagePlayer inicializado com HP máximo = 100
        player = new MagePlayer(mockBukkitPlayer);
        player.getStats().setHealth(100);
        player.setCurrentHealth(50); // 50% de vida

        // Configura PlayerStatsManager
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
        int hpAntes = player.getCurrentHealth();

        heal.cast(player);

        // Verifica que o HP do sistema RPG aumentou
        assertEquals(hpAntes + 30, player.getCurrentHealth());
        assertEquals(80, player.getCurrentHealth()); // 50 + 30 = 80

        // Verifica que setHealth foi chamado no mock Bukkit
        verify(mockBukkitPlayer, atLeastOnce()).setHealth(anyDouble());
    }

    @Test
    public void testHealDoesNotExceedMaxHealth() {
        // Player com 90 HP (perto do máximo de 100)
        player.setCurrentHealth(90);

        heal.cast(player);

        // Verifica que não passou de 100
        assertEquals(100, player.getCurrentHealth());
    }
}