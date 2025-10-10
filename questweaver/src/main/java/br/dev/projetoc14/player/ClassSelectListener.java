package br.dev.projetoc14.player;

import br.dev.projetoc14.player.classes.*;

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
        Inventory inventory = Bukkit.createInventory(null, 9, "Escolha sua Classe");

        // Mago
        ItemStack mage = new ItemStack(Material.BLAZE_ROD);
        ItemMeta mMeta = mage.getItemMeta();
        mMeta.setDisplayName(ChatColor.GOLD + "MAGO");
        mMeta.setLore(Arrays.asList("§7Alto poder mágico", "§7Baixa força física"));
        mage.setItemMeta(mMeta);
        inventory.setItem(2, mage);

        // Arqueiro
        ItemStack archer = new ItemStack(Material.BOW);
        ItemMeta aMeta = archer.getItemMeta();
        aMeta.setDisplayName(ChatColor.GOLD + "ARCHER");
        aMeta.setLore(Arrays.asList("§7Alta agilidade", "§7Especialista em ataques à distância"));
        archer.setItemMeta(aMeta);
        inventory.setItem(4, archer);

        // Guerreiro
        ItemStack warrior = new ItemStack(Material.IRON_SWORD);
        ItemMeta wMeta = warrior.getItemMeta();
        wMeta.setDisplayName(ChatColor.GOLD + "WARRIOR");
        wMeta.setLore(Arrays.asList("§7Alta força física", "§7Boa defesa"));
        warrior.setItemMeta(wMeta);
        inventory.setItem(6, warrior);

        player.openInventory(inventory);
        plugin.getLogger().info("[ClassSelect] Inventário aberto para " + player.getName());
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle() == null) return;
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
                player.sendMessage(ChatColor.DARK_PURPLE + "§aVocê escolheu a classe §5Mago§a!");
                player.closeInventory();
            }
            case BOW -> {
                ArcherPlayer archer = new ArcherPlayer(player);
                statsManager.setStats(player, archer.getStats());
                statsManager.applyStats(player);
                statsManager.createManaBar(player);
                player.getInventory().setContents(archer.getStartingEquipment());
                player.sendMessage(ChatColor.GREEN + "Você escolheu a classe Arqueiro!");
                player.closeInventory();
            }
            case IRON_SWORD -> {
                WarriorPlayer warrior = new WarriorPlayer(player);
                statsManager.setStats(player, warrior.getStats());
                statsManager.applyStats(player);
                statsManager.createManaBar(player);
                player.getInventory().setContents(warrior.getStartingEquipment());
                player.sendMessage(ChatColor.RED + "Você escolheu a classe Guerreiro!");
                player.closeInventory();
            }
            default -> {
                player.sendMessage(ChatColor.GRAY + "Classe inválida!");
                player.closeInventory();
            }
        }
    }
}