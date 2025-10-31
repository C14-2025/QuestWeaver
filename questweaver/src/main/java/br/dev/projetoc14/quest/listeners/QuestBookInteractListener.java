package br.dev.projetoc14.quest.listeners;

import br.dev.projetoc14.quest.utils.QuestBook;
import br.dev.projetoc14.quest.utils.QuestManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public class QuestBookInteractListener implements Listener {

    private final QuestManager questManager;

    public QuestBookInteractListener(QuestManager questManager) {
        this.questManager = questManager;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        // Verifica se é um livro
        if (item.getType() != Material.WRITTEN_BOOK) {
            return;
        }

        // Verifica se é o Livro de Quests
        BookMeta meta = (BookMeta) item.getItemMeta();
        if (meta == null || meta.title() == null) {
            return;
        }

        // Extrai o texto do título (remove formatação)
        String title = ((net.kyori.adventure.text.TextComponent) meta.title()).content();

        if (title.contains("Livro de Quests")) {
            // Cancela a abertura normal do livro
            event.setCancelled(true);

            // Abre o livro interativo com todas as quests
            QuestBook questBook = new QuestBook(questManager);
            questBook.showBook(player);
        }
    }
}