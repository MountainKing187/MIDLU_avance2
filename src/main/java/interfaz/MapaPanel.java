package interfaz;

import modelo.edificio.Piso;
import modelo.navegacion.Punto;
import modelo.navegacion.Ruta;
import servicios.ControladorPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class MapaPanel extends JPanel {

    private Piso piso;
    private Ruta ruta;
    private final int cellSize = 10;

    public MapaPanel(Piso piso, Ruta ruta, ControladorPanel controlador) {
        this.piso = piso;
        this.ruta = ruta;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX() / cellSize;
                int y = e.getY() / cellSize;
                if (piso != null) {
                    controlador.manejarClicEnMapa(x, y, piso);
                }
            }
        });
    }


    public void actualizarDatos(Piso nuevoPiso, Ruta nuevaRuta) {
        this.piso = nuevoPiso;
        this.ruta = nuevaRuta;
        repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(800, 600); // por defecto
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (piso == null || ruta == null) return;

        boolean[][] obstaculos = piso.getMapaObstaculos();
        List<Punto> puntosRuta = ruta.getPuntos();

        for (int y = 0; y < obstaculos.length; y++) {
            for (int x = 0; x < obstaculos[y].length; x++) {
                if (obstaculos[y][x]) {
                    g.setColor(Color.BLACK);
                    g.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);
                } else {
                    g.setColor(Color.LIGHT_GRAY);
                    g.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);
                }
            }
        }

        g.setColor(Color.RED);
        for (Punto p : puntosRuta) {
            if (p.getPiso().equals(piso)) {
                g.fillOval(p.getX() * cellSize, p.getY() * cellSize, cellSize, cellSize);
            }
        }
    }


}
