package br.dev.projetoc14.player;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

// Import de classes:
import br.dev.projetoc14.player.classes.*;

import java.util.Arrays;

public class ClassSelectListener implements Listener {

    private final PlayerStatsManager statsManager;
    private final JavaPlugin plugin;

    public ClassSelectListener(PlayerStatsManager statsManager, JavaPlugin plugin) {
        this.statsManager = statsManager;
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        plugin.getLogger().info("[ClassSelect] Jogador " + player.getName() + " entrou");
        plugin.getLogger().info("[ClassSelect] hasStats? " + statsManager.hasStats(player));

        if (!statsManager.hasStats(player)) {
            plugin.getLogger().info("[ClassSelect] Abrindo inventário de classe para " + player.getName());
            openClassInventory(player);
        } else {
            plugin.getLogger().info("[ClassSelect] Jogador já tem stats, pulando seleção");
        }
    }

    private void openClassInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 27, "Escolha sua Classe");

        // Mago
        ItemStack mage = new ItemStack(Material.BLAZE_ROD);
        ItemMeta mMeta = mage.getItemMeta();
        mMeta.setDisplayName(ChatColor.DARK_PURPLE + "MAGO");
        mMeta.setLore(Arrays.asList(
                ChatColor.GRAY + "Alto poder mágico",
                ChatColor.GRAY + "Baixa força física",
                ChatColor.GRAY + "Especialista em feitiços"
        ));
        mage.setItemMeta(mMeta);
        inventory.setItem(1, mage);

        // Arqueiro - Slot 12
        ItemStack archer = new ItemStack(Material.BOW);
        ItemMeta aMeta = archer.getItemMeta();
        aMeta.setDisplayName(ChatColor.GREEN + "ARQUEIRO");
        aMeta.setLore(Arrays.asList(
                ChatColor.GRAY + "Alta agilidade",
                ChatColor.GRAY + "Especialista em ataques à distância",
                ChatColor.GRAY + "Precisão letal"
        ));
        archer.setItemMeta(aMeta);
        inventory.setItem(3, archer);

        // Guerreiro - Slot 14
        ItemStack warrior = new ItemStack(Material.IRON_AXE);
        ItemMeta wMeta = warrior.getItemMeta();
        wMeta.setDisplayName(ChatColor.RED + "GUERREIRO");
        wMeta.setLore(Arrays.asList(
                ChatColor.GRAY + "Alta força física",
                ChatColor.GRAY + "Boa defesa",
                ChatColor.GRAY + "Combate corpo a corpo"
        ));
        warrior.setItemMeta(wMeta);
        inventory.setItem(5, warrior);

        // Assassino - Slot 16
        ItemStack assassin = new ItemStack(Material.IRON_SWORD);
        ItemMeta assMeta = assassin.getItemMeta();
        assMeta.setDisplayName(ChatColor.DARK_GRAY + "ASSASSINO");
        assMeta.setLore(Arrays.asList(
                ChatColor.GRAY + "Velocidade extrema",
                ChatColor.GRAY + "Dano crítico elevado",
                ChatColor.GRAY + "Mestre da furtividade"
        ));
        assassin.setItemMeta(assMeta);
        inventory.setItem(7, assassin);

        player.openInventory(inventory);
        plugin.getLogger().info("[ClassSelect] Inventário aberto para " + player.getName());
    }

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
                MagePlayer mage = new MagePlayer(player);
                statsManager.setStats(player, mage.getStats());
                statsManager.applyStats(player);
                statsManager.createManaBar(player);
                player.getInventory().setContents(mage.getStartingEquipment());
                player.sendMessage(ChatColor.DARK_PURPLE + "Você escolheu a classe " + ChatColor.BOLD + "Mago" + ChatColor.DARK_PURPLE + "!");
                player.closeInventory();
            }
            case BOW -> {
                ArcherPlayer archer = new ArcherPlayer(player);
                statsManager.setStats(player, archer.getStats());
                statsManager.applyStats(player);
                statsManager.createManaBar(player);
                player.getInventory().setContents(archer.getStartingEquipment());
                player.sendMessage(ChatColor.GREEN + "Você escolheu a classe " + ChatColor.BOLD + "Arqueiro" + ChatColor.GREEN + "!");
                player.closeInventory();
            }
            case IRON_AXE -> {
                WarriorPlayer warrior = new WarriorPlayer(player);
                statsManager.setStats(player, warrior.getStats());
                statsManager.applyStats(player);
                statsManager.createManaBar(player);
                player.getInventory().setContents(warrior.getStartingEquipment());
                player.sendMessage(ChatColor.RED + "Você escolheu a classe " + ChatColor.BOLD + "Guerreiro" + ChatColor.RED + "!");
                player.closeInventory();
            }
            case IRON_SWORD -> {
                AssassinPlayer assassin = new AssassinPlayer(player);
                statsManager.setStats(player, assassin.getStats());
                statsManager.applyStats(player);
                statsManager.createManaBar(player);
                player.getInventory().setContents(assassin.getStartingEquipment());
                player.sendMessage(ChatColor.DARK_GRAY + "Você escolheu a classe " + ChatColor.BOLD + "Assassino" + ChatColor.DARK_GRAY + "!");
                player.closeInventory();
            }
            default -> {
                player.sendMessage(ChatColor.GRAY + "Classe inválida!");
                player.closeInventory();
            }
        }
    }
}