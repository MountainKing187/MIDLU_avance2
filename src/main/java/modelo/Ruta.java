package modelo;

import java.util.ArrayList;
import java.util.List;

public class Ruta {
    private List<Punto> puntos;
    private double distanciaTotal;
    private boolean accesibleParaDiscapacitados;

    public Ruta() {
        this.puntos = new ArrayList<>();
        this.distanciaTotal = 0;
        this.accesibleParaDiscapacitados = true;
    }

    public void agregarPunto(Punto punto) {
        if (!puntos.isEmpty()) {
            Punto ultimo = puntos.get(puntos.size() - 1);
            distanciaTotal += ultimo.distanciaA(punto);

            // Si estamos cambiando de piso, verificar accesibilidad
            if (!punto.getPiso().equals(ultimo.getPiso())) {
                // Aquí deberías verificar si el cambio de piso fue por un ascensor
                // Esto es un placeholder - implementación real depende de tu lógica
                accesibleParaDiscapacitados = false;
            }
        }
        puntos.add(punto);
    }

    public List<Punto> getPuntos() {
        return new ArrayList<>(puntos); // Devuelve copia para evitar modificaciones externas
    }

    public double getDistanciaTotal() {
        return distanciaTotal;
    }

    public boolean esAccesibleParaDiscapacitados() {
        return accesibleParaDiscapacitados;
    }

    public Punto getPuntoInicio() {
        if (puntos.isEmpty()) return null;
        return puntos.get(0);
    }

    public Punto getPuntoFin() {
        if (puntos.isEmpty()) return null;
        return puntos.get(puntos.size() - 1);
    }

    public int getNumeroPisosVisitados() {
        if (puntos.isEmpty()) return 0;
        return (int) puntos.stream().map(Punto::getPiso).distinct().count();
    }
}