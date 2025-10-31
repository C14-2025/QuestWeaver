package br.dev.projetoc14.player;

import br.dev.projetoc14.match.ClassReadyManager;
import br.dev.projetoc14.match.PlayerFileManager;
import br.dev.projetoc14.quest.utils.QuestManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

// Import de classes:

import java.util.Arrays;

public class ClassSelectListener implements Listener {

    private final PlayerStatsManager statsManager;
    private final JavaPlugin plugin;
    private final PlayerFileManager fileManager;
    private final ClassReadyManager readyManager;
    private final QuestManager questManager;


    public ClassSelectListener(PlayerStatsManager statsManager, PlayerFileManager fileManager, JavaPlugin plugin, ClassReadyManager readyManager, QuestManager questManager) {
        this.statsManager = statsManager;
        this.fileManager = fileManager;
        this.plugin = plugin;
        this.readyManager = readyManager;
        this.questManager = questManager;
    }

    /**
     *
     * @param event
     * only runs the envent if the item is o seletor de classe
     */
    @EventHandler
    public void onJoin(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item == null || !item.hasItemMeta() || !item.getItemMeta().hasDisplayName())
            return;

        if (!item.getItemMeta().getDisplayName().equalsIgnoreCase("§eSeletor de Classe"))
            return;

        plugin.getLogger().info("[ClassSelect] Jogador " + player.getName() + " entrou");
        plugin.getLogger().info("[ClassSelect] hasStats? " + statsManager.hasStats(player));

        if (!statsManager.hasStats(player)) {
            plugin.getLogger().info("[ClassSelect] Abrindo inventário de classe para " + player.getName());
            openClassInventory(player);
        } else {
            plugin.getLogger().info("[ClassSelect] Jogador já tem stats, pulando seleção");
        }

    }

    /**
     *
     * @param player
     * lógica do inventário de seleção de classe
     */
    private void openClassInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 27, "Escolha sua Classe");

        ItemStack mage = new ItemStack(Material.BLAZE_ROD);
        ItemMeta mMeta = mage.getItemMeta();
        mMeta.setDisplayName(ChatColor.DARK_PURPLE + "MAGO");
        mMeta.setLore(Arrays.asList(
                ChatColor.GRAY + "Alto poder mágico",
                ChatColor.GRAY + "Baixa força física",
                ChatColor.GRAY + "Especialista em feitiços"
        ));
        mage.setItemMeta(mMeta);
        inventory.setItem(10, mage);

        ItemStack archer = new ItemStack(Material.BOW);
        ItemMeta aMeta = archer.getItemMeta();
        aMeta.setDisplayName(ChatColor.GREEN + "ARQUEIRO");
        aMeta.setLore(Arrays.asList(
                ChatColor.GRAY + "Alta agilidade",
                ChatColor.GRAY + "Especialista em ataques à distância",
                ChatColor.GRAY + "Precisão letal"
        ));
        archer.setItemMeta(aMeta);
        inventory.setItem(12, archer);

        ItemStack warrior = new ItemStack(Material.IRON_AXE);
        ItemMeta wMeta = warrior.getItemMeta();
        wMeta.setDisplayName(ChatColor.RED + "GUERREIRO");
        wMeta.setLore(Arrays.asList(
                ChatColor.GRAY + "Alta força física",
                ChatColor.GRAY + "Boa defesa",
                ChatColor.GRAY + "Combate corpo a corpo"
        ));
        warrior.setItemMeta(wMeta);
        inventory.setItem(14, warrior);

        ItemStack assassin = new ItemStack(Material.IRON_SWORD);
        ItemMeta assMeta = assassin.getItemMeta();
        assMeta.setDisplayName(ChatColor.DARK_GRAY + "ASSASSINO");
        assMeta.setLore(Arrays.asList(
                ChatColor.GRAY + "Velocidade extrema",
                ChatColor.GRAY + "Dano crítico elevado",
                ChatColor.GRAY + "Mestre da furtividade"
        ));
        assassin.setItemMeta(assMeta);
        inventory.setItem(16, assassin);

        player.openInventory(inventory);
        plugin.getLogger().info("[ClassSelect] Inventário aberto para " + player.getName());
    }


    /**
     * Mudei o metodo para ele apenas escrever no arquivo do jogador qual classe ele escolheu
     * Agora os itens sao setados quando a partida começa, não imediatamente quando ele seleciona :)
     * @param event
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals("Escolha sua Classe")) return;

        event.setCancelled(true);

        if (!(event.getWhoClicked() instanceof Player)) return;

        Player player = (Player) event.getWhoClicked();
        ItemStack clicked = event.getCurrentItem();

        if (clicked == null || clicked.getType() == Material.AIR) return;

        switch (clicked.getType()) {
            case BLAZE_ROD -> {
                fileManager.setPlayerClass(player, "Mago");
                player.sendMessage(ChatColor.DARK_PURPLE + "Você escolheu a classe " + ChatColor.BOLD + "Mago" + ChatColor.DARK_PURPLE + "!");
                player.closeInventory();
                readyManager.markPlayerReady(player);
                if (!questManager.hasQuests(player)) {
                    //Se o player não tiver nenhuma quest no momento
                    //começa a primeira quest de matar zumbis
                    questManager.createFirstQuest(player);
                }
            }
            case BOW -> {
                fileManager.setPlayerClass(player, "Arqueiro");
                player.sendMessage(ChatColor.GREEN + "Você escolheu a classe " + ChatColor.BOLD + "Arqueiro" + ChatColor.GREEN + "!");
                player.closeInventory();
                readyManager.markPlayerReady(player);
                if (!questManager.hasQuests(player)) {
                    //Se o player não tiver nenhuma quest no momento
                    //começa a primeira quest de matar zumbis
                    questManager.createFirstQuest(player);
                }
            }
            case IRON_AXE -> {
                fileManager.setPlayerClass(player, "Guerreiro");
                player.sendMessage(ChatColor.RED + "Você escolheu a classe " + ChatColor.BOLD + "Guerreiro" + ChatColor.RED + "!");
                player.closeInventory();
                readyManager.markPlayerReady(player);
                if (!questManager.hasQuests(player)) {
                    //Se o player não tiver nenhuma quest no momento
                    //começa a primeira quest de matar zumbis
                    questManager.createFirstQuest(player);
                }
            }
            case IRON_SWORD -> {
                fileManager.setPlayerClass(player, "Assassino");
                player.sendMessage(ChatColor.DARK_GRAY + "Você escolheu a classe " + ChatColor.BOLD + "Assassino" + ChatColor.DARK_GRAY + "!");
                player.closeInventory();
                readyManager.markPlayerReady(player);
                if (!questManager.hasQuests(player)) {
                    //Se o player não tiver nenhuma quest no momento
                    //começa a primeira quest de matar zumbis
                    questManager.createFirstQuest(player);
                }
            }
            default -> {
                player.sendMessage(ChatColor.GRAY + "Classe inválida!");
                player.closeInventory();
            }
        }
    }
}