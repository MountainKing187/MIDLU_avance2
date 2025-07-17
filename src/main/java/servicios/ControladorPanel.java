package servicios;

import interfaz.ImagenPanel;
import interfaz.MapaPanel;
import interfaz.PanelPrincipal;
import modelo.edificio.Edificio;
import modelo.edificio.Piso;
import modelo.elementos.Sala;
import modelo.navegacion.Punto;
import modelo.navegacion.Ruta;
import persistencia.CargadorEdificios;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ControladorPanel {

    private final Edificio edificio;
    private BufferedImage googleMapsImagen = null;
    private final ArrayList<Edificio> edificios;
    private final PanelPrincipal ventana;

    private int pisoActual;
    private Sala salaDestino;
    private Punto puntoOrigenUsuario;
    private ArrayList<Ruta> rutaCompleta = new ArrayList<>();

    public ControladorPanel(PanelPrincipal ventana) {
        try {
            this.edificios = CargadorEdificios.cargarEdificios("src/main/resources/EdificiosJSON/Edificios.json");
            this.edificio = edificios.getFirst();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        this.ventana = ventana;
        this.pisoActual = 1;
    }

    public void iniciarMapa(boolean necesitaAscensor) {
        Piso pisoInicial = edificio.getPiso(pisoActual);
        Ruta ruta = new Ruta(); // Ruta vacía por defecto

        if (salaDestino != null) {
            Punto destino = salaDestino.getEntradas().getFirst();
            Punto origen = destino; // este valor se utilizaria si se agrega gps al sistema

            puntoOrigenUsuario = origen;

            ArrayList<Ruta> rutas = Navegador.calcularRutaCompleta(edificio,origen, destino, necesitaAscensor);
            ruta = rutas.isEmpty() ? new Ruta() : rutas.getFirst();
        }

        ventana.iniciarMapa(edificio,pisoInicial, ruta, this, pisoActual, googleMapsImagen);
        ventana.actualizarBotonesPiso(pisoActual, edificio.getPisos().size());
    }

    public void iniciarMapaSala(Edificio edificioActual, Sala salaOrigen, Sala salaDestino, boolean necesitaAscensor) {
        Piso pisoInicial = edificioActual.getPiso(pisoActual);
        Ruta ruta = new Ruta();

        if (salaDestino != null) {
            ArrayList<Ruta> rutas = Navegador.navegarASalaASala(edificioActual,salaOrigen, salaDestino,necesitaAscensor);
            rutaCompleta = rutas;
            ruta = rutas.isEmpty() ? new Ruta() : rutas.getFirst();
        }

        ventana.iniciarMapa(edificioActual,pisoInicial, ruta, this, pisoActual, googleMapsImagen);
        ventana.actualizarBotonesPiso(pisoActual, edificioActual.getPisos().size());
    }

    public void iniciarMapaEdificios(Edificio edificioActual, Edificio edificioDestino, Sala salaOrigen,boolean necesitaAscensor) {
        googleMapsImagen = Navegador.crearRutaEdificios(edificioActual,edificioDestino);
        Piso pisoInicial = edificioActual.getPiso(pisoActual);
        Ruta ruta = new Ruta();

        ArrayList<Ruta> rutas = Navegador.navegarASalida(edificioActual,salaOrigen.getEntradas().getFirst(),necesitaAscensor);
        rutaCompleta = rutas;

        ruta = rutas.isEmpty() ? new Ruta() : rutas.getFirst();

        ventana.iniciarMapa(edificioActual,pisoInicial, ruta, this, pisoActual, googleMapsImagen);
        ventana.actualizarBotonesPiso(pisoActual, edificioActual.getPisos().size());
    }


    public void cambiarPiso(Edificio edificioActual,int delta) {
        int nuevoPiso = pisoActual + delta;
        if (nuevoPiso < 1 || nuevoPiso > edificioActual.getPisos().size()) {
            System.out.println("⚠️ Piso fuera de rango.");
            return;
        }

        pisoActual = nuevoPiso;
        Piso piso = edificioActual.getPiso(pisoActual);

        Ruta ruta = obtenerTramoParaPisoActual();

        ventana.iniciarMapa(edificioActual,piso, ruta, this, pisoActual,googleMapsImagen);
        ventana.actualizarBotonesPiso(pisoActual, edificioActual.getPisos().size());
    }

    public void setSalaDestino(String nombreSala) {
        this.salaDestino = edificio.getPisos().stream()
                .flatMap(p -> p.getSalas().stream())
                .filter(s -> s.getNombre().equals(nombreSala))
                .findFirst()
                .orElseThrow();
    }

    public List<String> getTodasLasSalas() {
        List<String> salas = new ArrayList<>();
        for (Edificio edificio : edificios){
            for (Piso piso : edificio.getPisos()){
                for (Sala sala : piso.getSalas()) {
                    salas.add(sala.getNombre());
                }
            }
        }
        return  salas;
    }

    public List<Sala> getSalasEdificios(Edificio edificio){
        List<Sala> salas = new ArrayList<>();
        for (Piso piso : edificio.getPisos()){
            salas.addAll(piso.getSalas());
        }
        return  salas;
    }

    private void recalcularRuta() {
        if (puntoOrigenUsuario != null && salaDestino != null) {
            rutaCompleta = Navegador.calcularRutaCompleta(edificio,
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
            if (!r.getPuntos().isEmpty() && r.getPuntos().getFirst().getPiso().getNumero() == pisoActual) {
                return r;
            }
        }
        return tramo; // ruta vacía si no hay tramo en ese piso
    }

    public ArrayList<Edificio> getEdificios() {
        return edificios;
    }

    public List<String> getNombresEdificios(){
        List<String> nombres = new ArrayList<>();
        for (Edificio edificio : edificios){
            nombres.add(edificio.getNombre());
        }
        return nombres;
    }
}
