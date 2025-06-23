package servicios;

import interfaz.PanelMapa;
import modelo.edificio.Edificio;
import modelo.edificio.Piso;
import modelo.navegacion.Punto;
import modelo.navegacion.Ruta;

import java.util.ArrayList;

public class ControladorPanel {

    private final Edificio edificio;
    private final Navegador navegador;
    private PanelMapa panelActual;

    // Punto objetivo fijo (ejemplo)
    private final Punto destino;

    public ControladorPanel(Edificio edificio) {
        this.edificio = edificio;
        this.navegador = new Navegador(edificio);
        this.destino = new Punto(53, 12, edificio.getPiso(2)); // Cambia si quieres otro
    }

    public void iniciar() {
        // Mostrar mapa inicial sin ruta, por ejemplo desde destino.getPiso()
        panelActual = new PanelMapa(destino.getPiso(), new Ruta(), this);
    }

    public void manejarClicEnMapa(int x, int y, Piso pisoClic) {
        Punto origen = new Punto(x, y, pisoClic);
        System.out.println("🖱️ Punto seleccionado: (" + x + ", " + y + ")");

        ArrayList<Ruta> rutas = navegador.calcularRutaCompleta(origen, destino, false);

        if (!rutas.isEmpty()) {
            System.out.println("✅ Ruta recalculada.");
            // Actualizar panel con nueva ruta (en el piso del punto de clic)
            panelActual.actualizarRuta(rutas.get(0), pisoClic);
        } else {
            System.out.println("❌ No se encontró una ruta desde este punto.");
        }
    }
}
