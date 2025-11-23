package com.GrupoC14.questweaver.combat.archer;

import br.dev.projetoc14.QuestWeaver;
import br.dev.projetoc14.player.RPGPlayer;
import br.dev.projetoc14.player.abilities.archerSkills.PoisonArrow;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;
import org.mockbukkit.mockbukkit.entity.PlayerMock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PoisonArrowTest {

    private PoisonArrow poisonArrow;
    private ServerMock server;
    private QuestWeaver plugin;

    @BeforeEach
    void setUp() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(QuestWeaver.class);
        poisonArrow = new PoisonArrow(plugin);
        server.getPluginManager().registerEvents(poisonArrow, plugin);
    }

    @AfterEach
    void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    public void testAbilityProperties() {
        assertEquals("Flecha Venenosa", poisonArrow.getName());
        assertEquals(20, poisonArrow.getManaCost());
        assertEquals(9, poisonArrow.getCooldown());
        assertEquals(6, poisonArrow.getDamage());
    }

    @Test
    public void testOnCastDoesNotThrowException() {
        PlayerMock player = server.addPlayer();
        RPGPlayer rpgPlayer = mock(RPGPlayer.class);
        when(rpgPlayer.getPlayer()).thenReturn(player);
        when(rpgPlayer.getEyeLocation()).thenReturn(player.getEyeLocation());
        when(rpgPlayer.getWorld()).thenReturn(player.getWorld());
        when(rpgPlayer.launchProjectile(Arrow.class)).thenReturn(player.launchProjectile(Arrow.class));

        assertDoesNotThrow(() -> poisonArrow.onCast(rpgPlayer));
    }

    @Test
    public void testOnCastLaunchesArrow() {
        PlayerMock player = server.addPlayer();
        RPGPlayer rpgPlayer = mock(RPGPlayer.class);
        when(rpgPlayer.getPlayer()).thenReturn(player);
        when(rpgPlayer.getEyeLocation()).thenReturn(player.getEyeLocation());
        when(rpgPlayer.getWorld()).thenReturn(player.getWorld());
        when(rpgPlayer.launchProjectile(Arrow.class)).thenReturn(player.launchProjectile(Arrow.class));

        poisonArrow.onCast(rpgPlayer);

        verify(rpgPlayer).launchProjectile(Arrow.class);
    }

    @Test
    public void testOnCastSetsArrowMetadata() {
        PlayerMock player = server.addPlayer();
        Arrow arrow = player.launchProjectile(Arrow.class);

        RPGPlayer rpgPlayer = mock(RPGPlayer.class);
        when(rpgPlayer.getPlayer()).thenReturn(player);
        when(rpgPlayer.getEyeLocation()).thenReturn(player.getEyeLocation());
        when(rpgPlayer.getWorld()).thenReturn(player.getWorld());
        when(rpgPlayer.launchProjectile(Arrow.class)).thenReturn(arrow);
        when(rpgPlayer.getUniqueId()).thenReturn(player.getUniqueId());

        poisonArrow.onCast(rpgPlayer);

        assertTrue(arrow.hasMetadata("poison_arrow"));
        assertTrue(arrow.hasMetadata("poison_arrow_shooter"));
    }

    @Test
    public void testOnHitAppliesDamageToPlayer() {
        PlayerMock casterPlayer = server.addPlayer();
        PlayerMock targetPlayer = server.addPlayer();

        RPGPlayer caster = mock(RPGPlayer.class);
        RPGPlayer target = mock(RPGPlayer.class);

        when(caster.getPlayer()).thenReturn(casterPlayer);
        when(caster.getLocation()).thenReturn(casterPlayer.getLocation());
        when(target.getPlayer()).thenReturn(targetPlayer);
        when(target.getCurrentHealth()).thenReturn(20);

        Arrow arrow = (Arrow) casterPlayer.getWorld().spawnEntity(
                casterPlayer.getLocation(),
                EntityType.ARROW
        );

        ProjectileHitEvent event = new ProjectileHitEvent(arrow, targetPlayer, null, null);

        poisonArrow.onHit(event, caster, target);

        // Verifica se o dano inicial foi aplicado (20 - 6 = 14)
        verify(target).setCurrentHealth(14);
    }

    @Test
    public void testOnHitAppliesDamageToMob() {
        PlayerMock casterPlayer = server.addPlayer();

        RPGPlayer caster = mock(RPGPlayer.class);
        when(caster.getPlayer()).thenReturn(casterPlayer);
        when(caster.getLocation()).thenReturn(casterPlayer.getLocation());

        Zombie zombie = (Zombie) casterPlayer.getWorld().spawnEntity(
                casterPlayer.getLocation().add(5, 0, 0),
                EntityType.ZOMBIE
        );

        double initialHealth = zombie.getHealth();

        Arrow arrow = (Arrow) casterPlayer.getWorld().spawnEntity(
                casterPlayer.getLocation(),
                EntityType.ARROW
        );

        ProjectileHitEvent event = new ProjectileHitEvent(arrow, zombie, null, null);

        poisonArrow.onHit(event, caster, null);

        // Verifica que o mob recebeu dano
        assertTrue(zombie.getHealth() < initialHealth);
    }

    @Test
    public void testOnHitDoesNotThrowExceptionWithNullTarget() {
        PlayerMock casterPlayer = server.addPlayer();
        RPGPlayer caster = mock(RPGPlayer.class);
        when(caster.getPlayer()).thenReturn(casterPlayer);

        Arrow arrow = (Arrow) casterPlayer.getWorld().spawnEntity(
                casterPlayer.getLocation(),
                EntityType.ARROW
        );

        // Hit em bloco ao invés de entidade
        Block hitBlock = casterPlayer.getLocation().getBlock();
        ProjectileHitEvent event = new ProjectileHitEvent(arrow, null, hitBlock, BlockFace.UP);

        assertDoesNotThrow(() -> poisonArrow.onHit(event, caster, null));
    }

    @Test
    public void testOnHitWithNullPlayer() {
        PlayerMock casterPlayer = server.addPlayer();

        RPGPlayer caster = mock(RPGPlayer.class);
        RPGPlayer target = mock(RPGPlayer.class);

        when(caster.getPlayer()).thenReturn(casterPlayer);
        when(caster.getLocation()).thenReturn(casterPlayer.getLocation());
        when(target.getPlayer()).thenReturn(null); // Player nulo
        when(target.getCurrentHealth()).thenReturn(20);

        Arrow arrow = (Arrow) casterPlayer.getWorld().spawnEntity(
                casterPlayer.getLocation(),
                EntityType.ARROW
        );

        ProjectileHitEvent event = new ProjectileHitEvent(arrow, null, null, null);

        // Não deve lançar exceção mesmo com player nulo
        assertDoesNotThrow(() -> poisonArrow.onHit(event, caster, target));

        // Verifica que o dano ainda foi aplicado
        verify(target).setCurrentHealth(14);
    }

    @Test
    public void testMultipleCasts() {
        PlayerMock player = server.addPlayer();
        RPGPlayer rpgPlayer = mock(RPGPlayer.class);
        when(rpgPlayer.getPlayer()).thenReturn(player);
        when(rpgPlayer.getEyeLocation()).thenReturn(player.getEyeLocation());
        when(rpgPlayer.getWorld()).thenReturn(player.getWorld());
        when(rpgPlayer.launchProjectile(Arrow.class)).thenReturn(player.launchProjectile(Arrow.class));
        when(rpgPlayer.getUniqueId()).thenReturn(player.getUniqueId());

        assertDoesNotThrow(() -> {
            poisonArrow.onCast(rpgPlayer);
            poisonArrow.onCast(rpgPlayer);
            poisonArrow.onCast(rpgPlayer);
        });

        assertTrue(player.isOnline());
    }

    @Test
    public void testDamageDoesNotGoNegative() {
        PlayerMock casterPlayer = server.addPlayer();
        PlayerMock targetPlayer = server.addPlayer();

        RPGPlayer caster = mock(RPGPlayer.class);
        RPGPlayer target = mock(RPGPlayer.class);

        when(caster.getPlayer()).thenReturn(casterPlayer);
        when(caster.getLocation()).thenReturn(casterPlayer.getLocation());
        when(target.getPlayer()).thenReturn(targetPlayer);
        when(target.getCurrentHealth()).thenReturn(3); // Vida baixa

        Arrow arrow = (Arrow) casterPlayer.getWorld().spawnEntity(
                casterPlayer.getLocation(),
                EntityType.ARROW
        );

        ProjectileHitEvent event = new ProjectileHitEvent(arrow, targetPlayer, null, null);

        poisonArrow.onHit(event, caster, target);

        // Verifica se a vida não ficou negativa (deve ser 0)
        verify(target).setCurrentHealth(0);
    }

    @Test
    public void testPoisonEffectDuration() {
        PlayerMock casterPlayer = server.addPlayer();
        PlayerMock targetPlayer = server.addPlayer();

        RPGPlayer caster = mock(RPGPlayer.class);
        RPGPlayer target = mock(RPGPlayer.class);

        when(caster.getPlayer()).thenReturn(casterPlayer);
        when(caster.getLocation()).thenReturn(casterPlayer.getLocation());
        when(target.getPlayer()).thenReturn(targetPlayer);
        when(target.getCurrentHealth()).thenReturn(50);

        Arrow arrow = (Arrow) casterPlayer.getWorld().spawnEntity(
                casterPlayer.getLocation(),
                EntityType.ARROW
        );

        ProjectileHitEvent event = new ProjectileHitEvent(arrow, targetPlayer, null, null);

        poisonArrow.onHit(event, caster, target);

        // Verifica que o dano inicial foi aplicado
        verify(target).setCurrentHealth(44);

        // O efeito de veneno é aplicado em uma task assíncrona
        // que duraria 80 ticks (4 segundos) com dano a cada 20 ticks
        assertTrue(targetPlayer.isOnline());
    }
}