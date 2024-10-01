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

    public Tablero() {
        fichasTablero = new ArrayList<>();
    }

    public List<Ficha> getFichasTablero() {
        return fichasTablero;
    }

    public void setFichasTablero(List<Ficha> fichasTablero) {
        this.fichasTablero = fichasTablero;
    }

}
