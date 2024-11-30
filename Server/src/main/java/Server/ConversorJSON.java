/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server;
import Dominio.Sala;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;

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
}
