package modelo;

import modelo.Ascensor;
import modelo.Escalera;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Piso {
    private final BufferedImage imagenMapa;
    private int numero;
    private String imagenPath;
    private ArrayList<Sala> salas;
    private ArrayList<Escalera> escaleras;
    private ArrayList<Ascensor> ascensores;
    private ArrayList<Salida> salidas;
    private boolean[][] obstaculos; // matriz de navegabilidad

    public Piso(int numero, String nombre, MapaProcesado mapa) {
        this.numero = numero;
        this.obstaculos = mapa.getObstaculos();
        this.imagenMapa = mapa.getImagenOriginal();
        this.salas = new ArrayList<>();
        this.escaleras = new ArrayList<>();
        this.ascensores = new ArrayList<>();
        this.salidas = new ArrayList<>();
    }

    // Métodos para manejar entidades
    public void agregarSala(Sala sala) { /* ... */ }
    public void agregarPuntoAcceso(PuntoAcceso punto) { /* ... */ }

    // Getter para pathfinding
    public boolean esTransitable(int x, int y) {
        return !obstaculos[x][y];
    }
}