package com.GrupoC14.questweaver.models;

import br.dev.projetoc14.items.players.SkillTree;
import br.dev.projetoc14.match.PlayerFileManager;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

class SkillTreeTest {

    private ServerMock server;
    private Player mockPlayer;
    private PlayerFileManager fileManager;
    private SkillTree skillTree;
    private PlayerInventory mockInventory;

    @BeforeEach
    void setup() {
        // Inicializa o MockBukkit server para evitar NullPointerException
        server = MockBukkit.mock();

        mockPlayer = mock(Player.class);
        fileManager = mock(PlayerFileManager.class);
        skillTree = new SkillTree(fileManager);

        // mock inventário inteiro
        mockInventory = mock(PlayerInventory.class);
        when(mockPlayer.getInventory()).thenReturn(mockInventory);

        // player sempre tem XP suficiente
        when(mockPlayer.getTotalExperience()).thenReturn(9999);
    }

    @Test
    void testUpgradeVida() {
        // mock do atributo MAX_HEALTH
        AttributeInstance att = mock(AttributeInstance.class);
        when(mockPlayer.getAttribute(Attribute.MAX_HEALTH)).thenReturn(att);

        // Simula valor base inicial de 20.0 (saúde padrão do Minecraft)
        when(att.getBaseValue()).thenReturn(20.0);

        // Aplica upgrade de nível 2 (adiciona +4 vida)
        skillTree.applyHealthUpgrade(mockPlayer, 2);

        // Verifica se foi chamado setBaseValue com o valor correto
        // O código real chama: setBaseValue(baseValue + (level * 2))
        // baseValue = 20.0, level = 2, então: 20.0 + (2 * 2) = 24.0
        verify(att).setBaseValue(24.0);
    }

    @Test
    void testUpgradeArmadura() {
        // mock de capacete / peitoral
        doNothing().when(mockInventory).setHelmet(any(ItemStack.class));
        doNothing().when(mockInventory).setChestplate(any(ItemStack.class));

        skillTree.applyArmorUpgrade(mockPlayer, 1); // level 1 = Iron

        verify(mockInventory).setHelmet(argThat(i -> i != null && i.getType() == Material.IRON_HELMET));
        verify(mockInventory).setChestplate(argThat(i -> i != null && i.getType() == Material.IRON_CHESTPLATE));
    }

    @Test
    void testInventoryClick() {
        // Teste simplificado - verifica apenas que o método não lança exceção
        // quando o título não corresponde à árvore de habilidades
        InventoryClickEvent event = mock(InventoryClickEvent.class);
        InventoryView view = mock(InventoryView.class);

        when(event.getView()).thenReturn(view);
        // Usa um título diferente para que o método retorne cedo sem executar a lógica complexa
        when(view.getTitle()).thenReturn("Outro Inventário");
        when(event.getWhoClicked()).thenReturn(mockPlayer);

        // Executa o método - não deve fazer nada pois o título não corresponde
        skillTree.onInventoryClick(event);

        // Verifica que o evento NÃO foi cancelado (pois não é a árvore de habilidades)
        verify(event, never()).setCancelled(true);
    }

    @AfterEach
    void tearDown() {
        // Limpa o MockBukkit após cada teste
        MockBukkit.unmock();
    }
}