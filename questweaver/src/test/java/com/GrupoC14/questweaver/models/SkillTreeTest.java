package com.GrupoC14.questweaver.models;

import br.dev.projetoc14.skilltree.SkillTree;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SkillTreeTest {

    // testes positivos
    @Test
    void testAdicionarHabilidade() {
        SkillTree arvore = new SkillTree();
        arvore.adicionarHabilidade("Bola de Fogo", 50);

        assertEquals(50, arvore.getCusto("Bola de Fogo"));
        assertTrue(arvore.getTodasHabilidades().contains("Bola de Fogo"));
    }

    @Test
    void testDesbloquearComXpSuficiente() {
        SkillTree arvore = new SkillTree();
        arvore.adicionarHabilidade("Raio Congelante", 80);

        assertTrue(arvore.desbloquear("Raio Congelante", 100));
        assertTrue(arvore.possui("Raio Congelante"));
    }

    @Test
    void testNaoDesbloquearSemXp() {
        SkillTree arvore = new SkillTree();
        arvore.adicionarHabilidade("Cura", 60);

        assertFalse(arvore.desbloquear("Cura", 40));
        assertFalse(arvore.possui("Cura"));
    }

    @Test
    void testListarDesbloqueadas() {
        SkillTree arvore = new SkillTree();
        arvore.adicionarHabilidade("Teleportar", 100);
        arvore.desbloquear("Teleportar", 150);

        assertEquals(1, arvore.getDesbloqueadas().size());
        assertTrue(arvore.getDesbloqueadas().contains("Teleportar"));
    }

    // testes negativos
    @Test
    void testCustoDeHabilidadeInexistente() {
        SkillTree arvore = new SkillTree();
        assertEquals(-1, arvore.getCusto("Invisibilidade")); // deve retornar -1
    }

    @Test
    void testNaoDesbloquearHabilidadeInexistente() {
        SkillTree arvore = new SkillTree();
        // tentar desbloquear sem nem ter adicionado
        assertFalse(arvore.desbloquear("Meteoro", 500));
        assertFalse(arvore.possui("Meteoro"));
    }

    @Test
    void testAdicionarHabilidadeDuplicadaSubstituiCusto() {
        SkillTree arvore = new SkillTree();
        arvore.adicionarHabilidade("Bola de Fogo", 50);
        arvore.adicionarHabilidade("Bola de Fogo", 100);

        // custo deve ser atualizado para 100
        assertEquals(100, arvore.getCusto("Bola de Fogo"));
    }
}

