package modelo;

public abstract class PuntoAcceso {
    protected int x, y;
    protected Piso pisoActual;
    protected PuntoAcceso puntoConectado; // Referencia directa al punto en el otro piso

    public PuntoAcceso(int x, int y, Piso pisoActual) {
        this.x = x;
        this.y = y;
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
    public int getX() { return x; }
    public int getY() { return y; }
    public Piso getPiso() { return pisoActual; }
}