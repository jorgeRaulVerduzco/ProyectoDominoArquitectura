/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dominio;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author INEGI
 */
public class Tablero {

    private List<Ficha> fichasTablero;

    /**
     * Constructor por defecto de la clase Tablero. Inicializa la lista de
     * fichas en el tablero como una lista vacía.
     */
    public Tablero() {
        fichasTablero = new ArrayList<>();
    }

    /**
     * Método getter para obtener la lista de fichas en el tablero.
     *
     * @return La lista de fichas que han sido colocadas en el tablero.
     */
    public List<Ficha> getFichasTablero() {
        return fichasTablero;
    }

    /**
     * Método setter para modificar la lista de fichas en el tablero. Permite
     * establecer una nueva lista de fichas en el tablero.
     *
     * @param fichasTablero La nueva lista de fichas que estarán en el tablero.
     */
    public void setFichasTablero(List<Ficha> fichasTablero) {
        this.fichasTablero = fichasTablero;
    }

}
