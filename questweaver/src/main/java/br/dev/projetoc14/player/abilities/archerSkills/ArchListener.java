package br.dev.projetoc14.player.abilities.archerSkills;

import br.dev.projetoc14.QuestWeaver;
import br.dev.projetoc14.player.RPGPlayer;
import br.dev.projetoc14.player.abilities.Ability;
import br.dev.projetoc14.player.abilities.AbilityUtil;
import br.dev.projetoc14.player.classes.ArcherPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class ArchListener implements Listener {

    private final QuestWeaver plugin;
    private final Map<UUID, Integer> abilityIndex = new HashMap<>();
    private final List<String> abilities = Arrays.asList("NORMALARROW", "EXPLOSIVEARROW");

    private final Map<String, Ability> abilityMap = new HashMap<>();

    public ArchListener(QuestWeaver plugin) {
        this.plugin = plugin;
        abilityMap.put("EXPLOSIVEARROW", new ExplosiveArrow(plugin));
        // NORMALARROW é disparo padrão, então não requer instância
    }

    @EventHandler
    public void onArrowSwitch(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        Action action = e.getAction();

        if (!isBow(player.getInventory().getItemInMainHand())) return;
        if (!player.isSneaking()) return;
        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) return;

        ArcherPlayer archer = getArcherPlayer(player);
        if (archer == null) return;

        AbilityUtil.switchAbility(player, e, abilityIndex, abilities, this::formatName);
    }

    @EventHandler
    public void onArrowShoot(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        Action a = e.getAction();

        if (a != Action.RIGHT_CLICK_AIR && a != Action.RIGHT_CLICK_BLOCK) return;
        if (!isBow(p.getInventory().getItemInMainHand())) return;
        if (p.isSneaking()) return;

        ArcherPlayer archer = getArcherPlayer(p);
        if (archer == null) {
            Component msg = Component.text("❌ Apenas arqueiros podem usar este arco!", NamedTextColor.RED);
            p.sendActionBar(msg);
            return;
        }

        // Obtém qual habilidade está selecionada
        String currentAbility = abilities.get(abilityIndex.getOrDefault(p.getUniqueId(), 0));

        // Se for flecha normal, deixa o Minecraft cuidar do disparo padrão
        if (currentAbility.equalsIgnoreCase("NORMALARROW")) {
            return;
        }

        AbilityUtil.executeAbility(p, e, abilityIndex, abilities, abilityMap, archer);
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (!(event.getEntity() instanceof Arrow arrow)) return;

        // Verificar se a flecha tem a metadata de explosiva
        if (!arrow.hasMetadata("explosive_arrow")) return;

        // Obter o atirador
        UUID shooterUUID = null;
        if (arrow.hasMetadata("explosive_arrow_shooter")) {
            shooterUUID = (UUID) arrow.getMetadata("explosive_arrow_shooter").get(0).value();
        }

        if (shooterUUID == null) return;

        Player shooter = Bukkit.getPlayer(shooterUUID);
        if (shooter == null) return;

        RPGPlayer rpgShooter = plugin.getRPGPlayer(shooter);

        // Verificar se acertou um jogador
        RPGPlayer target = null;
        Entity hitEntity = event.getHitEntity();
        if (hitEntity instanceof Player hitPlayer) {
            target = plugin.getRPGPlayer(hitPlayer);
        }

        // Obter a instância da habilidade ExplosiveArrow e executar o efeito
        Ability ability = abilityMap.get("EXPLOSIVEARROW");
        if (ability instanceof ExplosiveArrow explosiveArrow) {
            explosiveArrow.onHit(event, rpgShooter, target);
        }
    }

    private ArcherPlayer getArcherPlayer(Player p) {
        RPGPlayer rpgPlayer = plugin.getRPGPlayer(p);
        if (rpgPlayer instanceof ArcherPlayer archer) return archer;
        return null;
    }

    private boolean isBow(ItemStack item) {
        if (item == null) return false;
        Material type = item.getType();
        return type == Material.BOW || type == Material.CROSSBOW;
    }

    private String formatName(String nome) {
        return switch (nome.toUpperCase()) {
            case "EXPLOSIVEARROW" -> "Flecha Explosiva 💥";
            case "NORMALARROW" -> "Flecha Normal 🏹";
            default -> nome;
        };
    }
}