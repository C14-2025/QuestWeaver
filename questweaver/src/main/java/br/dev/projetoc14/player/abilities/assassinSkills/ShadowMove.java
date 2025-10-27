package br.dev.projetoc14.player.abilities.assassinSkills;

import br.dev.projetoc14.player.RPGPlayer;
import br.dev.projetoc14.player.abilities.Ability;

public class ShadowMove extends Ability {
    public ShadowMove() {
        super("Shadow Move", 12, 15);
    }

    @Override
    protected void onCast(RPGPlayer caster) {
        caster.getStats().setAgility((int)(caster.getStats().getAgility()*1.2));
    }
}
