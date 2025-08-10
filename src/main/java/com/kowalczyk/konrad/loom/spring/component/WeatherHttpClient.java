package com.kowalczyk.konrad.loom.spring.component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;


@Component
public class WeatherHttpClient {

    private final HttpClient httpClient;
    private final ObjectMapper mapper;

    public WeatherHttpClient() {
        this.httpClient = HttpClient.newHttpClient();
        this.mapper = new ObjectMapper();
    }

    public CompletableFuture<String> getTemperatureAsync(double latitude, double longitude) {
        String uri = String.format(
                "https://api.open-meteo.com/v1/forecast?latitude=%s&longitude=%s&hourly=temperature_2m",
                latitude, longitude
        );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .GET()
                .build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(this::parseTemperature);
    }

    public String getTemperatureSync(double latitude, double longitude) {
        String uri = String.format(
                "https://api.open-meteo.com/v1/forecast?latitude=%s&longitude=%s&hourly=temperature_2m",
                latitude, longitude
        );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .GET()
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return parseTemperature(response.body());
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch weather", e);
        }
    }

    private String parseTemperature(String json) {
        try {
            JsonNode root = mapper.readTree(json);
            JsonNode temps = root.path("hourly").path("temperature_2m");
            if (temps.isArray() && !temps.isEmpty()) {
//                return STR."\{temps.get(0).asDouble()}°C"; preview
                return temps.get(0).asDouble() + "°C";
            }
            return "No temperature data";
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse weather data", e);
        }
    }
}
