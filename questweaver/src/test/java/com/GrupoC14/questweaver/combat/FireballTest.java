package com.GrupoC14.questweaver.combat;

import br.dev.projetoc14.player.abilities.mageSkills.Fireball;
import br.dev.projetoc14.player.classes.MagePlayer;
import br.dev.projetoc14.player.RPGPlayer;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class FireballTest {

    private Fireball fireball;
    private RPGPlayer caster;
    private RPGPlayer target;

    @BeforeEach
    public void setUp() {
        fireball = new Fireball();

        // mocks básicos
        Player mockCaster = mock(Player.class);
        Player mockTarget = mock(Player.class);

        caster = new MagePlayer(mockCaster);
        target = new MagePlayer(mockTarget);

        target.setHealth(100);
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
        assertEquals(55, target.getCurrentHealth()); // 80 - 25
    }

    // Testa se o dano do player não fica abaixo de 0 após o dano
    @Test
    public void testFireballDoesNotGoBelowZero() {
        target.setCurrentHealth(10);
        fireball.applyDamage(caster, target);
        assertEquals(0, target.getCurrentHealth());
    }
}
