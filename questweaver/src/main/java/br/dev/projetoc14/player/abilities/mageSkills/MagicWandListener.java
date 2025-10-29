package br.dev.projetoc14.player.abilities.mageSkills;

import br.dev.projetoc14.QuestWeaver;
import br.dev.projetoc14.player.RPGPlayer;
import br.dev.projetoc14.player.abilities.Ability; // IMPORTANTE: Certifique-se de que este import existe
import br.dev.projetoc14.player.classes.MagePlayer;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.*;
import org.bukkit.inventory.*;

import java.util.*;

public class MagicWandListener implements Listener {

    private final QuestWeaver plugin;
    private final Map<UUID, Integer> habilidadeIndex = new HashMap<>();
    private final List<String> habilidades = Arrays.asList("FIREBALL", "CURE");

    // Map para gerir a execução das habilidades
    private final Map<String, Ability> abilityMap = new HashMap<>();

    private final Fireball fireballAbility = new Fireball();
    private final Healing healingAbility = new Healing();

    // 1. Construtor Corrigido
    public MagicWandListener(QuestWeaver plugin) {
        this.plugin = plugin;

        // Inicializa o mapa para uma execução escalável
        abilityMap.put("FIREBALL", fireballAbility);
        abilityMap.put("CURE", healingAbility);
    }

    @EventHandler
    public void onItemSwitch(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        Action action = e.getAction();

        if (!isWand(player.getInventory().getItemInMainHand())) return;
        if (!player.isSneaking()) return;
        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) return;

        MagePlayer mage = getMagePlayer(player);
        if (mage == null) return;

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
        if (!isWand(p.getInventory().getItemInMainHand())) return;
        if (p.isSneaking()) return;

        MagePlayer mage = getMagePlayer(p);
        if (mage == null) {
            p.sendActionBar(ChatColor.RED + "❌ Apenas magos podem usar este cajado!");
            return;
        }

        e.setCancelled(true);

        int index = habilidadeIndex.getOrDefault(p.getUniqueId(), 0);
        String habilidadeNome = habilidades.get(index);

        Ability ability = abilityMap.get(habilidadeNome);

        if (ability != null) {
            if (ability.canCast(mage)) {
                ability.cast(mage);
            } else {
                sendCooldownMessage(p, ability);
            }
        }
    }

    private void sendCooldownMessage(Player p, Ability ability) {
        p.sendActionBar(ChatColor.RED + "⏳ Habilidade em cooldown!");
        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
    }

    private MagePlayer getMagePlayer(Player p) {
        RPGPlayer rpgPlayer = plugin.getRPGPlayer(p);
        if (rpgPlayer instanceof MagePlayer mage) return mage;
        return null;
    }

    private boolean isWand(ItemStack item) {
        if (item == null || item.getType() != Material.BLAZE_ROD) return false;
        if (!item.hasItemMeta()) return false;
        return ChatColor.stripColor(item.getItemMeta().getDisplayName())
                .equalsIgnoreCase("Cajado Mágico");
    }

    private String formatName(String nome) {
        return switch (nome) {
            case "FIREBALL" -> "Bola de Fogo 🔥";
            case "CURE" -> "Cura 💚";
            default -> nome;
        };
    }
}