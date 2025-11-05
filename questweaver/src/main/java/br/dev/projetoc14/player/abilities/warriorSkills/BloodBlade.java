package br.dev.projetoc14.player.abilities.warriorSkills;

import br.dev.projetoc14.QuestWeaver;
import br.dev.projetoc14.player.RPGPlayer;
import br.dev.projetoc14.player.abilities.Ability;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BloodBlade extends Ability implements Listener {

    private static final int MAX_STACKS = 5;
    private static final double EXECUTE_DAMAGE = 30.0;

    private final Map<UUID, Map<UUID, BleedData>> playerBleedData = new HashMap<>();

    public BloodBlade() {
        super("Blood Blade", 20, 15);
        Bukkit.getPluginManager().registerEvents(this, QuestWeaver.getInstance());
    }

    @Override
    protected void onCast(RPGPlayer caster) {
        Player player = caster.getPlayer();

        if (player.getInventory().getItemInMainHand().getType() != Material.IRON_AXE) {
            player.sendMessage("§c[Blood Blade] Você precisa estar com um Machado de Ferro na mão!");
            return;
        }

        dyeAxeRed(player);
        player.sendMessage("§c[Blood Blade] §7Ativado! Seus ataques acumulam sangramento.");
    }

    private void dyeAxeRed(Player player) {
        org.bukkit.inventory.ItemStack axe = player.getInventory().getItemInMainHand();
        org.bukkit.inventory.meta.ItemMeta meta = axe.getItemMeta();

        if (meta instanceof org.bukkit.inventory.meta.LeatherArmorMeta) {
            ((org.bukkit.inventory.meta.LeatherArmorMeta) meta).setColor(org.bukkit.Color.RED);
        } else {
            meta.addEnchant(Enchantment.UNBREAKING, 1, true);
            meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);
        }

        meta.displayName(Component.text("§c§lMachado Sangrento"));
        axe.setItemMeta(meta);
        player.getInventory().setItemInMainHand(axe);
    }

    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player)) return;
        if (!(event.getEntity() instanceof LivingEntity target)) return;

        if (player.getInventory().getItemInMainHand().getType() != Material.IRON_AXE) return;

        RPGPlayer rpgPlayer = ((QuestWeaver) QuestWeaver.getInstance()).getRPGPlayer(player);
        if (rpgPlayer == null) return;

        addBleedStack(player, target);
    }

    private void addBleedStack(Player player, LivingEntity target) {
        UUID playerUUID = player.getUniqueId();
        UUID targetUUID = target.getUniqueId();

        playerBleedData.putIfAbsent(playerUUID, new HashMap<>());
        Map<UUID, BleedData> targetMap = playerBleedData.get(playerUUID);

        BleedData bleedData = targetMap.get(targetUUID);

        if (bleedData == null) {
            bleedData = new BleedData(player, target, playerBleedData);
            targetMap.put(targetUUID, bleedData);
        }

        bleedData.addStack();

        if (bleedData.getStacks() >= MAX_STACKS) {
            executeBleed(player, target, bleedData);
        }
    }

    private void executeBleed(Player player, LivingEntity target, BleedData bleedData) {
        target.damage(EXECUTE_DAMAGE, player);

        player.sendMessage("§c⚔ §4EXECUÇÃO DE SANGRAMENTO! §c⚔");
        target.getWorld().spawnParticle(
                Particle.CRIMSON_SPORE,
                target.getLocation().add(0, 1, 0),
                50,
                0.5, 0.5, 0.5,
                new org.bukkit.Particle.DustOptions(org.bukkit.Color.RED, 2.0f)
        );

        bleedData.remove();
        playerBleedData.get(player.getUniqueId()).remove(target.getUniqueId());
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        UUID targetUUID = event.getEntity().getUniqueId();

        playerBleedData.values().forEach(map -> {
            BleedData data = map.remove(targetUUID);
            if (data != null) {
                data.remove();
            }
        });
    }
}