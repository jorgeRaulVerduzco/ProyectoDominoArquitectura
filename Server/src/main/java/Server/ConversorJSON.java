/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server;

import Dominio.Jugador;
import Dominio.Sala;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 *
 * @author INEGI
 */
public class ConversorJSON {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String convertirSalasAJson(List<Sala> salas) throws IOException {
     Gson gson = new Gson();
        return gson.toJson(salas);
    }

    public static List<Sala> convertirJsonASalas(String json) throws IOException {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Sala.class, new JsonDeserializer<Sala>() {
                    @Override
                    public Sala deserialize(JsonElement json, Type typeOfT,
                            JsonDeserializationContext context) throws JsonParseException {
                        JsonObject jsonObject = json.getAsJsonObject();

                        Sala sala = new Sala(); // Asume constructor vacío

                        // Deserializar campos básicos
                        if (jsonObject.has("id")) {
                            sala.setId(jsonObject.get("id").getAsString());
                        }
                        if (jsonObject.has("cantJugadores")) {
                            sala.setCantJugadores(jsonObject.get("cantJugadores").getAsInt());
                        }
                        if (jsonObject.has("numeroFichas")) {
                            sala.setNumeroFichas(jsonObject.get("numeroFichas").getAsInt());
                        }
                        if (jsonObject.has("estado")) {
                            sala.setEstado(jsonObject.get("estado").getAsString());
                        }

                        // Deserializar jugadores
                        if (jsonObject.has("jugadores")) {
                            JsonArray jugadoresArray = jsonObject.getAsJsonArray("jugadores");
                            List<Jugador> jugadores = new ArrayList<>();
                            for (JsonElement jugadorElement : jugadoresArray) {
                                JsonObject jugadorObj = jugadorElement.getAsJsonObject();
                                Jugador jugador = new Jugador();
                                if (jugadorObj.has("nombre")) {
                                    jugador.setNombre(jugadorObj.get("nombre").getAsString());
                                }
                                // Agrega más propiedades de Jugador si es necesario
                                jugadores.add(jugador);
                            }
                            sala.setJugador(jugadores);
                        }

                        // Opcional: deserializar partida si es necesario
                        // if (jsonObject.has("partida")) {
                        //     Partida partida = new Partida();
                        //     // Mapear propiedades de partida
                        //     sala.setPartida(partida);
                        // }
                        return sala;
                    }
                })
                .create();

        Type listType = new TypeToken<List<Sala>>() {
        }.getType();
        return gson.fromJson(json, listType);
    }

    public static String convertirSocketsAJson(List<Socket> sockets) throws IOException {
        // Serializar los datos esenciales de cada socket (ejemplo: host y puerto)
        List<String> datosSockets = new ArrayList<>();
        for (Socket socket : sockets) {
            datosSockets.add(socket.getInetAddress().getHostAddress() + ":" + socket.getPort());
        }
        return objectMapper.writeValueAsString(datosSockets);
    }

    public static List<Socket> convertirJsonASockets(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        try {
            // Attempt to parse as a list of strings
            return mapper.readValue(json, new TypeReference<List<String>>() {
            })
                    .stream()
                    .map(address -> {
                        String[] parts = address.split(":");
                        try {
                            return new Socket(parts[0], Integer.parseInt(parts[1]));
                        } catch (IOException e) {
                            System.err.println("Could not create socket for " + address);
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Error parsing sockets: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // Método para convertir la lista de jugadores a formato JSON
    public static String convertirJugadoresAJson(List<Jugador> jugadores) {
        Gson gson = new Gson();
        return gson.toJson(jugadores);
    }

// Método para convertir un JSON en una lista de jugadores
    public static List<Jugador> convertirJsonAJugadores(String json) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Jugador.class, new JsonDeserializer<Jugador>() {
                    @Override
                    public Jugador deserialize(JsonElement json, Type typeOfT,
                            JsonDeserializationContext context) throws JsonParseException {
                        JsonObject jsonObject = json.getAsJsonObject();
                        // Add specific field mapping if needed
                        Jugador jugador = new Jugador();
                        // Manually set fields from jsonObject
                        if (jsonObject.has("nombre")) {
                            jugador.setNombre(jsonObject.get("nombre").getAsString());
                        }
                        // Add more field mappings as necessary
                        return jugador;
                    }
                })
                .create();

        try {
            Type listType = new TypeToken<List<Jugador>>() {
            }.getType();
            List<Jugador> jugadores = gson.fromJson(json, listType);
            return jugadores != null ? jugadores : new ArrayList<>();
        } catch (JsonSyntaxException e) {
            System.err.println("Error parsing JSON: " + e.getMessage());
            System.err.println("JSON content: " + json);
            return new ArrayList<>();
        }
    }
}
