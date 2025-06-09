package algoritmos;

import modelo.Piso;
import modelo.Punto;
import modelo.Ruta;

import java.util.ArrayList;

public class Pathfinding {
    public static Ruta encontrarRuta(Piso pisoOrigen, Punto inicio, Punto fin, boolean evitarEscaleras) {
        Ruta ruta = new Ruta();

        // Implementación del algoritmo A* aquí
        // Por ahora un ejemplo simplificado:

        // 1. Verificar si estamos en el mismo piso
        if (inicio.getPiso().equals(fin.getPiso())) {
            // Algoritmo para mismo piso
            ArrayList<Punto> puntosEnPiso = calcularRutaMismoPiso(inicio, fin, evitarEscaleras);
            puntosEnPiso.forEach(ruta::agregarPunto);
        } else {
            // Algoritmo para múltiples pisos
            // Necesitarías encontrar puntos de acceso (ascensores/escaleras)
            // y conectar las rutas entre pisos
        }

        return ruta;
    }

    private static ArrayList<Punto> calcularRutaMismoPiso(Punto inicio, Punto fin, boolean evitarEscaleras) {
        // Implementación real del pathfinding
        // Esto es solo un ejemplo
        ArrayList<Punto> ruta = new ArrayList<>();
        ruta.add(inicio);
        // ... lógica para encontrar camino ...
        ruta.add(fin);
        return ruta;
    }
}