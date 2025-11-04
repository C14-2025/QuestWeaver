package br.dev.projetoc14.player.classes;

import br.dev.projetoc14.player.PlayerClass;
import br.dev.projetoc14.player.RPGPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class WarriorPlayer extends RPGPlayer {

    public WarriorPlayer(Player player) {
        super(player, PlayerClass.WARRIOR);
    }

    @Override
    protected void initializeClass() {
        // Atributos iniciais do Guerreiro
        stats.setStrength(1);
        stats.setAgility(1);
        stats.setIntelligence(5);
        stats.setHealth(40);
        stats.setMana(40);
    }

    @Override
    public void levelUp() {
        level++;
        stats.setStrength(stats.getStrength() + 3);
        stats.setDefense(stats.getDefense() + 2);
        stats.setHealth(stats.getHealth() + 15);
        player.sendMessage(ChatColor.RED + "⚔ Guerreiro subiu para o nível " + level + "!");
    }

    @Override
    public void getStartingEquipment() {
        PlayerInventory inv = this.getPlayer().getInventory();

        // Cor vermelha para a armadura do guerreiro
        Color warriorColor = Color.RED;

        // Arma principal
        ItemStack axe = new ItemStack(Material.IRON_AXE);
        inv.addItem(axe);

        // Capacete
        ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
        LeatherArmorMeta helmetMeta = (LeatherArmorMeta) helmet.getItemMeta();
        helmetMeta.setColor(warriorColor);
        helmet.setItemMeta(helmetMeta);

        // Peitoral
        ItemStack chest = new ItemStack(Material.LEATHER_CHESTPLATE);
        LeatherArmorMeta chestMeta = (LeatherArmorMeta) chest.getItemMeta();
        chestMeta.setColor(warriorColor);
        chest.setItemMeta(chestMeta);

        // Calças
        ItemStack legs = new ItemStack(Material.LEATHER_LEGGINGS);
        LeatherArmorMeta legsMeta = (LeatherArmorMeta) legs.getItemMeta();
        legsMeta.setColor(warriorColor);
        legs.setItemMeta(legsMeta);

        // Botas
        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
        LeatherArmorMeta bootsMeta = (LeatherArmorMeta) boots.getItemMeta();
        bootsMeta.setColor(warriorColor);
        boots.setItemMeta(bootsMeta);

        // Equipa o jogador
        inv.setHelmet(helmet);
        inv.setChestplate(chest);
        inv.setLeggings(legs);
        inv.setBoots(boots);

        // Adiciona o livro de quests
        inv.addItem(createQuestBook());
    }
}
