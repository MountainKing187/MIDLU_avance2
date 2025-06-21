package modelo;

public class Sala {
    private String nombre;
    private Punto entrada;

    public Sala(String nombre, Punto entrada) {
        this.nombre = nombre;
        this.entrada = entrada;
    }

    // Getters
    public String getNombre() { return nombre; }
    public Punto getEntrada() { return entrada; }

}