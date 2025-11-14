package br.dev.projetoc14.player.abilities;

import br.dev.projetoc14.player.RPGPlayer;
import br.dev.projetoc14.player.abilities.cooldown.CooldownManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public abstract class Ability {

    private final String name;
    private final int manaCost;
    private final int cooldown;

    private CooldownManager cooldownManager;

    public Ability(String name, int manaCost, int cooldown) {
        this.name = name;
        this.manaCost = manaCost;
        this.cooldown = cooldown;
    }

    public void setCooldownListener(CooldownManager cooldownManager) {
        this.cooldownManager = cooldownManager;
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

        // Verifica cooldown usando o CooldownManager
        if (cooldownManager != null && cooldownManager.hasCooldown(player)) {
            int remaining = cooldownManager.getRemainingTime(player);
            player.sendActionBar(Component.text("⏱ Aguarde " + remaining + "s para usar " + name + "!")
                    .color(NamedTextColor.YELLOW));
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
            return CastResult.COOLDOWN;
        }

        return CastResult.SUCCESS;
    }

    /**
     * Aplica o custo de mana e registra o cooldown
     */
    private void applyCost(RPGPlayer caster) {
        caster.setCurrentMana(caster.getCurrentMana() - manaCost);

        if (cooldownManager != null && cooldown > 0) {
            cooldownManager.startCooldown(caster.getPlayer(), name, cooldown);
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

    /**
     * Obtém o cooldown restante diretamente do CooldownManager
     */
    public int getRemainingCooldown(Player player) {
        if (cooldownManager != null) {
            return cooldownManager.getRemainingTime(player);
        }
        return 0;
    }
}