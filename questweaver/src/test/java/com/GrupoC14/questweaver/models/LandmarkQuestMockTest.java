package com.GrupoC14.questweaver.models;

import br.dev.projetoc14.quest.LandmarkQuest;
import br.dev.projetoc14.quest.utils.LandmarkType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/*
 * Classe para mockar a LandmarkQuest de diferentes formas
 */
public class LandmarkQuestMockTest {

    @Mock
    private LandmarkQuest mockLandmarkQuest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /*
     * Mock de uma LandmarkQuest para procurar vila - NÃO completada
     */
    @Test
    void testMockVillageQuestNotCompleted() {
        // Configura o comportamento da quest mockada
        when(mockLandmarkQuest.getId()).thenReturn("find_village_001");
        when(mockLandmarkQuest.getName()).thenReturn("Primeira Vila");
        when(mockLandmarkQuest.getDescription()).thenReturn("Encontre sua primeira vila");
        when(mockLandmarkQuest.getTargetType()).thenReturn(LandmarkType.VILLAGE);
        when(mockLandmarkQuest.isFound()).thenReturn(false);
        when(mockLandmarkQuest.isCompleted()).thenReturn(false);
        when(mockLandmarkQuest.getProgressText()).thenReturn("🔍 Procurando por: Vila");

        // Testa se está funcionando
        assertEquals("find_village_001", mockLandmarkQuest.getId());
        assertEquals("Primeira Vila", mockLandmarkQuest.getName());
        assertEquals(LandmarkType.VILLAGE, mockLandmarkQuest.getTargetType());
        assertFalse(mockLandmarkQuest.isFound());
        assertFalse(mockLandmarkQuest.isCompleted());
        assertTrue(mockLandmarkQuest.getProgressText().contains("🔍"));
    }

    /*
     * Mock de uma LandmarkQuest para templo - COMPLETADA
     */
    @Test
    void testMockTempleQuestCompleted() {
        // Quest completada
        when(mockLandmarkQuest.getId()).thenReturn("find_temple_001");
        when(mockLandmarkQuest.getName()).thenReturn("Explorador de Templos");
        when(mockLandmarkQuest.getTargetType()).thenReturn(LandmarkType.TEMPLE);
        when(mockLandmarkQuest.isFound()).thenReturn(true);
        when(mockLandmarkQuest.isCompleted()).thenReturn(true);
        when(mockLandmarkQuest.getProgressText()).thenReturn("✅ Templo encontrado!");

        // Verifica se está completa
        assertTrue(mockLandmarkQuest.isFound());
        assertTrue(mockLandmarkQuest.isCompleted());
        assertEquals("✅ Templo encontrado!", mockLandmarkQuest.getProgressText());
    }
}

