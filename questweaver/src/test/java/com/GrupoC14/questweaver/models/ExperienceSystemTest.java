package com.GrupoC14.questweaver.models;

import br.dev.projetoc14.ExperienceSystem.ExperienceSystem;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class ExperienceSystemTest {

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
    void deveDarXPQuandoMobChamadoBichoEhMorto() {
        when(mockMob.getCustomName()).thenReturn("Bicho");
        when(mockPlayer.getLevel()).thenReturn(10);

        experienceSystem.onMobDeath(mockEvent);

        verify(mockPlayer).giveExpLevels(1);
        verify(mockPlayer).sendMessage("§a+1XP.");
    }

    @Test
    void naoDeveDarXPQuandoMobNaoTemNome() {
        when(mockMob.getCustomName()).thenReturn(null);

        experienceSystem.onMobDeath(mockEvent);

        verify(mockPlayer, never()).giveExpLevels(anyInt());
        verify(mockPlayer, never()).sendMessage(anyString());
    }

    @Test
    void naoDeveDarXPSePlayerJaTemNivelMaximo() {
        when(mockMob.getCustomName()).thenReturn("Bicho");
        when(mockPlayer.getLevel()).thenReturn(15);

        experienceSystem.onMobDeath(mockEvent);

        verify(mockPlayer, never()).giveExpLevels(anyInt());
        verify(mockPlayer).sendMessage("§cNível máximo atingido.");
    }
}
