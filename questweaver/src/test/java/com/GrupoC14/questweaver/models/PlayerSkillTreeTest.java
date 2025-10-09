package com.GrupoC14.questweaver.models;
import br.dev.projetoc14.skilltree.Player;
import br.dev.projetoc14.skilltree.PlayerSkillTree;
import br.dev.projetoc14.skilltree.SkillTree;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class PlayerSkillTreeTest {

    @Test
    void testDesbloquearHabilidadeComMock() {
        // mock do jogador
        Player jogador = mock(Player.class);
        when(jogador.getXp()).thenReturn(100);

        SkillTree arvore = new SkillTree();
        arvore.adicionarHabilidade("Bola de Fogo", 50);

        PlayerSkillTree interacao = new PlayerSkillTree(arvore);
        boolean desbloqueou = interacao.tentarDesbloquear(jogador, "Bola de Fogo");

        assertTrue(desbloqueou);
        verify(jogador, times(1)).removerXp(50); // verifica se XP foi consumido
    }

    @Test
    void testAbrirInterfaceHabilidades() {
        Player jogador = mock(Player.class);
        SkillTree arvore = new SkillTree();

        PlayerSkillTree interacao = new PlayerSkillTree(arvore);
        interacao.abrirInterface(jogador);

        verify(jogador, times(1)).abrirInterfaceHabilidades(arvore);
    }
}
