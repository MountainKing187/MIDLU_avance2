package modelo.elementos;

import modelo.navegacion.Punto;

import java.util.ArrayList;

public class Sala {
    private String nombre;
    private ArrayList<Punto> entradas;

    public Sala(String nombre) {
        this.nombre = nombre;
    }

    public void agregarEntrada(Punto entrada) {
        entradas.add(entrada);
    }

    // Getters
    public String getNombre() { return nombre; }
    public ArrayList<Punto> getEntradas() { return entradas; }

}