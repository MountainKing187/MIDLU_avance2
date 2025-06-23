import modelo.edificio.Edificio;
import persistencia.CargadorEdificios;
import servicios.ControladorPanel;

public class launch {
    public static void main(String[] args) throws Exception {
        Edificio edificio = CargadorEdificios.cargarDesdeJSON("src/main/resources/EdificiosJSON/EdificioEjemplo.json");
        ControladorPanel controlador = new ControladorPanel(edificio);
        controlador.iniciar();
    }
}
