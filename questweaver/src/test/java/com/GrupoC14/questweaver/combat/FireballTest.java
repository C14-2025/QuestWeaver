package com.GrupoC14.questweaver.combat;

import br.dev.projetoc14.player.abilities.mageSkills.Fireball;
import br.dev.projetoc14.player.classes.MagePlayer;
import br.dev.projetoc14.player.RPGPlayer;
import br.dev.projetoc14.player.PlayerStatsManager;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FireballTest {

    private Fireball fireball;
    private RPGPlayer caster;
    private RPGPlayer target;
    private Player mockCaster;
    private Player mockTarget;
    private PlayerStatsManager statsManager;

    @BeforeEach
    public void setUp() {
        fireball = new Fireball();

        // Mocks dos Players do Bukkit
        mockCaster = mock(Player.class);
        mockTarget = mock(Player.class);

        // Configura o mock do caster
        when(mockCaster.getHealth()).thenReturn(100.0);
        when(mockCaster.getMaxHealth()).thenReturn(100.0);
        doAnswer(invocation -> {
            double newHealth = invocation.getArgument(0);
            when(mockCaster.getHealth()).thenReturn(newHealth);
            return null;
        }).when(mockCaster).setHealth(anyDouble());

        // Configura o mock do target (começa com 80 HP)
        when(mockTarget.getHealth()).thenReturn(80.0);
        when(mockTarget.getMaxHealth()).thenReturn(100.0);
        doAnswer(invocation -> {
            double newHealth = invocation.getArgument(0);
            when(mockTarget.getHealth()).thenReturn(newHealth);
            return null;
        }).when(mockTarget).setHealth(anyDouble());

        // Cria os RPGPlayers
        caster = new MagePlayer(mockCaster);
        target = new MagePlayer(mockTarget);

        // Cria e seta o statsManager (CRÍTICO)
        statsManager = new PlayerStatsManager();
        caster.setStatsManager(statsManager);
        target.setStatsManager(statsManager);

        // Configura health inicial do target
        target.getStats().setHealth(100);
        target.setCurrentHealth(80); // começa com 80 de vida
    }

    // Testa se os valores da habilidade foram inicializados corretamente
    @Test
    public void testAbilityProperties() {
        assertEquals("Bola de Fogo", fireball.getName());
        assertEquals(20, fireball.getManaCost());
        assertEquals(5, fireball.getCooldown());
        assertEquals(25, fireball.getDamage());
    }

    // Testa se a bola de fogo causa dano
    @Test
    public void testFireballAppliesDamage() {
        fireball.applyDamage(caster, target);

        // Verifica que setHealth foi chamado com o valor correto
        verify(mockTarget).setHealth(55.0); // 80 - 25
        assertEquals(55, target.getCurrentHealth());
    }

    // Testa se o dano do player não fica abaixo de 0 após o dano
    @Test
    public void testFireballDoesNotGoBelowZero() {
        // Configura o mock para retornar 10 HP
        when(mockTarget.getHealth()).thenReturn(10.0);
        target.setCurrentHealth(10);

        fireball.applyDamage(caster, target);

        // Verifica que setHealth foi chamado com 0.0
        verify(mockTarget).setHealth(0.0);
        assertEquals(0, target.getCurrentHealth());
    }
}