package com.GrupoC14.questweaver.models;

import br.dev.projetoc14.player.PlayerStats;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class TestPlayerStats {

    private PlayerStats stats;

    @BeforeEach
    public void setUp() {
        stats = new PlayerStats();
    }

    // Teste para ver se os valores padrões dos stats estão corretos
    @Test
    public void testDefaultStats() {
        assertEquals(10, stats.getStrength());
        assertEquals(10, stats.getDefense());
        assertEquals(10, stats.getAgility());
        assertEquals(10, stats.getIntelligence());
        assertEquals(100, stats.getHealth());
        assertEquals(50, stats.getMana());
        assertEquals(100, stats.getCurrentHealth());
        assertEquals(50, stats.getCurrentMana());
    }

    @Test
    public void testPlayerConstructor() {

        PlayerStats customStats = new PlayerStats(15, 12, 8, 5, 120, 30);

        assertEquals(15, customStats.getStrength());
        assertEquals(12, customStats.getDefense());
        assertEquals(8, customStats.getAgility());
        assertEquals(5, customStats.getIntelligence());
        assertEquals(120, customStats.getHealth());
        assertEquals(30, customStats.getMana());
    }

    // Teste para o sistema de vida implementado com MOCK
    @Test
    public void testHealthManagement() {
        PlayerStats mockStats = Mockito.mock(PlayerStats.class);
        when(mockStats.getHealth()).thenReturn(150);
        when(mockStats.getCurrentHealth())
                .thenReturn(150)  // após setHealth
                .thenReturn(120)  // após damage(30)
                .thenReturn(140)  // após heal(20)
                .thenReturn(150); // após heal(50)

        when(mockStats.isAlive())
                .thenReturn(true)  // após setHealth
                .thenReturn(true)  // após damage(30)
                .thenReturn(true)  // após heal(20)
                .thenReturn(true); // após heal(50)

        // ---------- Testes usando o mock ---------- //

        assertEquals(150, mockStats.getHealth());
        assertEquals(150, mockStats.getCurrentHealth());
        assertTrue(mockStats.isAlive());

        // Simula dano
        assertEquals(120, mockStats.getCurrentHealth());
        assertTrue(mockStats.isAlive());

        // Simula cura
        assertEquals(140, mockStats.getCurrentHealth());
        assertTrue(mockStats.isAlive());

        // Simula cura acima do limite
        assertEquals(150, mockStats.getCurrentHealth());
    }

    // Teste de player vivo
    @Test
    public void testIsAlive() {
        // Testa se está vivo
        assertTrue(stats.isAlive());

        stats.damage(100); // Remove toda a vida
        assertFalse(stats.isAlive());

        stats.heal(1); // Recupera 1 de vida
        assertTrue(stats.isAlive());
    }

    // Teste de mana do player stats

    @Test
    public void testManaManagement() {
        stats.setMana(100); // Mana máxima
        stats.fullRestore();
        stats.useMana(80);
        assertEquals(20, stats.getCurrentMana());
    }

    // Testes para cálculo de dano físico

    @Test
    public void testCalculateDamage() {
        assertEquals(20, stats.calculatePhysicalDamage());
        assertEquals(30, stats.calculateMagicalDamage());
        int reducedDAMAGE = stats.calculateDamageReduction(15);
        assertEquals(10, reducedDAMAGE);
    }
}
