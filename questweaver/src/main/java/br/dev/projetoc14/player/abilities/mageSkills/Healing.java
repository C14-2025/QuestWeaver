package br.dev.projetoc14.player.abilities.mageSkills;

import br.dev.projetoc14.player.RPGPlayer;
import br.dev.projetoc14.player.abilities.Ability;

public class Healing extends Ability {

    public Healing() {
        super("Cura", 15, 3); // nome, custo de mana, cooldown
    }

    @Override
    protected void onCast(RPGPlayer caster) {
        int healAmount = 30;
        caster.heal(healAmount);
    }

    public String getName() {
        return "Cura";
    }

    public int getManaCost() {
        return 15;
    }

    public int getCooldown() {
        return 3;
    }

    public int getHealAmount() {
        return 30;
    }
}