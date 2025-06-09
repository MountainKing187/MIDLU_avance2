package modelo;

public class Ascensor extends PuntoAcceso {
    public Ascensor(int x, int y, Piso piso) {
        super(x, y, piso);
    }

    @Override
    public boolean esAccesibleParaDiscapacitados() {
        return true;
    }

    // Métodos específicos de ascensor si los hay
}