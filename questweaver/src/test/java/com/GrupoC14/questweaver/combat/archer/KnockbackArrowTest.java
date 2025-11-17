package com.GrupoC14.questweaver.combat.archer;

import br.dev.projetoc14.QuestWeaver;
import br.dev.projetoc14.player.RPGPlayer;
import br.dev.projetoc14.player.abilities.archerSkills.KnockbackArrow;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.MockBukkit;
import org.mockbukkit.mockbukkit.ServerMock;
import org.mockbukkit.mockbukkit.entity.PlayerMock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class KnockbackArrowTest {

    private KnockbackArrow knockbackArrow;
    private ServerMock server;

    @BeforeEach
    void setUp() {
        server = MockBukkit.mock();
        QuestWeaver plugin = MockBukkit.load(QuestWeaver.class);
        knockbackArrow = new KnockbackArrow(plugin);
        server.getPluginManager().registerEvents(knockbackArrow, plugin);
    }

    @AfterEach
    void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    public void testAbilityProperties() {
        assertEquals("Flecha Repulsora", knockbackArrow.getName());
        assertEquals(15, knockbackArrow.getManaCost());
        assertEquals(4, knockbackArrow.getCooldown());
        assertEquals(4, knockbackArrow.getDamage());
    }

    @Test
    public void testOnCastDoesNotThrowException() {
        PlayerMock player = server.addPlayer();
        RPGPlayer rpgPlayer = mock(RPGPlayer.class);
        when(rpgPlayer.getPlayer()).thenReturn(player);
        when(rpgPlayer.getEyeLocation()).thenReturn(player.getEyeLocation());
        when(rpgPlayer.getWorld()).thenReturn(player.getWorld());
        when(rpgPlayer.launchProjectile(Arrow.class)).thenReturn(player.launchProjectile(Arrow.class));

        assertDoesNotThrow(() -> knockbackArrow.onCast(rpgPlayer));
    }

    @Test
    public void testOnCastLaunchesArrow() {
        PlayerMock player = server.addPlayer();
        RPGPlayer rpgPlayer = mock(RPGPlayer.class);
        when(rpgPlayer.getPlayer()).thenReturn(player);
        when(rpgPlayer.getEyeLocation()).thenReturn(player.getEyeLocation());
        when(rpgPlayer.getWorld()).thenReturn(player.getWorld());
        when(rpgPlayer.launchProjectile(Arrow.class)).thenReturn(player.launchProjectile(Arrow.class));

        knockbackArrow.onCast(rpgPlayer);

        verify(rpgPlayer).launchProjectile(Arrow.class);
    }

    @Test
    public void testOnHitAppliesEffectsToTarget() {
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

        knockbackArrow.onHit(event, caster, target);

        // Verifica os efeitos aplicados
        PotionEffect slowness = targetPlayer.getPotionEffect(PotionEffectType.SLOWNESS);
        PotionEffect nausea = targetPlayer.getPotionEffect(PotionEffectType.NAUSEA);

        assertNotNull(slowness, "O efeito de SLOWNESS deve ser aplicado");
        assertNotNull(nausea, "O efeito de NAUSEA deve ser aplicado");

        assertEquals(60, slowness.getDuration());
        assertEquals(100, nausea.getDuration());

        assertEquals(1, slowness.getAmplifier()); // Slowness II
        assertEquals(0, nausea.getAmplifier());
    }

    @Test
    public void testOnHitAppliesDamage() {
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

        knockbackArrow.onHit(event, caster, target);

        // Verifica se o dano foi aplicado (20 - 4 = 16)
        verify(target).setCurrentHealth(16);
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

        assertDoesNotThrow(() -> knockbackArrow.onHit(event, caster, null));
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
        assertDoesNotThrow(() -> knockbackArrow.onHit(event, caster, target));

        // Verifica que o dano ainda foi aplicado
        verify(target).setCurrentHealth(16);
    }

    @Test
    public void testMultipleCasts() {
        PlayerMock player = server.addPlayer();
        RPGPlayer rpgPlayer = mock(RPGPlayer.class);
        when(rpgPlayer.getPlayer()).thenReturn(player);
        when(rpgPlayer.getEyeLocation()).thenReturn(player.getEyeLocation());
        when(rpgPlayer.getWorld()).thenReturn(player.getWorld());
        when(rpgPlayer.launchProjectile(Arrow.class)).thenReturn(player.launchProjectile(Arrow.class));

        assertDoesNotThrow(() -> {
            knockbackArrow.onCast(rpgPlayer);
            knockbackArrow.onCast(rpgPlayer);
            knockbackArrow.onCast(rpgPlayer);
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
        when(target.getCurrentHealth()).thenReturn(2); // Vida baixa

        Arrow arrow = (Arrow) casterPlayer.getWorld().spawnEntity(
                casterPlayer.getLocation(),
                EntityType.ARROW
        );

        ProjectileHitEvent event = new ProjectileHitEvent(arrow, targetPlayer, null, null);

        knockbackArrow.onHit(event, caster, target);

        // Verifica se a vida não ficou negativa (deve ser 0)
        verify(target).setCurrentHealth(0);
    }
}