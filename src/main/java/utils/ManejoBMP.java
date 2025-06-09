package utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ManejoBMP {
    private int numero;
    private String imagenPath;
    private boolean[][] mapaObstaculos; // true = obstáculo (pared)
    private BufferedImage imagenPiso;

    public ManejoBMP(int numero, String imagenPath) {
        this.numero = numero;
        this.imagenPath = imagenPath;
        this.mapaObstaculos = null;
        this.imagenPiso = null;
    }

    // Procesa el bitmap y genera el mapa de obstáculos
    public void procesarBitmap() throws IOException {
        // 1. Cargar la imagen
        this.imagenPiso = ImageIO.read(new File(imagenPath));

        // 2. Inicializar matriz de obstáculos
        int ancho = imagenPiso.getWidth();
        int alto = imagenPiso.getHeight();
        this.mapaObstaculos = new boolean[ancho][alto];

        // 3. Analizar píxeles
        for (int x = 0; x < ancho; x++) {
            for (int y = 0; y < alto; y++) {
                int color = imagenPiso.getRGB(x, y);
                mapaObstaculos[x][y] = esObstaculo(color);
            }
        }
    }

    private boolean esObstaculo(int colorRGB) {
        // Extraer componentes RGB
        int r = (colorRGB >> 16) & 0xFF;
        int g = (colorRGB >> 8) & 0xFF;
        int b = colorRGB & 0xFF;

        // Negro puro (R=0, G=0, B=0) = obstáculo
        return (r == 0 && g == 0 && b == 0);
    }

    // Getters
    public boolean[][] getMapaObstaculos() { return mapaObstaculos; }
    public BufferedImage getImagenPiso() { return imagenPiso; }
}