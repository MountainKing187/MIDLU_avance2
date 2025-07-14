package modelo.edificio;

import java.util.ArrayList;

public class Edificio {
    private String nombre;
    private double latitude;
    private double longitude;
    private ArrayList<Piso> pisos = new ArrayList<>();

    public Edificio(String nombre, double latitude, double longitude) {
        this.nombre = nombre;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void agregarPiso(Piso piso) {
        pisos.add(piso);
    }

    // Getters
    public String getNombre() { return nombre; }
    public ArrayList<Piso> getPisos() { return pisos; }

    public Piso getPiso(int numero) {
        return pisos.stream()
                .filter(p -> p.getNumero() == numero)
                .findFirst()
                .orElse(null);
    }
}