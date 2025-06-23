import modelo.edificio.Edificio;
import persistencia.CargadorEdificios;
import servicios.Navegador;
import modelo.edificio.Piso;
import modelo.navegacion.Punto;
import modelo.navegacion.Ruta;

import java.util.ArrayList;

public class launch {
    public static void main(String[] args) throws Exception {

        Edificio r = CargadorEdificios.cargarDesdeJSON("src/main/resources/EdificiosJSON/EdificioEjemplo.json"); //Crea un edificio desde un json
        Navegador navegador = new Navegador(r); //Crea un objeto navegador usando el edificio recien creado

        //Puntos que se usaran para navegar
        Punto punto1 = new Punto(53,50,r.getPiso(1));
        Punto punto2 = new Punto(53,12,r.getPiso(2));

        ArrayList<Ruta> rutas = navegador.calcularRutaCompleta(punto1,punto2,false);
        //Se crea un array de rutas, cada ruta ocurre en un solo piso, en este ejemplo, el primer elemento de rutas,
        //es la ruta del primer punto hacia un punto de acceso mas cercano que este disponible

        //este codigo se hace para imprimir cada ruta por piso
        for (Ruta ruta : rutas){
            for (Punto punto : ruta.getPuntos()){
                // System.out.println("(x= "+punto.getX()+", y= "+punto.getY()+", piso="+punto.getPiso().getNombre()+")");
                //esta linea imprime las coordenadas de todos los puntos dentro de la ruta y el piso que estan localizadas
            }
            imprimirRuta(ruta.getPuntoInicio().getPiso(), ruta);
        }

    }
    // Metodo para imprimir una matriz con las posiciones de las paredes y rutas dentro
    // no imprime puntos de accesos
    private static void imprimirRuta(Piso piso, Ruta ruta){
        boolean dibuja = false;
        boolean[][] obstaculos = piso.getMapaObstaculos();

        for (int i = 0; i < obstaculos.length; i++) {
            for (int j = 0; j < obstaculos[i].length; j++) {
                for (Punto punto : ruta.getPuntos()){
                    if (punto.getX() == j && punto.getY() == i) {
                        dibuja = true;
                        break;
                    }
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
