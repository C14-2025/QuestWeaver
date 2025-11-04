package br.dev.projetoc14.player.abilities.archerSkills;

import br.dev.projetoc14.player.RPGPlayer;
import br.dev.projetoc14.player.abilities.Ability;

public class PoisonArrow extends Ability {

    public PoisonArrow(String name, int manaCost, int cooldown) {
        super(name, manaCost, cooldown);
    }

    @Override
    protected void onCast(RPGPlayer caster) {

    }
}
