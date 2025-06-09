package modelo;

public class Escalera extends PuntoAcceso {
    public Escalera(int x, int y, Piso piso) {
        super(x, y, piso);
    }

    @Override
    public boolean esAccesibleParaDiscapacitados() {
        return false;
    }

    // Métodos específicos de escalera si los hay
}