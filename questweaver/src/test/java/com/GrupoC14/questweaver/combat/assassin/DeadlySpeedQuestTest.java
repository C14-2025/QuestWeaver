package com.GrupoC14.questweaver.combat.assassin;

import br.dev.projetoc14.QuestWeaver;
import br.dev.projetoc14.quest.assassin.DeadlySpeedQuest;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;
import org.mockbukkit.mockbukkit.entity.PlayerMock;

import static org.junit.jupiter.api.Assertions.*;

public class DeadlySpeedQuestTest {

    private DeadlySpeedQuest quest;
    private ServerMock server;
    private Location spawnLocation;

    @BeforeEach
    void setUp() {
        server = MockBukkit.mock();
        QuestWeaver plugin = MockBukkit.load(QuestWeaver.class);

        World world = server.addSimpleWorld("world");
        spawnLocation = new Location(world, 100, 64, 100);
        quest = new DeadlySpeedQuest(spawnLocation);
    }

    @AfterEach
    void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    public void testQuestProperties() {
        assertEquals("deadly_speed_quest", quest.getId());
        assertEquals("Velocidade Mortal", quest.getName());
        assertEquals("Mate 6 esqueletos em sequência rápida (máx. 5s entre kills)", quest.getDescription());
        assertEquals(200, quest.getExperienceReward());
        assertEquals("SKELETON", quest.targetMob);
        assertEquals(6, quest.getTargetCount());
    }

    @Test
    public void testValidWeapons() {
        assertTrue(quest.isValidWeapon(Material.IRON_SWORD));
        assertTrue(quest.isValidWeapon(Material.DIAMOND_SWORD));
        assertTrue(quest.isValidWeapon(Material.NETHERITE_SWORD));
        assertFalse(quest.isValidWeapon(Material.WOODEN_SWORD));
    }

    @Test
    public void testFastKillsMaintainStreak() {
        PlayerMock player = server.addPlayer();

        // Kills rápidos (dentro de 5 segundos)
        quest.updateProgress("SKELETON", Material.IRON_SWORD, player);
        assertEquals(1, quest.getCurrentCount());
        assertEquals(1, quest.getStreak(player));

        quest.updateProgress("SKELETON", Material.DIAMOND_SWORD, player);
        assertEquals(2, quest.getCurrentCount());
        assertEquals(2, quest.getStreak(player));

        quest.updateProgress("SKELETON", Material.NETHERITE_SWORD, player);
        assertEquals(3, quest.getCurrentCount());
        assertEquals(3, quest.getStreak(player));
    }

    @Test
    public void testQuestCompletionWithPerfectStreak() {
        PlayerMock player = server.addPlayer();

        // 6 kills rápidos
        quest.updateProgress("SKELETON", Material.IRON_SWORD, player);
        quest.updateProgress("SKELETON", Material.IRON_SWORD, player);
        quest.updateProgress("SKELETON", Material.IRON_SWORD, player);
        quest.updateProgress("SKELETON", Material.IRON_SWORD, player);
        quest.updateProgress("SKELETON", Material.IRON_SWORD, player);
        quest.updateProgress("SKELETON", Material.IRON_SWORD, player);

        assertTrue(quest.checkCompletion(), "Quest deve estar completa após 6 kills");
        assertEquals(6, quest.getCurrentCount(), "Progresso final deve ser 6");
        assertEquals(6, quest.getStreak(player), "Streak final deve ser 6");

    }

    @Test
    public void testWrongMobBreaksStreak() {
        PlayerMock player = server.addPlayer();

        // Primeiro kill válido
        quest.updateProgress("SKELETON", Material.IRON_SWORD, player);
        assertEquals(1, quest.getCurrentCount());
        assertEquals(1, quest.getStreak(player));

        // Mob errado - deve quebrar o streak
        quest.updateProgress("ZOMBIE", Material.IRON_SWORD, player);
        assertEquals(0, quest.getCurrentCount(), "Mob errado deve quebrar o streak");
        assertEquals(0, quest.getStreak(player), "Mob errado deve resetar o streak");

        // Próximo kill válido começa novo streak
        quest.updateProgress("SKELETON", Material.IRON_SWORD, player);
        assertEquals(1, quest.getCurrentCount());
        assertEquals(1, quest.getStreak(player));
    }

    @Test
    public void testWrongWeaponBreaksStreak() {
        PlayerMock player = server.addPlayer();

        // Primeiro kill válido
        quest.updateProgress("SKELETON", Material.IRON_SWORD, player);
        assertEquals(1, quest.getCurrentCount());
        assertEquals(1, quest.getStreak(player));

        // Arma errada - deve quebrar o streak
        quest.updateProgress("SKELETON", Material.WOODEN_SWORD, player);
        assertEquals(0, quest.getCurrentCount(), "Arma errada deve quebrar o streak");
        assertEquals(0, quest.getStreak(player), "Arma errada deve resetar o streak");

        // Próximo kill válido começa novo streak
        quest.updateProgress("SKELETON", Material.IRON_SWORD, player);
        assertEquals(1, quest.getCurrentCount());
        assertEquals(1, quest.getStreak(player));
    }

    @Test
    public void testGetStreakForNewPlayer() {
        PlayerMock player = server.addPlayer();

        assertEquals(0, quest.getStreak(player), "Jogador novo deve ter streak 0");
        assertEquals(0, quest.getCurrentCount(), "Jogador novo deve ter progresso 0");
    }

    @Test
    public void testProgressText() {
        PlayerMock player = server.addPlayer();

        quest.updateProgress("SKELETON", Material.IRON_SWORD, player);

        String progress = quest.getProgressText();
        assertTrue(progress.contains("1/6"));
        assertTrue(progress.contains("kills em sequência"));
    }

    @Test
    public void testPartialCompletion() {
        PlayerMock player = server.addPlayer();

        // Apenas 3 kills
        quest.updateProgress("SKELETON", Material.IRON_SWORD, player);
        quest.updateProgress("SKELETON", Material.DIAMOND_SWORD, player);
        quest.updateProgress("SKELETON", Material.NETHERITE_SWORD, player);

        assertFalse(quest.checkCompletion());
        assertEquals(3, quest.getCurrentCount());
        assertEquals(3, quest.getStreak(player));
    }

    @Test
    public void testStreakAfterInvalidKill() {
        PlayerMock player = server.addPlayer();

        // Streak de 2 kills
        quest.updateProgress("SKELETON", Material.IRON_SWORD, player);
        quest.updateProgress("SKELETON", Material.DIAMOND_SWORD, player);
        assertEquals(2, quest.getCurrentCount());
        assertEquals(2, quest.getStreak(player));

        // Kill inválido (mob errado)
        quest.updateProgress("ZOMBIE", Material.IRON_SWORD, player);
        assertEquals(0, quest.getCurrentCount());
        assertEquals(0, quest.getStreak(player));

        // Novo streak
        quest.updateProgress("SKELETON", Material.IRON_SWORD, player);
        assertEquals(1, quest.getCurrentCount());
        assertEquals(1, quest.getStreak(player));
    }

    @Test
    public void testNoUpdateAfterCompletion() {
        PlayerMock player = server.addPlayer();

        // Completa a quest
        for (int i = 0; i < 6; i++) {
            quest.updateProgress("SKELETON", Material.IRON_SWORD, player);
        }

        assertTrue(quest.checkCompletion());
        assertEquals(6, quest.getCurrentCount());
        assertEquals(6, quest.getStreak(player));

        // Tentativa de kill adicional não deve alterar nada
        quest.updateProgress("SKELETON", Material.IRON_SWORD, player);
        assertEquals(6, quest.getCurrentCount(), "Não deve alterar após completion");
        assertEquals(6, quest.getStreak(player), "Não deve alterar após completion");
    }
}