package br.dev.projetoc14.skilltree;

import br.dev.projetoc14.skilltree.ExperienceSystem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class SkillManager implements Listener {
    private final JavaPlugin plugin;
    private final ExperienceSystem xpSystem;
    private final NamespacedKey skillKey;
    private final Map<UUID, PlayerSkillTree> trees = new HashMap<>();
    private final Map<UUID, String> playerClass = new HashMap<>(); // persistir em arquivo
    private final File dataFile;
    private final FileConfiguration data;


    public SkillManager(JavaPlugin plugin, ExperienceSystem xpSystem) {
        this.plugin = plugin;
        this.xpSystem = xpSystem;
        this.skillKey = new NamespacedKey(plugin, "skill_id");
        this.dataFile = new File(plugin.getDataFolder(), "playerskills.yml");
        if (!plugin.getDataFolder().exists()) plugin.getDataFolder().mkdirs();
        this.data = YamlConfiguration.loadConfiguration(dataFile);
        loadAll();
    }

    public int getXP(Player player) {
        return player.getTotalExperience();
    }


    // chama quando o jogador clica no item inicial
    @EventHandler
    public void onInteract(PlayerInteractEvent ev) {
        if (ev.getAction() == org.bukkit.event.block.Action.RIGHT_CLICK_AIR ||
                ev.getAction() == org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK) {

            if (ev.getItem() == null) return;
            ItemMeta meta = ev.getItem().getItemMeta();
            if (meta == null || !meta.hasDisplayName()) return;

            if (ChatColor.stripColor(meta.getDisplayName()).equalsIgnoreCase("Livro de Habilidades")) {
                ev.setCancelled(true);
                openSkillGui(ev.getPlayer());
            }
        }
    }

    // detecta clique na GUI
    @EventHandler
    public void onInvClick(InventoryClickEvent ev) {
        if (!(ev.getWhoClicked() instanceof Player)) return;
        Player p = (Player) ev.getWhoClicked();
        if (!ev.getView().getTitle().equals("Árvore de Habilidades")) return;

        ev.setCancelled(true);
        if (ev.getClick() != ClickType.LEFT && ev.getClick() != ClickType.RIGHT) return;

        ItemStack clicked = ev.getCurrentItem();
        if (clicked == null || !clicked.hasItemMeta()) return;
        ItemMeta meta = clicked.getItemMeta();
        String skillId = meta.getPersistentDataContainer().get(skillKey, PersistentDataType.STRING);
        if (skillId == null) return;

        PlayerSkillTree pst = trees.get(p.getUniqueId());
        if (pst == null) return;

        int cost = pst.nextLevelCost(skillId);
        if (cost == Integer.MAX_VALUE) {
            p.sendMessage("§cNível máximo atingido ou skill inválida.");
            return;
        }

        int playerLevels = p.getLevel(); // usa levels do player (conforme ExperienceSystem do time)
        if (playerLevels < cost) {
            p.sendMessage("§cVocê precisa de " + cost + " níveis (você tem " + playerLevels + ").");
            return;
        }

        // OBS: se der problema no servidor usar -> p.setLevel(Math.max(0, p.getLevel() - cost));
        p.giveExpLevels(-cost);

        // só atualiza o modelo localmente
        boolean incrementou = pst.incrementLevel(skillId);
        if (incrementou) {
            SkillDefinition def = pst.getDefinitions().get(skillId);
            p.sendMessage("§aUpou " + def.getDisplayName() + " para o nível " + pst.getLevel(skillId) + "!");
            ev.getInventory().setItem(ev.getSlot(), createSkillItem(def, pst.getLevel(skillId)));
            savePlayer(p.getUniqueId()); // persiste alteração
        } else {
            p.sendMessage("§cErro: não foi possível upar (já no máximo?).");
        }
    }


    @EventHandler
    public void onJoin(PlayerJoinEvent ev) {
        Player p = ev.getPlayer();
        // criar árvore com base na classe (se não tiver, setar default)
        String cls = playerClass.getOrDefault(p.getUniqueId(), "guerreiro");
        playerClass.putIfAbsent(p.getUniqueId(), cls);
        Map<String, SkillDefinition> defs = getDefinitionsForClass(cls);
        PlayerSkillTree pst = new PlayerSkillTree(defs);

        // carregar níveis se existirem no arquivo
        if (data.contains(p.getUniqueId().toString())) {
            Map<String, Object> map = data.getConfigurationSection(p.getUniqueId().toString()).getValues(false);
            if (map.containsKey("skills")) {
                Object raw = map.get("skills");
                if (raw instanceof Map) {
                    //noinspection unchecked
                    pst.deserialize((Map<String, Object>) raw);
                }
            }
        }

        trees.put(p.getUniqueId(), pst);

        // dar o item que abre a GUI
        ItemStack book = new ItemStack(Material.BOOK);
        ItemMeta m = book.getItemMeta();
        m.setDisplayName(ChatColor.GOLD + "Livro de Habilidades");
        book.setItemMeta(m);
        // dar o item na mão do jogador no join se não tiver
        if (p.getInventory().firstEmpty() != -1 && !p.getInventory().contains(book)) {
            p.getInventory().addItem(book);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent ev) {
        savePlayer(ev.getPlayer().getUniqueId());
        trees.remove(ev.getPlayer().getUniqueId());
    }

    public void openSkillGui(Player player) {
        PlayerSkillTree pst = trees.get(player.getUniqueId());
        if (pst == null) return;
        Inventory inv = Bukkit.createInventory(null, 27, "Árvore de Habilidades");
        int slot = 0;
        for (SkillDefinition def : pst.getDefinitions().values()) {
            inv.setItem(slot++, createSkillItem(def, pst.getLevel(def.getId())));
            if (slot >= inv.getSize()) break;
        }
        player.openInventory(inv);
    }

    private ItemStack createSkillItem(SkillDefinition def, int level) {
        ItemStack item = new ItemStack(def.getIcon() == null ? Material.PAPER : def.getIcon());
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + def.getDisplayName());
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Nível: " + level + "/" + def.getMaxLevel());
        lore.add(ChatColor.GRAY + "Custo próximo: " + (level < def.getMaxLevel() ? def.costForLevel(level + 1) + " XP" : "Máximo"));
        meta.setLore(lore);
        meta.getPersistentDataContainer().set(skillKey, PersistentDataType.STRING, def.getId());
        item.setItemMeta(meta);
        return item;
    }

    private Map<String, SkillDefinition> getDefinitionsForClass(String cls) {
        switch (cls.toLowerCase()) {
            case "mago": return SkillTreeFactory.criarMago();
            case "arqueiro": return SkillTreeFactory.criarArqueiro();
            default: return SkillTreeFactory.criarGuerreiro();
        }
    }

    public void saveAll() {
        for (UUID id : trees.keySet()) savePlayer(id);
        try {
            data.save(dataFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Erro salvando playerskills.yml");
            e.printStackTrace();
        }
    }

    public void loadAll() {
        if (!dataFile.exists()) return;

        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(dataFile);

        for (String key : cfg.getKeys(false)) {
            UUID id;
            try {
                id = UUID.fromString(key);
            } catch (IllegalArgumentException ex) {
                plugin.getLogger().warning("UUID inválido em playerskills.yml: " + key);
                continue;
            }

            String cls = cfg.getString(key + ".class", "guerreiro");
            playerClass.put(id, cls);

            Map<String, SkillDefinition> defs = getDefinitionsForClass(cls);
            PlayerSkillTree pst = new PlayerSkillTree(defs);

            if (cfg.contains(key + ".skills")) {
                ConfigurationSection sec = cfg.getConfigurationSection(key + ".skills");
                for (String skillId : sec.getKeys(false)) {
                    int lvl = sec.getInt(skillId, 0);
                    pst.setLevel(skillId, lvl);
                }
            }

            trees.put(id, pst);
        }
    }

    private void savePlayer(UUID id) {
        PlayerSkillTree pst = trees.get(id);
        if (pst == null) return;

        String base = id.toString();
        data.set(base + ".class", playerClass.getOrDefault(id, "guerreiro"));
        data.set(base + ".skills", pst.serialize()); // YAML aceita Map direto

        try {
            data.save(dataFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Erro salvando playerskills.yml");
            e.printStackTrace();
        }
    }


    // metodo utilitário para setar classe via comando
    public void setPlayerClass(UUID uuid, String cls) {
        playerClass.put(uuid, cls.toLowerCase());
    }
}
