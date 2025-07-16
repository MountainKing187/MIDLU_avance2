package interfaz;

import modelo.edificio.Piso;
import modelo.navegacion.Punto;
import modelo.navegacion.Ruta;
import servicios.ControladorPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.List;

public class MapaPanel extends JPanel {

    private Piso piso;
    private Ruta ruta;
    private int cellSize;

    private int offsetX;
    private int offsetY;

    public MapaPanel(Piso piso, Ruta ruta, ControladorPanel controlador) {
        this.piso = piso;
        this.ruta = ruta;

    }


    public void actualizarDatos(Piso nuevoPiso, Ruta nuevaRuta) {
        this.piso = nuevoPiso;
        this.ruta = nuevaRuta;
        repaint();
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (piso == null || ruta == null) return;

        calcularCellSize();

        boolean[][] obstaculos = piso.getMapaObstaculos();
        List<Punto> puntosRuta = ruta.getPuntos();

        for (int y = 0; y < obstaculos.length; y++) {
            for (int x = 0; x < obstaculos[y].length; x++) {
                if (obstaculos[y][x]) {
                    g.setColor(Color.BLACK);
                    g.fillRect(x * cellSize + offsetX, y * cellSize + offsetY, cellSize, cellSize);
                } else {
                    g.setColor(Color.LIGHT_GRAY);
                    g.fillRect(x * cellSize + offsetX, y * cellSize + offsetY, cellSize, cellSize);
                }
            }
        }

        g.setColor(Color.RED);
        for (Punto p : puntosRuta) {
            if (p.getPiso().equals(piso)) {
                g.fillOval(p.getX() * cellSize + offsetX, p.getY() * cellSize + offsetY, cellSize, cellSize);
            }
        }
    }

    private void calcularCellSize() {
        if (piso == null || piso.getMapaObstaculos() == null) {
            cellSize = 10;
            offsetX = 0;
            offsetY = 0;
            return;
        }
        int cols = piso.getMapaObstaculos()[0].length;
        int rows = piso.getMapaObstaculos().length;

        int MAX_WIDTH = 800;
        int MAX_HEIGHT = 640; // deja 80px para los botones abajo

        int sizeX = MAX_WIDTH / cols;
        int sizeY = MAX_HEIGHT / rows;

        cellSize = Math.min(sizeX, sizeY);
        offsetX = (MAX_WIDTH - (cols * cellSize)) / 2;
        offsetY = (MAX_HEIGHT - (rows * cellSize)) / 2;
    }

}
