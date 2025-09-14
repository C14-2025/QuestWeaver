package br.dev.projetoc14.playerData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import br.dev.projetoc14.player.PlayerStats;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class PlayerDataManager {
    private final File dataFolder;
    private final Gson gson;
    private final JavaPlugin plugin;

    public PlayerDataManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.dataFolder = new File(plugin.getDataFolder(), "playerdata");

        if (!dataFolder.exists()) {
            if (dataFolder.mkdirs()) {
                plugin.getLogger().info("[PlayerData] Pasta 'playerdata' criada com sucesso.");
            } else {
                plugin.getLogger().warning("[PlayerData] Falha ao criar a pasta 'playerdata'. Verifique permissoes!");
            }
        }

        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    private File getPlayerFile(Player player) {
        return new File(dataFolder, player.getUniqueId().toString() + ".json");
    }

    // Salvar stats
    public void savePlayerStats(Player player, PlayerStats stats) {
        File file = getPlayerFile(player);
        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(stats, writer);
            plugin.getLogger().fine("[PlayerData] Stats salvos para " + player.getName());
        } catch (IOException e) {
            plugin.getLogger().severe("[PlayerData] Erro ao salvar stats de " + player.getName() + ": " + e.getMessage());
        }
    }

    // Carregar stats
    public PlayerStats loadPlayerStats(Player player) {
        File file = getPlayerFile(player);
        if (!file.exists()) {
            plugin.getLogger().info("[PlayerData] Nenhum arquivo encontrado para " + player.getName() + ", criando novo.");
            return new PlayerStats();
        }

        try (FileReader reader = new FileReader(file)) {
            PlayerStats stats = gson.fromJson(reader, PlayerStats.class);
            plugin.getLogger().info("[PlayerData] Stats carregados para " + player.getName());
            return stats != null ? stats : new PlayerStats();
        } catch (IOException e) {
            plugin.getLogger().severe("[PlayerData] Erro ao carregar stats de " + player.getName() + ": " + e.getMessage());
            return new PlayerStats();
        }
    }
}
