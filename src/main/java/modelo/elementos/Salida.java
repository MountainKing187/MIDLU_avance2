package modelo.elementos;

import modelo.navegacion.Punto;

public class Salida {
    private String nombre;
    private Punto ubicacion;  // Cambio a Punto
    private boolean esPrincipal;

    public Salida(String nombre, Punto ubicacion, boolean esPrincipal) {
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.esPrincipal = esPrincipal;
    }

    // Getters actualizados
    public String getNombre() { return nombre; }

    public Punto getUbicacion() { return ubicacion; }

    public int getX() { return ubicacion.getX(); }

    public int getY() { return ubicacion.getY(); }

    public boolean isEsPrincipal() { return esPrincipal; }
}