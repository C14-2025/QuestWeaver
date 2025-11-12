package br.dev.projetoc14.player.abilities.assassinSkills;

import br.dev.projetoc14.QuestWeaver;
import br.dev.projetoc14.player.RPGPlayer;
import br.dev.projetoc14.player.abilities.Ability;
import br.dev.projetoc14.player.abilities.AbilityUtil;
import br.dev.projetoc14.player.classes.AssassinPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
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

    private final Map<UUID, Integer> potionAbilityIndex = new HashMap<>();
    private final Map<UUID, Integer> swordAbilityIndex = new HashMap<>();

    private final List<String> potionAbilities = List.of("ShadowMove");
    private final List<String> swordAbilities = List.of("VampireKnives");

    private final Map<String, Ability> abilityMap = new HashMap<>();

    public AbilityListener(QuestWeaver plugin) {
        this.plugin = plugin;

        // Registra habilidades do assassino
        abilityMap.put("ShadowMove", new ShadowMove());
        abilityMap.put("VampireKnives", new VampireKnives());
    }

    @EventHandler
    public void onItemSwitch(PlayerInteractEvent e) {
        if (!isRightClick(e.getAction())) return;

        Player player = e.getPlayer();
        if (!player.isSneaking()) return;

        AssassinPlayer assassin = getAssassinPlayer(player);
        if (assassin == null) return;

        ItemStack item = player.getInventory().getItemInMainHand();

        // Alterna habilidades com shift + botão direito
        if (isCustomItem(item, Material.POTION, "Poção das Sombras")) {
            AbilityUtil.switchAbility(player, e, potionAbilityIndex, potionAbilities, this::formatName);
        } else if (isCustomItem(item, Material.IRON_SWORD, "Punhal Sombrio")) {
            AbilityUtil.switchAbility(player, e, swordAbilityIndex, swordAbilities, this::formatName);
        }
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        if (!isRightClick(e.getAction())) return;

        Player player = e.getPlayer();
        if (player.isSneaking()) return; // Evita conflito com troca de habilidade

        ItemStack item = player.getInventory().getItemInMainHand();

        // Só assassinos podem usar habilidades
        AssassinPlayer assassin = getAssassinPlayer(player);
        if (assassin == null) {
            player.sendActionBar(Component.text("❌ Apenas assassinos podem usar esta habilidade!")
                    .color(NamedTextColor.RED));
            return;
        }

        // Executa a habilidade correta com base no item
        if (isCustomItem(item, Material.POTION, "Poção das Sombras")) {
            AbilityUtil.executeAbility(player, e, potionAbilityIndex, potionAbilities, abilityMap, assassin);
        } else if (isCustomItem(item, Material.IRON_SWORD, "Punhal Sombrio")) {
            AbilityUtil.executeAbility(player, e, swordAbilityIndex, swordAbilities, abilityMap, assassin);
        }
    }

    // ==============================
    // 🔹 Métodos utilitários
    // ==============================

    private AssassinPlayer getAssassinPlayer(Player player) {
        RPGPlayer rpgPlayer = plugin.getRPGPlayer(player);
        return (rpgPlayer instanceof AssassinPlayer assassin) ? assassin : null;
    }

    private boolean isRightClick(Action action) {
        return action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK;
    }

    private boolean isCustomItem(ItemStack item, Material material, String nomeEsperado) {
        if (item == null || item.getType() != material || !item.hasItemMeta()) return false;

        ItemMeta meta = item.getItemMeta();
        if (meta.displayName() == null) return false;

        String displayName = PlainTextComponentSerializer.plainText()
                .serialize(Objects.requireNonNull(meta.displayName()));

        return displayName.equalsIgnoreCase(nomeEsperado);
    }

    public String formatName(String nome) {
        return switch (nome.toUpperCase()) {
            case "SHADOWMOVE" -> "Passos Sombrios";
            case "VAMPIREKNIVES" -> "Vampire Knives";
            default -> nome;
        };
    }
}
