package br.dev.projetoc14;

import br.dev.projetoc14.ExperienceSystem.ExperienceSystem;
import br.dev.projetoc14.skilltree.Texts;
import br.dev.projetoc14.player.ClassSelectListener;
import br.dev.projetoc14.player.PlayerListener;
import br.dev.projetoc14.player.PlayerStatsManager;
import br.dev.projetoc14.playerData.PlayerDataListener;
import br.dev.projetoc14.quest.utils.QuestBook;
import br.dev.projetoc14.quest.utils.QuestManager;
import br.dev.projetoc14.quest.listeners.MobKillQuestListener;
import br.dev.projetoc14.quest.listeners.PlayerQuestJoinListener;
import br.dev.projetoc14.playerData.PlayerDataManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class QuestWeaver extends JavaPlugin {

    private PlayerStatsManager statsManager;
    private PlayerDataManager dataManager;
    private QuestManager questManager;
    private QuestBook questBook;
    private static FileConfiguration config;
    private static Plugin instance;

    @Override
    public void onEnable() {
        instance = this;
        // Mensagem inicial do plugin
        Texts.StartupPlugin();

        // Inicializa PlayerStatsManager e PlayerDataManager
        this.statsManager = new PlayerStatsManager();
        this.dataManager = new PlayerDataManager(this);

        // get config.yml
        saveDefaultConfig();
        config = getConfig();
        // Inicializa PlayerStatsManager e PlayerDataManager
        this.statsManager = new PlayerStatsManager();
        this.questManager = new QuestManager();
        this.questBook = new QuestBook(questManager);

        // Listener de mecânica (mana, barra, regeneração)
        PlayerListener playerListener = new PlayerListener(statsManager, this);
        getServer().getPluginManager().registerEvents(playerListener, this);

        // Listener de Escolha de Classe
        ClassSelectListener classSelectListener = new ClassSelectListener(statsManager, this);
        getServer().getPluginManager().registerEvents(classSelectListener, this);

        // Listener de persistência JSON
        PlayerDataListener dataListener = new PlayerDataListener(this, statsManager);
        getServer().getPluginManager().registerEvents(dataListener, this);

        // Sistema de experiência
        Bukkit.getPluginManager().registerEvents(new ExperienceSystem(), this);

        PlayerQuestJoinListener questJoinListener = new PlayerQuestJoinListener(questManager);
        getServer().getPluginManager().registerEvents(questJoinListener, this);

        MobKillQuestListener mobKillListener = new MobKillQuestListener(questManager, this);
        getServer().getPluginManager().registerEvents(mobKillListener, this);

        getLogger().info("[QuestWeaver] Plugin iniciado com sucesso!");
    }

    @Override
    public void onDisable() {
        // Qualquer código de desligamento do plugin, se necessário
        getLogger().info("[QuestWeaver] Plugin finalizado!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("help")) {
            if (sender instanceof Player player) {
                player.sendMessage("Apenas testando o método!");
            } else {
                sender.sendMessage("Mensagem indo para o console!");
            }
            return true;
        }

        if (command.getName().equalsIgnoreCase("resetclass")) {
            if (sender instanceof Player player) {
                statsManager.removeStats(player);
                dataManager.deletePlayerData(player);
                player.sendMessage("§aClasse resetada! Relogue para escolher novamente.");
            } else {
                sender.sendMessage("§cEste comando só pode ser usado por jogadores!");
            }
        }

        if (label.equalsIgnoreCase("quests")) {
            if (sender instanceof Player player) {
                questBook.showBook(player);
            } else {
                sender.sendMessage("§cEste comando só pode ser usado por jogadores!");
            }
        }

            return true;
    }

    public static String getServerName(){
        return config.getString("HGconfigs.server-name");
    }

    public static Plugin getInstance() {
        return instance;
    }

    public QuestManager getQuestManager() {
        return questManager;
    }

    public QuestBook getQuestBook() {
        return questBook;
    }
}