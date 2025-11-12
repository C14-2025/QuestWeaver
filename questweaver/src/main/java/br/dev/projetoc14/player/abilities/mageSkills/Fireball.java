package br.dev.projetoc14.player.abilities.mageSkills;

import br.dev.projetoc14.QuestWeaver;
import br.dev.projetoc14.player.RPGPlayer;
import br.dev.projetoc14.player.abilities.Ability;
import br.dev.projetoc14.player.abilities.CooldownListener;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.persistence.PersistentDataType;

public class Fireball extends Ability implements Listener {

    private final int damage = 25;
    private final NamespacedKey magicDamageKey;

    public Fireball(QuestWeaver plugin) {
        super("Bola de Fogo", 15, 5); // nome, custo de mana, cooldown em segundos
        this.magicDamageKey = new NamespacedKey(plugin, "magic_damage");
        // Registra o listener
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @Override
    protected void onCast(RPGPlayer caster) {
        // Cria uma fireball na frente do player
        Location loc = caster.getEyeLocation();
        org.bukkit.entity.Fireball fireball = caster.launchProjectile(org.bukkit.entity.Fireball.class);
        fireball.setYield(2F); // tamanho da explosão
        fireball.setIsIncendiary(false);

        // Efeitos visuais/sonoros
        caster.getWorld().playSound(loc, Sound.ENTITY_BLAZE_SHOOT, 1f, 1f);
        caster.getWorld().spawnParticle(Particle.FLAME, loc, 20, 0.3, 0.3, 0.3, 0.01);

    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof org.bukkit.entity.Fireball)) return;
        if (!(event.getEntity() instanceof LivingEntity)) return;

        org.bukkit.entity.Fireball fireball = (org.bukkit.entity.Fireball) event.getDamager();
        LivingEntity target = (LivingEntity) event.getEntity();

        // Marca a entidade atingida com a tag de dano mágico
        target.getPersistentDataContainer().set(
                magicDamageKey,
                PersistentDataType.BOOLEAN,
                true
        );
    }

    public String getName() {
        return "Bola de Fogo";
    }

    public int getManaCost() {
        return 15;
    }

    public int getCooldown() {
        return 5;
    }

    public int getDamage() {
        return damage;
    }

    public void applyDamage(RPGPlayer caster, RPGPlayer target) {
        int newHealth = target.getCurrentHealth() - damage;
        if (newHealth < 0) newHealth = 0;
        target.setCurrentHealth(newHealth);
    }
}