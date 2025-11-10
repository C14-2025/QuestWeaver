package com.GrupoC14.questweaver.combat;

import br.dev.projetoc14.player.abilities.mageSkills.Fireball;
import br.dev.projetoc14.player.classes.MagePlayer;
import br.dev.projetoc14.player.RPGPlayer;
import br.dev.projetoc14.player.PlayerStatsManager;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
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

    @BeforeEach
    public void setUp() {
        fireball = new Fireball();

        // Mocks dos Players do Bukkit
        mockCaster = mock(Player.class);
        mockTarget = mock(Player.class);

        // ========== MOCK DOS ATRIBUTOS ========== //

        // Mock do AttributeInstance para o caster
        AttributeInstance casterMaxHealthAttr = mock(AttributeInstance.class);
        when(casterMaxHealthAttr.getValue()).thenReturn(20.0); // Bukkit padrão
        when(casterMaxHealthAttr.getBaseValue()).thenReturn(20.0);
        when(mockCaster.getAttribute(Attribute.GENERIC_MAX_HEALTH)).thenReturn(casterMaxHealthAttr);

        // Mock do AttributeInstance para o target
        AttributeInstance targetMaxHealthAttr = mock(AttributeInstance.class);
        when(targetMaxHealthAttr.getValue()).thenReturn(20.0); // Bukkit padrão
        when(targetMaxHealthAttr.getBaseValue()).thenReturn(20.0);
        when(mockTarget.getAttribute(Attribute.GENERIC_MAX_HEALTH)).thenReturn(targetMaxHealthAttr);

        // ========== CONFIGURAÇÃO DE HEALTH ========== //

        // Configura o mock do caster
        when(mockCaster.getHealth()).thenReturn(20.0);
        AttributeInstance attribute = mock(AttributeInstance.class);
        when(attribute.getBaseValue()).thenReturn(20.0);

        Player player = mock(Player.class);
        when(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).thenReturn(attribute);
        doAnswer(invocation -> {
            double newHealth = invocation.getArgument(0);
            when(mockCaster.getHealth()).thenReturn(newHealth);
            return null;
        }).when(mockCaster).setHealth(anyDouble());

        // Configura o mock do target (começa com 80 HP no sistema RPG)
        when(mockTarget.getHealth()).thenReturn(20.0);
        when(attribute.getBaseValue()).thenReturn(20.0);
        when(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).thenReturn(attribute);
        doAnswer(invocation -> {
            double newHealth = invocation.getArgument(0);
            when(mockTarget.getHealth()).thenReturn(newHealth);
            return null;
        }).when(mockTarget).setHealth(anyDouble());

        // ========== CRIAÇÃO DOS RPGPLAYERS ========== //

        // Cria os RPGPlayers
        caster = new MagePlayer(mockCaster);
        target = new MagePlayer(mockTarget);

        // Cria e seta o statsManager (CRÍTICO)
        PlayerStatsManager statsManager = new PlayerStatsManager();
        caster.setStatsManager(statsManager);
        target.setStatsManager(statsManager);

        // Configura health inicial do target (sistema RPG, não Bukkit)
        target.getStats().setHealth(100); // maxHealth = 100
        target.setCurrentHealth(80); // HP atual = 80
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
        int initialHealth = target.getCurrentHealth();
        int expectedDamage = fireball.getDamage();

        fireball.applyDamage(caster, target);

        // Verifica que o HP do RPG diminuiu
        assertEquals(initialHealth - expectedDamage, target.getCurrentHealth());

        // Verifica que setHealth foi chamado no mock (com valor proporcional)
        // 55/100 = 0.55 -> 0.55 * 20 = 11.0 na barra do Bukkit
        verify(mockTarget, atLeastOnce()).setHealth(anyDouble());
    }

    // Testa se o dano do player não fica abaixo de 0 após o dano
    @Test
    public void testFireballDoesNotGoBelowZero() {
        // Configura o target com apenas 10 HP no sistema RPG
        target.setCurrentHealth(10);

        fireball.applyDamage(caster, target);

        // Verifica que o HP não fica negativo
        assertEquals(0, target.getCurrentHealth());
        assertTrue(target.getCurrentHealth() >= 0);

        // Verifica que setHealth foi chamado
        verify(mockTarget, atLeastOnce()).setHealth(anyDouble());
    }

    // Teste adicional: verifica a conversão proporcional
    @Test
    public void testHealthSyncWithBukkit() {
        // Target tem 80/100 HP no RPG (80%)
        target.setCurrentHealth(80);

        // Isso deve resultar em 16/20 HP no Bukkit (80% de 20)
        // Verifica que setHealth foi chamado com valor próximo a 16.0
        verify(mockTarget, atLeastOnce()).setHealth(doubleThat(value ->
                value >= 15.9 && value <= 16.1
        ));
    }
}