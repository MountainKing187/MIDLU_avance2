import modelo.PuntoAcceso;
import modelo.edificio.Edificio;
import persistencia.CargadorEdificios;
import servicios.Navegador;
import modelo.edificio.Piso;
import modelo.navegacion.Punto;
import modelo.navegacion.Ruta;

import java.util.ArrayList;

public class launch {
    public static void main(String[] args) throws Exception {

        Edificio r = CargadorEdificios.cargarDesdeJSON("src/main/resources/EdificiosJSON/EdificioEjemplo.json");
        Navegador navegador = new Navegador(r);
        Punto punto1 = new Punto(53,50,r.getPiso(1));
        Punto punto2 = new Punto(53,12,r.getPiso(2));
        ArrayList<Ruta> rutas = navegador.calcularRutaCompleta(punto1,punto2,false);

        System.out.println(rutas.size());

        for (Ruta ruta : rutas){
            for (Punto punto : ruta.getPuntos()){
                System.out.println("(x= "+punto.getX()+", y= "+punto.getY()+", piso="+punto.getPiso().getNombre()+")");
            }
        }
        imprimirRuta(punto1.getPiso(),rutas.get(0));
        imprimirRuta(punto2.getPiso(),rutas.get(1));
        /*
        imprimirRuta(r.getPiso(1),rutas.getFirst());
        imprimirRuta(r.getPiso(2),rutas.get(2));
        */

    }

    private static void imprimirRuta(Piso piso, Ruta ruta){
        boolean dibuja = false;
        boolean[][] obstaculos = piso.getMapaObstaculos();

        for (int i = 0; i < obstaculos.length; i++) {
            for (int j = 0; j < obstaculos[i].length; j++) {
                for (Punto punto : ruta.getPuntos()){
                    if (punto.getX() == j && punto.getY() == i) dibuja = true;
                }
                if (dibuja) System.out.print("x ");
                else if (obstaculos[i][j]) System.out.print("■ ");
                else System.out.print(". ");
                dibuja = false;
            }
            System.out.println("");
        }
    }
}
