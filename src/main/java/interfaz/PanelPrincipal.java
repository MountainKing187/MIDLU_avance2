package interfaz;

import servicios.ControladorPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class PanelPrincipal extends JFrame {

    private ControladorPanel controlador;

    public PanelPrincipal() {
        setTitle("Menú Principal");
        setSize(800, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());  // Centra los botones
    }

    public void setControlador(ControladorPanel controlador) {
        this.controlador = controlador;
    }

    public void mostrarMenu() {
        getContentPane().removeAll(); // Limpiar contenido anterior si lo hay

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 0, 20, 0);
        gbc.gridx = 0;

        // Cargar íconos
        ImageIcon iconoHover = cargarIcono("iconos/buttonrectangleborder.png");
        ImageIcon iconoNormal = cargarIcono("iconos/buttonrectangledepthgloss.jpg");

        // Crear botones
        JButton btnIniciar = crearBotonConHover("Iniciar Mapa", iconoNormal, iconoHover);
        JButton btnSalir = crearBotonConHover("Cerrar Programa", iconoNormal, iconoHover);

        btnIniciar.addActionListener(e -> controlador.iniciarMapa());
        btnSalir.addActionListener(e -> System.exit(0));

        gbc.gridy = 0;
        add(btnIniciar, gbc);

        gbc.gridy = 1;
        add(btnSalir, gbc);

        revalidate();
        repaint();
        setVisible(true);
    }

    private JButton crearBotonConHover(String texto, ImageIcon iconoNormal, ImageIcon iconoHover) {
        JButton boton = new JButton(texto);
        if (iconoNormal != null) {
            boton.setIcon(iconoNormal);
        }

        boton.setHorizontalTextPosition(SwingConstants.CENTER);
        boton.setVerticalTextPosition(SwingConstants.CENTER);
        boton.setBorderPainted(false);
        boton.setFocusPainted(false);
        boton.setContentAreaFilled(false);

        // Efecto hover
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (iconoHover != null) boton.setIcon(iconoHover);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (iconoNormal != null) boton.setIcon(iconoNormal);
            }
        });

        return boton;
    }

    private ImageIcon cargarIcono(String path) {
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream(path);
            Image img = ImageIO.read(is);
            return new ImageIcon(img.getScaledInstance(192, 64, Image.SCALE_SMOOTH));
        } catch (IOException | NullPointerException e) {
            System.err.println("❌ Error cargando ícono: " + path);
            return null;
        }
    }
}
