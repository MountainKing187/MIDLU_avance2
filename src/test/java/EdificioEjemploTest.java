import modelo.edificio.Edificio;
import modelo.edificio.Piso;
import modelo.elementos.Sala;
import modelo.navegacion.Punto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistencia.CargadorEdificios;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class EdificioEjemploTest {

    private Edificio edificio;

    @BeforeEach
    public void setUp() {
        try {
            edificio = CargadorEdificios.cargarDesdeJSON("src/main/resources/EdificiosJSON/EdificioEjemplo.json");
        } catch (Exception e) {
            assertNotNull(edificio, "El edificio no debería ser null");
        }
    }

    @Test
    public void testSalaCocinaExiste() {
        Optional<Sala> salaCocina = edificio.getPisos().stream()
                .flatMap(p -> p.getSalas().stream())
                .filter(s -> s.getNombre().equalsIgnoreCase("Cocina"))
                .findFirst();

        assertTrue(salaCocina.isPresent(), "La sala 'Cocina' debería existir");
    }

    @Test
    public void testSalaCocinaEstaEnPisoCorrecto() {
        Piso pisoConCocina = edificio.getPisos().stream()
                .filter(p -> p.getSalas().stream().anyMatch(s -> s.getNombre().equalsIgnoreCase("Cocina")))
                .findFirst()
                .orElse(null);

        assertNotNull(pisoConCocina, "No se encontró un piso con la sala 'Cocina'");
        assertEquals(1, pisoConCocina.getNumero(), "La sala 'Cocina' debería estar en el piso 1");
    }

    @Test
    public void testCantidadDePisos() {
        assertEquals(2, edificio.getPisos().size(), "El edificio debería tener 2 pisos");
    }

    @Test
    public void testSalaInexistenteRetornaNull() {
        Optional<Sala> sala = edificio.getPisos().stream()
                .flatMap(p -> p.getSalas().stream())
                .filter(s -> s.getNombre().equalsIgnoreCase("Ovni Room"))
                .findFirst();

        assertTrue(sala.isEmpty(), "La sala 'Ovni Room' no debería existir");
    }

}
