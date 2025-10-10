package br.dev.projetoc14.skilltree;

public interface Player {
    int getXp();
    void removerXp(int xp);
    void abrirInterfaceHabilidades(SkillTree arvore);
}
