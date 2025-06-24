package servicios;

import modelo.elementos.Sala;
import modelo.edificio.Edificio;
import modelo.edificio.Piso;
import persistencia.CargadorEdificios;

import javax.swing.*;
import java.util.ArrayList;

public class CargadorInicial {

    private final Edificio edificio;
    private final ArrayList<Sala> salas;

    public CargadorInicial(String rutaJSON) {
        Edificio edificioCargado = null;

        try {
            edificioCargado = CargadorEdificios.cargarDesdeJSON(rutaJSON);
        } catch (Exception e) {
            mostrarErrorYSalir(
                    "Error en la instalación: revisa el mapa JSON.",
                    "Fallo al cargar JSON"
            );
        }

        if (edificioCargado == null) {
            mostrarErrorYSalir(
                    "Error en la instalación: revisa el mapa JSON.",
                    "Fallo crítico en estructura"
            );
        }

        // Verificar imágenes BMP de cada piso
        for (Piso piso : edificioCargado.getPisos()) {
            if (piso.getImagenMapa() == null) {
                System.err.println("❌ Imagen del piso no cargada correctamente.");
                mostrarErrorYSalir(
                        "Error en la instalación: BMP no encontrado.",
                        "Fallo en imagen del mapa"
                );
            }
        }


        this.edificio = edificioCargado;
        this.salas = extraerSalas(edificio);
    }

    private ArrayList<Sala> extraerSalas(Edificio edificio) {
        ArrayList<Sala> lista = new ArrayList<>();
        for (Piso piso : edificio.getPisos()) {
            lista.addAll(piso.getSalas());
        }
        return lista;
    }

    private void mostrarErrorYSalir(String mensaje, String titulo) {
        JOptionPane.showMessageDialog(
                null,
                mensaje,
                titulo,
                JOptionPane.ERROR_MESSAGE
        );
        System.exit(1);
    }

    public Edificio getEdificio() {
        return edificio;
    }

    public ArrayList<Sala> getSalas() {
        return salas;
    }
}
