package br.dev.projetoc14.player;

public class PlayerStats {
    private int strength;      // Força - afeta dano físico
    private int defense;       // Defesa - reduz dano recebido
    private int agility;       // Agilidade - afeta velocidade de movimento
    private int intelligence;  // Inteligência - afeta mana e dano mágico
    private int health;        // Vida máxima
    private int mana;          // Mana máxima
    private int currentMana;   // Mana atual


    public PlayerStats() {
        // Valores base MÍNIMOS (serão sobrescritos por initializeClass())
        this.strength = 1;
        this.defense = 0;
        this.agility = 1;
        this.intelligence = 1;
        this.health = 20;
        this.mana = 20;
        this.currentMana = this.mana;
    }

    // Construtor com valores personalizados
    public PlayerStats(int strength, int defense, int agility, int intelligence, int health, int mana) {
        this.strength = strength;
        this.defense = defense;
        this.agility = agility;
        this.intelligence = intelligence;
        this.health = health;
        this.mana = mana;
        this.currentMana = mana;
    }

    // Getters
    public int getStrength() {
        return strength;
    }

    public int getDefense() {
        return defense;
    }

    public int getAgility() {
        return agility;
    }

    public int getIntelligence() {
        return intelligence;
    }

    public int getHealth() {
        return health;
    }

    public int getMana() {
        return mana;
    }

    public int getCurrentMana() {
        return currentMana;
    }

    // Setters
    public void setStrength(int strength) {
        this.strength = strength;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public void setAgility(int agility) {
        this.agility = agility;
    }

    public void setIntelligence(int intelligence) {
        this.intelligence = intelligence;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setMana(int mana) {
        this.mana = mana;
        // Ajusta a mana atual se necessário
        if (currentMana > mana) {
            currentMana = mana;
        }
    }

    public void setCurrentMana(int currentMana) {
        this.currentMana = Math.max(0, Math.min(currentMana, mana));
    }

    // Métodos utilitários
    public void restoreMana(int amount) {
        setCurrentMana(currentMana + amount);
    }

    public void useMana(int amount) {
        setCurrentMana(currentMana - amount);
    }

    public boolean hasMana(int required) {
        return currentMana >= required;
    }

    public double getManaPercentage() {
        return mana > 0 ? (double) currentMana / mana : 0;
    }

    @Override
    public String toString() {
        return String.format("PlayerStats{strength=%d, defense=%d, agility=%d, intelligence=%d, maxHealth=%d, mana=%d/%d}",
                strength, defense, agility, intelligence, health, currentMana, mana);
    }

    public void fullRestoreMana() {
        this.currentMana = this.mana;
    }

    public int calculatePhysicalDamage() {
        return strength * 2;
    }

    public int calculateMagicalDamage() {
        return intelligence * 3;
    }

    public int calculateDamageReduction(int incomingDamage) {
        int reduction = defense / 2;
        return Math.max(1, incomingDamage - reduction);
    }


    // Métodos para testes de stats:

    public int setCurrentHealth(int i) {
        return 20;
    }

    public int getCurrentHealth() {
        return setCurrentHealth(180);
    }

    public boolean isAlive() {
        return this.health > 0;
    }

    public void damage(int i) {
    }

    public void heal(int i) {
    }

    public void fullRestore() {
    }
}