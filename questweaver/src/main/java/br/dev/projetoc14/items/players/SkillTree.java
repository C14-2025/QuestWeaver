package br.dev.projetoc14.items.players;

import br.dev.projetoc14.QuestWeaver;
import br.dev.projetoc14.items.ItemProtectionUtil;
import br.dev.projetoc14.match.PlayerFileManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

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

    public static ItemStack create() {
        ItemStack item = new ItemStack(ITEM_MATERIAL);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(DISPLAY_NAME);
            meta.setLore(LORE);
            item.setItemMeta(meta);
        }

        // Protege o item
        return ItemProtectionUtil.makeUndroppable(item);
    }

    private final Plugin plugin;

    public SkillTree(PlayerFileManager fileManager) {
        this.fileManager = fileManager;
        this.plugin = QuestWeaver.getInstance();
    }

    private int getSkillLevel(Player player, String skillName) {
        if (player.hasMetadata(skillName)) {
            return player.getMetadata(skillName).get(0).asInt();
        }
        return 0;
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

        int cost = 25;
        int playerXP = 500;
        // TODO: int playerXP = player.getTotalExperience();

        if (playerXP < cost) {
            player.sendMessage("§cXP insuficiente para melhorar essa habilidade!");
            return;
        }

        List<String> lore = meta.getLore();
        if (lore == null || lore.isEmpty()) return;

        int currentLevel = getSkillLevel(player, name);

        if (currentLevel >= 5) {
            player.sendMessage("§7Essa habilidade já está no nível máximo!");
            return;
        }

        player.giveExp(-cost);

        currentLevel++;
        lore.set(0, "§7♦ Nível: " + currentLevel);
        meta.setLore(lore);
        clicked.setItemMeta(meta);

        player.setMetadata(name, new FixedMetadataValue(plugin, currentLevel));

        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
        player.sendMessage("§e[Skills] §a" + name + " agora está no nível §e" + currentLevel + "!");

        if (name.toLowerCase().contains("armadura")) {
            applyArmorUpgrade(player, currentLevel);
        }
        if (name.toLowerCase().contains("vida")) {
            applyHealthUpgrade(player, currentLevel);
        }

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

    @EventHandler(priority = EventPriority.LOW)
    public void onClick(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR &&
                event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item == null || !item.hasItemMeta() || !item.getItemMeta().hasDisplayName())
            return;

        if (!item.getItemMeta().getDisplayName().equalsIgnoreCase("§eÁrvore de habilidades"))
            return;

        // Verifica se o item e protegido
        if (!ItemProtectionUtil.isUndroppable(item)) {
            return;
        }

        // Cancela o evento para evitar conflitos
        event.setCancelled(true);

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
            }
        }
    }

    public void applyArmorUpgrade(Player player, int level) {

        Material[][] armorTiers = {
                { Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS },
                { Material.IRON_HELMET, Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS, Material.IRON_BOOTS },
                { Material.GOLDEN_HELMET, Material.GOLDEN_CHESTPLATE, Material.GOLDEN_LEGGINGS, Material.GOLDEN_BOOTS },
                { Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS },
                { Material.NETHERITE_HELMET, Material.NETHERITE_CHESTPLATE, Material.NETHERITE_LEGGINGS, Material.NETHERITE_BOOTS }
        };

        if (level < 0) level = 0;
        if (level > 4) level = 4;

        Material[] selectedTier = armorTiers[level];

        ItemStack helmet = new ItemStack(selectedTier[0]);
        ItemStack chest  = new ItemStack(selectedTier[1]);
        ItemStack legs   = new ItemStack(selectedTier[2]);
        ItemStack boots  = new ItemStack(selectedTier[3]);

        ItemStack[] pieces = {helmet, chest, legs, boots};

        for (ItemStack piece : pieces) {
            ItemMeta meta = piece.getItemMeta();
            if (meta != null) {
                meta.setUnbreakable(true);
                meta.setDisplayName("§aArmadura Nível " + level);
                piece.setItemMeta(meta);
            }
        }

        player.getInventory().setHelmet(pieces[0]);
        player.getInventory().setChestplate(pieces[1]);
        player.getInventory().setLeggings(pieces[2]);
        player.getInventory().setBoots(pieces[3]);

        player.playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_DIAMOND, 1f, 1f);
        player.sendMessage("§6[Upgrade] §fSua armadura evoluiu para §e" + selectedTier[1].name());
    }

    private double getBaseClassHealth(Player player) {
        if (player.hasMetadata("base_class_health")) {
            return player.getMetadata("base_class_health").get(0).asDouble();
        }
        return player.getAttribute(Attribute.MAX_HEALTH).getBaseValue();
    }


    public void applyHealthUpgrade(Player player, int level) {
        double base = getBaseClassHealth(player);
        double bonus = level * 2.0;
        double total = base + bonus;

        player.getAttribute(Attribute.MAX_HEALTH).setBaseValue(total);

        // Atualiza a vida na tick seguinte (importante!)
        Bukkit.getScheduler().runTask(plugin, () -> {
            player.setHealth(player.getAttribute(Attribute.MAX_HEALTH).getBaseValue());
        });

        player.sendMessage("§6[Upgrade] §fSua vida aumentou em §c+1 coração!");
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
    }

}

