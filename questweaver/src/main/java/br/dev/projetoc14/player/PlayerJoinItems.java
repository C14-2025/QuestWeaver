package br.dev.projetoc14.player;

// fazer o import dos itens
import br.dev.projetoc14.items.ClassSelector;
import org.bukkit.entity.Player;

public class PlayerJoinItems {

    /*
        this method is responsible to set the joining items to the player:
        chest: kit selector
        emerald: kits shop
     */

    public static void give(Player player) {

        player.getInventory().setItem(0, ClassSelector.create());

    }

    private PlayerJoinItems() {}
}
