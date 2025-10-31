package br.dev.projetoc14.player.abilities.archerSkills;

import br.dev.projetoc14.QuestWeaver;
import br.dev.projetoc14.player.RPGPlayer;
import br.dev.projetoc14.player.abilities.Ability;
import br.dev.projetoc14.player.classes.ArcherPlayer;
import br.dev.projetoc14.player.classes.AssassinPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class ArchListener implements Listener {
    private final QuestWeaver plugin;
    private final Map<UUID, Integer> habilidadeIndex = new HashMap<>();
    private final List<String> habilidades = Arrays.asList("EXPLOSIVEARROW");

    // Map para gerir a execução das habilidades
    private final Map<String, Ability> abilityMap = new HashMap<>();

    // 1. Construtor Corrigido
    public ArchListener(QuestWeaver plugin) {
        this.plugin = plugin;

        // Inicializa o mapa para uma execução escalável
        ExplosiveArrow explosiveArrow = new ExplosiveArrow();
        abilityMap.put("ShadowMove", explosiveArrow);
    }

    @EventHandler
    public void onItemSwitch(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        Action action = e.getAction();

        if (!isBow(player.getInventory().getItemInMainHand())) return;
        if (!player.isSneaking()) return;
        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) return;

        ArcherPlayer archer = getArcherPlayer(player);
        if (archer == null) return;

        e.setCancelled(true);

        int index = habilidadeIndex.getOrDefault(player.getUniqueId(), 0);
        index = (index + 1) % habilidades.size();
        habilidadeIndex.put(player.getUniqueId(), index);

        String nova = habilidades.get(index);
        player.sendActionBar(ChatColor.AQUA + "✨ Habilidade: " + ChatColor.GOLD + formatName(nova));
        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1f, 1.5f);
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        Action a = e.getAction();

        if (a != Action.RIGHT_CLICK_AIR && a != Action.RIGHT_CLICK_BLOCK) return;
        if (!isBow(p.getInventory().getItemInMainHand())) return;
        if (p.isSneaking()) return;

        // todo: assassin may not get a wand but another iten
        ArcherPlayer archer = getArcherPlayer(p);
        if (archer == null) {
            p.sendActionBar(ChatColor.RED + "❌ Apenas arqueiros podem usar este arco!");
            return;
        }

        e.setCancelled(true);

        int index = habilidadeIndex.getOrDefault(p.getUniqueId(), 0);
        String habilidadeNome = habilidades.get(index);

        Ability ability = abilityMap.get(habilidadeNome);

        if (ability != null) {
            if (ability.canCast(archer)) {
                ability.cast(archer);
            } else {
                sendCooldownMessage(p, ability);
            }
        }
    }

    private void sendCooldownMessage(Player p, Ability ability) {
        p.sendActionBar(ChatColor.RED + "⏳ Habilidade em cooldown!");
        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
    }

    private ArcherPlayer getArcherPlayer(Player p) {
        RPGPlayer rpgPlayer = plugin.getRPGPlayer(p);
        if (rpgPlayer instanceof ArcherPlayer archer) return archer;
        return null;
    }

    // todo: set new item (wand) to assassin (choose an iten that makes sense to an Assassin class)
    private boolean isBow(ItemStack item) {
        if (item == null || item.getType() != Material.BOW) return false;
        if (!item.hasItemMeta()) return false;
        return ChatColor.stripColor(item.getItemMeta().getDisplayName())
                .equalsIgnoreCase("Arco Mágico");
    }

    private String formatName(String nome) {
        return switch (nome) {
            case "EXPLOSIVEARROW" -> "Flechas explosivas";
            //case "" -> "";
            default -> nome;
        };
    }
}
