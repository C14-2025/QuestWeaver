package com.GrupoC14.questweaver.quests;

import br.dev.projetoc14.QuestWeaver;
import br.dev.projetoc14.quest.mage.PyromancerQuest;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;
import org.mockbukkit.mockbukkit.entity.PlayerMock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PyromancerQuestTest {

    private PyromancerQuest quest;
    private ServerMock server;
    private NamespacedKey magicKey;

    @BeforeEach
    void setUp() {
        server = MockBukkit.mock();
        QuestWeaver plugin = MockBukkit.load(QuestWeaver.class);
        quest = new PyromancerQuest(new Location(server.addSimpleWorld("world"), 0, 0, 0));
        magicKey = new NamespacedKey(plugin, "magic_damage");
    }

    @AfterEach
    void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    public void testSpiderKilledWithMagic() {
        PlayerMock player = server.addPlayer();
        LivingEntity spider = mock(LivingEntity.class);
        PersistentDataContainer container = mock(PersistentDataContainer.class);

        when(spider.getPersistentDataContainer()).thenReturn(container);
        when(container.has(magicKey, PersistentDataType.BOOLEAN)).thenReturn(true);

        quest.updateProgress("SPIDER", Material.BLAZE_ROD, player, spider);

        assertEquals(1, quest.getCurrentCount());
    }

    @Test
    public void testSpiderKilledWithMeleeFails() {
        PlayerMock player = server.addPlayer();
        LivingEntity spider = mock(LivingEntity.class);
        PersistentDataContainer container = mock(PersistentDataContainer.class);

        when(spider.getPersistentDataContainer()).thenReturn(container);
        // Sem tag de magia
        when(container.has(magicKey, PersistentDataType.BOOLEAN)).thenReturn(false);

        quest.updateProgress("SPIDER", Material.IRON_SWORD, player, spider);

        assertEquals(0, quest.getCurrentCount());
    }

    @Test
    public void testWrongMobWithMagicFails() {
        PlayerMock player = server.addPlayer();
        LivingEntity zombie = mock(LivingEntity.class); // Zumbi, não aranha
        PersistentDataContainer container = mock(PersistentDataContainer.class);

        when(zombie.getPersistentDataContainer()).thenReturn(container);
        when(container.has(magicKey, PersistentDataType.BOOLEAN)).thenReturn(true);

        quest.updateProgress("ZOMBIE", Material.BLAZE_ROD, player, zombie);

        assertEquals(0, quest.getCurrentCount());
    }
}