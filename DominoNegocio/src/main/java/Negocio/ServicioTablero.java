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

    // Método para agregar fichas al tablero en el lado izquierdo o derecho
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
            throw new IllegalArgumentException("Lado inválido. Debe ser 'izquierdo' o 'derecho'.");
        }
    }

    // Método para mover una ficha de una posición a otra
    public void moverFicha(Tablero tablero, int indexOrigen, int indexDestino) {
        if (indexOrigen < 0 || indexOrigen >= tablero.getFichasTablero().size()
                || indexDestino < 0 || indexDestino >= tablero.getFichasTablero().size()) {
            throw new IndexOutOfBoundsException("Índice fuera de los límites del tablero.");
        }

        Ficha fichaAEncontrar = tablero.getFichasTablero().get(indexOrigen);
        tablero.getFichasTablero().remove(indexOrigen);
        tablero.getFichasTablero().add(indexDestino, fichaAEncontrar);
    }

    // Método para obtener el extremo izquierdo del tablero
    public Ficha obtenerExtremoIzquierdo(Tablero tablero) {
        return !tablero.getFichasTablero().isEmpty() ? tablero.getFichasTablero().get(0) : null;
    }

    // Método para obtener el extremo derecho del tablero
    public Ficha obtenerExtremoDerecho(Tablero tablero) {
        return !tablero.getFichasTablero().isEmpty() ? tablero.getFichasTablero().get(tablero.getFichasTablero().size() - 1) : null;
    }

    // Verificación para agregar ficha al lado izquierdo
    private boolean puedeAgregarAlIzquierdo(Tablero tablero, Ficha ficha) {
        return !tablero.getFichasTablero().isEmpty()
                && (ficha.getEspacio2() == tablero.getFichasTablero().get(0).getEspacio1());
    }

    private boolean puedeAgregarAlDerecho(Tablero tablero, Ficha ficha) {
        if (tablero.getFichasTablero().isEmpty()) {
            System.out.println("El tablero está vacío. Agregando la primera ficha.");
            return true;
        }
        Ficha fichaDerecha = obtenerExtremoDerecho(tablero);
        System.out.println("Ficha a agregar: " + ficha);
        System.out.println("Ficha en el extremo derecho: " + fichaDerecha);
        boolean resultado = (ficha.getEspacio1() == fichaDerecha.getEspacio2() || ficha.getEspacio2() == fichaDerecha.getEspacio2());
        System.out.println("Puede agregar al derecho: " + resultado);
        return resultado;
    }
}
