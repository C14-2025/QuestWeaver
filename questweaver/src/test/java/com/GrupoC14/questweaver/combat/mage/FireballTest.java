package com.GrupoC14.questweaver.combat.mage;

import br.dev.projetoc14.QuestWeaver;
import br.dev.projetoc14.player.abilities.mageSkills.Fireball;
import br.dev.projetoc14.player.classes.MagePlayer;
import br.dev.projetoc14.player.RPGPlayer;
import br.dev.projetoc14.player.PlayerStatsManager;
import org.bukkit.Server;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

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
        // Mock do plugin e suas dependências
        QuestWeaver mockedPlugin = mock(QuestWeaver.class);
        Server mockedServer = mock(Server.class);
        PluginManager mockedPluginManager = mock(PluginManager.class);

        when(mockedPlugin.getName()).thenReturn("questweaver");
        when(mockedPlugin.getServer()).thenReturn(mockedServer);
        when(mockedServer.getPluginManager()).thenReturn(mockedPluginManager);

        fireball = new Fireball(mockedPlugin);

        // Mock dos Players do Bukkit
        mockCaster = mock(Player.class);
        mockTarget = mock(Player.class);

        // ========== MOCK DOS ATRIBUTOS ========== //

        // Mock do AttributeInstance para o caster
        AttributeInstance casterMaxHealthAttr = mock(AttributeInstance.class);
        when(casterMaxHealthAttr.getValue()).thenReturn(20.0);
        when(casterMaxHealthAttr.getBaseValue()).thenReturn(20.0);
        when(mockCaster.getAttribute(Attribute.MAX_HEALTH)).thenReturn(casterMaxHealthAttr);

        // Mock do AttributeInstance para o target
        AttributeInstance targetMaxHealthAttr = mock(AttributeInstance.class);
        when(targetMaxHealthAttr.getValue()).thenReturn(20.0);
        when(targetMaxHealthAttr.getBaseValue()).thenReturn(20.0);
        when(mockTarget.getAttribute(Attribute.MAX_HEALTH)).thenReturn(targetMaxHealthAttr);

        // ========== CONFIGURAÇÃO DE HEALTH ========== //

        // Configura o health do caster
        when(mockCaster.getHealth()).thenReturn(20.0);
        doAnswer(invocation -> {
            double newHealth = invocation.getArgument(0);
            when(mockCaster.getHealth()).thenReturn(newHealth);
            return null;
        }).when(mockCaster).setHealth(anyDouble());

        // Configura o health do target
        when(mockTarget.getHealth()).thenReturn(20.0);
        doAnswer(invocation -> {
            double newHealth = invocation.getArgument(0);
            when(mockTarget.getHealth()).thenReturn(newHealth);
            return null;
        }).when(mockTarget).setHealth(anyDouble());

        // ========== CRIAÇÃO DOS RPGPLAYERS ========== //

        // Cria as instâncias dos RPGPlayers
        caster = new MagePlayer(mockCaster);
        target = new MagePlayer(mockTarget);

        // Cria e seta o stats manager
        statsManager = new PlayerStatsManager();
        caster.setStatsManager(statsManager);
        target.setStatsManager(statsManager);

        // Configura o health inicial do alvo
        target.getStats().setHealth(100); // maxHealth = 100
        target.setCurrentHealth(80); // HP atual = 80
    }

    @Test
    public void testAbilityProperties() {
        assertEquals("Bola de Fogo", fireball.getName());
        assertEquals(15, fireball.getManaCost());
        assertEquals(5, fireball.getCooldown());
        assertEquals(25, fireball.getDamage());
    }

    @Test
    public void testFireballAppliesDamage() {
        int initialHealth = target.getCurrentHealth();
        int expectedDamage = fireball.getDamage();

        // Aplica dano
        fireball.applyDamage(caster, target);

        // Verifica que o HP do RPG diminuiu
        assertEquals(initialHealth - expectedDamage, target.getCurrentHealth(),
                "HP do target deve diminuir pela quantidade de dano");

        // Verifica que setHealth foi chamado no player do Bukkit
        ArgumentCaptor<Double> healthCaptor = ArgumentCaptor.forClass(Double.class);
        verify(mockTarget, atLeastOnce()).setHealth(healthCaptor.capture());

        // Verifica se algum dos valores capturados é aproximadamente 11.0
        boolean foundExpectedHealth = healthCaptor.getAllValues().stream()
                .anyMatch(health -> Math.abs(health - 11.0) < 0.1);
        assertTrue(foundExpectedHealth,
                "setHealth deve ser chamado com aproximadamente 11.0 (55% de 20)");
    }

    @Test
    public void testFireballDoesNotGoBelowZero() {
        // Configura o alvo com apenas 10 HP no sistema RPG
        target.setCurrentHealth(10);

        // Aplica dano (25 de dano, mas target tem apenas 10 HP)
        fireball.applyDamage(caster, target);

        // Verifica que o HP não fica negativo
        assertEquals(0, target.getCurrentHealth(),
                "HP não deve ficar abaixo de zero");
        assertTrue(target.getCurrentHealth() >= 0,
                "HP deve ser não-negativo");

        // Verifica que setHealth foi chamado
        verify(mockTarget, atLeastOnce()).setHealth(anyDouble());
    }

    @Test
    public void testHealthSyncWithBukkit() {

        // Target tem 80/100 HP no RPG (80%)
        target.setCurrentHealth(80);

        // Isso deve resultar em 16/20 HP no Bukkit (80% de 20)
        ArgumentCaptor<Double> healthCaptor = ArgumentCaptor.forClass(Double.class);
        verify(mockTarget, atLeastOnce()).setHealth(healthCaptor.capture());

        // Pega o último valor de health setado
        Double lastHealth = healthCaptor.getValue();

        // Verifica que é aproximadamente 16.0
        assertTrue(Math.abs(lastHealth - 16.0) < 0.2,
                String.format("Esperado health ~16.0, mas recebeu %.2f", lastHealth));
    }

    @Test
    public void testMultipleFireballHits() {
        int initialHealth = target.getCurrentHealth(); // 80
        int damage = fireball.getDamage(); // 25

        // Aplica dano duas vezes
        fireball.applyDamage(caster, target);
        fireball.applyDamage(caster, target);

        // Verifica o dano total: 80 - 25 - 25 = 30
        assertEquals(initialHealth - (2 * damage), target.getCurrentHealth(),
                "HP deve diminuir pelo dano total de múltiplos acertos");

        // Verifica que setHealth foi chamado pelo menos duas vezes
        verify(mockTarget, atLeast(2)).setHealth(anyDouble());
    }

    @Test
    public void testOverkillDamage() {
        // Configura o target com 100 HP (vida cheia)
        target.setCurrentHealth(100);

        // Aplica dano 5 vezes (5 * 25 = 125 de dano, mais que 100 HP)
        for (int i = 0; i < 5; i++) {
            fireball.applyDamage(caster, target);
        }

        // HP deve ser 0, não negativo
        assertEquals(0, target.getCurrentHealth(),
                "Dano excessivo não deve tornar o HP negativo");
    }
}