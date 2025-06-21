package modelo;

public class Escalera extends PuntoAcceso {
    public Escalera(Punto ubicacion, Piso piso) {
        super(ubicacion, piso);
    }

    @Override
    public boolean esAccesibleParaDiscapacitados() {
        return false;
    }

    // Métodos específicos de escalera si los hay
}