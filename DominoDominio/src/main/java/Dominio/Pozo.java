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
