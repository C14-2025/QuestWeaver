package com.GrupoC14.questweaver.combat;

import br.dev.projetoc14.player.abilities.mageSkills.Healing;
import br.dev.projetoc14.player.classes.MagePlayer;
import br.dev.projetoc14.player.RPGPlayer;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class HealingTest {

    private Healing heal;
    private RPGPlayer player;

    @BeforeEach
    public void setUp() {
        heal = new Healing();

        //Como é necessário ter um player para castar a habilidade é nececssário um mock
        Player bukkitPlayer = mock(Player.class);
        player = new MagePlayer(bukkitPlayer);

        player.setHealth(100);
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
        assertEquals(80, player.getCurrentHealth()); // 50 + 30
    }

    @Test
    public void testHealDoesNotExceedMaxHealth() {
        player.setCurrentHealth(90);
        heal.cast(player);
        assertEquals(100, player.getCurrentHealth()); // não passa do limite máximo
    }
}