package br.dev.projetoc14;

import br.dev.projetoc14.commands.HelpCommand;
import br.dev.projetoc14.commands.QuestsCommand;
//import br.dev.projetoc14.items.ItemProtectionListener;
import br.dev.projetoc14.items.SkillTree;
import br.dev.projetoc14.match.*;
import br.dev.projetoc14.player.abilities.CooldownListener;
import br.dev.projetoc14.player.abilities.CooldownManager;
import br.dev.projetoc14.player.abilities.warriorSkills.CrimsonBladeListener;
import br.dev.projetoc14.player.listeners.*;
import br.dev.projetoc14.player.abilities.archerSkills.ArchListener;
import br.dev.projetoc14.player.abilities.assassinSkills.AbilityListener;
import br.dev.projetoc14.quest.archer.RangedCombatQuestListener;
import br.dev.projetoc14.quest.listeners.QuestCompletionListener;
import br.dev.projetoc14.skilltree.ExperienceSystem;
import br.dev.projetoc14.match.ClassReadyManager;
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
import br.dev.projetoc14.quest.listeners.QuestBookInteractListener;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
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
    private QuestBook questBook;
    private static FileConfiguration config;
    private static Plugin instance;
    private PlayerFileManager playerFileManager;
    private final Map<UUID, RPGPlayer> rpgPlayers = new HashMap<>();
    private QuestManager questmanager;
    private final MatchManager matchManager = new MatchManager();
    private CooldownManager cooldownManager;

    @Override
    public void onEnable() {
        instance = this;
        ClassReadyManager readyManager = new ClassReadyManager((QuestWeaver) instance);
        playerFileManager = new PlayerFileManager(this);
        // Mensagem inicial do plugin
        Texts.StartupPlugin();

        // get config.yml
        saveDefaultConfig();
        config = getConfig();

        // Inicializa PlayerStatsManager e PlayerDataManager
        PlayerDataManager dataManager = new PlayerDataManager(this);
        this.statsManager = new PlayerStatsManager();
        this.questmanager = new QuestManager();
        this.questBook = new QuestBook(questmanager);

        // player join & disconnect listener
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(playerFileManager,(QuestWeaver) instance), this);
        Bukkit.getPluginManager().registerEvents(new PlayerDisconnectListener(playerFileManager, statsManager, dataManager), this);
        Bukkit.getPluginManager().registerEvents(new BlockBreak(playerFileManager, (QuestWeaver) instance), this);

        // Listener de mecânica (mana, barra, regeneração)
        PlayerListener playerListener = new PlayerListener(statsManager, this);
        getServer().getPluginManager().registerEvents(playerListener, this);

        // Listener de Escolha de Classe
        ClassSelectListener classSelectListener = new ClassSelectListener(statsManager, playerFileManager, (JavaPlugin) instance, readyManager, questmanager);
        getServer().getPluginManager().registerEvents(classSelectListener, this);

        // Listener de persistência JSON
        PlayerDataListener dataListener = new PlayerDataListener(this, statsManager);
        getServer().getPluginManager().registerEvents(dataListener, this);

        // Sistema de experiência
        Bukkit.getPluginManager().registerEvents(new ExperienceSystem(), this);

        //Listeners das quests começa aqui
        PlayerQuestJoinListener questJoinListener = new PlayerQuestJoinListener(questmanager);
        getServer().getPluginManager().registerEvents(questJoinListener, this);
        getServer().getPluginManager().registerEvents(new QuestCompletionListener(), this);

        MobKillQuestListener mobKillListener = new MobKillQuestListener(questmanager);
        getServer().getPluginManager().registerEvents(mobKillListener, this);

        QuestBookInteractListener bookListener = new QuestBookInteractListener(questmanager);
        getServer().getPluginManager().registerEvents(bookListener, this);
        //Termina aqui

        // Magic Wand Listener (Habilidades de Mago)
        MagicWandListener magicWandListener = new MagicWandListener(this);
        getServer().getPluginManager().registerEvents(magicWandListener, this);

        // Crimson Blade Listener (Habilidades de Guerreiro)
        CrimsonBladeListener crimsonBladeListener = new CrimsonBladeListener(this);
        getServer().getPluginManager().registerEvents(crimsonBladeListener, this);

        // No Hunger & No Durability
        getServer().getPluginManager().registerEvents(new HungerDurabilityListener(), this);
        getLogger().info("QuestWeaver enabled — hunger and durability disabled!");

        // Cooldoown Bar
        CooldownListener cooldownListener = new CooldownListener(cooldownManager);
        getServer().getPluginManager().registerEvents(cooldownListener, this);

        // Archer listener (habilidades do arqueiro)
        ArchListener archListener = new ArchListener(this);
        getServer().getPluginManager().registerEvents(archListener, this);
        //Listener da quest do archer
        RangedCombatQuestListener rangedCombatQuestListener = new RangedCombatQuestListener(new QuestManager());
        getServer().getPluginManager().registerEvents(rangedCombatQuestListener, this);

        // Assassin listener
        AbilityListener assassinlistener = new AbilityListener(this);
        getServer().getPluginManager().registerEvents(assassinlistener, this);

        // skill tree listener
        getServer().getPluginManager().registerEvents(new SkillTree(playerFileManager), this);

        // item drop protection listener
        //getServer().getPluginManager().registerEvents(new ItemProtectionListener(this), this);

        // death listener
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(playerFileManager, this), this);

        // finalização da partida
        Bukkit.getPluginManager().registerEvents(new EndMatch(this, matchManager), this);

        // ativação dos comandos
        getCommand("quests").setExecutor(new QuestsCommand(questBook));
        getCommand("help").setExecutor(new HelpCommand());

        // desabilita as conquistas no mundo
        for (World w : Bukkit.getWorlds()) {
            w.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
        }

        getLogger().info("[QuestWeaver] Plugin iniciado com sucesso!");

        setMatchRunning(false);
    }

    @Override
    public void onDisable() {
        // Qualquer código de desligamento do plugin, se necessário
        // Para todas as regenerações de mana ativas
        if (statsManager != null) {
            statsManager.stopAllRegeneration();
            getLogger().info("[QuestWeaver] Regeneração de mana finalizada para todos os jogadores.");
        }

        if (cooldownManager != null) {
            cooldownManager.cleanupAll();
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (statsManager != null) {
                statsManager.removeManaBar(player);
            }
        }
        getLogger().info("[QuestWeaver] Plugin finalizado!");
    }


    public static String getServerName(){
        return config.getString("server-conf.server-name");
    }

    public static Boolean isMatchRunning(){
        return config.getBoolean("match-conf.isMatch-running");
    }
    public static void setMatchRunning(boolean value) {
        config.set("match-conf.isMatch-running", value);
        QuestWeaver.getInstance().saveConfig();
    }



    public static Plugin getInstance() {
        return instance;
    }

    public QuestManager getQuestManager() {
        return questmanager;
    }

    public QuestBook getQuestBook() {
        return questBook;
    }

    public PlayerFileManager getPlayerFileManager() { return playerFileManager; }

    public RPGPlayer getRPGPlayer(Player player) {
        return rpgPlayers.get(player.getUniqueId());
    }

    public PlayerStatsManager getStatsManager() {
        return  statsManager;
    }

    public void addRPGPlayer(@NotNull UUID uniqueId, RPGPlayer rpgPlayer) {
        this.rpgPlayers.put(uniqueId, rpgPlayer);
    }

    public MatchManager getMatchManager() {
        return matchManager;
    }

    public CooldownManager getCooldownListener() {
        return cooldownManager;
    }
}