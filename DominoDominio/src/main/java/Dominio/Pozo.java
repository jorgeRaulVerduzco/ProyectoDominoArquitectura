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
public class Pozo {

    private List<Ficha> fichasPozo;

    public Pozo() {
        fichasPozo = new ArrayList<>();
    }

    // Método para inicializar las fichas del dominó en el pozo
    public void inicializarFichas() {
        fichasPozo.clear(); // Limpia las fichas existentes
        for (int i = 0; i <= 6; i++) { // Cambia según las reglas de tu dominó
            for (int j = i; j <= 6; j++) {
                fichasPozo.add(new Ficha(i, j)); // Asegúrate de que Ficha tenga el constructor correcto
            }
        }
    }

    public Pozo(List<Ficha> fichasPozo) {
        this.fichasPozo = fichasPozo;
    }

    public List<Ficha> getFichasPozo() {
        return fichasPozo;
    }

    public void setFichasPozo(List<Ficha> fichasPozo) {
        this.fichasPozo = fichasPozo;
    }

    @Override
    public String toString() {
        return "Pozo{" + "fichasPozo=" + fichasPozo + '}';
    }

}
