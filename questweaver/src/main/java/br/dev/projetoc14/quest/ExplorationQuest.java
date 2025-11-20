package br.dev.projetoc14.quest;

import br.dev.projetoc14.quest.structures.QuestStructure;
import br.dev.projetoc14.quest.utils.QuestCompletedEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Quest de exploração - encontrar uma estrutura
 * Primeira quest de cada linha que desbloqueia as próximas
 */
public class ExplorationQuest extends Quest {

    private final QuestStructure structure;
    private final double detectionRadius;
    private boolean found;
    private Location structureLocation;

    public ExplorationQuest(String id, String name, String description,
                            int expReward, QuestStructure structure,
                            double detectionRadius) {
        super(id, name, description, expReward);
        this.structure = structure;
        this.detectionRadius = detectionRadius;
        this.found = false;
    }

    @Override
    public void assignToPlayer(Player player) {
        // Calcula uma localização próxima, mas não muito perto
        Location playerLoc = player.getLocation();
        int randomDistance = 50 + (int)(Math.random() * 50); // 50-100 blocos
        double randomAngle = Math.random() * 2 * Math.PI;

        int offsetX = (int)(Math.cos(randomAngle) * randomDistance);
        int offsetZ = (int)(Math.sin(randomAngle) * randomDistance);

        structureLocation = playerLoc.clone().add(offsetX, 0, offsetZ);

        // Ajusta Y para o chão
        structureLocation.setY(playerLoc.getWorld().getHighestBlockYAt(structureLocation) + 1);

        // Spawna a estrutura
        structure.spawn(structureLocation);

        // Informa o jogador
        player.sendMessage("§6✦ §eUma energia estranha emana de algum lugar próximo...");
        player.sendMessage("§7Procure por: §f" + structure.getName());

        // Dá uma direção geral
        String direction = getGeneralDirection(playerLoc, structureLocation);
        player.sendMessage("§7Direção aproximada: §e" + direction);
        player.sendMessage("§7Distância aproximada: §e~" + randomDistance + " blocos");
    }

    @Override
    public void updateProgress(Object... params) {
        if (found) return;

        // Espera: Player e Location
        if (params.length >= 1 && params[0] instanceof Player player) {
            Location playerLoc = player.getLocation();

            if (structureLocation != null &&
                    playerLoc.distance(structureLocation) <= detectionRadius) {

                found = true;
                completed = true;

                // Feedback épico
                player.sendMessage("");
                player.sendMessage("§6§l━━━━━━━━━━━━━━━━━━━━━━━━");
                player.sendMessage("§e§l     ✦ LOCALIZAÇÃO ENCONTRADA! ✦");
                player.sendMessage("§6§l━━━━━━━━━━━━━━━━━━━━━━━━");
                player.sendMessage("§fVocê encontrou: §a" + structure.getName());
                player.sendMessage("§6§l━━━━━━━━━━━━━━━━━━━━━━━━");
                player.sendMessage("");

                // Efeitos
                player.playSound(playerLoc, org.bukkit.Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f);
                player.spawnParticle(org.bukkit.Particle.END_ROD,
                        structureLocation.clone().add(0, 2, 0),
                        100, 2, 2, 2, 0.1);

                // Dispara evento de conclusão
                QuestCompletedEvent event = new QuestCompletedEvent(player, this);
                Bukkit.getServer().getPluginManager().callEvent(event);
            }
        }
    }

    @Override
    public boolean checkCompletion() {
        return found;
    }

    @Override
    public ItemStack[] getRewardItems() {
        return new ItemStack[0];
    }

    public String getProgressText() {
        if (found) {
            return "✅ " + structure.getName() + " encontrado!";
        } else {
            return "🔍 Procurando: " + structure.getName();
        }
    }

    public Location getStructureLocation() {
        return structureLocation;
    }

    public QuestStructure getStructure() {
        return structure;
    }

    /**
     * Retorna direção geral (N, S, L, O, NE, NO, SE, SO)
     */
    private String getGeneralDirection(Location from, Location to) {
        double dx = to.getX() - from.getX();
        double dz = to.getZ() - from.getZ();

        double angle = Math.atan2(dz, dx) * 180 / Math.PI;
        angle = (angle + 360) % 360;

        if (angle >= 337.5 || angle < 22.5) return "Leste";
        if (angle >= 22.5 && angle < 67.5) return "Sudeste";
        if (angle >= 67.5 && angle < 112.5) return "Sul";
        if (angle >= 112.5 && angle < 157.5) return "Sudoeste";
        if (angle >= 157.5 && angle < 202.5) return "Oeste";
        if (angle >= 202.5 && angle < 247.5) return "Noroeste";
        if (angle >= 247.5 && angle < 292.5) return "Norte";
        return "Nordeste";
    }
}