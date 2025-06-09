package modelo;

import java.util.Objects;

public class Punto {
    private int x;
    private int y;
    private Piso piso; // Referencia al piso donde está este punto

    public Punto(int x, int y, Piso piso) {
        this.x = x;
        this.y = y;
        this.piso = piso;
    }

    // Getters
    public int getX() { return x; }
    public int getY() { return y; }
    public Piso getPiso() { return piso; }

    // Setters
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }

    // Método para calcular distancia entre puntos
    public double distanciaA(Punto otro) {
        if (!this.piso.equals(otro.getPiso())) {
            return Double.MAX_VALUE; // No están en el mismo piso
        }
        int dx = this.x - otro.x;
        int dy = this.y - otro.y;
        return Math.sqrt(dx*dx + dy*dy);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Punto punto = (Punto) obj;
        return x == punto.x && y == punto.y && piso.equals(punto.piso);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, piso);
    }
}