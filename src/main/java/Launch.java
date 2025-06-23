import interfaz.PanelPrincipal;
import servicios.ControladorPanel;

public class Launch {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            PanelPrincipal ventana = new PanelPrincipal(); // Creamos el JFrame vacío
            ControladorPanel controlador = new ControladorPanel(ventana); // Pasamos referencia
            ventana.setControlador(controlador); // Lo conectamos
            ventana.mostrarMenu(); // Carga botones
        });
    }
}
