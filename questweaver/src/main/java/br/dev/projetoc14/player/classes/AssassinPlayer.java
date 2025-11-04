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

public class AssassinPlayer extends RPGPlayer {

    public AssassinPlayer(Player player) {
        super(player, PlayerClass.ASSASSIN);
    }

    @Override
    protected void initializeClass() {
        // Stats iniciais do assassino
        stats.setStrength(2);      // Alto dano físico
        stats.setAgility(5);       // Muito ágil
        stats.setIntelligence(6);  // Pouca inteligência
        stats.setHealth(20);       // Vida baixa
        stats.setMana(60);         // Usa mais ataques físicos
    }

    @Override
    public void levelUp() {
        level++;
        stats.setStrength(stats.getStrength() + 3);
        stats.setAgility(stats.getAgility() + 2);
        stats.setHealth(stats.getHealth() + 12);
        stats.setMana(stats.getMana() + 5);
        player.sendMessage("§8O Assassino subiu para o nível §5" + level + "§8!");
    }

    @Override
    public void getStartingEquipment() {
        PlayerInventory inv = this.getPlayer().getInventory();

        // Arma: Lâmina leve
        ItemStack sword = new ItemStack(Material.IRON_SWORD);
        ItemMeta swordMeta = sword.getItemMeta();
        if (swordMeta != null) {
            swordMeta.setDisplayName(ChatColor.DARK_GRAY + "Lâmina Sombria");
            swordMeta.setLore(List.of(
                    ChatColor.GRAY + "Uma lâmina leve e precisa, feita para matar em silêncio.",
                    "",
                    ChatColor.DARK_GRAY + "Classe: " + ChatColor.LIGHT_PURPLE + "Assassino"
            ));
            swordMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            sword.setItemMeta(swordMeta);
        }
        inv.addItem(sword);

        // Cor preta da armadura
        Color assassinColor = Color.fromRGB(25, 25, 25); // Preto escuro

        // Capacete
        ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
        LeatherArmorMeta helmetMeta = (LeatherArmorMeta) helmet.getItemMeta();
        helmetMeta.setColor(assassinColor);
        helmet.setItemMeta(helmetMeta);

        // Peitoral
        ItemStack chest = new ItemStack(Material.LEATHER_CHESTPLATE);
        LeatherArmorMeta chestMeta = (LeatherArmorMeta) chest.getItemMeta();
        chestMeta.setColor(assassinColor);
        chest.setItemMeta(chestMeta);

        // Calças
        ItemStack legs = new ItemStack(Material.LEATHER_LEGGINGS);
        LeatherArmorMeta legsMeta = (LeatherArmorMeta) legs.getItemMeta();
        legsMeta.setColor(assassinColor);
        legs.setItemMeta(legsMeta);

        // Botas
        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
        LeatherArmorMeta bootsMeta = (LeatherArmorMeta) boots.getItemMeta();
        bootsMeta.setColor(assassinColor);
        boots.setItemMeta(bootsMeta);

        // Equipa no jogador
        inv.setHelmet(helmet);
        inv.setChestplate(chest);
        inv.setLeggings(legs);
        inv.setBoots(boots);

        // Livro de quests
        inv.addItem(createQuestBook());

        // Poção das habilidades exclusivas do assassino
        inv.addItem(createAssassinPotion());
    }

    // Cria a poção mágica exclusiva do assassino para ativar habilidades
    private ItemStack createAssassinPotion() {
        ItemStack potion = new ItemStack(Material.POTION, 1);
        ItemMeta meta = potion.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ChatColor.DARK_PURPLE + "Poção das Sombras");
            meta.setLore(List.of(
                    ChatColor.GRAY + "Use enquanto agachado (Shift + botão direito)",
                    ChatColor.GRAY + "para alternar entre habilidades.",
                    ChatColor.GRAY + "Use normalmente para ativar a habilidade atual.",
                    "",
                    ChatColor.DARK_GRAY + "Classe: " + ChatColor.LIGHT_PURPLE + "Assassino"
            ));
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            potion.setItemMeta(meta);
        }

        return potion;
    }
}
