package br.dev.projetoc14.player;

// Classe essencial para linkar stats com o jogo


import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;



// Mapeamento de cada player para seus respectivos atributos:

public class PlayerStatsManager {
    private final HashMap<UUID, PlayerStats> statsMap = new HashMap<>();
    private final HashMap<UUID, BossBar> manaBars = new HashMap<>();

    // Retorna os stats de um player e cria um padrão se não existir(computeIfAbsent)
    public PlayerStats getStats(@NotNull Player player) {
        return statsMap.computeIfAbsent(player.getUniqueId(), uuid -> new PlayerStats());
    }

    // Aplica os atributos armazenados em PlayerStats diretamente no Player real do Minecraft
    public void applyStats(@NotNull Player player) {
        PlayerStats stats = getStats(player);

    // Manager de vida:

    player.getAttribute(Attribute.MAX_HEALTH).setBaseValue(stats.getHealth());
    double currentHealth = Math.min(player.getHealth(), stats.getHealth());
    player.setHealth(currentHealth);

    // Manager de dano físco:

    player.getAttribute(Attribute.ATTACK_DAMAGE).setBaseValue(stats.getStrength());

    // Manager de defesa/armadura:

    player.getAttribute(Attribute.ARMOR).setBaseValue(stats.getDefense());

    // Manager de agilidade/velocidade de movimento:

    double baseSpeed = 0.1; // default do Minecraft
        player.getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue(baseSpeed + (stats.getAgility() * 0.001));
    }

    // Manager de criação de barra de mana:

    public void createManaBar(@NotNull Player player) {
        BossBar bar = Bukkit.createBossBar("Mana", BarColor.PURPLE, BarStyle.SEGMENTED_10);
        bar.addPlayer(player);
        manaBars.put(player.getUniqueId(), bar);
        updateManaBar(player);
    }

    // Manager de update de barra de mana:

    public void updateManaBar(@NotNull Player player) {
        PlayerStats stats = getStats(player);
        BossBar bar = manaBars.get(player.getUniqueId());

        if (bar != null) {
            bar.setProgress(stats.getManaPercentage());
            bar.setTitle("Mana " + stats.getCurrentMana() + "/" + stats.getMana());
        }
    }

    // Remove barra (ex: ao sair do servidor)
    public void removeManaBar(Player player) {
        BossBar bar = manaBars.remove(player.getUniqueId());
        if (bar != null) {
            bar.removeAll();
        }
    }

}
