package br.dev.projetoc14.items;

import br.dev.projetoc14.QuestWeaver;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class ItemRegistry implements Item{

    public static final NamespacedKey ID_CHAVE = new NamespacedKey("questweaver", "item_id");
    private final QuestWeaver plugin;

    public ItemRegistry(QuestWeaver plugin) {
        this.plugin = plugin;
    }

    public ItemStack create() {
        ItemStack item = new ItemStack(Material.IRON_SWORD);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName("§8Punhal Sombrio");
        meta.setCustomModelData(1001);
        meta.getPersistentDataContainer().set(ID_CHAVE, PersistentDataType.STRING, "dagger");

        item.setItemMeta(meta);
        return item;
    }

    public boolean ehAdagaSombria(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        String id = item.getItemMeta().getPersistentDataContainer()
                .get(ID_CHAVE, PersistentDataType.STRING);
        return "dagger".equalsIgnoreCase(id);
    }
}
