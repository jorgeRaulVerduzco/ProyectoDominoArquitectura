/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;

import Dominio.Ficha;
import Dominio.Jugador;
import Dominio.Partida;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author INEGI
 */
public class ServicioPartida {

    public void iniciarPartida(Partida partida) {
        partida.setEstado("iniciada");
        repartirFichas(partida);
        Jugador jugadorInicial = determinarJugadorInicial(partida.getJugadores());
        partida.setEstado("jugando");
        System.out.println("La partida ha comenzado. El jugador " + jugadorInicial.getNombre() + " inicia.");
    }

    public boolean validarCantidadJugadores(List<Jugador> jugadores) {
        return jugadores.size() >= 2 && jugadores.size() <= 4;
    }

    public boolean validarCantidadFichas(int cantidadFichas) {
        return cantidadFichas >= 2 && cantidadFichas <= 7;
    }

    private void repartirFichas(Partida partida) {
        List<Ficha> fichas = partida.getFichas();
        List<Jugador> jugadores = partida.getJugadores();
        int cantFichasPorJugador = partida.getCantFichas();

        for (Jugador jugador : jugadores) {
            List<Ficha> fichasJugador = new ArrayList<>();
            for (int i = 0; i < cantFichasPorJugador; i++) {
                if (fichas.isEmpty()) {
                    break; // Evitar IndexOutOfBounds
                }
                int indexAleatorio = (int) (Math.random() * fichas.size());
                fichasJugador.add(fichas.remove(indexAleatorio));
            }
            jugador.setFichasJugador(fichasJugador);
        }

        partida.getPozo().setFichasPozo(fichas);
    }

    private Jugador determinarJugadorInicial(List<Jugador> jugadores) {
        Ficha mulaMayor = null;
        Jugador jugadorConMulaMayor = null;

        for (Jugador jugador : jugadores) {
            for (Ficha ficha : jugador.getFichasJugador()) {
                if (esMula(ficha) && (mulaMayor == null || ficha.getEspacio1() > mulaMayor.getEspacio1())) {
                    mulaMayor = ficha;
                    jugadorConMulaMayor = jugador;
                }
            }
        }

        if (jugadorConMulaMayor == null) {
            System.out.println("Ningún jugador tiene mula. Sacando fichas del pozo...");
            return jugadores.get((int) (Math.random() * jugadores.size())); // Elegir un jugador al azar
        }

        return jugadorConMulaMayor;
    }

    private boolean esMula(Ficha ficha) {
        return ficha.getEspacio1() == ficha.getEspacio2();
    }

    public void terminarPartida(Partida partida) {
        partida.setEstado("terminada");
        System.out.println("La partida ha terminado.");
        calcularPuntajes(partida);
    }

    public void calcularPuntajes(Partida partida) {
        // Calcular y mostrar puntajes
        for (Jugador jugador : partida.getJugadores()) {
            int puntaje = 0;
            for (Ficha ficha : jugador.getFichasJugador()) {
                puntaje += ficha.getEspacio1() + ficha.getEspacio2();
            }
            jugador.setPuntuacion(puntaje);
            System.out.println("Puntaje de " + jugador.getNombre() + ": " + puntaje);
        }

        // Determinar el orden de los jugadores según sus puntajes
        List<Jugador> jugadoresOrdenados = new ArrayList<>(partida.getJugadores());
        jugadoresOrdenados.sort(Comparator.comparing(Jugador::getPuntuacion));

        System.out.println("Orden de los jugadores:");
        for (Jugador jugador : jugadoresOrdenados) {
            System.out.println(jugador.getNombre() + " - Puntos: " + jugador.getPuntuacion());
        }
    }

}
