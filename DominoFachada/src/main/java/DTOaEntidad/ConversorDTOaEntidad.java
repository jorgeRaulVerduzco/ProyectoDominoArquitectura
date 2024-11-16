/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTOaEntidad;

import DTOs.*;
import Dominio.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author INEGI
 */
public class ConversorDTOaEntidad {

    public ConversorDTOaEntidad() {
    }

    /**
     * Convierte un objeto AvatarDTO en un objeto Avatar.
     *
     * @param avatarDTO el objeto AvatarDTO a convertir.
     * @return el objeto Avatar convertido, o null si el DTO es null.
     */
    public Avatar convertirAvatar(AvatarDTO avatarDTO) {
        if (avatarDTO == null) {
            return null;
        }
        Avatar avatar = new Avatar();
        avatar.setUrlAvatar(avatarDTO.getUrlAvatar());
        return avatar;
    }

    /**
     * Convierte un objeto FichaDTO en un objeto Ficha.
     *
     * @param fichaDTO el objeto FichaDTO a convertir.
     * @return el objeto Ficha convertido, o null si el DTO es null.
     */
    public Ficha convertirFicha(FichaDTO fichaDTO) {
        if (fichaDTO == null) {
            return null;
        }
        Ficha ficha = new Ficha();
        ficha.setEspacio1(fichaDTO.getEspacio1());
        ficha.setEspacio2(fichaDTO.getEspacio2());
        ficha.setColocada(fichaDTO.isColocada());
        ficha.setOrientacion(fichaDTO.getOrientacion());
        return ficha;
    }

    /**
     * Convierte una lista de objetos FichaDTO en una lista de objetos Ficha.
     *
     * @param fichasDTO la lista de objetos FichaDTO a convertir.
     * @return una lista de objetos Ficha, o null si la lista de DTO es null.
     */
    public List<Ficha> convertirListaFichas(List<FichaDTO> fichasDTO) {
        if (fichasDTO == null) {
            return null;
        }
        List<Ficha> fichas = new ArrayList<>();
        for (FichaDTO fichaDTO : fichasDTO) {
            fichas.add(convertirFicha(fichaDTO));
        }
        return fichas;
    }

    /**
     * Convierte un objeto JugadorDTO en un objeto Jugador.
     *
     * @param jugadorDTO el objeto JugadorDTO a convertir.
     * @return el objeto Jugador convertido, o null si el DTO es null.
     */
    public Jugador convertirJugador(JugadorDTO jugadorDTO) {
        if (jugadorDTO == null) {
            return null;
        }
        Jugador jugador = new Jugador();
        jugador.setNombre(jugadorDTO.getNombre());
        jugador.setAvatar(convertirAvatar(jugadorDTO.getAvatar()));
        jugador.setEstado(jugadorDTO.getEstado());
        jugador.setPuntuacion(jugadorDTO.getPuntuacion());
        jugador.setFichasJugador(convertirListaFichas(jugadorDTO.getFichasJugador()));
        return jugador;
    }

    /**
     * Convierte una lista de objetos JugadorDTO en una lista de objetos
     * Jugador.
     *
     * @param jugadoresDTO la lista de objetos JugadorDTO a convertir.
     * @return una lista de objetos Jugador, o null si la lista de DTO es null.
     */
    public List<Jugador> convertirListaJugadores(List<JugadorDTO> jugadoresDTO) {
        if (jugadoresDTO == null) {
            return null;
        }
        List<Jugador> jugadores = new ArrayList<>();
        for (JugadorDTO jugadorDTO : jugadoresDTO) {
            jugadores.add(convertirJugador(jugadorDTO));
        }
        return jugadores;
    }

    /**
     * Convierte un objeto TableroDTO en un objeto Tablero.
     *
     * @param tableroDTO el objeto TableroDTO a convertir.
     * @return el objeto Tablero convertido, o null si el DTO es null.
     */
    public Tablero convertirTablero(TableroDTO tableroDTO) {
        if (tableroDTO == null) {
            return null;
        }
        Tablero tablero = new Tablero();
        tablero.setFichasTablero(convertirListaFichas(tableroDTO.getFichasTablero()));
        return tablero;
    }

    /**
     * Convierte un objeto PozoDTO en un objeto Pozo.
     *
     * @param pozoDTO el objeto PozoDTO a convertir.
     * @return el objeto Pozo convertido, o null si el DTO es null.
     */
    public Pozo convertirPozo(PozoDTO pozoDTO) {
        if (pozoDTO == null) {
            return null;
        }
        Pozo pozo = new Pozo();
        pozo.setFichasPozo(convertirListaFichas(pozoDTO.getFichasPozo()));
        return pozo;
    }

    /**
     * Convierte un objeto PartidaDTO en un objeto Partida.
     *
     * @param partidaDTO el objeto PartidaDTO a convertir.
     * @return el objeto Partida convertido, o null si el DTO es null.
     */
    public Partida convertirPartida(PartidaDTO partidaDTO) {
        if (partidaDTO == null) {
            return null;
        }
        Partida partida = new Partida();
        partida.setCantJugadores(partidaDTO.getCantJugadores());
        partida.setCantFichas(partidaDTO.getCantFichas());
        partida.setEstado(partidaDTO.getEstado());
        partida.setTablero(convertirTablero(partidaDTO.getTablero()));
        partida.setJugadores(convertirListaJugadores(partidaDTO.getJugadores()));
        partida.setFichas(convertirListaFichas(partidaDTO.getFichas()));
        partida.setPozo(convertirPozo(partidaDTO.getPozo()));
        return partida;
    }

    /**
     * Convierte un objeto SalaDTO en un objeto Sala.
     *
     * @param salaDTO el objeto SalaDTO a convertir.
     * @return el objeto Sala convertido, o null si el DTO es null.
     */
    public Sala convertirSala(SalaDTO salaDTO) {
        if (salaDTO == null) {
            return null;
        }
        Sala sala = new Sala();
        sala.setCantJugadores(salaDTO.getCantJugadores());
        sala.setNumeroFichas(salaDTO.getNumeroFichas());
        sala.setJugador(convertirListaJugadores(salaDTO.getJugador()));
        sala.setEstado(salaDTO.getEstado());
        sala.setPartida(convertirPartida(salaDTO.getPartida()));
        return sala;
    }
}
