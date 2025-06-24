package interfaz;

import modelo.edificio.Piso;
import modelo.navegacion.Ruta;
import servicios.ControladorPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.util.List;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class PanelPrincipal extends JFrame {

    private ControladorPanel controlador;

    private JButton btnSubir;
    private JButton btnBajar;

    public PanelPrincipal() {
        setTitle("Menú Principal");
        setSize(800, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
          // Centra los botones
    }

    public void setControlador(ControladorPanel controlador) {
        this.controlador = controlador;
    }

    public void mostrarMenu() {
        getContentPane().removeAll(); // Limpiar contenido anterior si lo hay
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 0, 20, 0);
        gbc.gridx = 3;

        // Cargar íconos
        ImageIcon iconoHover = cargarIcono("iconos/buttonrectangleborder.png");
        ImageIcon iconoNormal = cargarIcono("iconos/buttonrectangledepthgloss.jpg");

        // Crear botones
        JButton btnIniciar = crearBotonConHover("Iniciar Mapa", iconoNormal, iconoHover);
        JButton btnSalir = crearBotonConHover("Cerrar Programa", iconoNormal, iconoHover);

        btnIniciar.addActionListener(e -> {
            List<String> salas = controlador.getTodasLasSalas();
            if (salas.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No hay salas disponibles en el edificio.");
                return;
            }

            JComboBox<String> comboSalas = new JComboBox<>(salas.toArray(new String[0]));
            JCheckBox chkAscensor = new JCheckBox("Necesito ascensor");

            JPanel panel = new JPanel(new GridLayout(2, 1));
            panel.add(new JLabel("Selecciona la sala destino:"));
            panel.add(comboSalas);
            panel.add(chkAscensor);

            int opcion = JOptionPane.showConfirmDialog(
                    this,
                    panel,
                    "Configuración de ruta",
                    JOptionPane.OK_CANCEL_OPTION
            );

            if (opcion == JOptionPane.OK_OPTION) {
                String salaSeleccionada = (String) comboSalas.getSelectedItem();
                boolean necesitaAscensor = chkAscensor.isSelected();

                controlador.setSalaDestino(salaSeleccionada);
                controlador.iniciarMapa(necesitaAscensor);
            }
        });

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
            assert is != null;
            Image img = ImageIO.read(is);
            return new ImageIcon(img.getScaledInstance(192, 64, Image.SCALE_SMOOTH));
        } catch (IOException | NullPointerException e) {
            System.err.println("❌ Error cargando ícono: " + path);
            return null;
        }
    }


//  Inicializacion Mapa, va después que carga MapaPanel
public void iniciarMapa(Piso pisoInicial, Ruta ruta,
                        ControladorPanel controlador, int pisoActual) {
    getContentPane().removeAll();
    setLayout(new BorderLayout());

    // Panel del mapa
    MapaPanel mapaPanel = new MapaPanel(pisoInicial, ruta, controlador);
    add(mapaPanel, BorderLayout.CENTER);

    // Panel inferior principal con BorderLayout
    JPanel panelInferior = new JPanel(new BorderLayout());
    panelInferior.setBackground(Color.DARK_GRAY);

    // Panel izquierdo con etiqueta del piso
    JPanel panelIzquierdo = new JPanel(new FlowLayout(FlowLayout.LEFT));
    panelIzquierdo.setOpaque(false);
    JLabel lblPiso = new JLabel("Piso: " + pisoActual);
    lblPiso.setForeground(Color.WHITE);
    lblPiso.setFont(new Font("Arial", Font.BOLD, 14));
    panelIzquierdo.add(lblPiso);

    // Panel central con botones subir/bajar
    JPanel panelCentro = new JPanel(new FlowLayout(FlowLayout.CENTER));
    panelCentro.setOpaque(false);
    btnBajar = crearBotonPiso("iconos/minuspng.png", e -> controlador.cambiarPiso(-1));
    btnSubir = crearBotonPiso("iconos/pluspng.png", e -> controlador.cambiarPiso(+1));
    panelCentro.add(btnBajar);
    panelCentro.add(btnSubir);

    // Panel derecho con botón volver
    JPanel panelDerecho = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    panelDerecho.setOpaque(false);
    JButton btnVolver = crearBotonConHover("← Menú", cargarIcono("iconos/buttonrectangledepthgloss.jpg"), cargarIcono("iconos/buttonrectangleborder.png"));
    btnVolver.setPreferredSize(new Dimension(120, 40)); // Más pequeño
    btnVolver.addActionListener(e -> {
        getContentPane().removeAll();
        repaint();
        revalidate();
        mostrarMenu();
    });
    panelDerecho.add(btnVolver);

    // Añadir los paneles al panel inferior
    panelInferior.add(panelIzquierdo, BorderLayout.WEST);
    panelInferior.add(panelCentro, BorderLayout.CENTER);
    panelInferior.add(panelDerecho, BorderLayout.EAST);

    add(panelInferior, BorderLayout.SOUTH);
    setVisible(true);
}

    private JButton crearBotonPiso(String pathIcono, java.awt.event.ActionListener action) {
        JButton boton = new JButton();
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream(pathIcono);
            if (is != null) {
                Image img = ImageIO.read(is);
                boton.setIcon(new ImageIcon(img.getScaledInstance(32, 32, Image.SCALE_SMOOTH)));
            } else {
                boton.setText("?");
            }
        } catch (IOException e) {
            boton.setText("?");
        }
        boton.setPreferredSize(new Dimension(40, 40));
        boton.setBorderPainted(false);
        boton.setFocusPainted(false);
        boton.setContentAreaFilled(false);
        boton.addActionListener(action);
        return boton;
    }

    public void actualizarBotonesPiso(int pisoActual, int totalPisos) {
        try {
            // Botón bajar
            if (pisoActual == 1) {
                actualizarIcono(btnBajar, "iconos/minusgrey.png", false);
            } else {
                actualizarIcono(btnBajar, "iconos/minuspng.png", true);
            }

            // Botón subir
            if (pisoActual >= totalPisos) {
                actualizarIcono(btnSubir, "iconos/plusgrey.png", false);
            } else {
                actualizarIcono(btnSubir, "iconos/pluspng.png", true);
            }
        } catch (IOException e) {
            System.err.println("Error actualizando botones de piso");
        }
    }

    private void actualizarIcono(JButton boton, String ruta, boolean habilitado) throws IOException {
        InputStream is = getClass().getClassLoader().getResourceAsStream(ruta);
        if (is != null) {
            Image img = ImageIO.read(is);
            boton.setIcon(new ImageIcon(img.getScaledInstance(32, 32, Image.SCALE_SMOOTH)));
        } else {
            boton.setText("?");
        }
        boton.setEnabled(habilitado);
    }

}
