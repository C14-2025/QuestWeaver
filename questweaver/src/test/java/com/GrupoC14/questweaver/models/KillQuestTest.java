package com.GrupoC14.questweaver.models;

import br.dev.projetoc14.quest.KillQuest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class KillQuestTest {
    private KillQuest killQuest;

    @BeforeEach
    void setUp() {
        killQuest = new KillQuest("kill_001", "Caçador de Zumbis",
                "Mate 5 zumbis", 100, "ZOMBIE", 5, 0);
    }

    @Test
    void testQuestInitialization() {
        assertEquals("kill_001", killQuest.getId());
        assertEquals("Caçador de Zumbis", killQuest.getName());
        assertFalse(killQuest.isCompleted());
        assertEquals(0, killQuest.getCurrentCount());
    }

    @Test
    void testProgressUpdate() {
        killQuest.updateProgress("ZOMBIE");
        assertEquals(1, killQuest.getCurrentCount());
        assertFalse(killQuest.isCompleted());
    }

    @Test
    void testQuestCompletion() {
        // Simula matar 5 zumbis
        for (int i = 0; i < 5; i++) {
            killQuest.updateProgress("ZOMBIE");
        }

        assertTrue(killQuest.isCompleted());
        assertEquals(5, killQuest.getCurrentCount());
    }

    @Test
    void testIgnoreWrongMobType() {
        killQuest.updateProgress("SKELETON");
        assertEquals(0, killQuest.getCurrentCount());
        assertFalse(killQuest.isCompleted());
    }
}