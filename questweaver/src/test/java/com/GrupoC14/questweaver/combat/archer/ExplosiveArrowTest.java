package com.GrupoC14.questweaver.combat.archer;

import br.dev.projetoc14.QuestWeaver;
import br.dev.projetoc14.player.RPGPlayer;
import br.dev.projetoc14.player.abilities.archerSkills.ExplosiveArrow;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zombie;
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

public class ExplosiveArrowTest {

    private ExplosiveArrow explosiveArrow;
    private ServerMock server;
    private QuestWeaver plugin;

    @BeforeEach
    void setUp() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(QuestWeaver.class);
        explosiveArrow = new ExplosiveArrow(plugin);
        server.getPluginManager().registerEvents(explosiveArrow, plugin);
    }

    @AfterEach
    void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    public void testAbilityProperties() {
        assertEquals("Flecha Explosiva", explosiveArrow.getName());
        assertEquals(15, explosiveArrow.getManaCost());
        assertEquals(6, explosiveArrow.getCooldown());
        assertEquals(5, explosiveArrow.getDamage());
        assertEquals(3, explosiveArrow.getAreaDamage());
        assertEquals(3.5, explosiveArrow.getExplosionRadius());
    }

    @Test
    public void testOnCastDoesNotThrowException() {
        PlayerMock player = server.addPlayer();
        RPGPlayer rpgPlayer = mock(RPGPlayer.class);
        when(rpgPlayer.getPlayer()).thenReturn(player);
        when(rpgPlayer.getEyeLocation()).thenReturn(player.getEyeLocation());
        when(rpgPlayer.getWorld()).thenReturn(player.getWorld());
        when(rpgPlayer.launchProjectile(Arrow.class)).thenReturn(player.launchProjectile(Arrow.class));

        assertDoesNotThrow(() -> explosiveArrow.onCast(rpgPlayer));
    }

    @Test
    public void testOnCastLaunchesArrow() {
        PlayerMock player = server.addPlayer();
        RPGPlayer rpgPlayer = mock(RPGPlayer.class);
        when(rpgPlayer.getPlayer()).thenReturn(player);
        when(rpgPlayer.getEyeLocation()).thenReturn(player.getEyeLocation());
        when(rpgPlayer.getWorld()).thenReturn(player.getWorld());
        when(rpgPlayer.launchProjectile(Arrow.class)).thenReturn(player.launchProjectile(Arrow.class));

        explosiveArrow.onCast(rpgPlayer);

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

        explosiveArrow.onCast(rpgPlayer);

        assertTrue(arrow.hasMetadata("explosive_arrow"));
        assertTrue(arrow.hasMetadata("explosive_arrow_shooter"));
    }

    @Test
    public void testOnCastSetsArrowOnFire() {
        PlayerMock player = server.addPlayer();
        Arrow arrow = player.launchProjectile(Arrow.class);

        RPGPlayer rpgPlayer = mock(RPGPlayer.class);
        when(rpgPlayer.getPlayer()).thenReturn(player);
        when(rpgPlayer.getEyeLocation()).thenReturn(player.getEyeLocation());
        when(rpgPlayer.getWorld()).thenReturn(player.getWorld());
        when(rpgPlayer.launchProjectile(Arrow.class)).thenReturn(arrow);
        when(rpgPlayer.getUniqueId()).thenReturn(player.getUniqueId());

        explosiveArrow.onCast(rpgPlayer);

        assertTrue(arrow.getFireTicks() > 0, "A flecha deve estar em chamas");
    }

    @Test
    public void testOnHitAppliesDirectDamageToPlayer() {
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

        explosiveArrow.onHit(event, caster, target);

        // Verifica se o dano direto foi aplicado (20 - 5 = 15)
        verify(target).setCurrentHealth(15);
    }

    @Test
    public void testOnHitAppliesBurnEffect() {
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

        explosiveArrow.onHit(event, caster, target);

        // Verifica se o jogador está em chamas
        assertTrue(targetPlayer.getFireTicks() > 0, "O jogador deve estar em chamas");

        // Verifica se o efeito de fraqueza foi aplicado
        PotionEffect weakness = targetPlayer.getPotionEffect(PotionEffectType.WEAKNESS);
        assertNotNull(weakness, "O efeito de WEAKNESS deve ser aplicado");
        assertEquals(40, weakness.getDuration());
        assertEquals(0, weakness.getAmplifier());
    }

    @Test
    public void testOnHitAppliesKnockback() {
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

        explosiveArrow.onHit(event, caster, target);

        // Verifica se a velocidade do jogador foi alterada (knockback)
        assertNotEquals(0.0, targetPlayer.getVelocity().length(),
                "O jogador deve ter recebido knockback");
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
        assertDoesNotThrow(() -> explosiveArrow.onHit(event, caster, target));

        // Verifica que o dano ainda foi aplicado
        verify(target).setCurrentHealth(15);
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
            explosiveArrow.onCast(rpgPlayer);
            explosiveArrow.onCast(rpgPlayer);
            explosiveArrow.onCast(rpgPlayer);
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

        explosiveArrow.onHit(event, caster, target);

        // Verifica se a vida não ficou negativa (deve ser 0)
        verify(target).setCurrentHealth(0);
    }

    @Test
    public void testAreaDamageToMobs() {
        PlayerMock casterPlayer = server.addPlayer();

        RPGPlayer caster = mock(RPGPlayer.class);
        when(caster.getPlayer()).thenReturn(casterPlayer);
        when(caster.getLocation()).thenReturn(casterPlayer.getLocation());

        // Cria mobs próximos
        Zombie zombie1 = (Zombie) casterPlayer.getWorld().spawnEntity(
                casterPlayer.getLocation().add(1, 0, 0),
                EntityType.ZOMBIE
        );
        Zombie zombie2 = (Zombie) casterPlayer.getWorld().spawnEntity(
                casterPlayer.getLocation().add(2, 0, 0),
                EntityType.ZOMBIE
        );

        double initialHealth1 = zombie1.getHealth();
        double initialHealth2 = zombie2.getHealth();

        Arrow arrow = (Arrow) casterPlayer.getWorld().spawnEntity(
                casterPlayer.getLocation(),
                EntityType.ARROW
        );

        ProjectileHitEvent event = new ProjectileHitEvent(arrow, null, null, null);

        explosiveArrow.onHit(event, caster, null);

        assertTrue(zombie1.getHealth() < initialHealth1,
                "Zumbi próximo deve receber dano de área");
        assertTrue(zombie2.getHealth() < initialHealth2,
                "Zumbi mais distante deve receber dano de área");
    }

    @Test
    public void testCasterDoesNotTakeDamage() {
        PlayerMock casterPlayer = server.addPlayer();

        RPGPlayer caster = mock(RPGPlayer.class);
        when(caster.getPlayer()).thenReturn(casterPlayer);
        when(caster.getLocation()).thenReturn(casterPlayer.getLocation());
        when(caster.getCurrentHealth()).thenReturn(20);

        Arrow arrow = (Arrow) casterPlayer.getWorld().spawnEntity(
                casterPlayer.getLocation(),
                EntityType.ARROW
        );

        ProjectileHitEvent event = new ProjectileHitEvent(arrow, null, null, null);

        explosiveArrow.onHit(event, caster, null);

        // Verifica que o caster não recebeu dano
        verify(caster, never()).setCurrentHealth(anyInt());
    }

    @Test
    public void testAreaDamageDecreasesWithDistance() {
        PlayerMock casterPlayer = server.addPlayer();

        RPGPlayer caster = mock(RPGPlayer.class);
        when(caster.getPlayer()).thenReturn(casterPlayer);
        when(caster.getLocation()).thenReturn(casterPlayer.getLocation());

        // Cria mobs a diferentes distâncias
        Zombie nearZombie = (Zombie) casterPlayer.getWorld().spawnEntity(
                casterPlayer.getLocation().add(0.5, 0, 0),
                EntityType.ZOMBIE
        );
        Zombie farZombie = (Zombie) casterPlayer.getWorld().spawnEntity(
                casterPlayer.getLocation().add(3, 0, 0),
                EntityType.ZOMBIE
        );

        nearZombie.setHealth(20);
        farZombie.setHealth(20);

        Arrow arrow = (Arrow) casterPlayer.getWorld().spawnEntity(
                casterPlayer.getLocation(),
                EntityType.ARROW
        );

        ProjectileHitEvent event = new ProjectileHitEvent(arrow, null, null, null);

        explosiveArrow.onHit(event, caster, null);

        assertTrue(nearZombie.getHealth() < farZombie.getHealth(),
                "Mob mais próximo deve receber mais dano que o distante");
    }

    @Test
    public void testEntitiesOutsideRadiusNotAffected() {
        PlayerMock casterPlayer = server.addPlayer();

        RPGPlayer caster = mock(RPGPlayer.class);
        when(caster.getPlayer()).thenReturn(casterPlayer);
        when(caster.getLocation()).thenReturn(casterPlayer.getLocation());

        // Cria mob fora do raio de explosão (> 3.5 blocos)
        Zombie distantZombie = (Zombie) casterPlayer.getWorld().spawnEntity(
                casterPlayer.getLocation().add(5, 0, 0),
                EntityType.ZOMBIE
        );

        double initialHealth = distantZombie.getHealth();

        Arrow arrow = (Arrow) casterPlayer.getWorld().spawnEntity(
                casterPlayer.getLocation(),
                EntityType.ARROW
        );

        ProjectileHitEvent event = new ProjectileHitEvent(arrow, null, null, null);

        explosiveArrow.onHit(event, caster, null);

        assertEquals(initialHealth, distantZombie.getHealth(),
                "Mob fora do raio de explosão não deve receber dano");
    }
}