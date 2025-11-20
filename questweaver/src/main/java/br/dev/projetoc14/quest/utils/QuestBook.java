package br.dev.projetoc14.quest.utils;

import br.dev.projetoc14.quest.ExplorationQuest;
import br.dev.projetoc14.quest.HitQuest;
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

        // Adiciona TODAS as páginas
        pages.add(createIndexPage());
        pages.add(createActiveQuestsPage(questData));
        pages.add(createCompletedQuestsPage(questData));
        pages.add(createStatsPage(questData));
        pages.add(createCryptSecretsPage());    // Página 5
        pages.add(createCryptSecretsPage2());   // Página 6
        pages.add(createCryptSecretsPage3());   // Página 7
        pages.add(createCryptSecretsPage4());   // Página 8

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
                .append(Component.text("\n"))
                .append(createIndexButton("Segredos da Cripta", 5))
                .append(Component.text("\n\n"))
                .append(Component.text("Dica: Explore cada canto!\n")
                        .color(TextColor.color(0xFFAA00)))
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
                // Suporta todos os tipos de quest
                if (quest instanceof KillQuest killQuest) {
                    page.append(createKillQuestEntry(killQuest));
                } else if (quest instanceof HitQuest hitQuest) {
                    page.append(createHitQuestEntry(hitQuest));
                } else if (quest instanceof ExplorationQuest explorationQuest) {
                    page.append(createExplorationQuestEntry(explorationQuest));
                } else {
                    // Fallback para outras quests
                    page.append(createGenericQuestEntry(quest));
                }
            }
        }

        return page.build();
    }

    private Component createKillQuestEntry(KillQuest quest) {
        return Component.text()
                .append(Component.text(quest.getName() + "\n")
                        .color(TextColor.color(0xFFAA00)))
                .append(Component.text(quest.getDescription() + "\n")
                        .color(TextColor.color(0xAAAAAA)))
                .append(createKillProgressBar(quest))
                .append(Component.text(quest.getProgressText() + "\n\n")
                        .color(TextColor.color(0xAAAAAA)))
                .build();
    }

    private Component createHitQuestEntry(HitQuest quest) {
        return Component.text()
                .append(Component.text(quest.getName() + "\n")
                        .color(TextColor.color(0xFFAA00)))
                .append(Component.text(quest.getDescription() + "\n")
                        .color(TextColor.color(0xAAAAAA)))
                .append(createHitProgressBar(quest))
                .append(Component.text(quest.getProgressText() + "\n\n")
                        .color(TextColor.color(0xAAAAAA)))
                .build();
    }

    private Component createExplorationQuestEntry(ExplorationQuest quest) {
        boolean isCompleted = quest.checkCompletion();

        return Component.text()
                .append(Component.text("✦ " + quest.getName() + "\n")
                        .color(TextColor.color(0xFFAA00))
                        .decoration(TextDecoration.BOLD, true))
                .append(Component.text(quest.getDescription() + "\n")
                        .color(TextColor.color(0xAAAAAA)))
                .append(Component.text(quest.getProgressText() + "\n")
                        .color(isCompleted ? TextColor.color(0x55FF55) : TextColor.color(0xAAAAAA)))
                .append(Component.text("\n"))
                .build();
    }

    private Component createGenericQuestEntry(Quest quest) {
        return Component.text()
                .append(Component.text("• " + quest.getName() + "\n")
                        .color(TextColor.color(0xFFAA00)))
                .append(Component.text(quest.getDescription() + "\n")
                        .color(TextColor.color(0xAAAAAA)))
                .append(Component.text("Status: " + (quest.checkCompletion() ? "Completa" : "Em andamento") + "\n\n")
                        .color(TextColor.color(0xAAAAAA)))
                .build();
    }

    private Component createHitProgressBar(HitQuest quest) {
        int progress = quest.getCurrentCount();
        int total = quest.getTargetCount();

        final String EMPTY = "□";
        final String FILLED = "■";

        int maxBars = 16;
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

    private Component createKillProgressBar(KillQuest quest) {
        int progress = quest.getCurrentCount();
        int total = quest.getTargetCount();

        final String EMPTY = "□";
        final String FILLED = "■";

        int maxBars = 16;
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
                String questSymbol = "✓ ";
                TextColor nameColor = TextColor.color(0x55FF55);

                // Diferentes símbolos para diferentes tipos de quest
                if (quest instanceof ExplorationQuest) {
                    questSymbol = "✦ ";
                    nameColor = TextColor.color(0xFFAA00);
                } else if (quest instanceof KillQuest) {
                    questSymbol = "⚔ ";
                } else if (quest instanceof HitQuest) {
                    questSymbol = "🎯 ";
                }

                page.append(Component.text(questSymbol + quest.getName() + "\n")
                                .color(nameColor))
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

        // Conta tipos de quests completadas
        long explorationQuests = questData.getCompletedQuests().values().stream()
                .filter(q -> q instanceof ExplorationQuest)
                .count();
        long combatQuests = questData.getCompletedQuests().values().stream()
                .filter(q -> q instanceof KillQuest)
                .count();
        long precisionQuests = questData.getCompletedQuests().values().stream()
                .filter(q -> q instanceof HitQuest)
                .count();

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
                .append(Component.text("Explorações: ")
                        .color(TextColor.color(0xAAAAAA))
                        .append(Component.text(explorationQuests + "\n")
                                .color(TextColor.color(0xFFAA00))))
                .append(Component.text("Combates: ")
                        .color(TextColor.color(0xAAAAAA))
                        .append(Component.text(combatQuests + "\n")
                                .color(TextColor.color(0xFF5555))))
                .append(Component.text("Precisão: ")
                        .color(TextColor.color(0xAAAAAA))
                        .append(Component.text(precisionQuests + "\n")
                                .color(TextColor.color(0x55FFFF))))
                .build();
    }

    // PÁGINA 5: Segredos da Cripta - Parte 1
    private Component createCryptSecretsPage() {
        return Component.text()
                .append(Component.text("Segredos da Cripta\n\n")
                        .decoration(TextDecoration.BOLD, true)
                        .decoration(TextDecoration.UNDERLINED, true)
                        .color(TextColor.color(0xAA00AA)))
                .append(Component.text("Tesouros Escondidos:\n\n")
                        .color(TextColor.color(0xFFAA00)))

                // Baú do Assassino
                .append(Component.text("🗡️ Baú do Assassino\n")
                        .color(TextColor.color(0x55FF55)))
                .append(Component.text("Local: Parede do fundo\n")
                        .color(TextColor.color(0xAAAAAA)))
                .append(Component.text("Tesouro: Equipamento completo\n\n")
                        .color(TextColor.color(0xAAAAAA)))

                // Baú de Flechas
                .append(Component.text("🏹 Baú de Flechas\n")
                        .color(TextColor.color(0x55FF55)))
                .append(Component.text("Local: Sob o piso NW\n")
                        .color(TextColor.color(0xAAAAAA)))
                .append(Component.text("Tesouro: Munição variada\n\n")
                        .color(TextColor.color(0xAAAAAA)))

                .append(Component.text("Próxima página →")
                        .color(TextColor.color(0x5555FF))
                        .clickEvent(ClickEvent.changePage(6))
                        .hoverEvent(HoverEvent.showText(Component.text("Clique para continuar")
                                .color(TextColor.color(0xAAAAAA)))))
                .build();
    }

    // PÁGINA 6: Segredos da Cripta - Parte 2
    private Component createCryptSecretsPage2() {
        return Component.text()
                .append(Component.text("Segredos da Cripta\n")
                        .decoration(TextDecoration.BOLD, true)
                        .decoration(TextDecoration.UNDERLINED, true)
                        .color(TextColor.color(0xAA00AA)))
                .append(Component.text("(Página 2)\n\n")
                        .color(TextColor.color(0x555555)))

                // Baú de Ferramentas
                .append(Component.text("🛠️ Baú de Ferramentas\n")
                        .color(TextColor.color(0x55FF55)))
                .append(Component.text("Local: Pilar Noroeste\n")
                        .color(TextColor.color(0xAAAAAA)))
                .append(Component.text("Tesouro: Ferramentas úteis\n\n")
                        .color(TextColor.color(0xAAAAAA)))

                // Baú do Tesouro
                .append(Component.text("💎 Baú do Tesouro\n")
                        .color(TextColor.color(0x55FF55)))
                .append(Component.text("Local: Dentro do Altar\n")
                        .color(TextColor.color(0xAAAAAA)))
                .append(Component.text("Tesouro: Gemas raras\n\n")
                        .color(TextColor.color(0xAAAAAA)))

                .append(Component.text("← Página anterior")
                        .color(TextColor.color(0x5555FF))
                        .clickEvent(ClickEvent.changePage(5))
                        .hoverEvent(HoverEvent.showText(Component.text("Voltar para página 1")
                                .color(TextColor.color(0xAAAAAA)))))
                .append(Component.text(" | "))
                .append(Component.text("Próxima →")
                        .color(TextColor.color(0x5555FF))
                        .clickEvent(ClickEvent.changePage(7))
                        .hoverEvent(HoverEvent.showText(Component.text("Continuar para página 3")
                                .color(TextColor.color(0xAAAAAA)))))
                .build();
    }

    // PÁGINA 7: Segredos da Cripta - Parte 3
    private Component createCryptSecretsPage3() {
        return Component.text()
                .append(Component.text("Segredos da Cripta\n")
                        .decoration(TextDecoration.BOLD, true)
                        .decoration(TextDecoration.UNDERLINED, true)
                        .color(TextColor.color(0xAA00AA)))
                .append(Component.text("(Página 3)\n\n")
                        .color(TextColor.color(0x555555)))

                // Baús dos Sarcófagos
                .append(Component.text("⚰️ Baús dos Sarcófagos\n")
                        .color(TextColor.color(0x55FF55)))
                .append(Component.text("Local: Todos os 4\n")
                        .color(TextColor.color(0xAAAAAA)))
                .append(Component.text("Tesouro: Recursos diversos\n\n")
                        .color(TextColor.color(0xAAAAAA)))

                // Baú de Armas
                .append(Component.text("⚔️ Baú de Armas\n")
                        .color(TextColor.color(0x55FF55)))
                .append(Component.text("Local: Sala secreta Leste\n")
                        .color(TextColor.color(0xAAAAAA)))
                .append(Component.text("Tesouro: Armas poderosas\n\n")
                        .color(TextColor.color(0xAAAAAA)))

                .append(Component.text("← Página anterior")
                        .color(TextColor.color(0x5555FF))
                        .clickEvent(ClickEvent.changePage(6))
                        .hoverEvent(HoverEvent.showText(Component.text("Voltar para página 2")
                                .color(TextColor.color(0xAAAAAA)))))
                .append(Component.text(" | "))
                .append(Component.text("Próxima →")
                        .color(TextColor.color(0x5555FF))
                        .clickEvent(ClickEvent.changePage(8))
                        .hoverEvent(HoverEvent.showText(Component.text("Continuar para página 4")
                                .color(TextColor.color(0xAAAAAA)))))
                .build();
    }

    // PÁGINA 8: Segredos da Cripta - Parte 4
    private Component createCryptSecretsPage4() {
        return Component.text()
                .append(Component.text("Segredos da Cripta\n")
                        .decoration(TextDecoration.BOLD, true)
                        .decoration(TextDecoration.UNDERLINED, true)
                        .color(TextColor.color(0xAA00AA)))
                .append(Component.text("(Página 4)\n\n")
                        .color(TextColor.color(0x555555)))

                // Baú Raro
                .append(Component.text("🌟 Baú Raro\n")
                        .color(TextColor.color(0x55FF55)))
                .append(Component.text("Local: No teto central\n")
                        .color(TextColor.color(0xAAAAAA)))
                .append(Component.text("Tesouro: Itens lendários\n\n")
                        .color(TextColor.color(0xAAAAAA)))

                // Dicas Gerais
                .append(Component.text("💡 Dicas de Exploração:\n")
                        .color(TextColor.color(0xFFAA00)))
                .append(Component.text("• Procure por padrões\n")
                        .color(TextColor.color(0xAAAAAA)))
                .append(Component.text("• Quebre blocos suspeitos\n")
                        .color(TextColor.color(0xAAAAAA)))
                .append(Component.text("• Verifique cada canto\n")
                        .color(TextColor.color(0xAAAAAA)))
                .append(Component.text("• Olhe para cima e baixo\n")
                        .color(TextColor.color(0xAAAAAA)))
                .append(Component.text("• Teste todas as paredes\n\n")
                        .color(TextColor.color(0xAAAAAA)))

                .append(Component.text("← Página anterior")
                        .color(TextColor.color(0x5555FF))
                        .clickEvent(ClickEvent.changePage(7))
                        .hoverEvent(HoverEvent.showText(Component.text("Voltar para página 3")
                                .color(TextColor.color(0xAAAAAA)))))
                .append(Component.text(" | "))
                .append(Component.text("Índice")
                        .color(TextColor.color(0x5555FF))
                        .clickEvent(ClickEvent.changePage(1))
                        .hoverEvent(HoverEvent.showText(Component.text("Voltar ao índice")
                                .color(TextColor.color(0xAAAAAA)))))
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
                BOOK_TITLE.equals(((net.kyori.adventure.text.TextComponent) meta.title()).content());
    }
}