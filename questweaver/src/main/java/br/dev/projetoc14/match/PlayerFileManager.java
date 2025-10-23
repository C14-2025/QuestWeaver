package br.dev.projetoc14.match;

import br.dev.projetoc14.QuestWeaver;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class PlayerFileManager {

    private final File dataFolder;
    private final QuestWeaver questWeaver;

    /**
     * cria a pasta onde vai ter os yml dos players
     */
    public PlayerFileManager(QuestWeaver plugin) {
        this.questWeaver = plugin;
        this.dataFolder = new File(plugin.getDataFolder(), "playerData");
        if (!dataFolder.exists()) dataFolder.mkdirs();
    }

    /**
     * Metodo responsavel por criar a pasta de arquivo do player caso nao existe
     * @param player
     * @param playerFile
     * @return
     * @throws IOException
     */
    public FileConfiguration setDefaultConfig(Player player, File playerFile) throws IOException {
        playerFile.createNewFile();
        FileConfiguration config = YamlConfiguration.loadConfiguration(playerFile);

        config.set("name", player.getName());
        config.set("kills", 0);
        config.set("class", "Nenhum");

        config.save(playerFile);

        return config;
    }

    /**
     * @param player
     * @return
     * cria um arquivo para cada player usando o uuid do player
     * cria campos neste arquivo para salvar as estatísticas do player
     */
    public FileConfiguration createPlayerFile(Player player) {
        File playerFile = new File(dataFolder, player.getUniqueId() + ".yml");

        if (playerFile.exists()) {
            return YamlConfiguration.loadConfiguration(playerFile);
        }

        try {
            return setDefaultConfig(player, playerFile);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Retorna o arquivo de configuração do jogador.
     * Se o arquivo não existir, ele será criado automaticamente.
     */
    public FileConfiguration getPlayerConfig(Player player) {
        File playerFile = new File(dataFolder, player.getUniqueId() + ".yml");

        if (!playerFile.exists()) {
            try {
                return setDefaultConfig(player, playerFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return YamlConfiguration.loadConfiguration(playerFile);
    }


    /**
     * Incrementa o número de kills de um jogador quando ele mata alguem
     * @param player
     * @throws IOException
     */
    public void addKill(Player player) throws IOException {
        File playerFile = new File(dataFolder, player.getUniqueId() + ".yml");
        FileConfiguration config = getPlayerConfig(player);

        int kills = config.getInt("kills");
        config.set("kills", kills + 1);

        try {
            config.save(playerFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * modifica a classe do jogador
     * @param player
     * @param className
     */
    public void setPlayerClass(Player player, String className) {
        File playerFile = new File(dataFolder, player.getUniqueId() + ".yml");
        FileConfiguration config = getPlayerConfig(player);

        config.set("class", className);

        try {
            config.save(playerFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String getPlayerClassName(Player player) {
        FileConfiguration config = getPlayerConfig(player);

        return config.getString("class");
    }

    public int getPlayerKills(Player player) {
        FileConfiguration config = getPlayerConfig(player);

        return config.getInt("kills");
    }


}
