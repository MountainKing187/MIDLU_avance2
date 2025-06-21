package modelo;

import java.util.ArrayList;
import java.util.List;

public class Edificio {
    private String nombre;
    private List<Piso> pisos = new ArrayList<>();

    public Edificio(String nombre) {
        this.nombre = nombre;
    }

    public void agregarPiso(Piso piso) {
        pisos.add(piso);
    }

    // Getters
    public String getNombre() { return nombre; }
    public List<Piso> getPisos() { return pisos; }

    public Piso getPiso(int numero) {
        return pisos.stream()
                .filter(p -> p.getNumero() == numero)
                .findFirst()
                .orElse(null);
    }
}