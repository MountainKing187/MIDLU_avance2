package servicios;

import interfaz.MapaPanel;
import interfaz.PanelPrincipal;
import modelo.elementos.Sala;
import modelo.edificio.Edificio;
import modelo.edificio.Piso;
import modelo.navegacion.Punto;
import modelo.navegacion.Ruta;

import java.util.ArrayList;
import java.util.List;

public class ControladorPanel {

    private final Edificio edificio;
    private final ArrayList<Sala> salas;
    private final Navegador navegador;
    private final PanelPrincipal ventana;

    private int pisoActual = 1;
    private Sala salaDestino;

    public ControladorPanel(CargadorInicial cargador, PanelPrincipal ventana) {
        this.edificio = cargador.getEdificio();
        this.salas = cargador.getSalas();
        this.ventana = ventana;
        this.navegador = new Navegador(edificio);
    }

    public ArrayList<Sala> getSalas() {
        return salas;
    }

    public void setSalaDestino(String nombreSala) {
        this.salaDestino = edificio.getPisos().stream()
                .flatMap(piso -> piso.getSalas().stream())
                .filter(sala -> sala.getNombre().equals(nombreSala))
                .findFirst()
                .orElseThrow();
    }




    public void iniciarMapa() {
        Piso pisoInicial = edificio.getPiso(pisoActual);
        Punto destino = getPuntoDestino();
        Punto origen = new Punto(destino.getX(), destino.getY(), pisoInicial);
        ArrayList<Ruta> rutas = navegador.calcularRutaCompleta(origen, destino, false);
        Ruta ruta = rutas.isEmpty() ? new Ruta() : rutas.get(0);

        ventana.iniciarMapa(pisoInicial, ruta, this);
        ventana.actualizarBotonesPiso(pisoActual, edificio.getPisos().size());
    }

    public void manejarClicEnMapa(int x, int y, Piso piso) {
        if (salaDestino == null) {
            System.out.println("⚠️ No hay sala destino seleccionada.");
            return;
        }

        Punto origen = new Punto(x, y, piso);
        Punto destino = getPuntoDestino();
        ArrayList<Ruta> rutas = navegador.calcularRutaCompleta(origen, destino, false);

        if (!rutas.isEmpty()) {
            MapaPanel panel = (MapaPanel) ventana.getContentPane().getComponent(0);
            panel.actualizarDatos(piso, rutas.get(0));
        } else {
            System.out.println("❌ No se encontró ruta desde el punto seleccionado.");
        }
    }

    public void cambiarPiso(int delta) {
        int nuevoPiso = pisoActual + delta;
        if (nuevoPiso < 1 || nuevoPiso > edificio.getPisos().size()) {
            System.out.println("⚠️ Piso fuera de rango.");
            return;
        }

        pisoActual = nuevoPiso;

        Piso piso = edificio.getPiso(pisoActual);
        Punto destino = getPuntoDestino();
        Punto origen = new Punto(destino.getX(), destino.getY(), piso);
        ArrayList<Ruta> rutas = navegador.calcularRutaCompleta(origen, destino, false);
        Ruta ruta = rutas.isEmpty() ? new Ruta() : rutas.get(0);

        ventana.iniciarMapa(piso, ruta, this);
        ventana.actualizarBotonesPiso(pisoActual, edificio.getPisos().size());
    }

    private Punto getPuntoDestino() {
        if (salaDestino != null && !salaDestino.getEntradas().isEmpty()) {
            return salaDestino.getEntradas().get(0); // Usa la primera entrada como destino
        }
        return new Punto(0, 0, edificio.getPiso(pisoActual)); // fallback
    }

    public List<String> getTodasLasSalas() {
        List<String> nombresSalas = new ArrayList<>();
        for (Piso piso : edificio.getPisos()) {
            for (Sala sala : piso.getSalas()) {
                nombresSalas.add(sala.getNombre());
            }
        }
        return nombresSalas;
    }

}
