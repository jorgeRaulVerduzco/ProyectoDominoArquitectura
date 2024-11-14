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

    private static final long serialVersionUID = 1L;  // Asegura la compatibilidad de serialización
    private String tipo;
    private Map<String, Object> datos;

    // Definición de tipos de eventos como constantes para evitar errores
    public static final String INICIO_JUEGO = "INICIO_JUEGO";
    public static final String MOVIMIENTO_FICHA = "MOVIMIENTO_FICHA";
    public static final String DESCONEXION = "DESCONEXION";
    // Agrega más tipos según sea necesario

    public Evento(String tipo) {
        this.tipo = tipo;
        this.datos = new HashMap<>();
    }

    /**
     * Agrega un dato al evento. Si la clave ya existe, se sobreescribe el
     * valor.
     *
     * @param clave La clave que identifica el dato.
     * @param valor El valor del dato.
     */
    public void agregarDato(String clave, Object valor) {
        if (clave == null || valor == null) {
            throw new IllegalArgumentException("La clave y el valor no pueden ser nulos.");
        }
        datos.put(clave, valor);
    }

    /**
     * Obtiene un dato asociado a una clave.
     *
     * @param clave La clave del dato a obtener.
     * @return El valor asociado a la clave.
     */
    public Object obtenerDato(String clave) {
        return datos.get(clave);
    }

    public String getTipo() {
        return tipo;
    }

    public Map<String, Object> getDatos() {
        return new HashMap<>(datos);  // Retorna una copia para evitar modificaciones externas
    }

    @Override
    public String toString() {
        return "Evento{"
                + "tipo='" + tipo + '\''
                + ", datos=" + datos
                + '}';
    }
}
