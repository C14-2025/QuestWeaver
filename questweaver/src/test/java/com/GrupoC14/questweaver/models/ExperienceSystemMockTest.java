package com.GrupoC14.questweaver.models;

import br.dev.projetoc14.skilltree.ExperienceSystem;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class ExperienceSystemMockTest {

    private ExperienceSystem experienceSystem;
    private Player mockPlayer;
    private LivingEntity mockMob;
    private EntityDeathEvent mockEvent;

    @BeforeEach
    void setup() {
        experienceSystem = new ExperienceSystem();

        mockPlayer = mock(Player.class);
        mockMob = mock(LivingEntity.class);
        mockEvent = mock(EntityDeathEvent.class);

        when(mockEvent.getEntity()).thenReturn(mockMob);
        when(mockMob.getKiller()).thenReturn(mockPlayer);
    }


    @Test
    void giveXpWhenOnList() {
        // Arrange
        when(mockMob.getCustomName()).thenReturn("Esqueleto");
        when(mockPlayer.getLevel()).thenReturn(5);

        // Act
        experienceSystem.onMobDeath(mockEvent);

        // Assert
        verify(mockPlayer).giveExpLevels(5);
        verify(mockPlayer).sendMessage("§a+5.");
    }

    @Test
    void mobNotInTheList() {
        when(mockMob.getCustomName()).thenReturn("Zumbi Aleatório");
        when(mockPlayer.getLevel()).thenReturn(5);

        experienceSystem.onMobDeath(mockEvent);

        // Não pode dar XP
        verify(mockPlayer, never()).giveExpLevels(anyInt());
        verify(mockPlayer, never()).sendMessage(contains("+"));
    }

    @Test
    void playerMaxLevel() {
        when(mockMob.getCustomName()).thenReturn("Chefe");
        when(mockPlayer.getLevel()).thenReturn(15); // nível máximo

        experienceSystem.onMobDeath(mockEvent);

        // Não dá XP, só manda a mensagem de máximo
        verify(mockPlayer).setLevel(15);
        verify(mockPlayer).sendMessage("§a[QuestWeaver] Nível máximo atingido.");
    }
}
