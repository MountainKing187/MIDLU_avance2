package servicios;

import modelo.elementos.Sala;
import modelo.edificio.Edificio;
import modelo.edificio.Piso;
import persistencia.CargadorEdificios;

import java.util.ArrayList;

public class CargadorInicial {

    private final Edificio edificio;
    private final ArrayList<Sala> salas;

    public CargadorInicial(String rutaJSON) {
        try {
            this.edificio = CargadorEdificios.cargarDesdeJSON(rutaJSON);
        } catch (Exception e) {
            throw new RuntimeException("Error al cargar edificio desde JSON", e);
        }
        this.salas = extraerSalas(edificio);
    }

    private ArrayList<Sala> extraerSalas(Edificio edificio) {
        ArrayList<Sala> lista = new ArrayList<>();
        for (Piso piso : edificio.getPisos()) {
            for (Sala sala : piso.getSalas()) {
                lista.add(sala);
            }
        }
        return lista;
    }

    public Edificio getEdificio() {
        return edificio;
    }

    public ArrayList<Sala> getSalas() {
        return salas;
    }
}
