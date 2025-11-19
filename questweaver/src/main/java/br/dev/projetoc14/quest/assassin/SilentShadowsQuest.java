package br.dev.projetoc14.quest.assassin;

import br.dev.projetoc14.quest.KillQuest;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.List;

/**
 * Quest fácil do assassino: Backstabs (ataque pelas costas)
 */
public class SilentShadowsQuest extends KillQuest {

    public SilentShadowsQuest(Location spawnLocation) {
        super("silent_shadows_quest",
                "Sombras Silenciosas",
                "Mate 6 zumbis atacando pelas costas (backstab)",
                100,
                "ZOMBIE",
                1,
                0,
                spawnLocation,
                List.of(Material.IRON_SWORD, Material.DIAMOND_SWORD, Material.NETHERITE_SWORD));
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

            // Verifica se é backstab (ataque pelas costas)
            if (mobType.equalsIgnoreCase(targetMob) &&
                    isValidWeapon(weapon) &&
                    isBackstab(player, victim)) {

                currentCount++;

                if (checkCompletion()) {
                    br.dev.projetoc14.quest.utils.QuestCompletedEvent customEvent =
                            new br.dev.projetoc14.quest.utils.QuestCompletedEvent(player, this);
                    org.bukkit.Bukkit.getServer().getPluginManager().callEvent(customEvent);
                }
            }
        }
    }

    /**
     * Verifica se o ataque foi pelas costas
     * Compara a direção que o mob está olhando com a direção do ataque
     */
    private boolean isBackstab(Player attacker, LivingEntity victim) {
        // Direção que o mob está olhando
        Vector mobDirection = victim.getLocation().getDirection().normalize();

        // Direção do atacante para o mob
        Vector attackDirection = victim.getLocation().toVector()
                .subtract(attacker.getLocation().toVector())
                .normalize();

        // Produto escalar: se > 0.5, está atacando por trás
        double dotProduct = mobDirection.dot(attackDirection);

        return dotProduct > 0.5;
    }

    @Override
    public String getProgressText() {
        return String.format("%d/%d backstabs executados", currentCount, targetCount);
    }
}