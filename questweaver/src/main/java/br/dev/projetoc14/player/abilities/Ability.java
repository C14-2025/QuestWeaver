package br.dev.projetoc14.player.abilities;

import br.dev.projetoc14.player.RPGPlayer;
import java.util.*;

public abstract class Ability {

    private final String name;
    private int manaCost;
    private int cooldown;
    private final Map<UUID, Long> lastUsed = new HashMap<>();

    public Ability(String name, int manaCost, int cooldown) {
        this.name = name;
        this.manaCost = manaCost;
        this.cooldown = cooldown;
    }

    // Método abstrato que cada habilidade implementa
    protected abstract void onCast(RPGPlayer caster);

    /**
     * Método principal para executar a habilidade.
     * Chama a lógica customizada (onCast) e aplica o custo.
     */
    public final void cast(RPGPlayer caster) {
        // 1. Executa a lógica da habilidade customizada
        this.onCast(caster);

        // 2. Aplica o custo de mana e registra o uso
        this.applyCost(caster);
    }

    /**
     * Verifica se o jogador pode usar a habilidade
     * Checa: mana suficiente + cooldown
     */
    public boolean canCast(RPGPlayer caster) {

        // Verifica mana
        if (caster.getCurrentMana() < manaCost) {
            caster.sendMessage("§c❌ Mana insuficiente! Necessário: " + manaCost);
            return false;
        }

        // Verifica cooldown
        UUID playerId = caster.getUniqueId();
        if (lastUsed.containsKey(playerId)) {
            long timeElapsed = (System.currentTimeMillis() - lastUsed.get(playerId)) / 1000;
            long timeRemaining = cooldown - timeElapsed;

            if (timeRemaining > 0) {
                caster.sendMessage("§c⏳ Aguarde " + timeRemaining + "s para usar " + name + " novamente!");
                return false;
            }
        }

        return true;
    }

    /**
     * Aplica o custo de mana e registra o uso (cooldown)
     */
    private void applyCost(RPGPlayer caster) {
        // Remove a mana
        int newMana = caster.getCurrentMana() - manaCost;
        caster.setCurrentMana(newMana);

        // Registra o momento do uso para cooldown
        lastUsed.put(caster.getUniqueId(), System.currentTimeMillis());
    }
}