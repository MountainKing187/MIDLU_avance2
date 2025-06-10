import algoritmos.Pathfinding;
import modelo.MapaProcesado;
import modelo.Piso;
import modelo.Punto;
import modelo.Ruta;
import utils.ManejoMapaBmp;

import java.io.IOException;

public class launch {
    public static void main(String[] args) throws IOException {
        ManejoMapaBmp mapa = new ManejoMapaBmp("src/main/resources/imagenes/Piso2.bmp");
        mapa.procesarBitmap();
        MapaProcesado mapaProcesado = new MapaProcesado(mapa.getMapaObstaculos(),mapa.getImagenPiso());
        Piso piso1 = new Piso(1,"piso 1", mapaProcesado);
        Ruta ruta = Pathfinding.encontrarRuta(new Punto(0,0, piso1),new Punto(19,8,piso1),false);

        boolean dibua = false;

        for (int i = 0; i < mapa.getMapaObstaculos().length; i++) {
            for (int j = 0; j < mapa.getMapaObstaculos()[i].length; j++) {
                for (Punto punto : ruta.getPuntos()){
                    if (punto.getX() == j && punto.getY() == i) dibua = true;
                }
                if (dibua) System.out.print("x ");
                else if (mapa.getMapaObstaculos()[i][j]) System.out.print("■ ");
                else System.out.print(". ");
                dibua = false;
            }
            System.out.println("");
        }
    }
}
