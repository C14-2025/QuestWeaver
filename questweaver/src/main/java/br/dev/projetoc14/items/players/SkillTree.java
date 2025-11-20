package br.dev.projetoc14.items;

import br.dev.projetoc14.match.PlayerFileManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

import static br.dev.projetoc14.skilltree.inventories.ArcherSkillTree.archerSkillTree;
import static br.dev.projetoc14.skilltree.inventories.AssassinSkillTree.assassinSkillTree;
import static br.dev.projetoc14.skilltree.inventories.MageSkillTree.mageSkillTree;
import static br.dev.projetoc14.skilltree.inventories.WarriorSkillTree.warriorSkillTree;

public class SkillTree implements Listener {

    private static final Material ITEM_MATERIAL = Material.ENCHANTED_BOOK;
    private static final String DISPLAY_NAME = "§eÁrvore de habilidades";
    private static final List<String> LORE = List.of("§7Clique para aumentar o nível de suas habilidades!");
    private PlayerFileManager fileManager;

    /*
        Cria e retorna o item configurado
        DONE: fazer abrir uma tela diferente de habilidades para cada classe
        TODO: change deprecated methods
        TODO: fazer com que o player n possa trocar os itens de lugar dentro da interface
        DONE: mudar a cor das descrições e titulos dos itens
     */

    public static ItemStack create() {
        ItemStack item = new ItemStack(ITEM_MATERIAL);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(DISPLAY_NAME);
            meta.setLore(LORE);
            item.setItemMeta(meta);
        }

        return item;
    }

    public SkillTree(PlayerFileManager fileManager) {
        this.fileManager = fileManager;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equalsIgnoreCase("Árvore de habilidades"))
            return;

        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || !clicked.hasItemMeta()) return;

        ItemMeta meta = clicked.getItemMeta();
        String name = meta.getDisplayName();

        // custo fixo por nível (pode variar depois)
        int cost = 25;
        int playerXP = player.getTotalExperience();

        if (playerXP < cost) {
            player.sendMessage("§cXP insuficiente para melhorar essa habilidade!");
            return;
        }

        List<String> lore = meta.getLore();
        if (lore == null || lore.isEmpty()) return;

        int currentLevel = getCurrentLevel(lore);
        if (currentLevel >= 4) {
            player.sendMessage("§7Essa habilidade já está no nível máximo!");
            return;
        }

        // desconta XP
        player.giveExp(-cost);

        // aumenta nível
        currentLevel++;
        lore.set(0, "§7♦ Nível: " + currentLevel);
        meta.setLore(lore);
        clicked.setItemMeta(meta);

        player.sendMessage("§a" + name + " melhorada para o nível " + currentLevel + "!");
    }

    private int getCurrentLevel(List<String> lore) {
        for (String line : lore) {
            if (line.contains("Nível:")) {
                try {
                    return Integer.parseInt(line.replaceAll("[^0-9]", ""));
                } catch (NumberFormatException ignored) {}
            }
        }
        return 0;
    }


    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (event.getView().getTitle().equalsIgnoreCase("Árvore de Habilidades")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item == null || !item.hasItemMeta() || !item.getItemMeta().hasDisplayName())
            return;

        if (!item.getItemMeta().getDisplayName().equalsIgnoreCase("§eÁrvore de habilidades"))
            return;

        switch(fileManager.getPlayerClassName(player))
        {
            case "Mago" -> {
                mageSkillTree(player);
            }
            case "Arqueiro" -> {
                archerSkillTree(player);
            }
            case "Guerreiro" -> {
                warriorSkillTree(player);
            }
            case "Assassino" -> {
                assassinSkillTree(player);
            }
            default -> {
                player.sendMessage("§cErro: Sua classe não foi reconhecida.");
                return;
            }
        }
    }



}
