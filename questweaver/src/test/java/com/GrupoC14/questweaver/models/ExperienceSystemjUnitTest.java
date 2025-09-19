package com.GrupoC14.questweaver.models;
import br.dev.projetoc14.skilltree.ExperienceSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ExperienceSystemjUnitTest {

    private ExperienceSystem experienceSystem;

    @BeforeEach
    void setup() {
        experienceSystem = new ExperienceSystem();
    }

    @Test
    void deveDarXPQuandoMobChamadoBichoEhMorto() {
        int novoNivel = experienceSystem.calcularNovoNivel("Bicho", 10);
        assertEquals(11, novoNivel, "O nível deveria aumentar em 1");
    }

    @Test
    void naoDeveDarXPQuandoMobNaoTemNome() {
        int novoNivel = experienceSystem.calcularNovoNivel(null, 5);
        assertEquals(5, novoNivel, "O nível não deveria mudar");
    }

    @Test
    void naoDevePassarDoNivelMaximo() {
        int novoNivel = experienceSystem.calcularNovoNivel("Bicho", 15);
        assertEquals(15, novoNivel, "O nível não deveria passar do máximo");
    }
}
