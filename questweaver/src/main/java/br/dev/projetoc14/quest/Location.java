package br.dev.projetoc14.quest;

import org.bukkit.Bukkit;
import org.bukkit.World;

public class Location extends org.bukkit.Location {

    public Location(World world, double x, double y, double z) {
        super(world, x, y, z);
    }

    public Location(String worldName, double x, double y, double z) {
        super(Bukkit.getWorld(worldName), x, y, z);
    }

    // Construtor para compatibilidade com o código existente (2D)
    public Location(int x, int z, String worldName) {
        super(Bukkit.getWorld(worldName), x, 0, z);
    }

    // Distância 2D simples (mantendo a funcionalidade original)
    public double distanceTo(Location other) {
        if (!this.getWorld().getName().equals(other.getWorld().getName())) {
            return Double.MAX_VALUE;
        }
        double dx = this.getX() - other.getX();
        double dz = this.getZ() - other.getZ();
        return Math.sqrt(dx * dx + dz * dz);
    }

    // Metodo para criar uma Location com offset
    public Location add2D(double x, double z) {
        return new Location(
                this.getWorld(),
                this.getX() + x,
                this.getY(),
                this.getZ() + z
        );
    }

    // Metodo para criar uma cópia da Location
    @Override
    public Location clone() {
        return new Location(
                this.getWorld(),
                this.getX(),
                this.getY(),
                this.getZ()
        );
    }

    // Metodo para converter ‘string’ de mundo em World do Bukkit
    public static World getWorldByName(String worldName) {
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            throw new IllegalArgumentException("World '" + worldName + "' not found!");
        }
        return world;
    }

    // Metodo para criar uma Location a partir de coordenadas ‘String’
    public static Location fromString(String worldName, String x, String z) {
        try {
            return new Location(
                    getWorldByName(worldName),
                    Double.parseDouble(x),
                    0,
                    Double.parseDouble(z)
            );
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid coordinates format");
        }
    }
}