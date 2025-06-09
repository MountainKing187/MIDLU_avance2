package modelo;

public class Usuario {
    private Punto ubicacion; // Ahora usa Punto en lugar de x,y separados
    private boolean tieneDiscapacidad;

    // Métodos actualizados
    public boolean estaEnSalida() {
        Salida salidaMasCercana = null /* lógica para encontrar salida más cercana */;
        return ubicacion.distanciaA(new Punto(salidaMasCercana.getX(),
                salidaMasCercana.getY(),
                ubicacion.getPiso())) < 10; // Radio de 10px
    }
}
