package servicios;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;

public class GoogleStaticMapRequester {
    private String apiKey;
    private static final String API_KEY_FILE = "api_key.txt";
    private static final String BASE_URL = "https://maps.googleapis.com/maps/api/staticmap";

    public GoogleStaticMapRequester() throws IOException {
        this.apiKey = readApiKeyFromFile();
        if (this.apiKey == null || this.apiKey.isEmpty()) {
            throw new IOException("API key is empty or invalid");
        }
    }

    private String readApiKeyFromFile() throws IOException {
        Path path = Paths.get(API_KEY_FILE);
        if (!Files.exists(path)) {
            throw new FileNotFoundException("API key file not found at: " + path.toAbsolutePath());
        }
        return Files.readString(path).trim();
    }

    public byte[] getRouteMap(double originLat, double originLng,
                              double destLat, double destLng)
            throws IOException, InterruptedException {
        return getRouteMap(originLat, originLng, destLat, destLng, 600, 400, null);
    }

    public byte[] getRouteMap(double originLat, double originLng,
                              double destLat, double destLng,
                              Integer width, Integer height,
                              Integer zoom) throws IOException, InterruptedException {
        if (width == null) width = 600;
        if (height == null) height = 400;

        // Get walking route polyline
        String polyline = getWalkingRoutePolyline(originLat, originLng, destLat, destLng);

        // URL encode parameters
        String markers = String.format("markers=%s&markers=%s",
                URLEncoder.encode("color:red|" + originLat + "," + originLng, "UTF-8"),
                URLEncoder.encode("color:blue|" + destLat + "," + destLng, "UTF-8"));

        // Use encoded polyline for path
        String path = String.format("path=%s",
                URLEncoder.encode("color:0x0000ff|weight:5|enc:" + polyline, "UTF-8"));

        String url = String.format("%s?%s&%s&size=%dx%d&key=%s",
                BASE_URL, markers, path, width, height, apiKey);

        if (zoom != null) {
            url += "&zoom=" + zoom;
        }

        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(15))
                .build();

        HttpResponse<byte[]> response = client.send(
                request,
                HttpResponse.BodyHandlers.ofByteArray()
        );

        if (response.statusCode() != 200) {
            throw new IOException("API request failed. Status: " + response.statusCode() +
                    "\nResponse: " + new String(response.body()));
        }

        return response.body();
    }

    private String getWalkingRoutePolyline(double originLat, double originLng,
                                           double destLat, double destLng)
            throws IOException, InterruptedException {
        String url = String.format(
                "https://maps.googleapis.com/maps/api/directions/json" +
                        "?origin=%f,%f&destination=%f,%f&mode=walking&key=%s",
                originLat, originLng, destLat, destLng, apiKey
        );

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(15))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("Directions API request failed. Status: " + response.statusCode());
        }

        // Parse JSON response
        JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();
        String status = json.get("status").getAsString();

        if (!"OK".equals(status)) {
            throw new IOException("Directions API error: " + status);
        }

        JsonArray routes = json.getAsJsonArray("routes");
        if (routes.size() == 0) {
            throw new IOException("No walking route found");
        }

        // Extract polyline from first route
        return routes.get(0).getAsJsonObject()
                .getAsJsonObject("overview_polyline")
                .get("points").getAsString();
    }
}