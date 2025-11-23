package br.dev.projetoc14.items;

import br.dev.projetoc14.QuestWeaver;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class ItemProtectionUtil {

    private static final NamespacedKey UNDROPPABLE_KEY = new NamespacedKey(QuestWeaver.getInstance(), "undroppable");

    /**
     * Marca um item como não-dropável (undroppable)
     * @param item O item a ser protegido
     * @return O item protegido (mesma instancia, modificada)
     */
    public static ItemStack makeUndroppable(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) {
            return item;
        }

        // Garante que o item tenha ItemMeta
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            // Cria um ItemMeta baseado no tipo do item
            ItemStack tempItem = new ItemStack(item.getType());
            meta = tempItem.getItemMeta();
            if (meta == null) return item; // Se ainda for null, nao pode ser protegido
        }

        // Adiciona a key de protecao
        meta.getPersistentDataContainer().set(UNDROPPABLE_KEY, PersistentDataType.BYTE, (byte) 1);
        item.setItemMeta(meta);

        return item;
    }



    /**
     * Marca multiplos itens como nao-dropaveis
     *
     * @param items Itens a serem protegidos
     * @return Os itens protegidos (mesma instancia, modificada)
     */
    public static ItemStack[] makeUndroppable(ItemStack... items) {
        for (ItemStack item : items) {
            makeUndroppable(item);
        }
        return items;
    }

    /**
     * Verifica se um item e nao-dropavel
     * @param item O item a ser verificado
     * @return true se o item for protegido, false caso contrario
     */
    public static boolean isUndroppable(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return false;
        }

        return item.getItemMeta().getPersistentDataContainer().has(UNDROPPABLE_KEY, PersistentDataType.BYTE);
    }

    /**
     * Remove a protecao de um item
     * @param item O item a ter a protecao removida
     * @return O item sem protecao
     */
    public static ItemStack makeDroppable(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return item;
        }

        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().remove(UNDROPPABLE_KEY);
        item.setItemMeta(meta);

        return item;
    }

    /**
     * Obtém a key usada para protecao (util para outros listeners)
     * @return A NamespacedKey de proteção
     */
    public static NamespacedKey getUndroppableKey() {
        return UNDROPPABLE_KEY;
    }
}