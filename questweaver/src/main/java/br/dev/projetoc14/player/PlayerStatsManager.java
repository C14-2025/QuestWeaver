package br.dev.projetoc14.player;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

public class PlayerStatsManager {
    private final HashMap<UUID, PlayerStats> statsMap = new HashMap<>();
    private final HashMap<UUID, BossBar> manaBars = new HashMap<>();
    private final HashMap<UUID, Integer> regenTasks = new HashMap<>();

    // Configurações de regeneração
    private static final int REGEN_INTERVAL_TICKS = 40; // 2 segundos (20 ticks = 1 segundo)
    private static final int BASE_MANA_REGEN = 2; // Mana regenerada por tick

    // Construtor padrão mantido para compatibilidade
    public PlayerStatsManager() {
    }

    // Retorna os stats de um player e cria um padrão se não existir
    public PlayerStats getStats(@NotNull Player player) {
        return statsMap.computeIfAbsent(player.getUniqueId(), uuid -> new PlayerStats());
    }

    // Aplica os atributos armazenados em PlayerStats diretamente no Player real do Minecraft
    public void applyStats(@NotNull Player player) {
        PlayerStats stats = getStats(player);

        // Manager de vida:
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(stats.getHealth());
        double currentHealth = Math.min(player.getHealth(), stats.getHealth());
        player.setHealth(currentHealth);

        // Manager de dano físico:
        player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(stats.getStrength());

        // Manager de defesa/armadura:
        player.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(stats.getDefense());

        // Manager de agilidade/velocidade de movimento:
        double baseSpeed = 0.1; // default do Minecraft
        player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(baseSpeed + (stats.getAgility() * 0.001));
    }

    public boolean hasStats(Player player) {
        return statsMap.containsKey(player.getUniqueId());
    }

    // Manager de criação de barra de mana:
    public void createManaBar(@NotNull Player player) {
        BossBar bar = Bukkit.createBossBar("Mana", BarColor.PURPLE, BarStyle.SEGMENTED_10);
        bar.addPlayer(player);
        manaBars.put(player.getUniqueId(), bar);
        updateManaBar(player);

        // Inicia a regeneração de mana automaticamente
        startManaRegeneration(player);
    }

    // Manager de update de barra de mana:
    public void updateManaBar(@NotNull Player player) {
        PlayerStats stats = getStats(player);
        BossBar bar = manaBars.get(player.getUniqueId());

        if (bar != null) {
            bar.setProgress(stats.getManaPercentage());
            bar.setTitle("§d✨ Mana §f" + stats.getCurrentMana() + "§7/§f" + stats.getMana());
        }
    }

    // Remove barra (ex: ao sair do servidor)
    public void removeManaBar(Player player) {
        BossBar bar = manaBars.remove(player.getUniqueId());
        if (bar != null) {
            bar.removeAll();
        }

        // Para a regeneração quando remove a barra
        stopManaRegeneration(player);
    }

    public void setStats(@NotNull Player player, @NotNull PlayerStats stats) {
        statsMap.put(player.getUniqueId(), stats);

        // Aplica imediatamente os stats carregados no Player
        applyStats(player);

        // Atualiza barra de mana se já existir
        if (manaBars.containsKey(player.getUniqueId())) {
            updateManaBar(player);
        }
    }

    public void removeStats(Player player) {
        UUID uuid = player.getUniqueId();

        // Remove do HashMap em memória
        statsMap.remove(uuid);

        // Remove a barra de mana (boss bar)
        if (manaBars.containsKey(uuid)) {
            BossBar manaBar = manaBars.get(uuid);
            manaBar.removePlayer(player);
            manaBars.remove(uuid);
        }

        // Cancela a task de regeneração se existir
        stopManaRegeneration(player);

        // Reseta os atributos do jogador para valores padrão
        player.setMaxHealth(20.0);
        player.setHealth(20.0);
        player.setLevel(0);
        player.setExp(0);

        // Limpa o inventário
        player.getInventory().clear();

        player.sendMessage(ChatColor.YELLOW + "Seus stats foram resetados!");
    }

    // ========== SISTEMA DE REGENERAÇÃO DE MANA ========== //

    /**
     * Inicia a regeneração automática de mana para um jogador
     * Usa o plugin instance global do Bukkit
     */
    public void startManaRegeneration(@NotNull Player player) {
        UUID uuid = player.getUniqueId();

        // Se já existe uma task de regen, cancela antes
        stopManaRegeneration(player);

        Plugin plugin = Bukkit.getPluginManager().getPlugin("QuestWeaver");
        if (plugin == null || !plugin.isEnabled()) {
            return;
        }

        // Cria uma task que roda repetidamente
        int taskId = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (!player.isOnline()) {
                stopManaRegeneration(player);
                return;
            }

            PlayerStats stats = getStats(player);

            // Só regenera se não estiver com mana cheia
            if (stats.getCurrentMana() < stats.getMana()) {
                // Calcula regeneração baseada na inteligência
                int regenAmount = calculateManaRegen(stats);

                // Restaura a mana
                stats.restoreMana(regenAmount);

                // Atualiza a barra
                updateManaBar(player);
            }
        }, REGEN_INTERVAL_TICKS, REGEN_INTERVAL_TICKS).getTaskId();

        regenTasks.put(uuid, taskId);
    }

    /**
     * Para a regeneração de mana de um jogador
     */
    public void stopManaRegeneration(@NotNull Player player) {
        UUID uuid = player.getUniqueId();

        if (regenTasks.containsKey(uuid)) {
            Bukkit.getScheduler().cancelTask(regenTasks.get(uuid));
            regenTasks.remove(uuid);
        }
    }

    /**
     * Pausa temporariamente a regeneração (útil durante combate)
     */
    public void pauseManaRegeneration(@NotNull Player player) {
        stopManaRegeneration(player);
    }

    /**
     * Retoma a regeneração
     */
    public void resumeManaRegeneration(@NotNull Player player) {
        startManaRegeneration(player);
    }

    /**
     * Calcula quanto de mana deve ser regenerado baseado nos stats
     * Jogadores com mais inteligência regeneram mais mana
     */
    private int calculateManaRegen(PlayerStats stats) {
        // Fórmula: Base + (Inteligência / 10) --- Regen Inteligente
        // Exemplo: Com 18 de int = 2 + 1 = 3 de mana por tick
        int intelligenceBonus = stats.getIntelligence() / 10;
        return BASE_MANA_REGEN + intelligenceBonus;
    }

    /**
     * Para todas as regenerações ativas - útil ao desabilitar o plugin
     */
    public void stopAllRegeneration() {
        regenTasks.values().forEach(taskId -> Bukkit.getScheduler().cancelTask(taskId));
        regenTasks.clear();
    }

    /**
     * Verifica se um jogador tem regeneração ativa
     */
    public boolean hasActiveRegeneration(@NotNull Player player) {
        return regenTasks.containsKey(player.getUniqueId());
    }
}