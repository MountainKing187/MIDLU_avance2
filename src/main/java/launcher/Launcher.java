package launcher;

import interfaz.PanelPrincipal;
import servicios.CargadorInicial;
import servicios.ControladorPanel;

public class Launcher {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            // 1. Cargar toda la información desde JSON
            CargadorInicial cargador = new CargadorInicial("src/main/resources/EdificiosJSON/EdificioEjemplo.json");

            // 2. Crear la ventana principal
            PanelPrincipal ventana = new PanelPrincipal();

            // 3. Crear el controlador con los datos cargados
            ControladorPanel controlador = new ControladorPanel(ventana);

            // 4. Conectar la ventana al controlador
            ventana.setControlador(controlador);

            // 5. Mostrar el menú principal
            ventana.mostrarMenu();
        });
    }
}
