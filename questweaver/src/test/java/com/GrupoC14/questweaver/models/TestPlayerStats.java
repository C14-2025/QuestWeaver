package com.GrupoC14.questweaver.models;

import br.dev.projetoc14.player.PlayerStats;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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

    // Teste para o sistema de vida implementado
    @Test
    public void testHealthManagement() {

        // Vida:
        stats.setHealth(150);
        assertEquals(150, stats.getHealth());

        // Dano:
        stats.damage(30);
        assertEquals(70, stats.getCurrentHealth());

        // Cura:
        stats.heal(20);
        assertEquals(90, stats.getCurrentHealth());

        // Cura além do limite (não deve passar do limite máximo)
        stats.heal(50);
        assertEquals(140, stats.getCurrentHealth());
    }

    @Test
    public void testIsAlive() {
        // Testa se está vivo
        assertTrue(stats.isAlive());

        stats.damage(100); // Remove toda a vida
        assertFalse(stats.isAlive());

        stats.heal(1); // Recupera 1 de vida
        assertTrue(stats.isAlive());
    }
}
