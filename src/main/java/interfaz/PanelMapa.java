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

public class PanelMapa extends JFrame {

    private Ruta ruta;
    private Piso piso;
    private final ControladorPanel controlador;
    private final int cellSize = 10;
    private final MapaPanel panel;

    public PanelMapa(Piso piso, Ruta ruta, ControladorPanel controlador) {
        this.piso = piso;
        this.ruta = ruta;
        this.controlador = controlador;

        setTitle("Visualización de Ruta");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        panel = new MapaPanel();
        add(panel);
        setVisible(true);
    }

    public void actualizarRuta(Ruta nuevaRuta, Piso nuevoPiso) {
        this.ruta = nuevaRuta;
        this.piso = nuevoPiso;
        panel.repaint();
    }

    private class MapaPanel extends JPanel {
        public MapaPanel() {
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int x = e.getX() / cellSize;
                    int y = e.getY() / cellSize;
                    controlador.manejarClicEnMapa(x, y, piso);
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (piso == null) return;

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
}
