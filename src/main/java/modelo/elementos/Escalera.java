package modelo.elementos;

import modelo.PuntoAcceso;
import modelo.edificio.Piso;
import modelo.navegacion.Punto;

public class Escalera extends PuntoAcceso {
    public Escalera(Punto ubicacion, Piso piso) {
        super(ubicacion, piso);
    }

    @Override
    public boolean esAccesibleParaDiscapacitados() {
        return false;
    }

    @Override
    public String getTipo() { return "ESCALERA";}


    // Métodos específicos de escalera si los hay
}