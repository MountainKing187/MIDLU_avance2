package modelo.edificio;

import java.awt.image.BufferedImage;

public class MapaProcesado {
    private final boolean[][] obstaculos;
    private final BufferedImage imagenOriginal;

    public MapaProcesado(boolean[][] obstaculos, BufferedImage imagenOriginal) {
        this.obstaculos = obstaculos;
        this.imagenOriginal = imagenOriginal;
    }

    // Getters
    public boolean[][] getObstaculos() { return obstaculos; }
    public BufferedImage getImagenOriginal() { return imagenOriginal; }
}