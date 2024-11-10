/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTOs;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author INEGI
 */
public class PozoDTO {

    private List<FichaDTO> fichasPozo;

    /**
     * Constructor por defecto de la clase Pozo. Inicializa el pozo con una
     * lista vacía de fichas.
     */
    public PozoDTO() {
        fichasPozo = new ArrayList<>();
    }

    /**
     * mucho texto pero este método es para inicializar las fichas del dominó en
     * el pozo. Este método limpia cualquier ficha existente en el pozo y lo
     * rellena con todas las combinaciones posibles de fichas de dominó estándar
     * (con valores entre 0 y 6 en ambos lados de la ficha).
     */
    public void inicializarFichas() {
        fichasPozo.clear(); // Limpia las fichas existentes
        for (int i = 0; i <= 6; i++) { // Cambia según las reglas de tu dominó
            for (int j = i; j <= 6; j++) {
                fichasPozo.add(new FichaDTO(i, j)); // Asegúrate de que Ficha tenga el constructor correcto
            }
        }
    }

    /**
     * Constructor sobrecargado de la clase Pozo. Permite crear un pozo con una
     * lista de fichas ya definida.
     *
     * @param fichasPozo La lista de fichas con la que se inicializará el pozo.
     */
    public PozoDTO(List<FichaDTO> fichasPozo) {
        this.fichasPozo = fichasPozo;
    }

    /**
     * Método getter para obtener la lista de fichas del pozo.
     *
     * @return La lista de fichas actualmente en el pozo.
     */
    public List<FichaDTO> getFichasPozo() {
        return fichasPozo;
    }

    /**
     * Método setter para modificar la lista de fichas del pozo.
     *
     * @param fichasPozo La nueva lista de fichas que se establecerá en el pozo.
     */
    public void setFichasPozo(List<FichaDTO> fichasPozo) {
        this.fichasPozo = fichasPozo;
    }

    /**
     * Método sobreescrito para generar una representación en formato String del
     * pozo, que incluye la lista de fichas que contiene.
     *
     * @return Una cadena con el formato "Pozo{fichasPozo=lista_de_fichas}".
     */
    @Override
    public String toString() {
        return "Pozo{" + "fichasPozo=" + fichasPozo + '}';
    }

}
