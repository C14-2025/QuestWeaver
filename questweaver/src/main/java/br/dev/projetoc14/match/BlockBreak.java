package br.dev.projetoc14.match;

import br.dev.projetoc14.QuestWeaver;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreak implements Listener {

    private final PlayerFileManager fileManager;

    public BlockBreak(PlayerFileManager fileManager, QuestWeaver instance) {
        this.fileManager = fileManager;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();

        if (fileManager.isPlayerSpec(player)) {
            event.setCancelled(true);
        }
    }
}
