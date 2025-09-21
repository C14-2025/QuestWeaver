package br.dev.projetoc14.combat;

import br.dev.projetoc14.player.RPGPlayer;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;

public class Fireball extends Ability {

    private final int damage = 25; // quanto de dano a fireball causa

    public Fireball() {
        super("Bola de Fogo", 20, 5); // nome, custo de mana, cooldown em segundos
    }

    @Override
    public void cast(RPGPlayer caster) {
        // Cria uma fireball na frente do player
        Location loc = caster.getEyeLocation();
        org.bukkit.entity.Fireball fireball = caster.launchProjectile(org.bukkit.entity.Fireball.class);
        fireball.setYield(2F); // tamanho da explosão
        fireball.setIsIncendiary(false);

        // Efeitos visuais/sonoros
        caster.getWorld().playSound(loc, Sound.ENTITY_BLAZE_SHOOT, 1f, 1f);
        caster.getWorld().spawnParticle(Particle.FLAME, loc, 20, 0.3, 0.3, 0.3, 0.01);

        caster.sendMessage("🔥 Você lançou uma Bola de Fogo!");
    }

    public void applyDamage(RPGPlayer caster, RPGPlayer target) {
        int newHealth = target.getCurrentHealth() - damage;
        if (newHealth < 0) newHealth = 0;
        target.setCurrentHealth(newHealth);
    }

    public int getDamage() {
        return damage;
    }
}