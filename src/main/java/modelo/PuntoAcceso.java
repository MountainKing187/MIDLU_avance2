package modelo;

import modelo.edificio.Piso;
import modelo.navegacion.Punto;

public abstract class PuntoAcceso {
    protected Punto ubicacion;
    protected Piso pisoActual;
    protected PuntoAcceso puntoConectado;

    public PuntoAcceso(Punto ubicacion, Piso pisoActual) {
        this.ubicacion = ubicacion;
        this.pisoActual = pisoActual;
    }

    // Métod para establecer conexión bidireccional
    public void conectarCon(PuntoAcceso otroPunto) {
        this.puntoConectado = otroPunto;
        otroPunto.puntoConectado = this;
    }

    public PuntoAcceso getPuntoConectado() {
        return puntoConectado;
    }

    public abstract boolean esAccesibleParaDiscapacitados();

    // Getters y setters
    public Piso getPiso() { return pisoActual; }
    public Punto getUbicacion() { return ubicacion; }

    public String getTipo() {return "PUNTOACCESO";
    }
}