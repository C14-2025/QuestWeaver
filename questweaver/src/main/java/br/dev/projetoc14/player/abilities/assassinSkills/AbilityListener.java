package br.dev.projetoc14.player.abilities.assassinSkills;

import br.dev.projetoc14.QuestWeaver;
import br.dev.projetoc14.player.RPGPlayer;
import br.dev.projetoc14.player.abilities.Ability;
import br.dev.projetoc14.player.abilities.AbilityUtil;
import br.dev.projetoc14.player.classes.AssassinPlayer;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer; // Cores
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class AbilityListener implements Listener {
    private final QuestWeaver plugin;
    private final Map<UUID, Integer> abilityIndex = new HashMap<>();
    private final List<String> abilities = List.of("ShadowMove", "VampireKnives");

    private final Map<String, Ability> abilityMap = new HashMap<>();

    public AbilityListener(QuestWeaver plugin) {
        this.plugin = plugin;
        abilityMap.put("ShadowMove", new ShadowMove());
        abilityMap.put("VampireKnives", new VampireKnives());
    }

    @EventHandler
    public void onItemSwitch(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        Action action = e.getAction();

        if (!isPotion(player.getInventory().getItemInMainHand())) return;
        if (!player.isSneaking()) return;
        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) return;

        AssassinPlayer assassin = getAssassinPlayer(player);
        if (assassin == null) return;

        AbilityUtil.switchAbility(player, e, abilityIndex, abilities, this::formatName);
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        Action a = e.getAction();

        // só reage a clique direito
        if (a != Action.RIGHT_CLICK_AIR && a != Action.RIGHT_CLICK_BLOCK) return;

        // verifica se o item é uma poção OU uma espada
        ItemStack item = p.getInventory().getItemInMainHand();
        if (!isPotion(item) && !isSword(item)) return;

        // evita ativar enquanto agachado (shift)
        if (p.isSneaking()) return;

        // só assassinos podem usar
        AssassinPlayer assassin = getAssassinPlayer(p);
        if (assassin == null) {
            p.sendActionBar(NamedTextColor.RED + "❌ Apenas assassinos podem usar esta habilidade!");
            return;
        }

        // executa a habilidade ativa
        AbilityUtil.executeAbility(p, e, abilityIndex, abilities, abilityMap, assassin);
    }


    private AssassinPlayer getAssassinPlayer(Player p) {
        RPGPlayer rpgPlayer = plugin.getRPGPlayer(p);
        if (rpgPlayer instanceof AssassinPlayer assassin) return assassin;
        return null;
    }

    private boolean isPotion(ItemStack item) {
        if (item == null || item.getType() != Material.POTION) return false;
        if (!item.hasItemMeta()) return false;

        ItemMeta meta = item.getItemMeta();
        if (meta.displayName() == null) return false;

        String displayName = PlainTextComponentSerializer.plainText()
                .serialize(Objects.requireNonNull(meta.displayName()));
        return displayName.equalsIgnoreCase("Poção das Sombras");
    }

    private boolean isSword(ItemStack item) {
        if (item == null || item.getType() != Material.IRON_SWORD) return false;
        if (!item.hasItemMeta()) return false;

        ItemMeta meta = item.getItemMeta();
        if (meta.displayName() == null) return false;

        String displayName = PlainTextComponentSerializer.plainText()
                .serialize(Objects.requireNonNull(meta.displayName()));
        return displayName.equalsIgnoreCase("Iron Sword");
    }

    public String formatName(String nome) {
        return switch (nome.toUpperCase()) {
            case "SHADOWMOVE" -> "Passos Sombrios";
            default -> nome;
        };
    }
}