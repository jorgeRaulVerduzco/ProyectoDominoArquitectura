/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server;
import Dominio.Jugador;
import Dominio.Sala;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;


/**
 *
 * @author INEGI
 */
public class ConversorJSON {
      private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String convertirSalasAJson(List<Sala> salas) throws IOException {
        return objectMapper.writeValueAsString(salas);
    }

    public static List<Sala> convertirJsonASalas(String json) throws IOException {
        return objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, Sala.class));
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
    return objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, String.class));
}
    
    // Método para convertir la lista de jugadores a formato JSON
public static String convertirJugadoresAJson(List<Jugador> jugadores) {
    Gson gson = new Gson();
    return gson.toJson(jugadores);
}

// Método para convertir un JSON en una lista de jugadores
public static List<Jugador> convertirJsonAJugadores(String json) {
    Gson gson = new Gson();
    // Usamos TypeToken para deserializar la lista correctamente
   try {
    Type listType = new TypeToken<List<Jugador>>(){}.getType();
    List<Jugador> jugadores = gson.fromJson(json, listType);
       
    System.out.println(jugadores);
return jugadores;
   } 
   catch (JsonSyntaxException e) {
    System.out.println("Error en el formato JSON: " + e.getMessage());
    System.out.println("JSON recibido: " + json);
}
          return null;

}
}