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
                    "Fallo al cargar JSON"
            );
        }

        if (edificioCargado == null) {
            mostrarErrorYSalir(
                    "Fallo crítico en estructura"
            );
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

    private void mostrarErrorYSalir(String titulo) {
        JOptionPane.showMessageDialog(
                null,
                "Error en la instalación: mapa faltante.",
                titulo,
                JOptionPane.ERROR_MESSAGE
        );
        System.exit(1);
    }

    public Edificio getEdificio() {
        return edificio;
    }
    
}
