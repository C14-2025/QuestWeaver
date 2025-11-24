package br.dev.projetoc14.quest.assassin;

import br.dev.projetoc14.quest.KillQuest;
import br.dev.projetoc14.quest.utils.QuestCompletedEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * Quest difícil do assassino: Execuções perfeitas sem dano
 */
public class PerfectAssassinationQuest extends KillQuest {

    // Rastreia a saúde inicial do jogador quando começa a quest
    private final Map<UUID, Integer> initialHealth = new HashMap<>();

    // Rastreia creepers que explodiram
    private final Set<UUID> explodedCreepers = new HashSet<>();

    public PerfectAssassinationQuest(Location spawnLocation) {
        super("perfect_assassination_quest",
                "Assassinato Perfeito",
                "Mate 4 creepers sem tomar dano e sem deixá-los explodir",
                350,
                "CREEPER",
                4,
                0,
                spawnLocation,
                List.of(Material.IRON_SWORD, Material.DIAMOND_SWORD, Material.NETHERITE_SWORD));
    }

    @Override
    public void assignToPlayer(Player player) {
        // Salva a saúde inicial
        initialHealth.put(player.getUniqueId(), getCurrentHealth(player));
        super.assignToPlayer(player);
    }

    @Override
    public void updateProgress(Object... params) {
        if (params.length >= 4 &&
                params[0] instanceof String mobType &&
                params[1] instanceof Material weapon &&
                params[2] instanceof Player player &&
                params[3] instanceof LivingEntity victim) {

            // Verifica se já está completa
            if (checkCompletion()) {
                return;
            }

            if (mobType.equalsIgnoreCase(targetMob) && isValidWeapon(weapon)) {
                UUID playerId = player.getUniqueId();

                // Verifica se o jogador tomou dano
                int currentHP = getCurrentHealth(player);
                int startHP = initialHealth.getOrDefault(playerId, currentHP);

                if (currentHP < startHP) {
                    // Perdeu! Tomou dano
                    player.sendMessage("§c✗ FALHOU! Você tomou dano.");
                    player.sendMessage("§cExecuções perfeitas exigem que você não seja atingido.");
                    currentCount = 0; // Reseta progresso
                    initialHealth.put(playerId, currentHP); // Atualiza saúde base
                    return;
                }

                // Verifica se o creeper explodiu
                if (victim instanceof Creeper creeper) {
                    if (explodedCreepers.contains(victim.getUniqueId())) {
                        player.sendMessage("§c✗ O creeper explodiu! Isso não conta.");
                        return;
                    }
                }

                currentCount++;

                // Feedback positivo
                player.sendMessage(String.format("§a✓ Execução Perfeita! §7(%d/4)", currentCount));

                if (currentCount >= 3) {
                    player.sendMessage("§e⚠ Cuidado! Mantenha a distância dos creepers!");
                }

                // Atualiza saúde base para próximo kill
                initialHealth.put(playerId, currentHP);

                if (checkCompletion()) {
                    player.sendMessage("§6✦ §e§lASSASSINATO PERFEITO COMPLETO!");
                    cleanupPlayerData(playerId);
                    QuestCompletedEvent customEvent = new QuestCompletedEvent(player, this);
                    Bukkit.getServer().getPluginManager().callEvent(customEvent);
                }
            }
        }
    }

    public void onPlayerDamaged(Player player) {
        UUID playerId = player.getUniqueId();

        if (currentCount > 0) {
            player.sendMessage("§c✗ Você tomou dano! Quest resetada.");
            player.sendMessage("§7Você precisa completar sem ser atingido.");
            currentCount = 0;
        }

        // Atualiza a saúde base
        initialHealth.put(playerId, getCurrentHealth(player));
    }

    public void onCreeperExplode(Creeper creeper) {
        explodedCreepers.add(creeper.getUniqueId());
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
        return String.format("%d/%d execuções perfeitas", currentCount, targetCount);
    }
}