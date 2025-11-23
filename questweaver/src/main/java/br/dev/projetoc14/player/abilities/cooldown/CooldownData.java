package br.dev.projetoc14.player.abilities.cooldown;

import org.bukkit.boss.BossBar;

public class CooldownData {
    BossBar cooldownBar;
    int remainingCooldown;

    CooldownData(BossBar cooldownBar, int remainingCooldown) {
        this.cooldownBar = cooldownBar;
        this.remainingCooldown = remainingCooldown;
    }

    void cancel() {
        cooldownBar.removeAll(); // Se precisar cancelar a barra
    }

}
