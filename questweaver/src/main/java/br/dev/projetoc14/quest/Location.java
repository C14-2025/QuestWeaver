package br.dev.projetoc14.quest;

/*
 * Localização simples com coordenadas X e Z.
 * Serve para saber onde o jogador está e calcular distâncias.
 */

public class Location {
    private final int x, z; // Só X e Z por enquanto, sem Y
    private final String world;

    public Location(int x, int z, String world) {
        this.x = x;
        this.z = z;
        this.world = world;
    }

    // Distância 2D simples
    public double distanceTo(Location other) {
        if (!this.world.equals(other.world)) {
            return Double.MAX_VALUE;
        }
        int dx = this.x - other.x;
        int dz = this.z - other.z;
        return Math.sqrt(dx * dx + dz * dz);
    }

    // Getters
    public int getX() { return x; }
    public int getZ() { return z; }
    public String getWorld() { return world; }
}