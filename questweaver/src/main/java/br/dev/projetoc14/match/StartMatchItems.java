package br.dev.projetoc14.match;

import br.dev.projetoc14.QuestWeaver;
import br.dev.projetoc14.player.PlayerStatsManager;
import br.dev.projetoc14.player.classes.ArcherPlayer;
import br.dev.projetoc14.player.classes.AssassinPlayer;
import br.dev.projetoc14.player.classes.MagePlayer;
import br.dev.projetoc14.player.classes.WarriorPlayer;
import br.dev.projetoc14.player.RPGPlayer;
import org.bukkit.entity.Player;

public class StartMatchItems {

    private final PlayerFileManager fileManager;
    private final PlayerStatsManager statsManager;
    private final QuestWeaver plugin;

    public StartMatchItems(PlayerFileManager fileManager, PlayerStatsManager statsManager, QuestWeaver plugin) {
        this.fileManager = fileManager;
        this.statsManager = statsManager;
        this.plugin = plugin; // Salva a referência
    }

    public void setItems(Player player) {
        RPGPlayer rpgPlayer = null;

        // 1. Instancia o objeto RPGPlayer
        switch(fileManager.getPlayerClassName(player))
        {
            case "Mago" -> {
                rpgPlayer = new MagePlayer(player);
            }
            case "Arqueiro" -> {
                rpgPlayer = new ArcherPlayer(player);
            }
            case "Guerreiro" -> {
                rpgPlayer = new WarriorPlayer(player);
            }
            case "Assassino" -> {
                rpgPlayer = new AssassinPlayer(player);
            }
            default -> {
                player.sendMessage("§cErro: Sua classe não foi reconhecida. Contate um administrador.");
                return;
            }
        }

        // 2. Verifica e registra o jogador
        if (rpgPlayer != null) {
            plugin.addRPGPlayer(player.getUniqueId(), rpgPlayer);

            // 3. Aplica as Stats e Itens
            statsManager.setStats(player, rpgPlayer.getStats());
            statsManager.applyStats(player);
            statsManager.createManaBar(player);
            player.getInventory().setContents(rpgPlayer.getStartingEquipment());
        }
    }
}