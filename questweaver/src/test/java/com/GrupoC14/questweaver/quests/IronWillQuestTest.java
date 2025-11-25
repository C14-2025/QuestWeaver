package com.GrupoC14.questweaver.quests;

import br.dev.projetoc14.QuestWeaver;
import br.dev.projetoc14.quest.warrior.IronWillQuest;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;
import org.mockbukkit.mockbukkit.entity.PlayerMock;

import static org.junit.jupiter.api.Assertions.*;

public class IronWillQuestTest {

    private IronWillQuest quest;
    private ServerMock server;

    @BeforeEach
    void setUp() {
        server = MockBukkit.mock();
        MockBukkit.load(QuestWeaver.class);
        quest = new IronWillQuest(new Location(server.addSimpleWorld("world"), 0, 0, 0));
    }

    @AfterEach
    void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    public void testKillWithShieldInOffHand() {
        PlayerMock player = server.addPlayer();

        // Equipa escudo na offhand
        player.getInventory().setItemInOffHand(new ItemStack(Material.SHIELD));
        // Equipa arma na main hand
        player.getInventory().setItemInMainHand(new ItemStack(Material.IRON_SWORD));

        quest.updateProgress("SKELETON", Material.IRON_SWORD, player);

        assertEquals(1, quest.getCurrentCount(), "Deve contar se tiver escudo na offhand");
    }

    @Test
    public void testKillWithoutShieldFails() {
        PlayerMock player = server.addPlayer();

        // Offhand vazia
        player.getInventory().setItemInOffHand(null);

        quest.updateProgress("SKELETON", Material.IRON_SWORD, player);

        assertEquals(0, quest.getCurrentCount(), "Não deve contar sem escudo");
    }

    @Test
    public void testKillWithShieldInMainHandFails() {
        PlayerMock player = server.addPlayer();

        // Escudo na mão principal (errado para esta quest específica, ou ataque fraco)
        player.getInventory().setItemInMainHand(new ItemStack(Material.SHIELD));
        player.getInventory().setItemInOffHand(null);

        // A quest espera uma arma na mão principal E escudo na secundária
        quest.updateProgress("SKELETON", Material.SHIELD, player);

        assertEquals(0, quest.getCurrentCount());
    }
}