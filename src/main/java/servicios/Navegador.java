package servicios;

import modelo.*;
import modelo.edificio.Edificio;
import modelo.edificio.Piso;
import modelo.elementos.Sala;
import modelo.navegacion.Punto;
import modelo.navegacion.Ruta;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.HashMap;
import java.util.Map;

public class Navegador {
    private final Edificio edificio;
    private final Pathfinding pathfinding;

    public Navegador(Edificio edificio) {
        this.edificio = edificio;
        this.pathfinding = new Pathfinding();
    }

    /**
     * Calcula la ruta completa desde la ubicación actual hasta un destino,
     * considerando posibles cambios de piso.
     *
     * @param inicio Punto de inicio
     * @param destino Punto de destino
     * @param evitarEscaleras Si true, solo usará ascensores para cambios de piso
     * @return Lista de rutas segmentadas por pisos
     */
    public ArrayList<Ruta> calcularRutaCompleta(Punto inicio, Punto destino, boolean evitarEscaleras) {
        ArrayList<Ruta> rutasSegmentadas = new ArrayList<>();

        // Caso simple: mismo piso
        if (inicio.getPiso().equals(destino.getPiso())) {
            Ruta ruta = pathfinding.encontrarRuta(inicio, destino, evitarEscaleras);
            if (!ruta.getPuntos().isEmpty()) {
                rutasSegmentadas.add(ruta);
            }
            return rutasSegmentadas;
        }

        // Encontrar secuencia óptima de puntos de acceso
        List<PuntoAcceso> puntosAccesoOptimos = encontrarSecuenciaPuntosAcceso(
                inicio.getPiso(),
                destino.getPiso(),
                evitarEscaleras
        );

        // No se encontró conexión entre pisos
        if (puntosAccesoOptimos.isEmpty()) {
            return rutasSegmentadas;
        }

        // Calcular ruta en piso inicial hasta el primer punto de acceso
        Ruta rutaInicial = pathfinding.encontrarRuta(
                inicio,
                puntosAccesoOptimos.get(0).getUbicacion(),
                evitarEscaleras
        );

        if (!rutaInicial.getPuntos().isEmpty()) {
            rutasSegmentadas.add(rutaInicial);
        }

        // Calcular rutas intermedias y cambios de piso
        for (int i = 0; i < puntosAccesoOptimos.size() - 1; i++) {
            PuntoAcceso actual = puntosAccesoOptimos.get(i);
            PuntoAcceso siguiente = puntosAccesoOptimos.get(i + 1);

            // Cambio de piso (representado como ruta especial)
            Ruta cambioPiso = new Ruta();
            cambioPiso.agregarPunto(actual.getUbicacion());
            cambioPiso.agregarPunto(siguiente.getUbicacion());
            rutasSegmentadas.add(cambioPiso);

            // Ruta en el nuevo piso entre puntos de acceso
            if (i < puntosAccesoOptimos.size() - 2) {
                Ruta rutaIntermedia = pathfinding.encontrarRuta(
                        siguiente.getUbicacion(),
                        puntosAccesoOptimos.get(i + 2).getUbicacion(),
                        evitarEscaleras
                );

                if (!rutaIntermedia.getPuntos().isEmpty()) {
                    rutasSegmentadas.add(rutaIntermedia);
                }
            }
        }

        // Calcular ruta final en el último piso
        PuntoAcceso ultimoPuntoAcceso = puntosAccesoOptimos.get(puntosAccesoOptimos.size() - 1);
        Ruta rutaFinal = pathfinding.encontrarRuta(
                ultimoPuntoAcceso.getUbicacion(),
                destino,
                evitarEscaleras
        );

        if (!rutaFinal.getPuntos().isEmpty()) {
            rutasSegmentadas.add(rutaFinal);
        }

        return rutasSegmentadas;
    }

    /**
     * Encuentra la mejor secuencia de puntos de acceso para conectar dos pisos.
     */
    private List<PuntoAcceso> encontrarSecuenciaPuntosAcceso(Piso inicio, Piso destino, boolean evitarEscaleras) {
        // Usaremos BFS para encontrar el camino más corto entre pisos
        Map<Piso, Piso> pisoAnterior = new HashMap<>();
        Map<Piso, PuntoAcceso> puntoAccesoUsado = new HashMap<>();
        Queue<Piso> cola = new LinkedList<>();

        cola.add(inicio);
        pisoAnterior.put(inicio, null);

        while (!cola.isEmpty()) {
            Piso actual = cola.poll();

            // Si llegamos al destino, reconstruimos el camino
            if (actual.equals(destino)) {
                return reconstruirSecuenciaPuntosAcceso(pisoAnterior, puntoAccesoUsado, inicio, destino);
            }

            // Explorar todos los puntos de acceso del piso actual
            for (PuntoAcceso pa : actual.getPuntosAcceso()) {
                // Filtrar por accesibilidad si es necesario
                if (evitarEscaleras && !pa.esAccesibleParaDiscapacitados()) {
                    continue;
                }

                PuntoAcceso paConectado = pa.getPuntoConectado();
                if (paConectado == null) continue;

                Piso pisoConectado = paConectado.getPiso();

                // Si no hemos visitado este piso
                if (!pisoAnterior.containsKey(pisoConectado)) {
                    pisoAnterior.put(pisoConectado, actual);
                    puntoAccesoUsado.put(pisoConectado, pa);
                    cola.add(pisoConectado);
                }
            }
        }

        // No se encontró camino
        return Collections.emptyList();
    }

    /**
     * Reconstruye la secuencia de puntos de acceso usados en el camino entre pisos.
     */
    private List<PuntoAcceso> reconstruirSecuenciaPuntosAcceso(
            Map<Piso, Piso> pisoAnterior,
            Map<Piso, PuntoAcceso> puntoAccesoUsado,
            Piso inicio,
            Piso destino
    ) {
        LinkedList<PuntoAcceso> secuencia = new LinkedList<>();
        Piso actual = destino;

        // Reconstruir desde el destino hacia el inicio
        while (!actual.equals(inicio)) {
            PuntoAcceso pa = puntoAccesoUsado.get(actual);
            if (pa == null) break;

            secuencia.addFirst(pa.getPuntoConectado()); // Punto de llegada
            secuencia.addFirst(pa);                     // Punto de salida

            actual = pisoAnterior.get(actual);
        }

        return secuencia;
    }

    /**
     * Calcula la ruta a una sala, seleccionando automáticamente la entrada óptima.
     */
    public ArrayList<Ruta> calcularRutaASala(Usuario usuario, Sala sala) {
        ArrayList<Ruta> mejorRuta = null;
        double menorDistancia = Double.MAX_VALUE;

        for (Punto entrada : sala.getEntradas()) {
            ArrayList<Ruta> rutaCandidata = calcularRutaCompleta(
                    usuario.getUbicacion(),
                    entrada,
                    usuario.tieneDiscapacidad()
            );

            double distancia = calcularDistanciaTotal(rutaCandidata);
            if (distancia < menorDistancia && !rutaCandidata.isEmpty()) {
                menorDistancia = distancia;
                mejorRuta = rutaCandidata;
            }
        }

        return mejorRuta != null ? mejorRuta : new ArrayList<>();
    }

    /**
     * Calcula la distancia total de una lista de rutas segmentadas.
     */
    private double calcularDistanciaTotal(ArrayList<Ruta> rutas) {
        return rutas.stream()
                .mapToDouble(Ruta::getDistanciaTotal)
                .sum();
    }
}