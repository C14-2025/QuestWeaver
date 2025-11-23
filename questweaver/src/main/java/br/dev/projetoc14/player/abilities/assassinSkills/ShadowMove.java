package br.dev.projetoc14.player.abilities.assassinSkills;

import br.dev.projetoc14.player.RPGPlayer;
import br.dev.projetoc14.player.abilities.Ability;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

// Habilidades contínuas precisam ser Listener
public class ShadowMove extends Ability implements Listener {
    public ShadowMove() {
        super("Shadow Move", 12, 15);
    }

    @Override
    public void onCast(RPGPlayer caster) {

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
