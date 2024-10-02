/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;

import Dominio.Ficha;
import Dominio.Jugador;
import Dominio.Tablero;

/**
 *
 * @author INEGI
 */
public class ServicioTablero {

    public void agregarFichaAlTablero(Tablero tablero, Ficha ficha, String lado) {
        if (lado.equals("izquierdo")) {
            if (!puedeAgregarAlIzquierdo(tablero, ficha)) {
                throw new IllegalArgumentException("No se puede agregar la ficha al lado izquierdo.");
            }
            tablero.getFichasTablero().add(0, ficha);
        } else if (lado.equals("derecho")) {
            if (!puedeAgregarAlDerecho(tablero, ficha)) {
                throw new IllegalArgumentException("No se puede agregar la ficha al lado derecho.");
            }
            tablero.getFichasTablero().add(ficha);
        } else {
            throw new IllegalArgumentException("Lado invalido. Debe ser 'izquierdo' o 'derecho'.");
        }
    }

    private boolean puedeAgregarAlIzquierdo(Tablero tablero, Ficha ficha) {
        return !tablero.getFichasTablero().isEmpty()
                && (ficha.getEspacio2() == tablero.getFichasTablero().get(0).getEspacio1());
    }

    private boolean puedeAgregarAlDerecho(Tablero tablero, Ficha ficha) {
        return !tablero.getFichasTablero().isEmpty()
                && (ficha.getEspacio1() == tablero.getFichasTablero().get(tablero.getFichasTablero().size() - 1).getEspacio2());
    }

    public Ficha obtenerExtremoIzquierdo(Tablero tablero) {
        return tablero.getFichasTablero().isEmpty() ? null : tablero.getFichasTablero().get(0);
    }

    public Ficha obtenerExtremoDerecho(Tablero tablero) {
        return tablero.getFichasTablero().isEmpty() ? null : tablero.getFichasTablero().get(tablero.getFichasTablero().size() - 1);
    }

}
