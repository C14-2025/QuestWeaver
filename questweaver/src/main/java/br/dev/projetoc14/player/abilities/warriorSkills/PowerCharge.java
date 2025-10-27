package br.dev.projetoc14.player.abilities.warriorSkills;

import br.dev.projetoc14.player.RPGPlayer;
import br.dev.projetoc14.player.abilities.Ability;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import java.util.List;
import java.util.stream.Collectors;

public class PowerCharge extends Ability {

    public PowerCharge() {
        super("Investida Poderosa", 12, 8);
    }

    @Override
    protected void onCast(RPGPlayer caster) {

    }
}