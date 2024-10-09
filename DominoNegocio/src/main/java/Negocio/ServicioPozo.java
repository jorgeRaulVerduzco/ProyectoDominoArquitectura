/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;

import Dominio.Ficha;
import Dominio.Pozo;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author INEGI
 */
public class ServicioPozo {

    private Pozo pozo;
    private Random random = new Random();

    public ServicioPozo() {
        pozo = new Pozo();
    }

    public Ficha tomarFichaDelPozo(Pozo pozo, int indice) {
        if (pozo.getFichasPozo().isEmpty()) {
            return null;
        }

        if (indice < 0 || indice >= pozo.getFichasPozo().size()) {
            return null;
        }

        return pozo.getFichasPozo().remove(indice);
    }

    public void guardarFichasPozo() {
        List<Ficha> fichas = new ArrayList<>();
        for (int i = 0; i < 28; i++) { 
            int ladoIzquierdo;
            int ladoDerecho;

            if (fichas.isEmpty()) {
                ladoIzquierdo = random.nextInt(7); 
                ladoDerecho = random.nextInt(7);   
            } else {
                Ficha ultimaFicha = fichas.get(fichas.size() - 1);  
                ladoIzquierdo = ultimaFicha.getEspacio2();  
                ladoDerecho = random.nextInt(7);  
            }

          
            if (ladoIzquierdo >= 0 && ladoIzquierdo <= 6 && ladoDerecho >= 0 && ladoDerecho <= 6) {
                Ficha nuevaFicha = new Ficha(ladoIzquierdo, ladoDerecho);
                fichas.add(nuevaFicha);  // Agregar ficha generada
            } else {
                i--;  
            }
        }
        pozo.setFichasPozo(fichas);  // Asigna las fichas generadas al pozo
    }

}
