package br.dev.projetoc14.commands;

import br.dev.projetoc14.QuestWeaver;
import br.dev.projetoc14.quest.utils.QuestBook;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


public class QuestsCommand implements CommandExecutor {

    private final QuestBook questBook;

    public QuestsCommand(QuestBook questBook) {
        this.questBook = questBook;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (command.getName().equalsIgnoreCase("quests")) {
            if (sender instanceof Player player) {
                questBook.showBook(player);
            } else {
                sender.sendMessage("§cEste comando só pode ser usado por jogadores!");
            }
        }
        return true;
    }
}
