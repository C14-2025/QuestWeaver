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
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

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

        ShadowMove shadowMove = new ShadowMove();
        VampireKnives vampireKnives = new VampireKnives();

        shadowMove.setCooldownListener(plugin.getCooldownListener());
        vampireKnives.setCooldownListener(plugin.getCooldownListener());

        abilityMap.put("ShadowMove", shadowMove);
        abilityMap.put("VampireKnives", vampireKnives);
    }

    @EventHandler
    public void onItemSwitch(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        Action action = e.getAction();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (!player.isSneaking()) return;
        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) return;

        AssassinPlayer assassin = getAssassinPlayer(player);
        if (assassin == null) return;

        if (isPotion(item)) {
            AbilityUtil.switchAbility(player, e, potionAbilityIndex, potionAbilities, this::formatName);
        } else if (isSword(item)) {
            AbilityUtil.switchAbility(player, e, swordAbilityIndex, swordAbilities, this::formatName);
        }
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        Action a = e.getAction();

        if (a != Action.RIGHT_CLICK_AIR && a != Action.RIGHT_CLICK_BLOCK) return;
        if (p.isSneaking()) return;

        ItemStack item = p.getInventory().getItemInMainHand();
        if (!isPotion(item) && !isSword(item)) return;

        AssassinPlayer assassin = getAssassinPlayer(p);
        if (assassin == null) {
            p.sendActionBar(Component.text("❌ Apenas assassinos podem usar esta habilidade!")
                    .color(NamedTextColor.RED));
            return;
        }

        if (isPotion(item)) {
            AbilityUtil.executeAbility(p, e, potionAbilityIndex, potionAbilities, abilityMap, assassin);
        } else if (isSword(item)) {
            AbilityUtil.executeAbility(p, e, swordAbilityIndex, swordAbilities, abilityMap, assassin);
        }
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
        PersistentDataContainer data = meta.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, "custom_item");

        if (data.has(key, PersistentDataType.STRING)) {
            String id = data.get(key, PersistentDataType.STRING);
            return "shadow_potion".equals(id);
        }

        // fallback: compatibilidade com itens antigos
        if (meta.displayName() == null) return false;
        String displayName = PlainTextComponentSerializer.plainText()
                .serialize(Objects.requireNonNull(meta.displayName()));
        return displayName.equalsIgnoreCase("Poção das Sombras");
    }

    private boolean isSword(ItemStack item) {
        if (item == null || item.getType() != Material.IRON_SWORD) return false;
        if (!item.hasItemMeta()) return false;

        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer data = meta.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(plugin, "custom_item");

        if (data.has(key, PersistentDataType.STRING)) {
            String id = data.get(key, PersistentDataType.STRING);
            return "shadow_dagger".equals(id);
        }

        // fallback: compatibilidade com itens antigos
        if (meta.displayName() == null) return false;
        String displayName = PlainTextComponentSerializer.plainText()
                .serialize(Objects.requireNonNull(meta.displayName()));
        return displayName.equalsIgnoreCase("Punhal Sombrio");
    }

    public String formatName(String nome) {
        return switch (nome.toUpperCase()) {
            case "SHADOWMOVE" -> "Passos Sombrios";
            case "VAMPIREKNIVES" -> "Vampire Knives";
            default -> nome;
        };
    }
}
