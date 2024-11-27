/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;

import Dominio.Ficha;
import Dominio.Jugador;
import Dominio.Partida;
import Dominio.Pozo;
import Dominio.Sala;
import Dominio.Tablero;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

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
    private List<Sala> salas;

    public ServicioControlJuego() {
        this.salas = new ArrayList<>();
        this.servicioPozo = new ServicioPozo();
    }

    public boolean agregarJugador(Sala sala, Jugador jugador) {
        if (sala.getJugador().size() < sala.getCantJugadores()) {
            sala.getJugador().add(jugador);
            jugador.setEstado("ACTIVO");

            if (sala.getJugador().size() == sala.getCantJugadores()) {
                iniciarPartida(sala);
            }
            return true;
        }
        return false;
    }
    
    /**
 * Procesa una lista de salas actualizando o realizando acciones según las reglas del sistema.
 *
 * @param salas Lista de salas a procesar.
 */
public void procesarSalas(List<Sala> salas) {
    if (salas == null || salas.isEmpty()) {
        System.out.println("ServicioControlJuego: No hay salas para procesar.");
        return;
    }

    System.out.println("ServicioControlJuego: Procesando salas recibidas...");

    for (Sala sala : salas) {
        // Ejemplo: Verificar si la sala ya existe en el sistema
        boolean salaExiste = this.salas.stream()
                .anyMatch(s -> s.getId().equals(sala.getId()));

        if (salaExiste) {
            System.out.println(" - Sala ID: " + sala.getId() + " ya existe. Actualizando su estado...");
            actualizarSalaExistente(sala);
        } else {
            System.out.println(" - Sala ID: " + sala.getId() + " es nueva. Agregándola al sistema...");
            agregarSala(sala);
        }
    }

    System.out.println("ServicioControlJuego: Salas procesadas. Total de salas en el sistema: " + this.salas.size());
}

/**
 * Actualiza una sala existente en el sistema.
 *
 * @param sala Sala con los nuevos datos a actualizar.
 */
private void actualizarSalaExistente(Sala sala) {
    for (int i = 0; i < salas.size(); i++) {
        if (salas.get(i).getId().equals(sala.getId())) {
            salas.set(i, sala); // Actualiza la sala en la lista
            System.out.println("ServicioControlJuego: Sala ID: " + sala.getId() + " actualizada.");
            break;
        }
    }
}

    
    

      public void agregarSala(Sala sala) {
        if (sala != null) {
            salas.add(sala);
            System.out.println("ServicioControlJuego: Sala agregada al sistema. Total de salas: " + salas.size());
        } else {
            System.err.println("ServicioControlJuego: Intento de agregar una sala nula.");
        }
        
        
    }

    public List<Sala> getSalasDisponibles() {
        return new ArrayList<>(salas); // Devuelve una copia de la lista
    }



    public void iniciarPartida(Sala sala) {
        Partida partida = new Partida();
        partida.setCantJugadores(sala.getCantJugadores());
        partida.setCantFichas(sala.getNumeroFichas());
        partida.setEstado("EN_CURSO");
        partida.setJugadores(sala.getJugador());
        partida.setTablero(new Tablero());

        servicioPozo.iniciarNuevoJuego(sala.getJugador());
        partida.setPozo(servicioPozo.getPozo());

        sala.setPartida(partida);
        sala.setEstado("EN_JUEGO");
    }

   
    public boolean abandonarSala(Sala sala, Jugador jugador) {
        if (sala.getJugador().contains(jugador)) {
            jugador.setEstado("INACTIVO");
            sala.getJugador().remove(jugador);

            // Si la sala queda vacía, la eliminamos
            if (sala.getJugador().isEmpty()) {
                salas.remove(sala);
            }
            return true;
        }
        return false;
    }

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

    /**
     * Obtiene el jugador actual del turno.
     *
     * @param partida
     * @return
     */
    public Jugador obtenerJugadorActual(Partida partida) {
        return partida.getJugadores().get(jugadorActual);
    }

    /**
     * Verifica si un jugador ha ganado el juego o si el juego está bloqueado.
     *
     * @param partida
     */
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

    /**
     * Verifica si el juego está bloqueado. Un juego está bloqueado si el pozo
     * está vacío y ningún jugador puede colocar una ficha.
     */
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

    /**
     * Establece el orden de turnos de los jugadores, moviendo al jugador
     * inicial al principio y barajando al resto de los jugadores al azar.
     *
     * @param jugadorInicial El jugador que comenzará el juego.
     * @param jugadores Lista de todos los jugadores en la partida.
     */
    public void establecerOrdenDeTurnos(Jugador jugadorInicial, List<Jugador> jugadores) {
        ordenDeTurnos = new ArrayList<>(jugadores);

        // Mover al jugador inicial al inicio de la lista
        ordenDeTurnos.remove(jugadorInicial);
        Collections.shuffle(ordenDeTurnos);  // Ordenar al azar los jugadores restantes
        ordenDeTurnos.add(0, jugadorInicial);  // Colocar al jugador inicial al inicio
    }

    /**
     * Devuelve el orden de turnos de los jugadores.
     *
     * @return Lista de jugadores en el orden en el que deben jugar.
     */
    public List<Jugador> getOrdenDeTurnos() {
        return ordenDeTurnos;
    }

    /**
     * Determina cuál jugador comenzará el juego en base a la mula más alta. Si
     * ningún jugador tiene una mula, se asignan fichas adicionales desde el
     * pozo hasta que se encuentre una mula.
     *
     * @param jugadores Lista de jugadores que participan en la partida.
     * @param pozo Pozo de fichas disponibles para repartir.
     * @return El jugador que tiene la mula más alta y comenzará la partida.
     */
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

    /**
     * Finaliza la partida cambiando su estado a "finalizada".
     *
     * @param partida La partida que debe finalizar.
     */
    public void terminarPartida(Partida partida) {
        partida.setEstado("finalizada");
        System.out.println("La partida ha finalizado.");
    }

    /**
     * Inicia el juego, repartiendo las fichas a los jugadores, determinando el
     * jugador inicial y estableciendo el orden de los turnos. Coloca la primera
     * ficha en el tablero.
     *
     * @param partida La partida que se va a iniciar.
     */
    public void iniciarJuego(Partida partida) {
        // Repartir fichas
        servicioTablero.repartirFichas(partida.getPozo(), partida.getJugadores());

        // Determinar jugador inicial
        Jugador jugadorInicial = determinarJugadorInicial(partida.getJugadores(), partida.getPozo());

        // Establecer orden de turnos
        establecerOrdenDeTurnos(jugadorInicial, partida.getJugadores());

        // Colocar la primera ficha (mula) en el tablero
        Ficha primeraFicha = encontrarMulaMayor(jugadorInicial);
        servicioTablero.agregarFichaAlTablero(partida.getTablero(), primeraFicha, "izquierdo");
        jugadorInicial.getFichasJugador().remove(primeraFicha);

        partida.setEstado("en curso");
    }

    /**
     * Busca y devuelve la mula más alta del jugador inicial.
     *
     * @param jugador El jugador inicial del que se va a encontrar la mula
     * mayor.
     * @return La ficha mula más alta del jugador.
     * @throws IllegalStateException Si el jugador no tiene mulas.
     */
    private Ficha encontrarMulaMayor(Jugador jugador) {
        return jugador.getFichasJugador().stream()
                .filter(Ficha::esMula)
                .max(Comparator.comparingInt(Ficha::getEspacio1))
                .orElseThrow(() -> new IllegalStateException("El jugador inicial no tiene mulas"));
    }

    /**
     * Realiza el turno actual del jugador. El jugador intenta colocar una ficha
     * en el tablero. Si no puede, toma fichas del pozo hasta que pueda jugar o
     * hasta que no queden más fichas.
     *
     * @param partida La partida en curso.
     */
    public void realizarTurno(Partida partida) {
        Jugador jugadorActual = obtenerJugadorActual(partida);
        Tablero tablero = partida.getTablero();
        Pozo pozo = partida.getPozo();

        boolean jugadaRealizada = false;

        for (Ficha ficha : new ArrayList<>(jugadorActual.getFichasJugador())) {
            String lado = determinarLado(ficha, tablero);
            if (!lado.equals("ninguno")) {
                servicioTablero.agregarFichaAlTablero(tablero, ficha, lado);
                jugadorActual.getFichasJugador().remove(ficha);
                jugadaRealizada = true;
                break;
            }
        }

        if (!jugadaRealizada) {
            while (!pozo.getFichasPozo().isEmpty() && !jugadaRealizada) {
                Ficha nuevaFicha = servicioPozo.tomarFichaDelPozo(pozo, 0);
                jugadorActual.getFichasJugador().add(nuevaFicha);
                String lado = determinarLado(nuevaFicha, tablero);
                if (!lado.equals("ninguno")) {
                    servicioTablero.agregarFichaAlTablero(tablero, nuevaFicha, lado);
                    jugadorActual.getFichasJugador().remove(nuevaFicha);
                    jugadaRealizada = true;
                }
            }
        }

        if (!jugadaRealizada) {
            System.out.println(jugadorActual.getNombre() + " no pudo jugar en este turno.");
        }

        verificarGanador(partida);
        pasarTurno();
    }

    /**
     * Avanza el turno al siguiente jugador en el orden de turnos.
     */
    private void pasarTurno() {
        jugadorActual = (jugadorActual + 1) % ordenDeTurnos.size();
    }

    /**
     * Calcula los puntajes de cada jugador sumando los valores de las fichas
     * que les quedan en la mano. Determina y muestra el jugador ganador con la
     * menor cantidad de puntos.
     *
     * @param partida La partida en la que se van a calcular los puntajes.
     */
    public void calcularPuntajes(Partida partida) {
        for (Jugador jugador : partida.getJugadores()) {
            int puntaje = jugador.getFichasJugador().stream()
                    .mapToInt(ficha -> ficha.getEspacio1() + ficha.getEspacio2())
                    .sum();
            jugador.setPuntuacion(puntaje);
        }

        Jugador ganador = partida.getJugadores().stream()
                .min(Comparator.comparingInt(Jugador::getPuntuacion))
                .orElseThrow(() -> new IllegalStateException("No se pudo determinar un ganador"));

        System.out.println("El ganador es " + ganador.getNombre() + " con " + ganador.getPuntuacion() + " puntos.");
    }

}
