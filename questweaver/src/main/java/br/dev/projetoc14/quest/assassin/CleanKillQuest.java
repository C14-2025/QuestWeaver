package br.dev.projetoc14.quest.assassin;

import br.dev.projetoc14.quest.KillQuest;
import br.dev.projetoc14.quest.utils.QuestCompletedEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Quest FÁCIL do assassino: Matar sem tomar dano
 */
public class CleanKillQuest extends KillQuest {

    private final Map<UUID, Integer> initialHealth = new HashMap<>();

    public CleanKillQuest(Location spawnLocation) {
        super("clean_kill_quest",
                "Assassinato Limpo",
                "Mate 3 zumbis sem tomar dano",
                120,
                "ZOMBIE",
                3,
                0,
                spawnLocation,
                List.of(Material.WOODEN_SWORD, Material.STONE_SWORD, Material.IRON_SWORD));
    }

    @Override
    public void assignToPlayer(Player player) {
        // Registra a saúde inicial
        initialHealth.put(player.getUniqueId(), getCurrentHealth(player));

        player.sendMessage("§a🎯 Missão: Assassinato Limpo");
        player.sendMessage("§7" + getDescription());
        player.sendMessage("§e💡 Dica: Use movimentos rápidos e evite ataques!");

        super.assignToPlayer(player);
    }

    @Override
    public void updateProgress(Object... params) {
        if (params.length >= 3 &&
                params[0] instanceof String mobType &&
                params[1] instanceof Material weapon &&
                params[2] instanceof Player player) {

            if (checkCompletion()) return;

            if (mobType.equalsIgnoreCase(targetMob) && isValidWeapon(weapon)) {
                UUID playerId = player.getUniqueId();

                // Verifica se o jogador tomou dano
                int currentHP = getCurrentHealth(player);
                int startHP = initialHealth.getOrDefault(playerId, currentHP);

                if (currentHP < startHP) {
                    // Tomou dano - não conta
                    player.sendMessage("§c✗ Você tomou dano! Este kill não conta.");

                    // Atualiza a saúde base para a próxima tentativa
                    initialHealth.put(playerId, currentHP);
                    return;
                }

                // Kill limpo - conta!
                currentCount++;

                // Feedback positivo
                switch (currentCount) {
                    case 1:
                        player.sendMessage("§a✓ Primeiro assassinato limpo!");
                        break;
                    case 2:
                        player.sendMessage("§a✓ Dois kills perfeitos!");
                        break;
                    case 3:
                        player.sendMessage("§a✓ Terceiro kill limpo!");
                        break;
                }

                player.sendMessage("§a📊 " + getProgressText());

                if (checkCompletion()) {
                    player.sendMessage("§6🎉 §e§lASSASSINATO LIMPO COMPLETO!");
                    player.sendMessage("§6Você dominou o básico do combate furtivo!");

                    cleanupPlayerData(playerId);
                    QuestCompletedEvent customEvent = new QuestCompletedEvent(player, this);
                    Bukkit.getServer().getPluginManager().callEvent(customEvent);
                }
            }
        }
    }

    /**
     * Chamado quando o jogador toma dano (para feedback imediato)
     */
    public void onPlayerDamaged(Player player) {
        UUID playerId = player.getUniqueId();

        // Apenas dá feedback, não reseta o progresso
        if (currentCount > 0 && currentCount < targetCount) {
            player.sendMessage("§c💥 Você foi atingido! Cuidado com os próximos ataques.");
        }

        // Atualiza a saúde base
        initialHealth.put(playerId, getCurrentHealth(player));
    }

    private int getCurrentHealth(Player player) {
        // Tenta pegar do RPGPlayer, senão usa HP do Minecraft
        try {
            br.dev.projetoc14.QuestWeaver plugin = (br.dev.projetoc14.QuestWeaver) br.dev.projetoc14.QuestWeaver.getInstance();
            if (plugin != null) {
                br.dev.projetoc14.player.RPGPlayer rpgPlayer = plugin.getRPGPlayer(player);
                if (rpgPlayer != null) {
                    return rpgPlayer.getCurrentHealth();
                }
            }
        } catch (Exception e) {
            // Fallback para sistema vanilla
        }

        return (int) player.getHealth();
    }

    private void cleanupPlayerData(UUID playerId) {
        initialHealth.remove(playerId);
    }

    @Override
    public String getProgressText() {
        return String.format("%d/%d kills limpos (sem dano)", currentCount, targetCount);
    }
}