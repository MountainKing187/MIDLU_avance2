package modelo.edificio;

import modelo.PuntoAcceso;
import modelo.elementos.Sala;
import modelo.elementos.Salida;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Piso {
    private int numero;
    private String nombre;
    private boolean[][] obstaculos;
    private BufferedImage imagenMapa;

    private ArrayList<Sala> salas = new ArrayList<>();
    private ArrayList<PuntoAcceso> puntosAcceso = new ArrayList<>();
    private ArrayList<Salida> salidas = new ArrayList<>();

    public Piso(int numero, String nombre, MapaProcesado mapa) {
        this.numero = numero;
        this.nombre = nombre;
        this.obstaculos = mapa.getObstaculos();
        this.imagenMapa = mapa.getImagenOriginal();
    }

    // Métodos para agregar entidades
    public void agregarSala(Sala sala) { salas.add(sala); }
    public void agregarPuntoAcceso(PuntoAcceso punto) { puntosAcceso.add(punto); }
    public void agregarSalida(Salida salida) { salidas.add(salida); }

    // Getters
    public int getNumero() { return numero; }
    public String getNombre() { return nombre; }
    public boolean[][] getMapaObstaculos() { return obstaculos; }
    public ArrayList<Sala> getSalas() { return salas; }
    public ArrayList<PuntoAcceso> getPuntosAcceso() { return puntosAcceso; }
    public ArrayList<Salida> getSalidas() { return salidas; }

    public boolean esTransitable(int x, int y) {
        if (x < 0 || y < 0 || x >= obstaculos.length || y >= obstaculos[0].length) {
            return false;
        }
        return !obstaculos[x][y];
    }

    public BufferedImage getImagenMapa() {
        return imagenMapa;
    }

}