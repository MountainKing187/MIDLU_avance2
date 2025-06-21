package modelo;

import java.util.ArrayList;

public class Sala {
    private String nombre;
    private ArrayList<Punto> entradas;

    public Sala(String nombre, ArrayList<Punto> entradas) {
        this.nombre = nombre;
        this.entradas = entradas;
    }

    public void agregarEntrada(Punto entrada) {
        entradas.add(entrada);
    }

    // Getters
    public String getNombre() { return nombre; }
    public ArrayList<Punto> getEntradas() { return entradas; }

}