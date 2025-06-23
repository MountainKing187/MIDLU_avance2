package servicios;

import interfaz.MapaPanel;
import interfaz.PanelPrincipal;
import modelo.edificio.Edificio;
import modelo.edificio.Piso;
import modelo.navegacion.Punto;
import modelo.navegacion.Ruta;
import persistencia.CargadorEdificios;

import java.util.ArrayList;

public class ControladorPanel {

    private final Edificio edificio;
    private final Navegador navegador;
    private final PanelPrincipal ventana;
    private int pisoActual;

    public ControladorPanel(PanelPrincipal ventana) {
        try {
            this.edificio = CargadorEdificios.cargarDesdeJSON("src/main/resources/EdificiosJSON/EdificioEjemplo.json");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        this.navegador = new Navegador(edificio);
        this.ventana = ventana;
        this.pisoActual = 1;
    }

    public void iniciarMapa() {
        Piso pisoInicial = edificio.getPiso(pisoActual);
        Punto destino = getDestinoDePiso(pisoActual);
        Punto origen = new Punto(destino.getX(), destino.getY(), pisoInicial);
        ArrayList<Ruta> rutas = navegador.calcularRutaCompleta(origen, destino, false);
        Ruta ruta = rutas.isEmpty() ? new Ruta() : rutas.get(0);

        ventana.iniciarMapa(pisoInicial, ruta, this);
        ventana.actualizarBotonesPiso(pisoActual, edificio.getPisos().size());
    }

    public void manejarClicEnMapa(int x, int y, Piso piso) {
        Punto origen = new Punto(x, y, piso);
        Punto destino = getDestinoDePiso(piso.getNumero());
        ArrayList<Ruta> rutas = navegador.calcularRutaCompleta(origen, destino, false);
        if (!rutas.isEmpty()) {
            MapaPanel panel = (MapaPanel) ventana.getContentPane().getComponent(0); // Asume que solo hay uno
            panel.actualizarDatos(piso, rutas.get(0));
        } else {
            System.out.println("❌ No se encontró ruta desde el punto seleccionado.");
        }
    }

    private Punto getDestinoDePiso(int numeroPiso) {
        if (numeroPiso == 1) return new Punto(10, 10, edificio.getPiso(1));
        if (numeroPiso == 2) return new Punto(5, 5, edificio.getPiso(2));
        return new Punto(0, 0, edificio.getPiso(1)); // fallback
    }

    public void cambiarPiso(int delta) {
        int nuevoPiso = pisoActual + delta;
        if (nuevoPiso < 1 || nuevoPiso > edificio.getPisos().size()) {
            System.out.println("⚠️ Piso fuera de rango.");
            return;
        }

        pisoActual = nuevoPiso;

        Piso piso = edificio.getPiso(pisoActual);
        Punto destino = getDestinoDePiso(pisoActual);
        Punto origen = new Punto(destino.getX(), destino.getY(), piso);
        ArrayList<Ruta> rutas = navegador.calcularRutaCompleta(origen, destino, false);
        Ruta ruta = rutas.isEmpty() ? new Ruta() : rutas.get(0);

        ventana.iniciarMapa(piso, ruta, this); // 🔁 Reutilizamos iniciarMapa de PanelPrincipal
        ventana.actualizarBotonesPiso(pisoActual, edificio.getPisos().size());
    }
}
