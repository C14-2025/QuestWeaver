package com.GrupoC14.questweaver.combat;
import br.dev.projetoc14.combat.Fireball;
import br.dev.projetoc14.player.RPGPlayer;
import org.bukkit.Location;
import org.bukkit.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import br.dev.projetoc14.player.RPGPlayer;

public class FireballTest {

    private Fireball fireball;

    @BeforeEach
    public void setUp() {
        fireball = new Fireball();
    }

    // Testa se os valores da habilidade foram inicializados corretamente
    @Test
    public void testAbilityProperties() {
        assertEquals("Bola de Fogo", fireball.getName());
        assertEquals(20, fireball.getManaCost());
        assertEquals(5, fireball.getCooldown());
    }

    // Teste para confirmar se o cast pode ser chamado sem erros
    // TODO: Fazer usando Mock será necessário um player para castar a habilidade

    // Teste para confirmar se o send message foi enviado com sucesso
    // Todo: Fazer usando Mock será necessário um player para castar a habilidade
}
