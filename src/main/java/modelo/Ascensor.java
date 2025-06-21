package modelo;

public class Ascensor extends PuntoAcceso {
    public Ascensor(Punto ubicacion, Piso piso) {
        super(ubicacion, piso);
    }

    @Override
    public boolean esAccesibleParaDiscapacitados() {
        return true;
    }

    // Métodos específicos de ascensor si los hay
}