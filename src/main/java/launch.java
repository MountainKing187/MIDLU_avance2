import utils.ManejoMapaBmp;

import java.io.IOException;

public class launch {
    public static void main(String[] args) throws IOException {
        ManejoMapaBmp mapa = new ManejoMapaBmp("src/main/resources/imagenes/Piso2.bmp");
        mapa.procesarBitmap();
        for(boolean[] fila : mapa.getMapaObstaculos()){
            for (boolean columna: fila){
                if (columna) System.out.print("■ ");
                else System.out.print(". ");
            }
            System.out.println("");
        }
    }
}
