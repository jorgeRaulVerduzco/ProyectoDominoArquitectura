/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;

import Dominio.Ficha;
import Dominio.Jugador;
import Dominio.Partida;
import Dominio.Tablero;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author INEGI
 */
public class ServicioControlJuego {

    private int jugadorActual = 0;
    private ServicioPartida servicioPartida = new ServicioPartida();
    private ServicioJugador servicioJugador = new ServicioJugador();
    private ServicioTablero servicioTablero = new ServicioTablero();
    private ServicioPozo servicioPozo = new ServicioPozo();
    private ServicioFicha servicioFicha = new ServicioFicha();

    public void jugarTurno(Partida partida, Jugador jugador) {
        if (partida.getEstado().equals("jugando")) {
            if (!jugador.getEstado().equals("activo")) {
                System.out.println(jugador.getNombre() + " no puede jugar porque está inactivo.");
                return;
            }

            Ficha fichaJugada = obtenerFichaJugable(jugador, partida);
            if (fichaJugada != null) {
                String lado = determinarLado(fichaJugada, partida.getTablero());
                servicioTablero.agregarFichaAlTablero(partida.getTablero(), fichaJugada, lado);
                jugador.getFichasJugador().remove(fichaJugada);
                System.out.println(jugador.getNombre() + " ha jugado la ficha " + fichaJugada);
            } else {
                manejarTomaDelPozo(jugador, partida);
            }

            if (partida.getTablero().getFichasTablero().size() == 0) {
                // Jugador que jugó la última ficha gana
                servicioPartida.terminarPartida(partida);
                System.out.println(jugador.getNombre() + " ha ganado el juego.");
            }
        } else {
            System.out.println("La partida no está en estado 'jugando'.");
        }
    }

    private Ficha obtenerFichaJugable(Jugador jugador, Partida partida) {
        for (Ficha ficha : jugador.getFichasJugador()) {
            if (servicioJugador.puedeColocarFicha(jugador, ficha, partida.getTablero())) {
                return ficha; // Devuelve la primera ficha jugable
            }
        }
        return null; // Ninguna ficha jugable
    }

    private void manejarTomaDelPozo(Jugador jugador, Partida partida) {
        List<Ficha> fichasPozo = partida.getPozo().getFichasPozo();
        if (fichasPozo.isEmpty()) {
            System.out.println("El pozo está vacío. No se puede tomar más fichas.");
            return;
        }

        System.out.println("Fichas en el pozo:");
        for (int i = 0; i < fichasPozo.size(); i++) {
            System.out.println(i + ": " + fichasPozo.get(i));
        }

        int indiceElegido = obtenerIndiceFichaDelJugador(jugador);
        if (indiceElegido < 0 || indiceElegido >= fichasPozo.size()) {
            System.out.println("Índice de ficha no válido.");
            return;
        }

        // Tomar la ficha elegida
        Ficha fichaTomada = servicioPozo.tomarFichaDelPozo(partida.getPozo(), indiceElegido);
        jugador.getFichasJugador().add(fichaTomada);
        System.out.println(jugador.getNombre() + " ha tomado una ficha del pozo: " + fichaTomada);
    }

    int obtenerIndiceFichaDelJugador(Jugador jugador) {
        Scanner scanner = new Scanner(System.in);
        int indice = -1;

        System.out.println(jugador.getNombre() + ", tus fichas son:");
        for (int i = 0; i < jugador.getFichasJugador().size(); i++) {
            System.out.println(i + ": " + jugador.getFichasJugador().get(i));
        }

        while (indice < 0 || indice >= jugador.getFichasJugador().size()) {
            System.out.print("Elige el índice de la ficha que deseas jugar (o -1 para cancelar): ");
            indice = scanner.nextInt();

            // Verifica si el índice es válido
            if (indice < -1 || indice >= jugador.getFichasJugador().size()) {
                System.out.println("Índice inválido. Por favor, elige un índice válido.");
            } else if (indice == -1) {
                System.out.println("Has cancelado la selección de ficha.");
                return -1; // Permitir cancelar la selección
            }
        }

        return indice; // Retorna el índice de la ficha elegida
    }

    private String determinarLado(Ficha ficha, Tablero tablero) {
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

    public boolean puedeContinuarJuego(Partida partida) {
        return !partida.getPozo().getFichasPozo().isEmpty() || jugadoresPuedenJugar(partida.getJugadores(), partida.getTablero());
    }

    private boolean jugadoresPuedenJugar(List<Jugador> jugadores, Tablero tablero) {
        for (Jugador jugador : jugadores) {
            for (Ficha ficha : jugador.getFichasJugador()) {
                if (servicioJugador.puedeColocarFicha(jugador, ficha, tablero)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void cambiarTurno(Partida partida) {
        // Asegúrate de que la lista de jugadores no esté vacía
        if (partida.getJugadores().isEmpty()) {
            throw new IllegalStateException("No hay jugadores en la partida.");
        }

        // Cambia al siguiente jugador
        jugadorActual = (jugadorActual + 1) % partida.getJugadores().size();

        // Opcional: imprimir el nuevo turno
        System.out.println("Es el turno de: " + partida.getJugadores().get(jugadorActual).getNombre());
    }

}
