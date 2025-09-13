package br.dev.projetoc14.player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SkillTree {

    private Map<String, Integer> habilidades = new HashMap<>();
    private Set<String> desbloqueadas = new HashSet<>();

    // nova habilidade com custo de XP
    public void adicionarHabilidade(String nome, int custoXP) {
        habilidades.put(nome, custoXP);
    }

    // desbloquear habilidade usando XP atual do personagem
    public boolean desbloquear(String habilidade, int xpDisponivel) {
        if (habilidades.containsKey(habilidade) && xpDisponivel >= habilidades.get(habilidade)) {
            desbloqueadas.add(habilidade);
            return true;
        }
        return false;
    }

    // verificar se já está desbloqueada
    public boolean possui(String habilidade) {
        return desbloqueadas.contains(habilidade);
    }

    // custo de uma habilidade
    public int getCusto(String habilidade) {
        return habilidades.getOrDefault(habilidade, -1);
    }

    // todas as habilidades cadastradas
    public Set<String> getTodasHabilidades() {
        return habilidades.keySet();
    }

    // habilidades desbloqueadas
    public Set<String> getDesbloqueadas() {
        return desbloqueadas;
    }
}

