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

        // Configura o mock para responder aos métodos de health
        // Começa com 50 HP
        when(mockBukkitPlayer.getHealth()).thenReturn(50.0);
        when(mockBukkitPlayer.getMaxHealth()).thenReturn(100.0);

        // Quando setHealth for chamado, atualiza o retorno do getHealth
        doAnswer(invocation -> {
            double newHealth = invocation.getArgument(0);
            when(mockBukkitPlayer.getHealth()).thenReturn(newHealth);
            return null;
        }).when(mockBukkitPlayer).setHealth(anyDouble());

        // Cria o MagePlayer
        player = new MagePlayer(mockBukkitPlayer);

        // Cria e seta o statsManager (CRÍTICO para a barra de mana funcionar)
        statsManager = new PlayerStatsManager();
        player.setStatsManager(statsManager);

        // Configura health inicial
        player.getStats().setHealth(100);
        player.setCurrentHealth(50); // começa com metade da vida
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
        // Verifica através do mock que setHealth foi chamado com 80.0
        verify(mockBukkitPlayer).setHealth(80.0); // 50 + 30
        assertEquals(80, player.getCurrentHealth());
    }

    @Test
    public void testHealDoesNotExceedMaxHealth() {
        // Configura o mock para retornar 90 HP
        when(mockBukkitPlayer.getHealth()).thenReturn(90.0);
        player.setCurrentHealth(90);

        heal.cast(player);

        // Verifica que setHealth foi chamado com 100.0 (máximo)
        verify(mockBukkitPlayer).setHealth(100.0);
        assertEquals(100, player.getCurrentHealth());
    }
}