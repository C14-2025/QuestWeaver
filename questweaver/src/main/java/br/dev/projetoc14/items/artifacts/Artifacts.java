package br.dev.projetoc14.items.artifacts;

import br.dev.projetoc14.player.RPGPlayer;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public abstract class Artifacts {
    protected final String name;
    protected final Material material;
    protected final ArtifactRarity rarity;
    protected final ArtifactType type;

    public Artifacts(String name, Material material, ArtifactRarity rarity, ArtifactType type) {
        this.name = name;
        this.material = material;
        this.rarity = rarity;
        this.type = type;
    }

    public abstract ItemStack createItem();
    public abstract void applyEffects(RPGPlayer player);

    public String getName() { return name; }
    public Material getMaterial() { return material; }
    public ArtifactRarity getRarity() { return rarity; }
    public ArtifactType getType() { return type; }
}