package modelo;

public class Sala {
    private final String nombre;
    private final Punto ubicacion;  // Cambio de coordenadas separadas a Punto
    private final int ancho, alto;

    public Sala(String nombre, Punto ubicacion, int ancho, int alto) {
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.ancho = ancho;
        this.alto = alto;
    }

    // Getters y setters actualizados
    public String getNombre() { return nombre; }

    public Punto getUbicacion() { return ubicacion; }

    public int getX() { return ubicacion.getX(); } // Métod de conveniencia

    public int getY() { return ubicacion.getY(); } // Métod de conveniencia

    public int getAncho() { return ancho; }

    public int getAlto() { return alto; }

    // Métod para verificar si un punto está dentro de la sala
    public boolean contiene(Punto punto) {
        if (!ubicacion.getPiso().equals(punto.getPiso())) {
            return false;
        }
        return punto.getX() >= getX() &&
                punto.getX() <= getX() + ancho &&
                punto.getY() >= getY() &&
                punto.getY() <= getY() + alto;
    }
}