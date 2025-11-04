package br.dev.projetoc14.player.abilities.assassinSkills;

import br.dev.projetoc14.player.RPGPlayer;
import br.dev.projetoc14.player.abilities.Ability;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ShadowMove extends Ability {
    public ShadowMove() {
        super("Shadow Move", 12, 15);
    }

    @Override
    protected void onCast(RPGPlayer caster) {

        caster.getPlayer().addPotionEffect(
            new PotionEffect(
                    PotionEffectType.SPEED,
                    20 * 10,
                    1,
                    false,
                    false,
                    true
            )
        );

        caster.getPlayer().addPotionEffect(
                new PotionEffect(
                        PotionEffectType.INVISIBILITY,
                        20 * 10,
                        1,
                        false,
                        false,
                        true
                )
        );

        caster.getPlayer().sendMessage("§8☯ §7Você se move nas sombras... §");
    }
}
