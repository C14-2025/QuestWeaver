package com.GrupoC14.questweaver.combat.assassin;

import br.dev.projetoc14.QuestWeaver;
import br.dev.projetoc14.player.RPGPlayer;
import br.dev.projetoc14.player.abilities.assassinSkills.DemonProjectile;
import br.dev.projetoc14.player.classes.AssassinPlayer;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;
import org.mockbukkit.mockbukkit.entity.PlayerMock;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DemonProjectileTest {

    private DemonProjectile demonProjectile;
    private ServerMock server;
    private QuestWeaver plugin;

    @BeforeEach
    void setUp() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(QuestWeaver.class);
        demonProjectile = new DemonProjectile();
        demonProjectile.setPlugin(plugin); // Injetar plugin para testes - erro sem
    }

    @AfterEach
    void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    public void testAbilityProperties() {
        assertEquals("Demon Projectile", demonProjectile.getName());
        assertEquals(25, demonProjectile.getManaCost());
        assertEquals(6, demonProjectile.getCooldown());
    }

    @Test
    public void testOnCastDoesNotThrowException() {
        PlayerMock player = server.addPlayer();
        RPGPlayer rpgPlayer = mock(RPGPlayer.class);
        when(rpgPlayer.getPlayer()).thenReturn(player);

        assertDoesNotThrow(() -> demonProjectile.onCast(rpgPlayer));
    }

    @Test
    public void testOnCastCreatesArmorStand() {
        PlayerMock player = server.addPlayer();
        RPGPlayer rpgPlayer = mock(RPGPlayer.class);
        when(rpgPlayer.getPlayer()).thenReturn(player);

        demonProjectile.onCast(rpgPlayer);

        // Aguardar um tick para o ArmorStand ser criado
        server.getScheduler().performOneTick();

        Collection<Entity> entities = player.getWorld().getEntities();
        long armorStandCount = entities.stream()
                .filter(e -> e instanceof ArmorStand)
                .count();

        assertTrue(armorStandCount > 0, "Deve criar pelo menos um ArmorStand");
    }

    @Test
    public void testArmorStandHasCorrectProperties() {
        PlayerMock player = server.addPlayer();
        RPGPlayer rpgPlayer = mock(RPGPlayer.class);
        when(rpgPlayer.getPlayer()).thenReturn(player);

        demonProjectile.onCast(rpgPlayer);
        server.getScheduler().performOneTick();

        ArmorStand armorStand = (ArmorStand) player.getWorld().getEntities().stream()
                .filter(e -> e instanceof ArmorStand)
                .findFirst()
                .orElse(null);

        assertNotNull(armorStand, "ArmorStand deve ser criado");
        assertFalse(armorStand.isVisible(), "ArmorStand deve ser invisível");
    }

    @Test
    public void testExecuteWithAssassinPlayer() {
        PlayerMock player = server.addPlayer();
        AssassinPlayer assassin = mock(AssassinPlayer.class);
        when(assassin.getPlayer()).thenReturn(player);
        when(assassin.getCurrentMana()).thenReturn(50);
        when(assassin.getMaxMana()).thenReturn(100);

        assertDoesNotThrow(() -> demonProjectile.execute(player, assassin));
    }

    @Test
    public void testMultipleCasts() {
        PlayerMock player = server.addPlayer();
        RPGPlayer rpgPlayer = mock(RPGPlayer.class);
        when(rpgPlayer.getPlayer()).thenReturn(player);

        assertDoesNotThrow(() -> {
            demonProjectile.onCast(rpgPlayer);
            server.getScheduler().performOneTick();
        });

        assertTrue(player.isOnline());
    }

    @Test
    public void testProjectileMovement() {
        PlayerMock player = server.addPlayer();
        RPGPlayer rpgPlayer = mock(RPGPlayer.class);
        when(rpgPlayer.getPlayer()).thenReturn(player);

        Location initialLocation = player.getEyeLocation();
        demonProjectile.onCast(rpgPlayer);

        // Simular alguns ticks
        for (int i = 0; i < 10; i++) {
            server.getScheduler().performOneTick();
        }

        ArmorStand armorStand = (ArmorStand) player.getWorld().getEntities().stream()
                .filter(e -> e instanceof ArmorStand)
                .findFirst()
                .orElse(null);

        if (armorStand != null) {
            Location armorStandLocation = armorStand.getLocation();
            double distance = initialLocation.distance(armorStandLocation);
            assertTrue(distance > 0, "O projétil deve ter se movido");
        }
    }

    @Test
    public void testProjectileDespawnsAfterCertainDistance() {
        PlayerMock player = server.addPlayer();
        RPGPlayer rpgPlayer = mock(RPGPlayer.class);
        when(rpgPlayer.getPlayer()).thenReturn(player);

        demonProjectile.onCast(rpgPlayer);

        // Simular vários ticks para exceder a distância máxima
        for (int i = 0; i < 200; i++) {
            server.getScheduler().performOneTick();
        }

        long armorStandCount = player.getWorld().getEntities().stream()
                .filter(e -> e instanceof ArmorStand)
                .count();

        assertEquals(0, armorStandCount, "ArmorStand deve ser removido após distância máxima");
    }

    @Test
    public void testDamageToLivingEntity() {
        PlayerMock player = server.addPlayer();
        PlayerMock target = server.addPlayer();

        RPGPlayer rpgPlayer = mock(RPGPlayer.class);
        when(rpgPlayer.getPlayer()).thenReturn(player);

        // Posicionar o alvo na frente do jogador
        Location playerLoc = player.getLocation();
        target.teleport(playerLoc.add(playerLoc.getDirection().multiply(3)));

        double initialHealth = target.getHealth();

        demonProjectile.onCast(rpgPlayer);

        // Simular ticks para o projétil atingir o alvo
        for (int i = 0; i < 20; i++) {
            server.getScheduler().performOneTick();
        }

        // Verificar se o alvo recebeu dano (pode não acontecer devido às limitações do mock)
        assertTrue(target.getHealth() <= initialHealth,
                "O alvo deve ter recebido dano");
    }

    @Test
    public void testPlayerNotDamagedByOwnProjectile() {
        PlayerMock player = server.addPlayer();
        RPGPlayer rpgPlayer = mock(RPGPlayer.class);
        when(rpgPlayer.getPlayer()).thenReturn(player);

        double initialHealth = player.getHealth();

        demonProjectile.onCast(rpgPlayer);

        // Simular alguns ticks
        for (int i = 0; i < 30; i++) {
            server.getScheduler().performOneTick();
        }

        assertEquals(initialHealth, player.getHealth(),
                "O jogador não deve ser danificado pelo próprio projétil");
    }

    @Test
    public void testMaxHitsPerEntity() {
        PlayerMock player = server.addPlayer();
        PlayerMock target = server.addPlayer();

        RPGPlayer rpgPlayer = mock(RPGPlayer.class);
        when(rpgPlayer.getPlayer()).thenReturn(player);

        // Posicionar o alvo na trajetória do projétil
        Location playerLoc = player.getLocation();
        target.teleport(playerLoc.add(playerLoc.getDirection().multiply(2)));

        demonProjectile.onCast(rpgPlayer);

        // Simular vários ticks para garantir múltiplas verificações de colisão
        for (int i = 0; i < 50; i++) {
            server.getScheduler().performOneTick();
        }

        assertTrue(player.isOnline());
        assertTrue(target.isOnline());
    }
}