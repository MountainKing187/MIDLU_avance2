package modelo;

import java.util.ArrayList;

public class Piso {
    private int numero;
    private String imagenPath;
    private ArrayList<Sala> salas;
    private ArrayList<PuntoAcceso> puntosAcceso; // escaleras/ascensores
    private ArrayList<Salida> salidas;
    private boolean[][] mapaObstaculos; // matriz de navegabilidad

    // métodos para procesar el bitmap y generar mapaObstaculos
}