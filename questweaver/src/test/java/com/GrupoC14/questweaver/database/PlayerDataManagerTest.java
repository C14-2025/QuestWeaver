package com.GrupoC14.questweaver.database;

import br.dev.projetoc14.player.PlayerStats;
import br.dev.projetoc14.playerData.PlayerDataManager;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
// JUnit 5
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

// Mockito
import org.mockito.Mockito;

import java.io.File;
import java.util.UUID;

public class PlayerDataManagerTest {

    private PlayerDataManager manager;
    private File tempFolder;

    @BeforeEach
    public void setUp() {
        // Mock do plugin
        JavaPlugin pluginMock = Mockito.mock(JavaPlugin.class);

        // Pasta temporária para simular getDataFolder()
        tempFolder = new File(System.getProperty("java.io.tmpdir"), "pluginTestData");
        tempFolder.mkdirs();
        Mockito.when(pluginMock.getDataFolder()).thenReturn(tempFolder);

        // Mock do logger do plugin para evitar NPE
        Mockito.when(pluginMock.getLogger()).thenReturn(java.util.logging.Logger.getLogger("TestLogger"));

        // Inicializa PlayerDataManager
        manager = new PlayerDataManager(pluginMock);
    }

    @AfterEach
    public void tearDown() {
        deleteFolder(tempFolder);
    }

    private void deleteFolder(File folder) {
        if (folder.isDirectory()) {
            for (File f : folder.listFiles()) {
                deleteFolder(f);
            }
        }
        folder.delete();
    }

    @Test
    public void testFolderCreated() {
        File dataFolder = new File(tempFolder, "playerdata");
        assertTrue(dataFolder.exists(), "A pasta de dados deve existir.");
        assertTrue(dataFolder.isDirectory(), "Deve ser uma pasta.");
    }

    @Test
    public void testSaveAndLoadPlayerStats() {
        // Mock do player com UUID único
        Player playerMock = Mockito.mock(Player.class);
        UUID playerUUID = UUID.randomUUID();
        Mockito.when(playerMock.getUniqueId()).thenReturn(playerUUID);
        Mockito.when(playerMock.getName()).thenReturn("TestPlayer");

        // Cria stats de teste
        PlayerStats stats = new PlayerStats();
        stats.setHealth(200);
        stats.setMana(150);
        stats.setCurrentHealth(180);
        stats.setCurrentMana(120);

        // Salva os stats
        manager.savePlayerStats(playerMock, stats);

        // Carrega os stats
        PlayerStats loaded = manager.loadPlayerStats(playerMock);

        assertNotNull(loaded);
        assertEquals(stats.getHealth(), loaded.getHealth());
        assertEquals(stats.getMana(), loaded.getMana());
        assertEquals(stats.getCurrentHealth(), loaded.getCurrentHealth());
        assertEquals(stats.getCurrentMana(), loaded.getCurrentMana());
    }
}
