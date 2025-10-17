package br.dev.projetoc14.player.abilities.mageSkills;

import br.dev.projetoc14.player.RPGPlayer;
import br.dev.projetoc14.player.abilities.Ability;

public class Healing extends Ability {

    private final int healAmount = 30;

    public Healing() {
        super("Cura", 15, 3); // nome, custo de mana, cooldown
    }

    @Override
    public void cast(RPGPlayer caster) {
        caster.heal(healAmount); // 🔥 agora realmente aplica a cura
    }

    public int getHealAmount() {
        return healAmount;
    }
}