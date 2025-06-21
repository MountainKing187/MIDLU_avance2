import servicios.Pathfinding;
import modelo.edificio.MapaProcesado;
import modelo.edificio.Piso;
import modelo.navegacion.Punto;
import modelo.navegacion.Ruta;
import servicios.ProcesadorMapa;

import java.io.IOException;

public class launch {
    public static void main(String[] args) throws IOException {
        MapaProcesado mapa = ProcesadorMapa.procesarBitmap("src/main/resources/imagenes/Piso2.bmp");
        Piso piso1 = new Piso(1,"piso 1", mapa);
        Ruta ruta = Pathfinding.encontrarRuta(new Punto(0,0, piso1),new Punto(19,8,piso1),false);

        boolean dibua = false;

        for (int i = 0; i < mapa.getObstaculos().length; i++) {
            for (int j = 0; j < mapa.getObstaculos()[i].length; j++) {
                for (Punto punto : ruta.getPuntos()){
                    if (punto.getX() == j && punto.getY() == i) dibua = true;
                }
                if (dibua) System.out.print("x ");
                else if (mapa.getObstaculos()[i][j]) System.out.print("■ ");
                else System.out.print(". ");
                dibua = false;
            }
            System.out.println("");
        }
    }
}
