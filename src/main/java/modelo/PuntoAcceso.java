package modelo;

public abstract class PuntoAcceso {
    protected int x, y;
    protected Piso piso;

    public abstract boolean esAccesibleParaDiscapacitados();
}

public class Ascensor extends PuntoAcceso {
    @Override
    public boolean esAccesibleParaDiscapacitados() {
        return true;
    }
}

public class Escalera extends PuntoAcceso {
    @Override
    public boolean esAccesibleParaDiscapacitados() {
        return false;
    }
}