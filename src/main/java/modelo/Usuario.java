package modelo;

public class Usuario {
    private Punto ubicacion;
    private boolean tieneDiscapacidad;

    public boolean estaEnSalida(Salida salida) {
        return ubicacion.getPiso().equals(salida.getUbicacion().getPiso()) &&
                ubicacion.distanciaA(salida.getUbicacion()) < 10; // Radio de 10px
    }

    public Punto getUbicacion() { return ubicacion; }
    public boolean tieneDiscapacidad() { return tieneDiscapacidad; }
    /*
    public boolean estaEnSala(Sala sala) {
        return sala.contiene(ubicacion);
    }

     */
}