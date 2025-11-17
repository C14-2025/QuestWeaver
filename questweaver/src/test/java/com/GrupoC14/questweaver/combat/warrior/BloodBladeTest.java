package com.GrupoC14.questweaver.combat.warrior;

import br.dev.projetoc14.QuestWeaver;
import br.dev.projetoc14.player.RPGPlayer;
import br.dev.projetoc14.player.abilities.warriorSkills.BloodBlade;

import org.bukkit.Material;

import org.junit.jupiter.api.*;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;
import org.mockbukkit.mockbukkit.entity.PlayerMock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BloodBladeTest {

    private ServerMock server;
    private BloodBlade bloodBlade;

    @BeforeEach
    void setUp() {
        server = MockBukkit.mock();
        MockBukkit.load(QuestWeaver.class);
        bloodBlade = new BloodBlade();
    }

    @AfterEach
    void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    void testAbilityProperties() {
        assertEquals("Blood Blade", bloodBlade.getName());
        assertEquals(20, bloodBlade.getManaCost());
        assertEquals(15, bloodBlade.getCooldown());
    }

    @Test
    void testActivationRequiresIronAxe() {
        PlayerMock player = server.addPlayer();
        // consome mensagem automática do PlayerJoinEvent
        player.nextMessage();
        RPGPlayer rpg = mock(RPGPlayer.class);
        when(rpg.getPlayer()).thenReturn(player);


        // sem machado - deve avisar erro
        player.getInventory().setItemInMainHand(null);

        bloodBlade.onCast(rpg);

        assertEquals("§c[Blood Blade] Você precisa estar com um Machado de Ferro na mão!", player.nextMessage());
    }

    @Test
    void testActivationWorks() {
        PlayerMock player = server.addPlayer();
        // consome mensagem automática do PlayerJoinEvent
        player.nextMessage();
        RPGPlayer rpg = mock(RPGPlayer.class);
        when(rpg.getPlayer()).thenReturn(player);

        // dá machado
        player.getInventory().setItemInMainHand(new org.bukkit.inventory.ItemStack(Material.IRON_AXE));

        bloodBlade.onCast(rpg);

        assertEquals("§c[Blood Blade] §7Ativado! Seus ataques acumulam sangramento por §c30 segundos§7.", player.nextMessage());
    }
}