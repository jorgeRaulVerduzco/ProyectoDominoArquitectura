/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Singleton;


import Dominio.Ficha;
import Dominio.Tablero;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author INEGI
 */
public class TableroSingleton {
    private List<Ficha> fichasTablero;
    private static Tablero instancia;
      private TableroSingleton() {
        fichasTablero = new ArrayList<>();
    }

    // Método público estático que devuelve la única instancia de la clase
    public static Tablero getInstancia() {
        if (instancia == null) {
            instancia = new Tablero(); // Crea la instancia si no existe
        }
        return instancia;
    }

    public List<Ficha> getFichasTablero() {
        return fichasTablero;
    }

    public void setFichasTablero(List<Ficha> fichasTablero) {
        this.fichasTablero = fichasTablero;
    }

    @Override
    public String toString() {
        return "Pozo{" + "fichasTablero=" + fichasTablero + '}';
    }
}
