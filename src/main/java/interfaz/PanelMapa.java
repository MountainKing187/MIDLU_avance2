package interfaz;

import modelo.Punto;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class PanelMapa extends JPanel {
    private BufferedImage imagenPiso;
    private ArrayList<Punto> ruta;
    private Object destinoSeleccionado;

    @Override
    protected void paintComponent(Graphics g) {
        // Dibujar mapa con colores según especificación
        // Resaltar destino seleccionado (parpadeo)
        // Dibujar ruta si existe
    }

    public void parpadearDestino() {
        // Implementar lógica de parpadeo con un Timer
    }
}