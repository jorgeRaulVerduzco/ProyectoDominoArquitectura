/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;

import Dominio.Ficha;
import Dominio.Jugador;
import Dominio.Partida;
import Dominio.Pozo;
import Dominio.Tablero;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author INEGI
 */
public class ServicioControlJuego {

    private List<Jugador> ordenDeTurnos; // Lista de jugadores que contiene el orden de turnos
    private int turnoActual;
    private int jugadorActual = 0;
    private ServicioPartida servicioPartida = new ServicioPartida();
    private ServicioJugador servicioJugador = new ServicioJugador();
    private ServicioTablero servicioTablero = new ServicioTablero();
    private ServicioPozo servicioPozo = new ServicioPozo();
    private ServicioFicha servicioFicha = new ServicioFicha();

  
    public String determinarLado(Ficha ficha, Tablero tablero) {
        if (tablero.getFichasTablero().isEmpty()) {
            return "izquierdo"; // Si el tablero está vacío, se agrega al lado izquierdo
        }

        Ficha extremoIzquierdo = servicioTablero.obtenerExtremoIzquierdo(tablero);
        Ficha extremoDerecho = servicioTablero.obtenerExtremoDerecho(tablero);

        if (ficha.getEspacio1() == extremoIzquierdo.getEspacio1() || ficha.getEspacio2() == extremoIzquierdo.getEspacio1()) {
            return "izquierdo";
        } else if (ficha.getEspacio1() == extremoDerecho.getEspacio2() || ficha.getEspacio2() == extremoDerecho.getEspacio2()) {
            return "derecho";
        }

        return "ninguno"; // No se puede determinar
    }

    public Jugador obtenerJugadorActual(Partida partida) {
        return partida.getJugadores().get(jugadorActual);
    }

    public void verificarGanador(Partida partida) {
        // Verifica si un jugador ha ganado
        for (Jugador jugador : partida.getJugadores()) {
            if (jugador.getFichasJugador().isEmpty()) {
                servicioPartida.terminarPartida(partida);
                System.out.println(jugador.getNombre() + " ha ganado el juego al jugar su última ficha.");
                return;
            }
        }

        // Verifica si el juego está bloqueado
        if (isJuegoBloqueado(partida)) {
            servicioPartida.terminarPartida(partida);
            System.out.println("El juego está bloqueado. Se procede a calcular los puntajes.");
            return;
        }

        // Si todos los jugadores acordaron terminar el juego
        if (partida.getEstado().equals("Terminado")) {
            servicioPartida.terminarPartida(partida);
            System.out.println("El juego ha terminado por acuerdo de los jugadores. Se procede a calcular los puntajes.");
        }
    }

    private boolean isJuegoBloqueado(Partida partida) {
        // Verifica si el pozo está vacío
        if (!partida.getPozo().getFichasPozo().isEmpty()) {
            return false;
        }

        // Verifica si ninguno de los jugadores puede jugar
        for (Jugador jugador : partida.getJugadores()) {
            for (Ficha ficha : jugador.getFichasJugador()) {
                if (servicioJugador.puedeColocarFicha(jugador, ficha, partida.getTablero())) {
                    return false; // Al menos un jugador puede jugar
                }
            }
        }
        return true; // Ningún jugador puede jugar
    }

    public void establecerOrdenDeTurnos(Jugador jugadorInicial, List<Jugador> jugadores) {
        ordenDeTurnos = new ArrayList<>(jugadores);

        // Mover al jugador inicial al inicio de la lista
        ordenDeTurnos.remove(jugadorInicial);
        Collections.shuffle(ordenDeTurnos);  // Ordenar al azar los jugadores restantes
        ordenDeTurnos.add(0, jugadorInicial);  // Colocar al jugador inicial al inicio
    }

    public List<Jugador> getOrdenDeTurnos() {
        return ordenDeTurnos;
    }

    public Jugador determinarJugadorInicial(List<Jugador> jugadores, Pozo pozo) {
        Ficha mulaMayor = null;
        Jugador jugadorInicial = null;

        for (Jugador jugador : jugadores) {
            for (Ficha ficha : jugador.getFichasJugador()) {
                if (ficha.esMula()) {
                    if (mulaMayor == null || ficha.getEspacio1() > mulaMayor.getEspacio2()) {
                        mulaMayor = ficha;
                        jugadorInicial = jugador;
                    }
                }
            }
        }

        // Si no se encontró mula, se asigna una ficha del pozo hasta que alguien tenga una mula
        while (mulaMayor == null) {
            for (Jugador jugador : jugadores) {
                Ficha nuevaFicha = pozo.getFichasPozo().remove(0);
                jugador.getFichasJugador().add(nuevaFicha);
                if (nuevaFicha.esMula()) {
                    mulaMayor = nuevaFicha;
                    jugadorInicial = jugador;
                    break;
                }
            }
        }
        return jugadorInicial;
    }

    public void terminarPartida(Partida partida) {
        partida.setEstado("finalizada");
        System.out.println("La partida ha finalizado.");
    }
}
