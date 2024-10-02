/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;

import Dominio.Ficha;
import Dominio.Pozo;
import java.util.List;

/**
 *
 * @author INEGI
 */
public class ServicioPozo {

    public Ficha tomarFichaDelPozo(Pozo pozo, int indice) {
        if (pozo.getFichasPozo().isEmpty()) {
            return null;
        }

        if (indice < 0 || indice >= pozo.getFichasPozo().size()) {
            return null;
        }

        return pozo.getFichasPozo().remove(indice);
    }

    public void agregarFichasAlPozo(Pozo pozo, List<Ficha> fichas) {
        pozo.getFichasPozo().addAll(fichas);
    }

}
