package br.dev.projetoc14.items;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class ItemProtectionListener implements Listener {

    public ItemProtectionListener(Plugin plugin) {
        // Não precisa mais armazenar a key, usa a da classe utilitária
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        ItemStack item = event.getItemDrop().getItemStack();

        if (ItemProtectionUtil.isUndroppable(item)) {
            event.setCancelled(true);
            Player player = event.getPlayer();
            player.sendMessage("§cVocê não pode dropar este item!");

            // Debug para verificar se está funcionando
            // player.sendMessage("§7[DEBUG] Item protegido: " + item.getType().name());
        }
    }
}