package br.dev.projetoc14.quest;

import br.dev.projetoc14.QuestWeaver;
import br.dev.projetoc14.quest.utils.QuestCompletedEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

/**
 * Quest base para desafios de combate com suporte a arenas
 */
public abstract class HitQuest extends Quest {
    protected final String targetMob;
    protected final int targetCount;
    protected int currentCount;
    protected final Location questLocation;
    protected final List<Material> validWeapons;
    protected boolean environmentBuilt = false;

    // Controle de mobs spawnados
    protected List<Entity> spawnedEntities = new ArrayList<>();

    public HitQuest(String id, String name, String description, int experienceReward,
                    String targetMob, int targetCount, int currentCount,
                    Location questLocation, List<Material> validWeapons) {
        super(id, name, description, experienceReward);
        this.targetMob = targetMob;
        this.targetCount = targetCount;
        this.currentCount = currentCount;
        this.questLocation = questLocation;
        this.validWeapons = validWeapons != null ? validWeapons : new ArrayList<>();
    }

    /**
     * Método abstrato para construir o ambiente da quest
     * Cada subclasse implementa sua própria arena/campo de batalha
     */
    public abstract void buildQuestEnvironment(Player player);

    /**
     * Método para spawnar entidades de forma estratégica
     * Diferente do método antigo que spawnava em cima do jogador
     */
    public abstract void spawnStrategicEntities(Player player);

    /**
     * Limpa a arena e remove entidades quando a quest terminar
     */
    public void cleanupEnvironment(Player player) {
        // Remove todas as entidades spawnadas
        for (Entity entity : spawnedEntities) {
            if (entity != null && entity.isValid()) {
                entity.remove();
            }
        }
        spawnedEntities.clear();

        // Opcional: Remover blocos da arena (dependendo da implementação)
        // cleanupArenaBlocks(player);
    }

    @Override
    public ItemStack[] getRewardItems() {
        // Recompensas base - pode ser sobrescrito por subclasses
        return new ItemStack[]{
                new ItemStack(Material.ARROW, 16),
                new ItemStack(Material.EXPERIENCE_BOTTLE, 3)
        };
    }

    @Override
    public void assignToPlayer(Player player) {
        // Constrói o ambiente primeiro
        buildQuestEnvironment(player);

        // Depois spawna as entidades
        spawnStrategicEntities(player);

        player.sendMessage("§a🎯 " + getName() + " iniciada!");
        player.sendMessage("§7" + getDescription());
    }

    @Override
    public boolean checkCompletion() {
        return currentCount >= targetCount;
    }

    @Override
    public void updateProgress(Object... params) {
        if (params.length >= 3 &&
                params[0] instanceof String mobType &&
                params[1] instanceof Material weapon &&
                params[2] instanceof Player player) {

            boolean validHit = false;

            // Validação com projétil (para quests de arqueiro)
            if (params.length >= 4 && params[3] instanceof Arrow arrow) {
                if (mobType.equalsIgnoreCase(targetMob) && isValidProjectile(arrow)) {
                    validHit = true;
                }
            }
            // Validação com arma normal (para quests de combate corpo-a-corpo)
            else if (mobType.equalsIgnoreCase(targetMob) && isValidWeapon(weapon)) {
                validHit = true;
            }

            if (validHit) {
                currentCount++;
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);

                // Feedback visual
                if (currentCount % 3 == 0) { // A cada 3 acertos
                    player.sendMessage("§a✦ " + getProgressText());
                }

                if (checkCompletion()) {
                    player.sendMessage("§6🎉 " + getName() + " completada!");
                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);

                    // Limpa o ambiente
                    cleanupEnvironment(player);

                    QuestCompletedEvent customEvent = new QuestCompletedEvent(player, this);
                    Bukkit.getServer().getPluginManager().callEvent(customEvent);
                }
            }
        }
    }

    // Getters
    public int getCurrentCount() {
        return currentCount;
    }

    public int getTargetCount() {
        return targetCount;
    }

    public String getProgressText() {
        return String.format("%d/%d %s", currentCount, targetCount,
                targetMob.toLowerCase() + (targetCount > 1 ? "s" : ""));
    }

    /**
     * Valida se a arma usada é permitida para esta quest
     */
    protected boolean isValidWeapon(Material weapon) {
        return validWeapons.isEmpty() || validWeapons.contains(weapon);
    }

    /**
     * Valida projéteis especiais - para ser sobrescrito por subclasses
     */
    protected boolean isValidProjectile(Arrow arrow) {
        return true; // Aceita qualquer projétil por padrão
    }

    /**
     * Método utilitário para spawnar entidade com configurações padrão
     */
    protected Entity spawnQuestEntity(World world, Location location, EntityType type, String displayName) {
        Entity entity = world.spawnEntity(location, type);

        // Configurações comuns para todos os mobs de quest
        if (entity instanceof LivingEntity livingEntity) {
            livingEntity.setAI(true);
            livingEntity.setRemoveWhenFarAway(false);

            // Previne que mobs queimem no sol
            if (livingEntity instanceof Zombie zombie) {
                zombie.setShouldBurnInDay(false);
            }
            if (livingEntity instanceof Skeleton skeleton) {
                skeleton.setShouldBurnInDay(false);
            }

            // Configurações específicas por tipo
            if (livingEntity instanceof Creeper creeper) {
                creeper.setMaxFuseTicks(60); // Mais tempo antes de explodir
            }
        }

        // Marca como entidade da quest
        NamespacedKey key = new NamespacedKey(QuestWeaver.getInstance(), "quest_target");
        entity.getPersistentDataContainer().set(key, PersistentDataType.BYTE, (byte) 1);

        if (displayName != null) {
            entity.customName(Component.text(displayName));
            entity.setCustomNameVisible(true);
        }

        spawnedEntities.add(entity);
        return entity;
    }

    /**
     * Método utilitário para colocar blocos de forma segura
     */
    protected void setBlockSafe(World world, Location location, Material material) {
        Block block = world.getBlockAt(location);
        Material currentType = block.getType();

        // Permite substituir apenas blocos "inofensivos"
        if (currentType == Material.AIR ||
                currentType == Material.SHORT_GRASS ||  // Grama baixa (planta)
                currentType == Material.TALL_GRASS ||   // Grama alta
                currentType == Material.FERN ||         // Samambaia
                currentType == Material.LARGE_FERN ||   // Samambaia grande
                currentType == Material.DEAD_BUSH ||    // Arbusto morto
                currentType == Material.VINE ||         // Vinha
                currentType == Material.SNOW ||         // Neve
                currentType == Material.WATER ||        // Água (se quiser permitir)
                currentType == Material.LAVA) {         // Lava (se quiser permitir)
            block.setType(material);
        }
        // Opcional: também permitir substituir alguns blocos naturais comuns
        else if (currentType == Material.DIRT ||
                currentType == Material.COARSE_DIRT ||
                currentType == Material.PODZOL ||
                currentType == Material.ROOTED_DIRT ||
                currentType == Material.MOSS_BLOCK ||
                currentType == Material.STONE ||
                currentType == Material.COBBLESTONE ||
                currentType == Material.GRAVEL ||
                currentType == Material.SAND) {
            block.setType(material);
        }
    }

    /**
     * Cria uma plataforma simples
     */
    protected void createPlatform(World world, Location center, int size, Material material) {
        for (int x = -size; x <= size; x++) {
            for (int z = -size; z <= size; z++) {
                setBlockSafe(world, center.clone().add(x, -1, z), material);
            }
        }
    }

    /**
     * Cria paredes ao redor de uma área
     */
    protected void createWalls(World world, Location center, int radius, int height, Material material) {
        for (int y = 0; y < height; y++) {
            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    if (Math.abs(x) == radius || Math.abs(z) == radius) {
                        setBlockSafe(world, center.clone().add(x, y, z), material);
                    }
                }
            }
        }
    }

    /**
     * Método antigo - mantido para compatibilidade, mas não recomendado
     * @deprecated Use spawnStrategicEntities instead
     */
    @Deprecated
    public void spawnTargetEntities(Player player) {
        // Implementação antiga - não usar
        if (questLocation == null) return;

        EntityType entityType;
        try {
            entityType = EntityType.valueOf(targetMob.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Tipo de entidade inválido: " + targetMob);
            return;
        }

        World world = player.getWorld();
        for (int i = 0; i < targetCount; i++) {
            Location randomLocation = new Location(
                    world,
                    questLocation.getX() + (Math.random() * 6 - 2),
                    player.getLocation().getY(),
                    questLocation.getZ() + (Math.random() * 6 - 2)
            );

            spawnQuestEntity(world, randomLocation, entityType, "Alvo de Quest");
        }
    }

    @Override
    public void giveRewards(Player player) {
        // Garante que o ambiente seja limpo antes de dar recompensas
        cleanupEnvironment(player);
        super.giveRewards(player);
    }
}