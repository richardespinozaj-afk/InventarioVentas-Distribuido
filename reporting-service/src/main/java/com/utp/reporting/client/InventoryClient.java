package com.utp.reporting.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

// Cliente REST hacia el servicio principal (a traves del balanceador), con reintentos.
public class InventoryClient {

    private static String base() {
        String v = System.getenv("INVENTORY_URL");
        return (v == null || v.isEmpty()) ? "http://loadbalancer/api" : v;
    }

    private final HttpClient http = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(3)).build();
    private final ObjectMapper mapper = new ObjectMapper();

    public JsonNode get(String path) throws Exception {
        Exception last = null;
        for (int intento = 1; intento <= 3; intento++) {
            try {
                HttpRequest req = HttpRequest.newBuilder()
                        .uri(URI.create(base() + path))
                        .timeout(Duration.ofSeconds(4))
                        .header("Accept", "application/json")
                        .GET().build();
                HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
                if (res.statusCode() >= 200 && res.statusCode() < 300) {
                    return mapper.readTree(res.body());
                }
                last = new RuntimeException("HTTP " + res.statusCode());
            } catch (Exception e) {
                last = e;
                Thread.sleep(300L * intento);
            }
        }
        throw new RuntimeException("No se pudo contactar al servicio de inventario: "
                + (last == null ? "" : last.getMessage()), last);
    }
}
