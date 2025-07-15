package modelo.edificio;

import java.util.ArrayList;

public class Edificio {
    private String nombre;
    private double latitud;
    private double longitud;
    private ArrayList<Piso> pisos = new ArrayList<>();

    public Edificio(String nombre, double latitud, double longitud) {
        this.nombre = nombre;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public void agregarPiso(Piso piso) {
        pisos.add(piso);
    }

    // Getters
    public String getNombre() { return nombre; }
    public ArrayList<Piso> getPisos() { return pisos; }

    public double getLatitud() { return latitud; }

    public double getLongitud() { return longitud; }

    public Piso getPiso(int numero) {
        return pisos.stream()
                .filter(p -> p.getNumero() == numero)
                .findFirst()
                .orElse(null);
    }
}