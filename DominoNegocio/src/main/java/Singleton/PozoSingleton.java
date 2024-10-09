/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Singleton;

import Dominio.Ficha;
import Dominio.Pozo;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author INEGI
 */
public class PozoSingleton {

    private List<Ficha> fichasPozo;
    private static Pozo instancia;

    private PozoSingleton() {
        fichasPozo = new ArrayList<>();
    }

    // Método público estático que devuelve la única instancia de la clase
    public static Pozo getInstancia() {
        if (instancia == null) {
            instancia = new Pozo(); // Crea la instancia si no existe
        }
        return instancia;
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
