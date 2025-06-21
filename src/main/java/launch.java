import modelo.edificio.Edificio;
import persistencia.CargadorEdificios;
import servicios.Navegador;
import servicios.Pathfinding;
import modelo.edificio.MapaProcesado;
import modelo.edificio.Piso;
import modelo.navegacion.Punto;
import modelo.navegacion.Ruta;
import servicios.ProcesadorMapa;

import java.io.IOException;
import java.util.ArrayList;

public class launch {
    public static void main(String[] args) throws Exception {

        Edificio r = CargadorEdificios.cargarDesdeJSON("src/main/resources/EdificiosJSON/EdificioEjemplo.json");
        Navegador navegador = new Navegador(r);
        Punto punto1 = new Punto(22,71,r.getPiso(1));
        Punto punto2 = new Punto(49,61,r.getPiso(2));
        ArrayList<Ruta> rutas = navegador.calcularRutaCompleta(punto1,punto2,true);


        for (Ruta ruta : rutas){
            for (Punto punto : ruta.getPuntos()){
                System.out.println("(x= "+punto.getX()+", y= "+punto.getY()+", piso="+punto.getPiso().getNombre()+")");
            }
            imprimirRuta(ruta.getPuntos().getFirst().getPiso(),ruta);
        }/*
        imprimirRuta(r.getPiso(1),rutas.getFirst());
        imprimirRuta(r.getPiso(2),rutas.get(2));
        */

    }

    private static void imprimirRuta(Piso piso, Ruta ruta){
        boolean dibua = false;
        boolean[][] obstaculos = piso.getMapaObstaculos();

        for (int i = 0; i < obstaculos.length; i++) {
            for (int j = 0; j < obstaculos[i].length; j++) {
                for (Punto punto : ruta.getPuntos()){
                    if (punto.getX() == j && punto.getY() == i) dibua = true;
                }
                if (dibua) System.out.print("x ");
                else if (obstaculos[i][j]) System.out.print("■ ");
                else System.out.print(". ");
                dibua = false;
            }
            System.out.println("");
        }
    }
}
