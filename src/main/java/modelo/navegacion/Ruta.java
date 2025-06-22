package modelo.navegacion;

import java.util.ArrayList;
import java.util.List;

public class Ruta {
    private List<Punto> puntos;
    private double distanciaTotal;

    public Ruta() {
        this.puntos = new ArrayList<>();
        this.distanciaTotal = 0;
    }

    public void agregarPunto(Punto punto) {
        if (!puntos.isEmpty()) {
            Punto ultimo = puntos.get(puntos.size() - 1);
            distanciaTotal += ultimo.distanciaA(punto);

        }
        puntos.add(punto);
    }

    public List<Punto> getPuntos() {
        return new ArrayList<>(puntos); // Devuelve copia para evitar modificaciones externas
    }

    public double getDistanciaTotal() {
        return distanciaTotal;
    }

    public Punto getPuntoInicio() {
        if (puntos.isEmpty()) return null;
        return puntos.get(0);
    }

    public Punto getPuntoFin() {
        if (puntos.isEmpty()) return null;
        return puntos.get(puntos.size() - 1);
    }
}