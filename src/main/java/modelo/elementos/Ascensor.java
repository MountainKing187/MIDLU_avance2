package modelo.elementos;

import modelo.PuntoAcceso;
import modelo.edificio.Piso;
import modelo.navegacion.Punto;

public class Ascensor extends PuntoAcceso {
    public Ascensor(Punto ubicacion, Piso piso) {
        super(ubicacion, piso);
    }

    @Override
    public boolean esAccesibleParaDiscapacitados() {
        return true;
    }

    @Override
    public String getTipo() { return "ASCENSOR";}

    // Métodos específicos de ascensor si los hay
}