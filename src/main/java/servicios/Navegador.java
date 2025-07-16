package servicios;

import modelo.edificio.Edificio;
import modelo.edificio.Piso;
import modelo.elementos.Sala;
import modelo.navegacion.Punto;
import modelo.PuntoAcceso;
import modelo.navegacion.Ruta;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.*;
import java.util.List;

public class Navegador {
    private final ArrayList<Edificio> edificios = new ArrayList<Edificio>();

    public Navegador(Edificio edificio) {}

    /**
     * Calcula la ruta más corta entre dos puntos, que pueden estar en diferentes pisos.
     * Si los puntos están en el mismo piso, devuelve una lista con una única ruta.
     * Si están en pisos diferentes, devuelve una lista de rutas segmentadas
     * (ruta en piso origen -> acceso -> ruta en piso intermedio -> ... -> acceso -> ruta en piso destino).
     *
     * @param edificio El edificio en que se hara la navegacion
     * @param inicio El punto de inicio de la navegación.
     * @param destino El punto de destino de la navegación.
     * @param evitarEscaleras Indica si se deben evitar las escaleras (true) o si se pueden usar (false).
     * @return Un ArrayList de objetos Ruta que representan la secuencia de rutas a seguir.
     * Devuelve una lista vacía si no se encuentra una ruta.
     */
    public static ArrayList<Ruta> calcularRutaCompleta(Edificio edificio, Punto inicio, Punto destino, boolean evitarEscaleras) {
        ArrayList<Ruta> rutasSegmentadas = new ArrayList<>();

        // Caso 1: Inicio y destino en el mismo piso
        if (inicio.getPiso().equals(destino.getPiso())) {
            Ruta ruta = Pathfinding.encontrarRuta(inicio, destino);
            if (!ruta.getPuntos().isEmpty()) {
                rutasSegmentadas.add(ruta);
            }
            return rutasSegmentadas;
        }

        // Caso 2: Inicio y destino en diferentes pisos
        // Buscar todas las secuencias posibles de PuntosAcceso que conectan los pisos.
        // Se establece una profundidad máxima para evitar búsquedas infinitas en edificios complejos.
        List<List<PuntoAcceso>> todasSecuencias = encontrarTodasSecuenciasPuntosAcceso(
                inicio.getPiso(),
                destino.getPiso(),
                evitarEscaleras,
                edificio.getPisos().size() * 2 // Una heurística para maxProfundidad: el doble del número de pisos.
                // Esto es para asegurar que se pueda ir y volver si es necesario,
                // pero sin demasiada recursión.
        );

        // Si no se encuentra ninguna secuencia de puntos de acceso, no hay ruta entre pisos.
        if (todasSecuencias.isEmpty()) {
            System.out.println("No se encontraron secuencias de puntos de acceso entre los pisos.");
            return rutasSegmentadas;
        }

        // 3. Evaluar la mejor secuencia basada en la distancia total
        List<PuntoAcceso> mejorSecuencia = null;
        double menorDistancia = Double.MAX_VALUE;

        for (List<PuntoAcceso> secuencia : todasSecuencias) {
            double distancia = calcularDistanciaSecuencia(inicio, destino, secuencia);
            if (distancia < menorDistancia) {
                menorDistancia = distancia;
                mejorSecuencia = secuencia;
            }
        }

        // Si se encontró una mejor secuencia, generar las rutas detalladas.
        if (mejorSecuencia != null && !mejorSecuencia.isEmpty()) {
            rutasSegmentadas = generarRutasParaSecuencia(inicio, destino, mejorSecuencia);
        } else {
            System.out.println("No se pudo determinar la mejor secuencia de puntos de acceso.");
        }

        return rutasSegmentadas;
    }

    /**
     * Calcula la ruta a una sala específica desde un punto de inicio.
     * Encuentra las entradas de la sala en el piso de destino y usa el punto de entrada más cercano
     * como destino para `calcularRutaCompleta`.
     *
     * @param inicio El punto de inicio del usuario.
     * @param salaDestino La Sala a la que se desea navegar.
     * @param evitarEscaleras Indica si se deben evitar las escaleras.
     * @return Un ArrayList de objetos Ruta que representan la secuencia de rutas a seguir.
     * Devuelve una lista vacía si la sala no tiene entradas o no se encuentra una ruta.
     */
    public static ArrayList<Ruta> navegarASala(Edificio edificio, Punto inicio, Sala salaDestino, boolean evitarEscaleras) {
        if (salaDestino == null || salaDestino.getEntradas().isEmpty()) {
            System.out.println("La sala de destino no existe o no tiene entradas.");
            return new ArrayList<>();
        }

        Punto mejorEntrada = null;
        double menorDistancia = Double.MAX_VALUE;
        ArrayList<Ruta> mejorRutaCompleta = new ArrayList<>();

        // Para cada entrada de la sala, calcula la ruta completa y elige la más corta.
        for (Punto entradaSala : salaDestino.getEntradas()) {
            ArrayList<Ruta> rutaActual = calcularRutaCompleta(edificio, inicio, entradaSala, evitarEscaleras);
            if (!rutaActual.isEmpty()) {
                double distanciaActual = rutaActual.stream().mapToDouble(Ruta::getDistanciaTotal).sum();
                if (distanciaActual < menorDistancia) {
                    menorDistancia = distanciaActual;
                    mejorEntrada = entradaSala; // No es estrictamente necesario, pero útil para depuración.
                    mejorRutaCompleta = rutaActual;
                }
            }
        }
        if(mejorEntrada == null){
            System.out.println("No se pudo encontrar una ruta a ninguna entrada de la sala.");
        }
        return mejorRutaCompleta;
    }


    /**
     * Reimplementación de este métod para corregir el cálculo de distancia entre pisos.
     * Calcula la distancia total de una secuencia de puntos de acceso, incluyendo las rutas inter-piso.
     *
     * @param inicio El punto de inicio.
     * @param destino El punto de destino final.
     * @param secuencia La lista de puntos de acceso que conectan los pisos.
     * @return La distancia total acumulada para esa secuencia.
     */
    private static double calcularDistanciaSecuencia(Punto inicio, Punto destino, List<PuntoAcceso> secuencia) {
        double distanciaTotal = 0.0;
        Punto puntoOrigenSegmento = inicio;

        for (int i = 0; i < secuencia.size(); i++) {
            PuntoAcceso paActual = secuencia.get(i);
            Punto ubicacionPA_OrigenPiso = paActual.getUbicacion(); // Punto del PA en el piso actual
            Punto ubicacionPA_SiguientePiso = paActual.getPuntoConectado().getUbicacion(); // Punto del PA en el siguiente piso

            // 1. Ruta dentro del piso actual hasta la entrada del PuntoAcceso
            Ruta rutaHastaPA = Pathfinding.encontrarRuta(puntoOrigenSegmento, ubicacionPA_OrigenPiso);
            if (rutaHastaPA.getPuntos().isEmpty()) {
                return Double.MAX_VALUE; // Ruta inválida, esta secuencia no es viable
            }
            distanciaTotal += rutaHastaPA.getDistanciaTotal();

            // 2. Transición al siguiente piso (distancia "vertical" es 0 o un valor fijo si aplica)
            // No hay distancia de píxeles al "saltar" entre pisos, pero se podría añadir un costo fijo.
            // Por ahora, asumimos que la distancia de la transición es insignificante o ya manejada.

            // 3. El punto de origen para el siguiente segmento de ruta es la salida del PA en el siguiente piso.
            puntoOrigenSegmento = ubicacionPA_SiguientePiso;
        }

        // 4. Ruta final desde el último punto de acceso (en el piso destino) hasta el destino final
        Ruta rutaFinal = Pathfinding.encontrarRuta(puntoOrigenSegmento, destino);
        if (rutaFinal.getPuntos().isEmpty()) {
            return Double.MAX_VALUE; // Ruta inválida
        }
        distanciaTotal += rutaFinal.getDistanciaTotal();

        return distanciaTotal;
    }


    /**
     * Reimplementación de este métod para corregir la generación de rutas entre pisos.
     * Genera una lista de objetos Ruta que componen la ruta completa entre pisos.
     *
     * @param inicio El punto de inicio.
     * @param destino El punto de destino final.
     * @param secuencia La mejor secuencia de puntos de acceso para la navegación.
     * @return Un ArrayList de objetos Ruta.
     */
    private static ArrayList<Ruta> generarRutasParaSecuencia(Punto inicio, Punto destino, List<PuntoAcceso> secuencia) {
        ArrayList<Ruta> rutasSegmentadas = new ArrayList<>();
        Punto puntoOrigenSegmento = inicio; // El punto de inicio del segmento de ruta actual

        for (int i = 0; i < secuencia.size(); i++) {
            PuntoAcceso paActual = secuencia.get(i);
            Punto ubicacionPA_OrigenPiso = paActual.getUbicacion(); // Punto del PA en el piso actual
            Punto ubicacionPA_SiguientePiso = paActual.getPuntoConectado().getUbicacion(); // Punto del PA en el siguiente piso

            // 1. Ruta dentro del piso actual hasta la entrada del PuntoAcceso
            Ruta rutaHastaPA = Pathfinding.encontrarRuta(puntoOrigenSegmento, ubicacionPA_OrigenPiso);
            if (rutaHastaPA.getPuntos().isEmpty()) {
                System.err.println("Error: No se pudo encontrar ruta al PA " + paActual.getTipo() + " en piso " + paActual.getUbicacion().getPiso().getNumero());
                return new ArrayList<>(); // Si un segmento no se puede calcular, toda la secuencia es inválida.
            }
            rutasSegmentadas.add(rutaHastaPA);

            // Opcional: Agregar un "punto de transición" para visualizar mejor el salto entre pisos
            // Podrías añadir un Punto especial que represente la subida/bajada.
            // Por simplicidad, por ahora simplemente avanzamos el punto de origen.

            // 2. El punto de origen para el siguiente segmento de ruta es la salida del PA en el siguiente piso.
            puntoOrigenSegmento = ubicacionPA_SiguientePiso;
        }

        // 3. Ruta final desde el último punto de acceso (en el piso destino) hasta el destino final
        Ruta rutaFinal = Pathfinding.encontrarRuta(puntoOrigenSegmento, destino);
        if (rutaFinal.getPuntos().isEmpty()) {
            System.err.println("Error: No se pudo encontrar ruta desde el último PA hasta el destino final en piso " + destino.getPiso().getNumero());
            return new ArrayList<>(); // Si el último segmento no se puede calcular, toda la secuencia es inválida.
        }
        rutasSegmentadas.add(rutaFinal);

        return rutasSegmentadas;
    }

    /**
     * BFS para encontrar secuencias de puntos de acceso.
     * No necesita grandes cambios, solo el cálculo de maxProfundidad que se mueve al método público.
     */
    private static List<List<PuntoAcceso>> encontrarTodasSecuenciasPuntosAcceso(
            Piso pisoInicioBusqueda, // Renombrado para claridad
            Piso pisoDestinoBusqueda, // Renombrado para claridad
            boolean evitarEscaleras,
            int maxProfundidad) {

        List<List<PuntoAcceso>> resultados = new ArrayList<>();
        Queue<NodoBusqueda> cola = new LinkedList<>();

        // Estado inicial: un nodo que representa el piso de inicio de la búsqueda, sin puntos de acceso aún en la secuencia
        cola.add(new NodoBusqueda(pisoInicioBusqueda, new ArrayList<>(), new HashSet<>()));

        while (!cola.isEmpty()) {
            NodoBusqueda actual = cola.poll();

            // Si el piso actual es el destino, hemos encontrado una secuencia válida.
            if (actual.piso.equals(pisoDestinoBusqueda)) {
                resultados.add(new ArrayList<>(actual.secuencia));
                continue;
            }

            // Limitar la profundidad máxima de la secuencia de puntos de acceso
            if (actual.secuencia.size() >= maxProfundidad) {
                continue;
            }

            // Explorar todos los puntos de acceso en el piso actual
            for (PuntoAcceso paEnPisoActual : actual.piso.getPuntosAcceso()) {
                // Filtrar por accesibilidad si se deben evitar escaleras
                if (evitarEscaleras && !paEnPisoActual.esAccesibleParaDiscapacitados()) {
                    continue;
                }

                PuntoAcceso paConectadoEnSiguientePiso = paEnPisoActual.getPuntoConectado();
                // Asegurarse de que el punto de acceso esté realmente conectado a algo
                if (paConectadoEnSiguientePiso == null) {
                    continue;
                }

                Piso siguientePiso = paConectadoEnSiguientePiso.getPiso();

                // Evitar ciclos en la secuencia de pisos (ej. ir del piso 1 al 2, y luego del 2 al 1 de nuevo por otro PA)
                if (actual.visitados.contains(siguientePiso)) {
                    continue;
                }

                // Crear un nuevo estado para el siguiente paso en la búsqueda BFS
                Set<Piso> nuevosVisitados = new HashSet<>(actual.visitados);
                nuevosVisitados.add(actual.piso); // Añadir el piso actual a los visitados para la siguiente iteración

                List<PuntoAcceso> nuevaSecuencia = new ArrayList<>(actual.secuencia);
                nuevaSecuencia.add(paEnPisoActual); // Añadir el punto de acceso que nos lleva al siguiente piso

                cola.add(new NodoBusqueda(siguientePiso, nuevaSecuencia, nuevosVisitados));
            }
        }

        return resultados;
    }

    private static class NodoBusqueda {
        Piso piso; // El piso actual en el que nos encontramos en la búsqueda BFS
        List<PuntoAcceso> secuencia; // La secuencia de PuntoAcceso que nos llevó a este piso
        Set<Piso> visitados; // Los pisos que ya hemos visitado en esta ruta de búsqueda para evitar ciclos

        public NodoBusqueda(Piso piso, List<PuntoAcceso> secuencia, Set<Piso> visitados) {
            this.piso = piso;
            this.secuencia = secuencia;
            this.visitados = visitados;
        }
    }

    public static BufferedImage crearRutaEdificios(Edificio edificioInicio, Edificio edificioFinal){
        try {
            GoogleStaticMapRequester googleMapApi = new GoogleStaticMapRequester();

            byte[] mapaBytes = googleMapApi.getRouteMap(
                    edificioInicio.getLatitud(),
                    edificioInicio.getLongitud(),
                    edificioFinal.getLatitud(),
                    edificioFinal.getLongitud()
            );

            ByteArrayInputStream bis = new ByteArrayInputStream(mapaBytes);
            return ImageIO.read(bis);
        } catch (Exception e){
            System.out.print(e);
            return null;
        }
    }
}