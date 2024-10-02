/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;

import Dominio.Avatar;
import Dominio.Ficha;
import Dominio.Jugador;
import Dominio.Pozo;
import Dominio.Tablero;
import java.util.ArrayList;

/**
 *
 * @author INEGI
 */
public class ServicioJugador {

    public void abandonarPartida(Jugador jugador, Pozo pozo) {
        jugador.setEstado("inactivo");
        pozo.getFichasPozo().addAll(jugador.getFichasJugador());
        jugador.setFichasJugador(new ArrayList<>());
        System.out.println(jugador.getNombre() + " ha abandonado la partida.");
    }

    public boolean puedeColocarFicha(Jugador jugador, Ficha ficha, Tablero tablero) {
        Ficha extremoIzquierdo = tablero.getFichasTablero().isEmpty() ? null : tablero.getFichasTablero().get(0);
        Ficha extremoDerecho = tablero.getFichasTablero().isEmpty() ? null
                : tablero.getFichasTablero().get(tablero.getFichasTablero().size() - 1);

        return (extremoIzquierdo != null && (ficha.getEspacio1() == extremoIzquierdo.getEspacio1() || ficha.getEspacio2() == extremoIzquierdo.getEspacio1()))
                || (extremoDerecho != null && (ficha.getEspacio1() == extremoDerecho.getEspacio2() || ficha.getEspacio2() == extremoDerecho.getEspacio2()));
    }

    public void configurarAvatar(Jugador jugador, Avatar avatar) {
        jugador.setAvatar(avatar);
        System.out.println(jugador.getNombre() + " ha elegido el avatar: " + avatar);
    }

    public boolean verificarEstadoJugador(Jugador jugador) {
        return jugador.getEstado().equals("activo");
    }
}
