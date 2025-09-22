package br.dev.projetoc14.skilltree;

public class PlayerSkillTree {
    private final SkillTree arvore;

    public PlayerSkillTree(SkillTree arvore) {
        this.arvore = arvore;
    }

    public boolean tentarDesbloquear(Player jogador, String habilidade) {
        int custo = arvore.getCusto(habilidade);
        if (custo > 0 && jogador.getXp() >= custo) {
            jogador.removerXp(custo); // consome XP
            return arvore.desbloquear(habilidade, jogador.getXp());
        }
        return false;
    }

    public void abrirInterface(Player jogador) {
        jogador.abrirInterfaceHabilidades(arvore);
    }
}
