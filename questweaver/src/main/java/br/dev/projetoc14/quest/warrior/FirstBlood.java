package br.dev.projetoc14.quest.warrior;

import br.dev.projetoc14.quest.KillQuest;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public final class FirstBlood extends KillQuest {
    public FirstBlood(Location spawnLocation) {
        super("warrior_first_blood",
                "Primeira Matança",
                "Mate 10 zumbis usando uma espada ou machado",
                100,
                "ZOMBIE",
                10,
                0,
                spawnLocation,
                Arrays.asList(
                        Material.WOODEN_SWORD,
                        Material.STONE_SWORD,
                        Material.IRON_SWORD,
                        Material.DIAMOND_SWORD,
                        Material.NETHERITE_SWORD,
                        Material.WOODEN_AXE,
                        Material.STONE_AXE,
                        Material.IRON_AXE,
                        Material.DIAMOND_AXE,
                        Material.NETHERITE_AXE
                ));
    }

    @Override
    public ItemStack[] getRewardItems() {
        ItemStack axe = new ItemStack(Material.IRON_AXE);
        axe.addEnchantment(Enchantment.SHARPNESS, 1);

        ItemStack chestplate = new ItemStack(Material.IRON_CHESTPLATE);

        ItemStack shield = new ItemStack(Material.SHIELD);

        return new ItemStack[]{axe, chestplate, shield};
    }
}
