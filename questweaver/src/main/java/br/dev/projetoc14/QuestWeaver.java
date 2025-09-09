package br.dev.projetoc14;

import br.dev.projetoc14.ExperienceSystem.Texts;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class QuestWeaver extends JavaPlugin {

    @Override
    public void onEnable() {
        // setup inicial do plugin
        Texts.StartupPlugin(); /* fazendo a mensagem inicial do plugin */

    }

    @Override
    public void onDisable() {
        // lógica de quando o plugin é desligado
        Bukkit.getLogger().info("Plugin finalizado!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        /* Comando /help */
        if(label.equalsIgnoreCase("help")) {
            if(sender instanceof Player player){
                player.sendMessage("Apenas testando o método!!!!!");
            } else {
                sender.sendMessage("Mensagem indo para o console!!!!");
            }

        }


        return true;
    }
}
