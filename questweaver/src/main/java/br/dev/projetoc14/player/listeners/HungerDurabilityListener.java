package br.dev.projetoc14.player.listeners;

import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;

/**
 * Listener que desativa a fome, mantém saturação no máximo e
 * impede perda de durabilidade de itens.
 * Também garante que a regeneração natural (por fome) esteja desativada.
 * Ideal para sistemas de RPG, servidores de arena ou mundos criativos controlados.
 */
public class HungerDurabilityListener implements Listener {

    public HungerDurabilityListener() {
        // Desativa regeneração natural de vida em todos os mundos
        for (World world : Bukkit.getWorlds()) {
            world.setGameRule(GameRule.NATURAL_REGENERATION, false);
        }
    }

    /**
     * Impede que o nível de fome diminua e mantém o jogador sempre saciado.
     */
    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player player) {
            event.setCancelled(true);
            player.setFoodLevel(20);      // Fome sempre cheia
            player.setSaturation(20f);    // Saturação máxima (mantém sem piscar a barra)
            player.setExhaustion(0f);     // Impede qualquer drenagem futura
        }
    }

    /**
     * Garante que todo jogador, ao entrar, tenha fome e saturação cheias.
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.setFoodLevel(20);
        player.setSaturation(20f);
        player.setExhaustion(0f);
    }

    /**
     * Impede que qualquer item perca durabilidade.
     */
    @EventHandler
    public void onItemDamage(PlayerItemDamageEvent event) {
        event.setCancelled(true);
    }

    /**
     * Bloqueia regeneração natural de vida por fome/saturação
     * Isso garante que APENAS o sistema customizado controle a cura
     */
    @EventHandler
    public void onNaturalRegen(EntityRegainHealthEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        // Bloqueia apenas regeneração por saturação/fome
        if (event.getRegainReason() == EntityRegainHealthEvent.RegainReason.SATIATED ||
                event.getRegainReason() == EntityRegainHealthEvent.RegainReason.REGEN) {
            event.setCancelled(true);
        }
    }
}