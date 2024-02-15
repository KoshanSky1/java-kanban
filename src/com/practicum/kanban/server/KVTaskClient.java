package com.practicum.kanban.server;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private final String url = "http://localhost:8078";
    private String apiToken;
    private final HttpClient client = HttpClient.newHttpClient();

    public KVTaskClient() {
        register();
    }

    public void put(String key, String json) {
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(URI.create(url + "/save" + key + "?API_TOKEN=" + apiToken))
                .header("Content-Type", "application/json")
                .build();
        try {
            HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public String load(String key) {
        HttpResponse<String> response = null;
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url + "/load" + key + "?API_TOKEN=" + apiToken))
                .header("Content-Type", "application/json")
                .build();
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        assert response != null;
        return response.body();
    }

    private void register() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create(url + "/register"))
                    .header("Content-Type", "application/json")
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            apiToken = response.body();
            System.out.println(apiToken);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

}