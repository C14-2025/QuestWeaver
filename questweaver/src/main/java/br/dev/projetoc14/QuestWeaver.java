package br.dev.projetoc14;

import br.dev.projetoc14.ExperienceSystem.ExperienceSystem;
import br.dev.projetoc14.commands.HelpCommand;
import br.dev.projetoc14.commands.QuestsCommand;
import br.dev.projetoc14.match.PlayerFileManager;
import br.dev.projetoc14.player.*;
import br.dev.projetoc14.player.abilities.mageSkills.MagicWandListener;
import br.dev.projetoc14.skilltree.Texts;
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
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class QuestWeaver extends JavaPlugin {

    private PlayerStatsManager statsManager;
    private PlayerDataManager dataManager;
    private QuestManager questManager;
    private QuestBook questBook;
    private static FileConfiguration config;
    private static Plugin instance;
    private PlayerFileManager playerFileManager;
    private final Map<UUID, RPGPlayer> rpgPlayers = new HashMap<>();



    @Override
    public void onEnable() {
        instance = this;
        playerFileManager = new PlayerFileManager(this);
        // Mensagem inicial do plugin
        Texts.StartupPlugin();

        // get config.yml
        saveDefaultConfig();
        config = getConfig();

        // Inicializa PlayerStatsManager e PlayerDataManager
        this.dataManager = new PlayerDataManager(this);
        this.statsManager = new PlayerStatsManager();
        this.questManager = new QuestManager();
        this.questBook = new QuestBook(questManager);




        // player join & disconnect listener
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerDisconnectListener(playerFileManager, statsManager, dataManager), this);

        // Listener de mecânica (mana, barra, regeneração)
        PlayerListener playerListener = new PlayerListener(statsManager, this);
        getServer().getPluginManager().registerEvents(playerListener, this);

        // Listener de Escolha de Classe
        ClassSelectListener classSelectListener = new ClassSelectListener(statsManager, playerFileManager, this);
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

        // Magic Wand Listener (Habilidades de Mago)
        MagicWandListener magicWandListener = new MagicWandListener(this);
        getServer().getPluginManager().registerEvents(magicWandListener, this);

        // ativação dos comandos
        getCommand("quests").setExecutor(new QuestsCommand(questBook));
        getCommand("help").setExecutor(new HelpCommand());

        getLogger().info("[QuestWeaver] Plugin iniciado com sucesso!");
    }

    @Override
    public void onDisable() {
        // Qualquer código de desligamento do plugin, se necessário
        getLogger().info("[QuestWeaver] Plugin finalizado!");
    }



    public static String getServerName(){
        return config.getString("server-conf.server-name");
    }

    public static Plugin getInstance() {
        return instance;
    }

    public QuestBook getQuestBook() {
        return questBook;
    }

    public PlayerFileManager getPlayerFileManager() { return playerFileManager; }

    public RPGPlayer getRPGPlayer(Player player) {
        return rpgPlayers.get(player.getUniqueId());
    }


}