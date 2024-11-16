/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package NegocioFachada;

import DTOaEntidad.ConversorDTOaEntidad;
import DTOaEntidad.ConversorEntidadADTO;
import DTOs.FichaDTO;
import DTOs.JugadorDTO;
import DTOs.PartidaDTO;
import DTOs.PozoDTO;
import DTOs.SalaDTO;
import DTOs.TableroDTO;
import Dominio.Ficha;
import Dominio.Jugador;
import Dominio.Partida;
import Dominio.Pozo;
import Dominio.Sala;
import Dominio.Tablero;
import Negocio.ServicioControlJuego;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author INEGI
 */
public class FachadaServicioControlJuego {

    private ServicioControlJuego servicioControlJuego;
    private ConversorDTOaEntidad conversorAEntidad;
    private ConversorEntidadADTO conversorADTO;

    /**
     * Constructor de la clase FachadaServicioControlJuego. Inicializa los
     * objetos de servicio y conversores para convertir entre entidades y DTOs.
     */
    public FachadaServicioControlJuego() {
        this.servicioControlJuego = new ServicioControlJuego();
        this.conversorAEntidad = new ConversorDTOaEntidad();
        this.conversorADTO = new ConversorEntidadADTO();
    }

    /**
     * Crea una nueva sala de juego
     *
     * @param cantJugadores cantidad de jugadores para la sala
     * @param numeroFichas número de fichas para el juego
     * @return SalaDTO con la información de la sala creada
     */
    public SalaDTO crearSala(int cantJugadores, int numeroFichas) {
        Sala sala = new Sala();
        sala.setCantJugadores(cantJugadores);
        sala.setNumeroFichas(numeroFichas);
        sala.setEstado("ESPERANDO");
        sala.setJugador(new ArrayList<>());
        return conversorADTO.convertirSala(sala);
    }

    /**
     * Agrega un jugador a una sala de juego.
     *
     * @param salaDTO el DTO de la sala donde se agregará el jugador.
     * @param jugadorDTO el DTO del jugador que se agregará a la sala.
     * @return true si el jugador fue agregado exitosamente; false si no se pudo
     * agregar.
     */
    public boolean agregarJugador(SalaDTO salaDTO, JugadorDTO jugadorDTO) {
        Sala sala = conversorAEntidad.convertirSala(salaDTO);
        Jugador jugador = conversorAEntidad.convertirJugador(jugadorDTO);
        boolean resultado = servicioControlJuego.agregarJugador(sala, jugador);
        return resultado;
    }

    /**
     * Inicia una partida en una sala de juego.
     *
     * @param salaDTO el DTO de la sala donde se iniciará la partida.
     */
    public void iniciarPartida(SalaDTO salaDTO) {
        Sala sala = conversorAEntidad.convertirSala(salaDTO);
        servicioControlJuego.iniciarPartida(sala);
    }

    /**
     * Obtiene la lista de salas de juego disponibles.
     *
     * @return una lista de objetos SalaDTO con la información de las salas
     * disponibles.
     */
    public List<SalaDTO> getSalasDisponibles() {
        List<Sala> salas = servicioControlJuego.getSalasDisponibles();
        return conversorADTO.convertirListaSalas(salas);
    }

    public boolean abandonarSala(SalaDTO salaDTO, JugadorDTO jugadorDTO) {
        Sala sala = conversorAEntidad.convertirSala(salaDTO);
        Jugador jugador = conversorAEntidad.convertirJugador(jugadorDTO);
        boolean resultado = servicioControlJuego.abandonarSala(sala, jugador);
        return resultado;
    }

    /**
     * Determina el lado de una ficha en el tablero.
     *
     * @param fichaDTO el DTO de la ficha a evaluar.
     * @param tableroDTO el DTO del tablero donde se evalúa la ficha.
     * @return el lado de la ficha en el tablero (por ejemplo, "IZQUIERDA" o
     * "DERECHA").
     */
    public String determinarLado(FichaDTO fichaDTO, TableroDTO tableroDTO) {
        Ficha ficha = conversorAEntidad.convertirFicha(fichaDTO);
        Tablero tablero = conversorAEntidad.convertirTablero(tableroDTO);
        return servicioControlJuego.determinarLado(ficha, tablero);
    }

    /**
     * Obtiene el jugador actual en una partida.
     *
     * @param partidaDTO el DTO de la partida en curso.
     * @return el DTO del jugador actual en la partida.
     */
    public JugadorDTO obtenerJugadorActual(PartidaDTO partidaDTO) {
        Partida partida = conversorAEntidad.convertirPartida(partidaDTO);
        Jugador jugador = servicioControlJuego.obtenerJugadorActual(partida);
        return conversorADTO.convertirJugador(jugador);
    }

    /**
     * Verifica el ganador de una partida.
     *
     * @param partidaDTO el DTO de la partida que se está evaluando.
     */
    public void verificarGanador(PartidaDTO partidaDTO) {
        Partida partida = conversorAEntidad.convertirPartida(partidaDTO);
        servicioControlJuego.verificarGanador(partida);
        // Actualizar el DTO con el nuevo estado después de verificar ganador
    }

    /**
     * Establece el orden de turnos de los jugadores en una partida.
     *
     * @param jugadorInicialDTO el DTO del jugador inicial.
     * @param jugadoresDTO una lista de DTOs de los jugadores.
     */
    public void establecerOrdenDeTurnos(JugadorDTO jugadorInicialDTO, List<JugadorDTO> jugadoresDTO) {
        Jugador jugadorInicial = conversorAEntidad.convertirJugador(jugadorInicialDTO);
        List<Jugador> jugadores = conversorAEntidad.convertirListaJugadores(jugadoresDTO);
        servicioControlJuego.establecerOrdenDeTurnos(jugadorInicial, jugadores);
    }

    /**
     * Obtiene el orden de turnos de los jugadores en la partida.
     *
     * @return una lista de DTOs de los jugadores en orden de turnos.
     */
    public List<JugadorDTO> getOrdenDeTurnos() {
        List<Jugador> jugadores = servicioControlJuego.getOrdenDeTurnos();
        return conversorADTO.convertirListaJugadores(jugadores);
    }

    /**
     * Determina el jugador inicial para el juego.
     *
     * @param jugadoresDTO una lista de jugadores en formato DTO.
     * @param pozoDTO el DTO del pozo de fichas.
     * @return el DTO del jugador inicial.
     */
    public JugadorDTO determinarJugadorInicial(List<JugadorDTO> jugadoresDTO, PozoDTO pozoDTO) {
        List<Jugador> jugadores = conversorAEntidad.convertirListaJugadores(jugadoresDTO);
        Pozo pozo = conversorAEntidad.convertirPozo(pozoDTO);
        Jugador jugadorInicial = servicioControlJuego.determinarJugadorInicial(jugadores, pozo);
        return conversorADTO.convertirJugador(jugadorInicial);
    }

    /**
     * Termina una partida en curso.
     *
     * @param partidaDTO el DTO de la partida a finalizar.
     */
    public void terminarPartida(PartidaDTO partidaDTO) {
        Partida partida = conversorAEntidad.convertirPartida(partidaDTO);
        servicioControlJuego.terminarPartida(partida);
    }

    /**
     * Inicia una partida.
     *
     * @param partidaDTO el DTO de la partida a iniciar.
     */
    public void iniciarJuego(PartidaDTO partidaDTO) {
        Partida partida = conversorAEntidad.convertirPartida(partidaDTO);
        servicioControlJuego.iniciarJuego(partida);
    }

    /**
     * Realiza un turno en una partida.
     *
     * @param partidaDTO el DTO de la partida donde se realiza el turno.
     */
    public void realizarTurno(PartidaDTO partidaDTO) {
        Partida partida = conversorAEntidad.convertirPartida(partidaDTO);
        servicioControlJuego.realizarTurno(partida);
    }

    /**
     * Calcula los puntajes de los jugadores en una partida.
     *
     * @param partidaDTO el DTO de la partida para la cual se calcularán los
     * puntajes.
     */
    public void calcularPuntajes(PartidaDTO partidaDTO) {
        Partida partida = conversorAEntidad.convertirPartida(partidaDTO);
        servicioControlJuego.calcularPuntajes(partida);
    }

    /**
     * Obtiene el estado actual de una partida
     *
     * @param partidaDTO la partida de la cual obtener el estado
     * @return PartidaDTO actualizado con el estado actual
     */
    public PartidaDTO obtenerEstadoPartida(PartidaDTO partidaDTO) {
        Partida partida = conversorAEntidad.convertirPartida(partidaDTO);
        return conversorADTO.convertirPartida(partida);
    }

    /**
     * Actualiza el estado de una sala
     *
     * @param salaDTO la sala a actualizar
     * @return SalaDTO actualizado con el estado actual
     */
    public SalaDTO actualizarEstadoSala(SalaDTO salaDTO) {
        Sala sala = conversorAEntidad.convertirSala(salaDTO);
        return conversorADTO.convertirSala(sala);
    }
}
