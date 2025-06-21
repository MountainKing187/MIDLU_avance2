package servicios;

import modelo.edificio.MapaProcesado;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ProcesadorMapa {
    public static MapaProcesado procesarBitmap(String rutaImagen) throws IOException {
        BufferedImage imagen = ImageIO.read(new File(rutaImagen));
        int ancho = imagen.getWidth();
        int alto = imagen.getHeight();
        boolean[][] obstaculos = new boolean[alto][ancho];

        for (int x = 0; x < ancho; x++) {
            for (int y = 0; y < alto; y++) {
                obstaculos[y][x] = esPared(imagen.getRGB(x, y));
            }
        }

        return new MapaProcesado(obstaculos, imagen);
    }

    private static boolean esPared(int colorRGB) {
        int r = (colorRGB >> 16) & 0xFF;
        int g = (colorRGB >> 8) & 0xFF;
        int b = colorRGB & 0xFF;
        return (r < 50 && g < 50 && b < 50);
    }
}