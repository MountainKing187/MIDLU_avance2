package modelo.edificio;

import java.util.ArrayList;

public class Edificio {
    private String nombre;
    private ArrayList<Piso> pisos = new ArrayList<>();

    public Edificio(String nombre) {
        this.nombre = nombre;
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