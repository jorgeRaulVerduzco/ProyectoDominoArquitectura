/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package EventoJuego;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author INEGI
 */
public class Evento implements Serializable {

    private String tipo;
    private Map<String, Object> datos;

    public Evento(String tipo) {
        this.tipo = tipo;
        this.datos = new HashMap<>();
    }

    public void agregarDato(String clave, Object valor) {
        datos.put(clave, valor);
    }

    public Object obtenerDato(String clave) {
        return datos.get(clave);
    }

    public String getTipo() {
        return tipo;
    }
}
