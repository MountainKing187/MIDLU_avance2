package servicios;

import interfaz.MapaPanel;
import interfaz.PanelPrincipal;
import modelo.edificio.Edificio;
import modelo.edificio.Piso;
import modelo.elementos.Sala;
import modelo.navegacion.Punto;
import modelo.navegacion.Ruta;
import persistencia.CargadorEdificios;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ControladorPanel {

    private final Edificio edificio;
    private final Navegador navegador;
    private final PanelPrincipal ventana;

    private int pisoActual;
    private Sala salaDestino;
    private Punto puntoOrigenUsuario;
    private ArrayList<Ruta> rutaCompleta = new ArrayList<>(); // NUEVO: guarda la ruta total

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

        Ruta ruta = new Ruta(); // vacía por defecto

        if (salaDestino != null) {
            Punto destino = salaDestino.getEntradas().get(0);
            Punto origen = destino; // por ahora
            puntoOrigenUsuario = origen;

            rutaCompleta = navegador.calcularRutaCompleta(origen, destino, false);
            ruta = obtenerTramoParaPisoActual();
        }

        ventana.iniciarMapa(pisoInicial, ruta, this);
        ventana.actualizarBotonesPiso(pisoActual, edificio.getPisos().size());
    }

    public void manejarClicEnMapa(int x, int y, Piso piso) {
        puntoOrigenUsuario = new Punto(x, y, piso);
        recalcularRuta();
    }

    public void cambiarPiso(int delta) {
        int nuevoPiso = pisoActual + delta;
        if (nuevoPiso < 1 || nuevoPiso > edificio.getPisos().size()) {
            System.out.println("⚠️ Piso fuera de rango.");
            return;
        }

        pisoActual = nuevoPiso;
        Piso piso = edificio.getPiso(pisoActual);

        Ruta ruta = obtenerTramoParaPisoActual();

        ventana.iniciarMapa(piso, ruta, this);
        ventana.actualizarBotonesPiso(pisoActual, edificio.getPisos().size());
    }

    public void setSalaDestino(String nombreSala) {
        this.salaDestino = edificio.getPisos().stream()
                .flatMap(p -> p.getSalas().stream())
                .filter(s -> s.getNombre().equals(nombreSala))
                .findFirst()
                .orElseThrow();
    }

    public List<String> getTodasLasSalas() {
        return edificio.getPisos().stream()
                .flatMap(p -> p.getSalas().stream())
                .map(Sala::getNombre)
                .collect(Collectors.toList());
    }

    private void recalcularRuta() {
        if (puntoOrigenUsuario != null && salaDestino != null) {
            rutaCompleta = navegador.calcularRutaCompleta(
                    puntoOrigenUsuario,
                    salaDestino.getEntradas().get(0),
                    false
            );
            if (!rutaCompleta.isEmpty()) {
                Ruta ruta = obtenerTramoParaPisoActual();
                MapaPanel panel = (MapaPanel) ventana.getContentPane().getComponent(0);
                panel.actualizarDatos(edificio.getPiso(pisoActual), ruta);
            }
        }
    }

    // NUEVO: filtra el tramo de la ruta correspondiente al piso actual
    private Ruta obtenerTramoParaPisoActual() {
        Ruta tramo = new Ruta();
        for (Ruta r : rutaCompleta) {
            if (!r.getPuntos().isEmpty() && r.getPuntos().get(0).getPiso().getNumero() == pisoActual) {
                return r;
            }
        }
        return tramo; // ruta vacía si no hay tramo en ese piso
    }
}
