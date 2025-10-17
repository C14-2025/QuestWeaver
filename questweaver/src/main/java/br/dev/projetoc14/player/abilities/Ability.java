package br.dev.projetoc14.player.abilities;

import br.dev.projetoc14.player.RPGPlayer;

public abstract class Ability {
    private String name;
    private int manaCost;
    private int cooldown; // em segundos

    public Ability(String name, int manaCost, int cooldown) {
        this.name = name;
        this.manaCost = manaCost;
        this.cooldown = cooldown;
    }

    // Méto do que recebe o player que irá 'castar' a habilidade
    public abstract void cast(RPGPlayer caster);

    // getters
    public String getName() { return name; }
    public int getManaCost() { return manaCost; }
    public int getCooldown() { return cooldown; }
}
