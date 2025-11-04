package br.dev.projetoc14.player.classes;

import br.dev.projetoc14.player.PlayerClass;
import br.dev.projetoc14.player.RPGPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.List;

public class ArcherPlayer extends RPGPlayer {

    public ArcherPlayer(Player player) {
        super(player, PlayerClass.ARCHER);
    }

    @Override
    protected void initializeClass() {
        // Stats iniciais do arqueiro
        stats.setStrength(1);
        stats.setAgility(1);
        stats.setIntelligence(7);
        stats.setHealth(30);
        stats.setMana(60);
    }

    @Override
    public void levelUp() {
        level++;
        stats.setAgility(stats.getAgility() + 3);
        stats.setStrength(stats.getStrength() + 2);
        stats.setHealth(stats.getHealth() + 10);
        stats.setMana(stats.getMana() + 5);
        player.sendMessage(ChatColor.GREEN + "🏹 Arqueiro subiu para o nível " + level + "!");
    }

    @Override
    public void getStartingEquipment() {
        PlayerInventory inv = this.getPlayer().getInventory();

        // Cria e adiciona o arco mágico
        inv.addItem(createMagicBow());
        inv.addItem(new ItemStack(Material.ARROW, 10));

        // Cor verde para o arqueiro
        Color archerColor = Color.fromRGB(0, 150, 0); // Verde floresta

        // Capacete
        ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
        LeatherArmorMeta helmetMeta = (LeatherArmorMeta) helmet.getItemMeta();
        helmetMeta.setColor(archerColor);
        helmet.setItemMeta(helmetMeta);

        // Peitoral
        ItemStack chest = new ItemStack(Material.LEATHER_CHESTPLATE);
        LeatherArmorMeta chestMeta = (LeatherArmorMeta) chest.getItemMeta();
        chestMeta.setColor(archerColor);
        chest.setItemMeta(chestMeta);

        // Calças
        ItemStack legs = new ItemStack(Material.LEATHER_LEGGINGS);
        LeatherArmorMeta legsMeta = (LeatherArmorMeta) legs.getItemMeta();
        legsMeta.setColor(archerColor);
        legs.setItemMeta(legsMeta);

        // Botas
        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
        LeatherArmorMeta bootsMeta = (LeatherArmorMeta) boots.getItemMeta();
        bootsMeta.setColor(archerColor);
        boots.setItemMeta(bootsMeta);

        // Equipa o jogador
        inv.setHelmet(helmet);
        inv.setChestplate(chest);
        inv.setLeggings(legs);
        inv.setBoots(boots);

        // Livro de quests
        inv.addItem(createQuestBook());
    }

    private ItemStack createMagicBow() {
        ItemStack bow = new ItemStack(Material.BOW, 1);
        ItemMeta meta = bow.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ChatColor.GOLD + "Arco Mágico");
            meta.setLore(List.of(
                    ChatColor.GRAY + "Clique esquerdo no ar para alternar o tipo de flecha.",
                    ChatColor.GRAY + "Clique direito para disparar a habilidade atual.",
                    "",
                    ChatColor.DARK_GRAY + "Classe: " + ChatColor.GREEN + "Arqueiro"
            ));
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            bow.setItemMeta(meta);
        }

        return bow;
    }
}
