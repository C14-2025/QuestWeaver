package com.GrupoC14.questweaver.quests;

import br.dev.projetoc14.QuestWeaver;
import br.dev.projetoc14.quest.mage.FrostbiteQuest;
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

public class FrostbiteQuestTest {

    private FrostbiteQuest quest;
    private ServerMock server;
    private Location spawnLocation;
    private NamespacedKey frostKey;

    @BeforeEach
    void setUp() {
        server = MockBukkit.mock();
        QuestWeaver plugin = MockBukkit.load(QuestWeaver.class);
        spawnLocation = new Location(server.addSimpleWorld("world"), 100, 64, 100);
        quest = new FrostbiteQuest(spawnLocation);
        frostKey = new NamespacedKey(plugin, "frost_ray");
    }

    @AfterEach
    void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    public void testFrozenEnemyCount() {
        PlayerMock player = server.addPlayer();

        // Mock da entidade morta
        LivingEntity entity = mock(LivingEntity.class);
        PersistentDataContainer container = mock(PersistentDataContainer.class);

        // Configura o mock para ter a tag de congelamento
        when(entity.getPersistentDataContainer()).thenReturn(container);
        when(container.has(frostKey, PersistentDataType.INTEGER)).thenReturn(true);

        // Executa update
        quest.updateProgress("SKELETON", Material.BLAZE_ROD, player, entity);

        assertEquals(1, quest.getCurrentCount(), "Inimigo congelado deve contar");
    }

    @Test
    public void testNonFrozenEnemyDoesNotCount() {
        PlayerMock player = server.addPlayer();

        LivingEntity entity = mock(LivingEntity.class);
        PersistentDataContainer container = mock(PersistentDataContainer.class);

        when(entity.getPersistentDataContainer()).thenReturn(container);
        // Configura para NÃO ter a tag
        when(container.has(frostKey, PersistentDataType.INTEGER)).thenReturn(false);

        quest.updateProgress("SKELETON", Material.BLAZE_ROD, player, entity);

        assertEquals(0, quest.getCurrentCount(), "Inimigo normal não deve contar");
    }
}