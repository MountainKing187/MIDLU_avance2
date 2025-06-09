package modelo;

public abstract class PuntoAcceso {
    protected int x, y;
    protected Piso piso;

    public PuntoAcceso(int x, int y, Piso piso) {
        this.x = x;
        this.y = y;
        this.piso = piso;
    }

    public abstract boolean esAccesibleParaDiscapacitados();

    // Getters y setters
    public int getX() { return x; }
    public int getY() { return y; }
    public Piso getPiso() { return piso; }
}