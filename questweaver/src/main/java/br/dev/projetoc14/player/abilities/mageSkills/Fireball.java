package br.dev.projetoc14.player.abilities.mageSkills;

import br.dev.projetoc14.player.RPGPlayer;
import br.dev.projetoc14.player.abilities.Ability;
import br.dev.projetoc14.player.abilities.CooldownListener;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;

public class Fireball extends Ability {

    private final int damage = 25;

    public Fireball() {
        super("Bola de Fogo", 15, 5); // nome, custo de mana, cooldown em segundos
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

    public String getName() {
        return "Bola de Fogo";
    }

    public int getManaCost() {
        return 20;
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