/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server;

import Dominio.Jugador;
import Dominio.Sala;
import Dominio.Tablero;
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
    private static final Gson gson = new Gson();

    // Conversores para las salas
    public static String convertirSalasAJson(List<Sala> salas) {
        return gson.toJson(salas);
    }

    public static List<Sala> convertirJsonASalas(String json) {
        Type tipoListaSalas = new TypeToken<List<Sala>>(){}.getType();
        return gson.fromJson(json, tipoListaSalas);
    }

    // Conversores para los tableros
    public static String convertirTablerosAJson(List<Tablero> tableros) {
        return gson.toJson(tableros);
    }

    public static List<Tablero> convertirJsonATableros(String json) {
        Type tipoListaTableros = new TypeToken<List<Tablero>>(){}.getType();
        return gson.fromJson(json, tipoListaTableros);
    }

    // Conversores para los sockets
    public static String convertirSocketsAJson(List<Socket> sockets) throws IOException {
        List<String> datosSockets = new ArrayList<>();
        for (Socket socket : sockets) {
            datosSockets.add(socket.getInetAddress().getHostAddress() + ":" + socket.getPort());
        }
        return objectMapper.writeValueAsString(datosSockets);
    }

    public static List<Socket> convertirJsonASockets(String json) throws IOException {
        try {
            return objectMapper.readValue(json, new TypeReference<List<String>>() {
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

    // Conversores para los jugadores
    public static String convertirJugadoresAJson(List<Jugador> jugadores) {
        return gson.toJson(jugadores);
    }

    public static List<Jugador> convertirJsonAJugadores(String json) {
        try {
            Type listType = new TypeToken<List<Jugador>>(){}.getType();
            return gson.fromJson(json, listType);
        } catch (JsonSyntaxException e) {
            System.err.println("Error parsing JSON: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
