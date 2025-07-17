package interfaz;

import modelo.edificio.Edificio;
import modelo.edificio.Piso;
import modelo.elementos.Sala;
import modelo.navegacion.Ruta;
import servicios.ControladorPanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.image.BufferedImage;
import java.beans.Visibility;
import java.util.List;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.io.File;


public class PanelPrincipal extends JFrame {

    private ControladorPanel controlador;

    private JButton btnSubir;
    private JButton btnBajar;
    private JButton btnMapaGlobal;
    private JComboBox<Edificio> edificioComboInicio;
    private JComboBox<Edificio> edificioComboDestino;
    private JComboBox<Sala> salaComboInicio;
    private JComboBox<Sala> salaComboDestino;
    private boolean recursosCargadosCorrectamente = true;

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

        btnIniciar.addActionListener(e -> panelDestino());

        btnSalir.addActionListener(e -> System.exit(0));

        gbc.gridy = 0;
        add(btnIniciar, gbc);

        gbc.gridy = 1;
        add(btnSalir, gbc);

        revalidate();
        repaint();
        setVisible(true);
        if (!recursosCargadosCorrectamente) {
            JOptionPane.showMessageDialog(
                    this,
                    "⚠️ Algunos archivos gráficos no se encontraron.\nPor favor, revisa la instalación.",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE
            );
        }

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
            if (is == null) {
                System.err.println("❌ No se encontró el archivo: " + path);
                recursosCargadosCorrectamente = false;
                return null;
            }
            Image img = ImageIO.read(is);
            return new ImageIcon(img.getScaledInstance(192, 64, Image.SCALE_SMOOTH));
        } catch (IOException e) {
            System.err.println("❌ Error al cargar ícono: " + path);
            recursosCargadosCorrectamente = false;
            return null;
        }
    }



//  Inicializacion Mapa, va después que carga MapaPanel
public void iniciarMapa(Edificio edificioActual,Piso pisoInicial, Ruta ruta,
                        ControladorPanel controlador, int pisoActual, BufferedImage imagenMapaGoogle) {
    getContentPane().removeAll();
    setLayout(new BorderLayout());
    JPanel container = new JPanel(new CardLayout());

    // Panel del mapa
    MapaPanel mapaPanel = new MapaPanel(pisoInicial, ruta, controlador);
    container.add(mapaPanel,"MapaPanel");

    CardLayout cl = (CardLayout) container.getLayout();
    cl.show(container, "MapaPanel");
    add(container);

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
    btnBajar = crearBotonPiso("iconos/minuspng.png", e -> controlador.cambiarPiso(edificioActual,-1));
    btnSubir = crearBotonPiso("iconos/pluspng.png", e -> controlador.cambiarPiso(edificioActual,+1));
    panelCentro.add(btnBajar);
    panelCentro.add(btnSubir);

    if (imagenMapaGoogle != null){
        ImagenPanel mapaGoogle = new ImagenPanel(imagenMapaGoogle);
        container.add(mapaGoogle,"MapaGoogle");

        btnMapaGlobal = crearBotonPiso("iconos/earth.png",e -> {
            cl.next(container);
        });
        panelCentro.add(btnMapaGlobal);
    }

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
                System.err.println("❌ Falta ícono: " + pathIcono);
                recursosCargadosCorrectamente = false;
                boton.setText("?");
            }
        } catch (IOException e) {
            System.err.println("❌ Error cargando ícono: " + pathIcono);
            recursosCargadosCorrectamente = false;
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

    private void updateSalaCombo(JComboBox<Sala> salaCombo, JComboBox<Edificio> edificioCombo) {
        // Get the currently selected building
        Edificio selectedEdificio = (Edificio) edificioCombo.getSelectedItem();

        // Clear the current room list
        salaCombo.removeAllItems();

        if (selectedEdificio != null) {
            for (Sala sala : controlador.getSalasEdificios(selectedEdificio)) {
                salaCombo.addItem(sala);
            }
        }

        // Enable/disable based on room availability
        salaCombo.setEnabled(salaCombo.getItemCount() > 0);
    }

    private void panelDestino(){
        List<Edificio> edificios = controlador.getEdificios();

        edificioComboInicio = new JComboBox<>();
        for (Edificio edificio : edificios) {
            edificioComboInicio.addItem(edificio);
        }

        edificioComboDestino = new JComboBox<>();
        for (Edificio edificio : edificios) {
            edificioComboDestino.addItem(edificio);
        }


        salaComboInicio = new JComboBox<Sala>();
        salaComboDestino = new JComboBox<Sala>();

        JCheckBox chkAscensor = new JCheckBox("Necesito ascensor");

        JPanel panel = new JPanel(new GridLayout(3, 1));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        edificioComboInicio.addActionListener(ev -> updateSalaCombo(salaComboInicio,edificioComboInicio));
        edificioComboDestino.addActionListener(ev -> updateSalaCombo(salaComboDestino,edificioComboDestino));


        JPanel inicio = new JPanel(new GridLayout(1,2));
        JPanel destino = new JPanel(new GridLayout(1,2));


        inicio.add(new JLabel("Seleccione Edificio actual: "));
        inicio.add(edificioComboInicio,BorderLayout.NORTH);

        inicio.add(new JLabel("Seleccione Sala actual: "));
        inicio.add(salaComboInicio,BorderLayout.SOUTH);

        destino.add(new JLabel("Seleccione Edificio destino: "));
        destino.add(edificioComboDestino,BorderLayout.NORTH);


        destino.add(new JLabel("Seleccione Sala destino: "));
        destino.add(salaComboDestino,BorderLayout.SOUTH);

        panel.add(inicio);
        panel.add(destino);
        panel.add(chkAscensor);

        edificioComboInicio.setSelectedIndex(0);
        updateSalaCombo(salaComboInicio,edificioComboInicio);

        edificioComboDestino.setSelectedIndex(0);
        updateSalaCombo(salaComboDestino,edificioComboDestino);

        int opcion = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Configuración de ruta",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (opcion == JOptionPane.OK_OPTION) {
            Edificio edificioOrigen = (Edificio) edificioComboInicio.getSelectedItem();
            Edificio edificioDestino = (Edificio) edificioComboDestino.getSelectedItem();
            Sala salaOrigen = (Sala) salaComboInicio.getSelectedItem();
            Sala salaDestino = (Sala) salaComboDestino.getSelectedItem();
            boolean necesitaAscensor = chkAscensor.isSelected();

            System.out.println(edificioOrigen.getNombre());
            System.out.println(edificioDestino.getNombre());

            if (Objects.equals(edificioOrigen.getNombre(), edificioDestino.getNombre())){
                controlador.iniciarMapaSala(edificioOrigen,salaOrigen,salaDestino,necesitaAscensor);
            }
            else {
                verificarClaveGoogle();
                controlador.iniciarMapaEdificios(edificioOrigen,edificioDestino,salaOrigen,necesitaAscensor);
            }
        }


    }

    private void verificarClaveGoogle() {
        File keyFile = new File("api_key.txt");

        if (!keyFile.exists()) {
            JOptionPane.showMessageDialog(
                    this,
                    "❌ No se encontró el archivo 'api_key.txt'.\nLa clave de Google Maps es necesaria para el funcionamiento del mapa.",
                    "Clave faltante",
                    JOptionPane.ERROR_MESSAGE
            );
            System.exit(1); // Cierra el programa porque no puede continuar sin clave
        }
    }

}
