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
        assertEquals(1, stats.getStrength());
        assertEquals(0, stats.getDefense());
        assertEquals(1, stats.getAgility());
        assertEquals(1, stats.getIntelligence());
        assertEquals(20, stats.getHealth());
        assertEquals(20, stats.getMana());
        assertEquals(20, stats.getCurrentMana());
    }

    // Teste do construtor customizado
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

    // REMOVIDO testHealthManagement()
    // Motivo: A gestão de vida agora é responsabilidade do RPGPlayer, não do PlayerStats

    // REMOVIDO testIsAlive()
    // Motivo: isAlive() agora está no RPGPlayer, não no PlayerStats

    // Teste de mana do player stats
    @Test
    public void testManaManagement() {
        stats.setMana(100); // Mana máxima
        stats.fullRestoreMana(); // ⚠️ Nome do método mudou
        assertEquals(100, stats.getCurrentMana());

        stats.useMana(80);
        assertEquals(20, stats.getCurrentMana());

        // Testa se não pode usar mais mana do que tem
        stats.useMana(30);
        assertEquals(0, stats.getCurrentMana());

        // Testa hasMana
        assertFalse(stats.hasMana(10));

        // Restaura mana e testa novamente
        stats.restoreMana(50);
        assertEquals(50, stats.getCurrentMana());
        assertTrue(stats.hasMana(10));
    }

    // Testes para cálculo de dano físico
    @Test
    public void testCalculateDamage() {
        // Strength padrão = 1, então dano físico = 1 * 2 = 2
        assertEquals(2, stats.calculatePhysicalDamage());

        // Intelligence padrão = 1, então dano mágico = 1 * 3 = 3 (não 30!)
        assertEquals(3, stats.calculateMagicalDamage()); // ⚠️ CORRIGIDO

        // Defense padrão = 0, então redução = 0/2 = 0
        // Dano 15 - 0 = 15
        int reducedDamage = stats.calculateDamageReduction(15);
        assertEquals(15, reducedDamage);
    }

    // Teste de redução de dano com defesa
    @Test
    public void testDamageReductionWithDefense() {
        stats.setDefense(10); // Defense = 10, redução = 10/2 = 5

        int reducedDamage = stats.calculateDamageReduction(20);
        assertEquals(15, reducedDamage); // 20 - 5 = 15

        // Testa que sempre causa pelo menos 1 de dano
        reducedDamage = stats.calculateDamageReduction(3);
        assertEquals(1, reducedDamage); // Mínimo de 1
    }

    // Teste de cálculo de dano com stats customizados
    @Test
    public void testCustomStatsDamage() {
        stats.setStrength(10);
        stats.setIntelligence(15);

        assertEquals(20, stats.calculatePhysicalDamage()); // 10 * 2
        assertEquals(45, stats.calculateMagicalDamage());  // 15 * 3
    }

    // Teste de porcentagem de mana
    @Test
    public void testManaPercentage() {
        stats.setMana(100);
        stats.fullRestoreMana();
        assertEquals(1.0, stats.getManaPercentage(), 0.01);

        stats.useMana(50);
        assertEquals(0.5, stats.getManaPercentage(), 0.01);

        stats.useMana(50);
        assertEquals(0.0, stats.getManaPercentage(), 0.01);
    }

    // Teste de setters com validação
    @Test
    public void testSettersValidation() {
        // Testa se setMana ajusta currentMana se necessário
        stats.setMana(100);
        stats.fullRestoreMana();
        assertEquals(100, stats.getCurrentMana());

        stats.setMana(50); // Reduz mana máxima
        assertEquals(50, stats.getCurrentMana()); // currentMana deve ser ajustada
    }

    // Teste do método toString
    @Test
    public void testToString() {
        String expected = "PlayerStats{strength=1, defense=0, agility=1, intelligence=1, maxHealth=20, mana=20/20}";
        assertEquals(expected, stats.toString());
    }
}