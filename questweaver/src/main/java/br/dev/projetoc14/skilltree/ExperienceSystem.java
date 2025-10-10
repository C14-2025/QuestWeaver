package br.dev.projetoc14.skilltree;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Listener;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.HashMap;
import java.util.Map;


public class ExperienceSystem implements Listener {

    private static final int maxLevel = 15; // nível máximo que o player pode atingir
    private static final Map<String, Integer> mobMap = new HashMap<>();
    static {
        mobMap.put("Bicho", 1);
        mobMap.put("Esqueleto", 5);
        mobMap.put("Chefe", 10);
    }

    /**
     * Esse metodo tem o intuito de sempre que um mob da lista acima for morto
     * retornar a quantidade de XP equivalente à chave
     * @param e
     *          event handler
     *
     **/
    @EventHandler
    public void onMobDeath(EntityDeathEvent e)
    {
        LivingEntity mob = e.getEntity();
        Player killer = mob.getKiller();

        if (killer == null) return;

        String mobName = mob.getCustomName();

        if(mobMap.containsKey(mobName)){
            if(killer.getLevel() + mobMap.get(mobName) >= maxLevel)
            {
                killer.setLevel(maxLevel);
                killer.sendMessage("§a[QuestWeaver] Nível máximo atingido.");
            }
            else if (killer.getLevel() < maxLevel)
            {
                Integer xp = mobMap.get(mobName);
                killer.giveExpLevels(xp);
                killer.sendMessage("§a+" + xp + ".");
            } else
                killer.sendMessage("§a[QuestWeaver] Nível máximo atingido.");

        }
    }

    /**
     * Metodo dedicado à fazer o calculo do novo nivel do jogador
     * @param pMobName
     *                  nome do mob que o player está matando
     * @param pNivelAtual
     *                    nível atual do jogador
     **/
    public int calcularNovoNivel(String pMobName, int pNivelAtual)
    {

        if (pMobName == null) return pNivelAtual;

        if (mobMap.containsKey(pMobName) && pNivelAtual < maxLevel) {
            return pNivelAtual + mobMap.get(pMobName);
        }
        return pNivelAtual; // não ganha XP
    }

    // retorna a quantidade total de níveis do jogador
    public static int getLevel(Player player) {
        return player.getLevel();
    }

    // remove níveis do jogador (sem deixar abaixo de 0)
    public static void removeLevels(Player player, int amount) {
        int newLevel = Math.max(0, player.getLevel() - amount);
        player.setLevel(newLevel);
    }

}
