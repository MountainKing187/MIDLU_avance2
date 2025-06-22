package servicios;

import modelo.edificio.Piso;
import modelo.navegacion.Punto;
import modelo.navegacion.Ruta;
import java.util.*;

public class Pathfinding {
    private static final int[][] DIRECCIONES = {{0, 1}, {1, 0}, {0, -1}, {-1, 0},  // Movimientos cardinales
            {1, 1}, {1, -1}, {-1, 1}, {-1, -1}}; // Movimientos diagonales

    public static Ruta encontrarRuta(Punto inicio, Punto fin) {
        return encontrarRutaMismoPiso(inicio, fin);
    }

    private static Ruta encontrarRutaMismoPiso(Punto inicio, Punto fin) {
        Piso piso = inicio.getPiso();
        boolean[][] obstaculos = piso.getMapaObstaculos();

        PriorityQueue<Nodo> openSet = new PriorityQueue<>();
        Map<Punto, Nodo> todosNodos = new HashMap<>();

        Nodo inicioNodo = new Nodo(inicio, null, 0, heuristica(inicio, fin));
        openSet.add(inicioNodo);
        todosNodos.put(inicio, inicioNodo);

        while (!openSet.isEmpty()) {
            Nodo actual = openSet.poll();

            if (actual.getPunto().equals(fin)) {
                return reconstruirRuta(actual);
            }

            for (int[] dir : DIRECCIONES) {
                int x = actual.getPunto().getX() + dir[0];
                int y = actual.getPunto().getY() + dir[1];

                // Verificar límites y obstáculos
                if (x < 0 || y < 0 || y >= obstaculos.length || x >= obstaculos[0].length || obstaculos[y][x]) {
                    continue;
                }

                Punto vecinoPunto = new Punto(x, y, piso);
                double costoMovimiento = (Math.abs(dir[0]) + Math.abs(dir[1]) == 2 ? 1.414 : 1.0); // Costo diagonal
                double nuevoG = actual.getG() + costoMovimiento;

                Nodo vecino = todosNodos.getOrDefault(vecinoPunto, new Nodo(vecinoPunto));
                todosNodos.putIfAbsent(vecinoPunto, vecino);

                if (nuevoG < vecino.getG()) {
                    vecino.setPadre(actual);
                    vecino.setG(nuevoG);
                    vecino.setF(nuevoG + heuristica(vecinoPunto, fin));

                    if (!openSet.contains(vecino)) {
                        openSet.add(vecino);
                    }
                }
            }
        }

        return new Ruta(); // Ruta vacía si no se encuentra camino
    }

    private static double heuristica(Punto a, Punto b) {
        // Distancia euclidiana
        return Math.sqrt(Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getY() - b.getY(), 2));
    }

    private static Ruta reconstruirRuta(Nodo nodoFinal) {
        Ruta ruta = new Ruta();
        Nodo actual = nodoFinal;

        while (actual != null) {
            ruta.agregarPunto(actual.getPunto());
            actual = actual.getPadre();
        }

        Collections.reverse(ruta.getPuntos());
        return ruta;
    }

    // Clase interna para nodos del algoritmo A*
    private static class Nodo implements Comparable<Nodo> {
        private final Punto punto;
        private Nodo padre;
        private double g; // Costo desde inicio
        private double f; // Costo total (g + heurística)

        public Nodo(Punto punto, Nodo padre, double g, double f) {
            this.punto = punto;
            this.padre = padre;
            this.g = g;
            this.f = f;
        }

        public Nodo(Punto punto) {
            this(punto, null, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        }

        // Getters y setters
        public Punto getPunto() { return punto; }
        public Nodo getPadre() { return padre; }
        public double getG() { return g; }
        public double getF() { return f; }

        public void setPadre(Nodo padre) { this.padre = padre; }
        public void setG(double g) { this.g = g; }
        public void setF(double f) { this.f = f; }

        @Override
        public int compareTo(Nodo otro) {
            return Double.compare(this.f, otro.f);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Nodo otro = (Nodo) obj;
            return punto.equals(otro.punto);
        }

        @Override
        public int hashCode() {
            return punto.hashCode();
        }
    }
}