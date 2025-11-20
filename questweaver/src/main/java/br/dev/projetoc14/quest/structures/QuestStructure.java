package br.dev.projetoc14.quest.structures;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

/**
 * Representa uma estrutura que pode ser spawnada para quests
 */
public abstract class QuestStructure {

    protected final String name;
    protected final int width;
    protected final int height;
    protected final int depth;
    protected Location centerLocation;

    public QuestStructure(String name, int width, int height, int depth) {
        this.name = name;
        this.width = width;
        this.height = height;
        this.depth = depth;
    }

    /**
     * Spawna a estrutura no mundo
     */
    public void spawn(Location location) {
        this.centerLocation = location;
        build(location);
    }

    /**
     * Método abstrato para construir a estrutura
     * Cada tipo de estrutura implementa sua própria construção
     */
    protected abstract void build(Location center);

    /**
     * Remove a estrutura do mundo (cleanup)
     */
    public void remove() {
        if (centerLocation == null) return;
        clear(centerLocation);
    }

    /**
     * Limpa a área da estrutura
     */
    protected void clear(Location center) {
        World world = center.getWorld();
        if (world == null) return;

        for (int x = -width/2; x <= width/2; x++) {
            for (int y = 0; y <= height; y++) {
                for (int z = -depth/2; z <= depth/2; z++) {
                    Block block = world.getBlockAt(
                            center.getBlockX() + x,
                            center.getBlockY() + y,
                            center.getBlockZ() + z
                    );
                    block.setType(Material.AIR);
                }
            }
        }
    }

    /**
     * Verifica se uma localização está dentro da estrutura
     */
    public boolean isInside(Location location) {
        if (centerLocation == null) return false;
        if (!location.getWorld().equals(centerLocation.getWorld())) return false;

        double dx = Math.abs(location.getX() - centerLocation.getX());
        double dy = Math.abs(location.getY() - centerLocation.getY());
        double dz = Math.abs(location.getZ() - centerLocation.getZ());

        return dx <= width/2.0 && dy <= height && dz <= depth/2.0;
    }

    /**
     * Retorna a localização central da estrutura
     */
    public Location getCenterLocation() {
        return centerLocation;
    }

    public String getName() {
        return name;
    }

    /**
     * Método auxiliar para colocar blocos
     */
    protected void setBlock(Location center, int x, int y, int z, Material material) {
        World world = center.getWorld();
        if (world == null) return;

        world.getBlockAt(
                center.getBlockX() + x,
                center.getBlockY() + y,
                center.getBlockZ() + z
        ).setType(material);
    }
}