package br.dev.projetoc14.commands;


import br.dev.projetoc14.QuestWeaver;
import br.dev.projetoc14.match.StartMatch;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static br.dev.projetoc14.QuestWeaver.getServerName;

public class StartMatchCommand implements CommandExecutor {

    /*
        CommandExecutor to force the match start
        author: sno0s
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        QuestWeaver plugin = (QuestWeaver) QuestWeaver.getInstance();
        StartMatch match = new StartMatch(plugin.getPlayerFileManager(),plugin.getMatchManager() , plugin.getStatsManager(), plugin);

        // verify if sender is not a player, so is the console
        if (!(sender instanceof Player)) {
            Bukkit.getLogger().info(getServerName() + " Console forçou início da partida.");
            match.execute(); // starting match
            return true;
        }

        Player player = (Player) sender; // getting player instance

        // execute match start and say it to the player
        sender.sendMessage(getServerName() + " §eVocê iniciou a partida."); // message to player
        Bukkit.getLogger().info(player.getName() + " iniciou a partida."); // message to console
        match.execute();

        return true;
    }

}
