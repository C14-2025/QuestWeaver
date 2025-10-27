package br.dev.projetoc14.match;

import br.dev.projetoc14.player.PlayerStatsManager;
import br.dev.projetoc14.player.classes.ArcherPlayer;
import br.dev.projetoc14.player.classes.AssassinPlayer;
import br.dev.projetoc14.player.classes.MagePlayer;
import br.dev.projetoc14.player.classes.WarriorPlayer;
import org.bukkit.entity.Player;



public class StartMatchItems {

    private final PlayerFileManager fileManager;
    private final PlayerStatsManager statsManager;


    public StartMatchItems(PlayerFileManager fileManager, PlayerStatsManager statsManager) {
        this.fileManager = fileManager;
        this.statsManager = statsManager;
    }

    public void setItems(Player player)
    {
        switch(fileManager.getPlayerClassName(player))
        {
            case "Mago" -> {

                MagePlayer mage = new MagePlayer(player);
                statsManager.setStats(player, mage.getStats());
                statsManager.applyStats(player);
                statsManager.createManaBar(player);
                player.getInventory().setContents(mage.getStartingEquipment());

            }
            case "Arqueiro" -> {

                ArcherPlayer archer = new ArcherPlayer(player);
                statsManager.setStats(player, archer.getStats());
                statsManager.applyStats(player);
                statsManager.createManaBar(player);
                player.getInventory().setContents(archer.getStartingEquipment());
            }
            case "Guerreiro" -> {

                WarriorPlayer warrior = new WarriorPlayer(player);
                statsManager.setStats(player, warrior.getStats());
                statsManager.applyStats(player);
                statsManager.createManaBar(player);
                player.getInventory().setContents(warrior.getStartingEquipment());
            }
            case "Assassino" -> {

                AssassinPlayer assassin = new AssassinPlayer(player);
                statsManager.setStats(player, assassin.getStats());
                statsManager.applyStats(player);
                statsManager.createManaBar(player);
                player.getInventory().setContents(assassin.getStartingEquipment());
            }
        }


    }
}
