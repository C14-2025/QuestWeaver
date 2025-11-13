package br.dev.projetoc14.quest.utils;

import br.dev.projetoc14.quest.KillQuest;
import br.dev.projetoc14.quest.Quest;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QuestBook {
    private final QuestManager questManager;
    private static final String BOOK_TITLE = "Livro de Quests";

    public QuestBook(QuestManager questManager) {
        this.questManager = questManager;
    }

    public void showBook(Player player) {
        PlayerQuestData questData = questManager.getPlayerQuests(player);
        if (questData == null) {
            player.sendMessage(Component.text("Você não tem quests disponíveis.")
                    .color(TextColor.color(0xFF5555)));
            return;
        }

        ItemStack book = createQuestBook(questData);
        player.openBook(book);
    }

    private ItemStack createQuestBook(PlayerQuestData questData) {
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) book.getItemMeta();

        meta.title(Component.text(BOOK_TITLE));
        meta.author(Component.text("QuestWeaver"));

        List<Component> pages = new ArrayList<>();

        // Adiciona as páginas
        pages.add(createIndexPage());
        pages.add(createActiveQuestsPage(questData));
        pages.add(createCompletedQuestsPage(questData));
        pages.add(createStatsPage(questData));

        meta.pages(pages);
        book.setItemMeta(meta);
        return book;
    }

    private Component createIndexPage() {
        return Component.text()
                .append(Component.text("Livro de Quests\n\n")
                        .decoration(TextDecoration.BOLD, true)
                        .decoration(TextDecoration.UNDERLINED, true))
                .append(Component.text("Índice:\n\n"))
                .append(createIndexButton("Quests Ativas", 2))
                .append(Component.text("\n"))
                .append(createIndexButton("Quests Completadas", 3))
                .append(Component.text("\n"))
                .append(createIndexButton("Estatísticas", 4))
                .build();
    }

    private Component createIndexButton(String text, int page) {
        return Component.text("▶ " + text)
                .color(TextColor.color(0x5555FF))
                .clickEvent(ClickEvent.changePage(page))
                .hoverEvent(HoverEvent.showText(Component.text("Clique para ir para " + text)
                        .color(TextColor.color(0xAAAAAA))));
    }

    private Component createActiveQuestsPage(PlayerQuestData questData) {
        TextComponent.Builder page = Component.text()
                .append(Component.text("Quests Ativas\n\n")
                        .decoration(TextDecoration.BOLD, true)
                        .decoration(TextDecoration.UNDERLINED, true));

        Map<String, Quest> activeQuests = questData.getActiveQuests();

        if (activeQuests.isEmpty()) {
            page.append(Component.text("Nenhuma quest ativa no momento.")
                    .color(TextColor.color(0x555555)));
        } else {
            for (Quest quest : activeQuests.values()) {
                // Suporta qualquer KillQuest (incluindo RangedCombatQuest)
                if (quest instanceof KillQuest killQuest) {
                    page.append(createQuestEntry(killQuest));
                }
            }
        }

        return page.build();
    }

    private Component createQuestEntry(KillQuest quest) {
        return Component.text()
                .append(Component.text(quest.getName() + "\n")
                        .color(TextColor.color(0xFFAA00)))
                .append(Component.text(quest.getDescription() + "\n")
                        .color(TextColor.color(0xAAAAAA)))
                .append(createProgressBar(quest))
                .append(Component.text(quest.getProgressText() + "\n\n")
                        .color(TextColor.color(0xAAAAAA)))
                .build();
    }

    private Component createProgressBar(KillQuest quest) {
        int progress = quest.getCurrentCount();
        int total = quest.getTargetCount();

        final String EMPTY = "□";
        final String FILLED = "■";

        // Um livro do Minecraft tem aproximadamente 14-16 caracteres por linha
        int maxBars = 16;

        // Calcula quantas barras devem ser preenchidas
        int filledBars = Math.min(maxBars, (int) Math.round((double) progress / total * maxBars));

        TextComponent.Builder progressBar = Component.text();

        progressBar.append(Component.text(String.format("%d/%d ", progress, total))
                .color(TextColor.color(0xAAAAAA)));

        for (int i = 0; i < maxBars; i++) {
            String symbol = i < filledBars ? FILLED : EMPTY;
            progressBar.append(Component.text(symbol)
                    .color(i < filledBars ? TextColor.color(0x55FF55) : TextColor.color(0x555555)));
        }

        int percentage = (int) ((double) progress / total * 100);
        progressBar.append(Component.text(String.format(" %d%%", percentage))
                .color(TextColor.color(0xAAAAAA)));

        progressBar.append(Component.text("\n"));

        return progressBar.build();
    }

    private Component createCompletedQuestsPage(PlayerQuestData questData) {
        TextComponent.Builder page = Component.text()
                .append(Component.text("Quests Completadas\n\n")
                        .decoration(TextDecoration.BOLD, true)
                        .decoration(TextDecoration.UNDERLINED, true));

        Map<String, Quest> completedQuests = questData.getCompletedQuests();

        if (completedQuests.isEmpty()) {
            page.append(Component.text("Nenhuma quest completada ainda.")
                    .color(TextColor.color(0x555555)));
        } else {
            for (Quest quest : completedQuests.values()) {
                page.append(Component.text("✓ " + quest.getName() + "\n")
                                .color(TextColor.color(0x55FF55)))
                        .append(Component.text(quest.getDescription() + "\n")
                                .color(TextColor.color(0xAAAAAA)))
                        .append(Component.text("XP: +" + quest.getExperienceReward() + "\n\n")
                                .color(TextColor.color(0xFFAA00)));
            }
        }

        return page.build();
    }

    private Component createStatsPage(PlayerQuestData questData) {
        int totalQuests = questData.getActiveQuests().size() + questData.getCompletedQuests().size();
        int completedQuests = questData.getCompletedQuests().size();
        int totalXP = questData.getCompletedQuests().values().stream()
                .mapToInt(Quest::getExperienceReward)
                .sum();

        return Component.text()
                .append(Component.text("Estatísticas\n\n")
                        .decoration(TextDecoration.BOLD, true)
                        .decoration(TextDecoration.UNDERLINED, true))
                .append(Component.text("Quests Totais: ")
                        .color(TextColor.color(0xAAAAAA))
                        .append(Component.text(totalQuests + "\n")
                                .color(TextColor.color(0xFFFFFF))))
                .append(Component.text("Quests Completadas: ")
                        .color(TextColor.color(0xAAAAAA))
                        .append(Component.text(completedQuests + "\n")
                                .color(TextColor.color(0x55FF55))))
                .append(Component.text("XP Total Ganho: ")
                        .color(TextColor.color(0xAAAAAA))
                        .append(Component.text(totalXP + "\n")
                                .color(TextColor.color(0xFFAA00))))
                .append(Component.text("\nMais estatísticas em breve...")
                        .color(TextColor.color(0x555555)))
                .build();
    }

    public void updateBook(Player player) {
        PlayerQuestData questData = questManager.getPlayerQuests(player);
        if (questData == null) return;

        for (ItemStack item : player.getInventory().getContents()) {
            if (isQuestBook(item)) {
                ItemStack newBook = createQuestBook(questData);
                item.setItemMeta(newBook.getItemMeta());
                break;
            }
        }
    }

    private boolean isQuestBook(ItemStack item) {
        if (item == null || item.getType() != Material.WRITTEN_BOOK) return false;
        BookMeta meta = (BookMeta) item.getItemMeta();
        return meta != null && meta.title() != null &&
                BOOK_TITLE.equals(meta.title().toString());
    }
}