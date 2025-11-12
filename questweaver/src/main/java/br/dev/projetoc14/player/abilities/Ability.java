package br.dev.projetoc14.player.abilities;

import br.dev.projetoc14.QuestWeaver;
import br.dev.projetoc14.player.RPGPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.*;

public abstract class Ability {

    private final String name;
    private final int manaCost;
    private final int cooldown;
    private final Map<UUID, Long> lastUsed = new HashMap<>();

    private CooldownListener cooldownListener;

    public Ability(String name, int manaCost, int cooldown) {
        this.name = name;
        this.manaCost = manaCost;
        this.cooldown = cooldown;
    }

    public void setCooldownListener(CooldownListener cooldownListener) {
        this.cooldownListener = cooldownListener;
    }

    protected abstract void onCast(RPGPlayer caster);

    /**
     * Método principal para executar a habilidade.
     */
    public final void cast(RPGPlayer caster) {
        this.onCast(caster);
        this.applyCost(caster);
    }

    /**
     * Verifica se o jogador pode usar a habilidade (mana + cooldown)
     */
    public CastResult canCast(RPGPlayer caster) {
        Player player = caster.getPlayer();

        // Verifica mana
        if (caster.getCurrentMana() < manaCost) {
            player.sendActionBar(Component.text("❌ Mana insuficiente! (" +
                    caster.getCurrentMana() + "/" + manaCost + ")").color(NamedTextColor.RED));
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1f, 0.8f);
            return CastResult.NO_MANA;
        }

        // Verifica cooldown
        UUID playerId = caster.getUniqueId();
        if (lastUsed.containsKey(playerId)) {
            long timeElapsed = (System.currentTimeMillis() - lastUsed.get(playerId)) / 1000;
            long timeRemaining = cooldown - timeElapsed;

            if (timeRemaining > 0) {
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                return CastResult.COOLDOWN;
            }
        }

        return CastResult.SUCCESS;
    }

    /**
     * Aplica o custo de mana e registra o cooldown
     */
    private void applyCost(RPGPlayer caster) {
        caster.setCurrentMana(caster.getCurrentMana() - manaCost);
        lastUsed.put(caster.getUniqueId(), System.currentTimeMillis());

        if (cooldownListener != null && cooldown > 0) {
            cooldownListener.startCooldown(caster.getPlayer(), name, cooldown);
        }
    }

    public String getName() {
        return name;
    }

    public int getCooldown() {
        return cooldown;
    }

    public int getManaCost() {
        return manaCost;
    }
    public long getRemainingCooldown(UUID playerId) {
        if (!lastUsed.containsKey(playerId)) {
            return 0;
        }

        long timeElapsed = (System.currentTimeMillis() - lastUsed.get(playerId)) / 1000;
        long timeRemaining = cooldown - timeElapsed;

        return Math.max(0, timeRemaining);
    }
}