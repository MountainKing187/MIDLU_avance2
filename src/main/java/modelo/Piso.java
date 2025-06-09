package modelo;

import modelo.Ascensor;
import modelo.Escalera;
import java.util.ArrayList;

public class Piso {
    private int numero;
    private String imagenPath;
    private ArrayList<Sala> salas;
    private ArrayList<Escalera> escaleras;
    private ArrayList<Ascensor> ascensores;
    private ArrayList<Salida> salidas;
    private boolean[][] mapaObstaculos; // matriz de navegabilidad

    // métodos para procesar el bitmap y generar mapaObstaculos
}