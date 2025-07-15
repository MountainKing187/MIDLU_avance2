package persistencia;

import modelo.*;
import modelo.edificio.Edificio;
import modelo.edificio.MapaProcesado;
import modelo.edificio.Piso;
import modelo.elementos.Ascensor;
import modelo.elementos.Escalera;
import modelo.elementos.Sala;
import modelo.elementos.Salida;
import modelo.navegacion.Punto;
import servicios.ProcesadorMapa;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class CargadorEdificios {

    public static Edificio cargarDesdeJSON(String rutaArchivo) throws Exception {
        try (InputStream is = new FileInputStream(new File(rutaArchivo))) {
            JSONTokener tokener = new JSONTokener(is);
            JSONObject jsonEdificio = new JSONObject(tokener);

            // Crear el edificio
            Edificio edificio = new Edificio(jsonEdificio.getString("nombre"), jsonEdificio.getDouble("latitud"), jsonEdificio.getDouble("longitud") );

            // Mapa para conectar puntos de acceso por ID
            Map<String, PuntoAcceso> mapaPuntosAcceso = new HashMap<>();

            // Primera pasada: crear todos los pisos
            JSONArray jsonPisos = jsonEdificio.getJSONArray("pisos");
            for (int i = 0; i < jsonPisos.length(); i++) {
                JSONObject jsonPiso = jsonPisos.getJSONObject(i);
                Piso piso = crearPisoDesdeJSON(jsonPiso);

                // Procesar salas y puntos de acceso
                procesarSalas(jsonPiso, piso);
                procesarPuntosAcceso(jsonPiso, piso, mapaPuntosAcceso);
                procesarSalidas(jsonPiso, piso);

                edificio.agregarPiso(piso);
            }

            // Segunda pasada: establecer conexiones entre puntos de acceso
            establecerConexionesPuntosAcceso(jsonPisos, mapaPuntosAcceso);

            return edificio;
        }
    }

    private static Piso crearPisoDesdeJSON(JSONObject jsonPiso) throws Exception {
        int numero = jsonPiso.getInt("numero");
        String nombre = jsonPiso.getString("nombre");
        String imagenPath = jsonPiso.getString("imagen");

        // Procesar el bitmap para obtener mapa de obstáculos
        MapaProcesado mapa = ProcesadorMapa.procesarBitmap(imagenPath);
        return new Piso(numero, nombre, mapa);
    }

    private static void procesarSalas(JSONObject jsonPiso, Piso piso) {
        if (!jsonPiso.has("salas")) return;

        JSONArray jsonSalas = jsonPiso.getJSONArray("salas");
        for (int i = 0; i < jsonSalas.length(); i++) {
            JSONObject jsonSala = jsonSalas.getJSONObject(i);
            String nombre = jsonSala.getString("nombre");

            Sala sala = new Sala(nombre);

            // Procesar todas las entradas
            JSONArray jsonEntradas = jsonSala.getJSONArray("entradas");
            for (int j = 0; j < jsonEntradas.length(); j++) {
                JSONObject jsonEntrada = jsonEntradas.getJSONObject(j);
                Punto entrada = crearPuntoDesdeJSON(jsonEntrada, piso);
                sala.agregarEntrada(entrada);
            }

            piso.agregarSala(sala);
        }
    }

    private static void procesarPuntosAcceso(JSONObject jsonPiso, Piso piso,
                                             Map<String, PuntoAcceso> mapaPuntosAcceso) {
        if (!jsonPiso.has("puntosAcceso")) return;

        JSONArray jsonPuntos = jsonPiso.getJSONArray("puntosAcceso");
        for (int i = 0; i < jsonPuntos.length(); i++) {
            JSONObject jsonPA = jsonPuntos.getJSONObject(i);

            // Crear punto de acceso
            Punto ubicacion = crearPuntoDesdeJSON(jsonPA, piso);
            PuntoAcceso punto = crearPuntoAccesoDesdeJSON(jsonPA, ubicacion);

            // Registrar con ID si existe
            if (jsonPA.has("id")) {
                String id = jsonPA.getString("id");
                mapaPuntosAcceso.put(id, punto);
            }

            piso.agregarPuntoAcceso(punto);
        }
    }

    private static PuntoAcceso crearPuntoAccesoDesdeJSON(JSONObject jsonPA, Punto ubicacion) {
        String tipo = jsonPA.getString("tipo");

        switch (tipo) {
            case "ASCENSOR":
                return new Ascensor(ubicacion, ubicacion.getPiso());
            case "ESCALERA":
                return new Escalera(ubicacion, ubicacion.getPiso());
            default:
                throw new IllegalArgumentException("Tipo de punto de acceso desconocido: " + tipo);
        }
    }

    private static void procesarSalidas(JSONObject jsonPiso, Piso piso) {
        if (!jsonPiso.has("salidas")) return;

        JSONArray jsonSalidas = jsonPiso.getJSONArray("salidas");
        for (int i = 0; i < jsonSalidas.length(); i++) {
            JSONObject jsonSalida = jsonSalidas.getJSONObject(i);

            String nombre = jsonSalida.getString("nombre");
            Punto ubicacion = crearPuntoDesdeJSON(jsonSalida, piso);
            boolean esPrincipal = jsonSalida.getBoolean("esPrincipal");

            piso.agregarSalida(new Salida(nombre, ubicacion, esPrincipal));
        }
    }

    private static Punto crearPuntoDesdeJSON(JSONObject jsonPunto, Piso piso) {
        int x = jsonPunto.getInt("x");
        int y = jsonPunto.getInt("y");
        return new Punto(x, y, piso);
    }

    private static void establecerConexionesPuntosAcceso(JSONArray jsonPisos,
                                                         Map<String, PuntoAcceso> mapaPuntosAcceso) {
        for (int i = 0; i < jsonPisos.length(); i++) {
            JSONObject jsonPiso = jsonPisos.getJSONObject(i);

            if (!jsonPiso.has("puntosAcceso")) continue;

            JSONArray jsonPuntos = jsonPiso.getJSONArray("puntosAcceso");
            for (int j = 0; j < jsonPuntos.length(); j++) {
                JSONObject jsonPA = jsonPuntos.getJSONObject(j);

                // Si tiene conexión, establecerla
                if (jsonPA.has("conectadoId")) {
                    String idOrigen = jsonPA.getString("id");
                    String idDestino = jsonPA.getString("conectadoId");

                    PuntoAcceso origen = mapaPuntosAcceso.get(idOrigen);
                    PuntoAcceso destino = mapaPuntosAcceso.get(idDestino);

                    if (origen != null && destino != null) {
                        origen.conectarCon(destino);
                    } else {
                        System.err.println("Error conectando puntos: " + idOrigen + " -> " + idDestino);
                    }
                }
            }
        }
    }
}