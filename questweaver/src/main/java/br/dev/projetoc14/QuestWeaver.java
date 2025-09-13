package br.dev.projetoc14;

import br.dev.projetoc14.ExperienceSystem.ExperienceSystem;
import br.dev.projetoc14.ExperienceSystem.Texts;
import br.dev.projetoc14.player.PlayerListener;
import br.dev.projetoc14.player.PlayerStatsManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class QuestWeaver extends JavaPlugin {

    @Override
    public void onEnable() {
        // setup inicial do plugin
        Texts.StartupPlugin(); /* fazendo a mensagem inicial do plugin */

        PlayerStatsManager statsManager = new PlayerStatsManager();
        PlayerListener listener = new PlayerListener(statsManager, this);

        getServer().getPluginManager().registerEvents(listener, this);

        // ativando o sistema de experiencia
        Bukkit.getPluginManager().registerEvents(new ExperienceSystem(), this);
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
