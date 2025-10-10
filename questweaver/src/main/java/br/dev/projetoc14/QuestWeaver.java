package br.dev.projetoc14;

import br.dev.projetoc14.commands.StartMatchCommand;
import br.dev.projetoc14.skilltree.ExperienceSystem;
import br.dev.projetoc14.skilltree.Texts;
import br.dev.projetoc14.player.PlayerListener;
import br.dev.projetoc14.player.PlayerStatsManager;
import br.dev.projetoc14.playerData.PlayerDataListener;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class QuestWeaver extends JavaPlugin {

    private static FileConfiguration config;
    private static QuestWeaver instance;

    public static Plugin getInstance() { return instance; }

    @Override
    public void onEnable() {
        instance = this;
        // Mensagem inicial do plugin
        Texts.StartupPlugin();

        // Inicializa PlayerStatsManager
        PlayerStatsManager statsManager = new PlayerStatsManager();

        // get config.yml
        saveDefaultConfig();
        config = getConfig();

        // Listener de mecânica (mana, barra, regeneração)
        PlayerListener playerListener = new PlayerListener(statsManager, this);
        getServer().getPluginManager().registerEvents(playerListener, this);

        // Listener de persistência JSON
        PlayerDataListener dataListener = new PlayerDataListener(this, statsManager);
        getServer().getPluginManager().registerEvents(dataListener, this);

        // Sistema de experiência
        Bukkit.getPluginManager().registerEvents(new ExperienceSystem(), this);

        // Comandos
        getCommand("startmatch").setExecutor(new StartMatchCommand());

        getLogger().info("[QuestWeaver] Plugin iniciado com sucesso!");
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("[QuestWeaver] Plugin finalizado!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(label.equalsIgnoreCase("help")) {
            if(sender instanceof Player player){
                player.sendMessage("Apenas testando o método!");
            } else {
                sender.sendMessage("Mensagem indo para o console!");
            }
        }

        return true;
    }

    public static String getServerName(){
        return config.getString("HGconfigs.server-name");

    }
}
