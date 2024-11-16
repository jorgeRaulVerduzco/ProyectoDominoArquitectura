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
public class ConversorEntidadADTO {

    public ConversorEntidadADTO() {
    }

    public AvatarDTO convertirAvatar(Avatar avatar) {
        if (avatar == null) {
            return null;
        }
        AvatarDTO avatarDTO = new AvatarDTO();
        avatarDTO.setUrlAvatar(avatar.getUrlAvatar());
        return avatarDTO;
    }

    public FichaDTO convertirFicha(Ficha ficha) {
        if (ficha == null) {
            return null;
        }
        FichaDTO fichaDTO = new FichaDTO();
        fichaDTO.setEspacio1(ficha.getEspacio1());
        fichaDTO.setEspacio2(ficha.getEspacio2());
        fichaDTO.setColocada(ficha.isColocada());
        fichaDTO.setOrientacion(ficha.getOrientacion());
        return fichaDTO;
    }

    public List<FichaDTO> convertirListaFichas(List<Ficha> fichas) {
        if (fichas == null) {
            return null;
        }
        List<FichaDTO> fichasDTO = new ArrayList<>();
        for (Ficha ficha : fichas) {
            fichasDTO.add(convertirFicha(ficha));
        }
        return fichasDTO;
    }

    public JugadorDTO convertirJugador(Jugador jugador) {
        if (jugador == null) {
            return null;
        }
        JugadorDTO jugadorDTO = new JugadorDTO();
        jugadorDTO.setNombre(jugador.getNombre());
        jugadorDTO.setAvatar(convertirAvatar(jugador.getAvatar()));
        jugadorDTO.setEstado(jugador.getEstado());
        jugadorDTO.setPuntuacion(jugador.getPuntuacion());
        jugadorDTO.setFichasJugador(convertirListaFichas(jugador.getFichasJugador()));
        return jugadorDTO;
    }

    public List<JugadorDTO> convertirListaJugadores(List<Jugador> jugadores) {
        if (jugadores == null) {
            return null;
        }
        List<JugadorDTO> jugadoresDTO = new ArrayList<>();
        for (Jugador jugador : jugadores) {
            jugadoresDTO.add(convertirJugador(jugador));
        }
        return jugadoresDTO;
    }

    public TableroDTO convertirTablero(Tablero tablero) {
        if (tablero == null) {
            return null;
        }
        TableroDTO tableroDTO = new TableroDTO();
        tableroDTO.setFichasTablero(convertirListaFichas(tablero.getFichasTablero()));
        return tableroDTO;
    }

    public PozoDTO convertirPozo(Pozo pozo) {
        if (pozo == null) {
            return null;
        }
        PozoDTO pozoDTO = new PozoDTO();
        pozoDTO.setFichasPozo(convertirListaFichas(pozo.getFichasPozo()));
        return pozoDTO;
    }

    public PartidaDTO convertirPartida(Partida partida) {
        if (partida == null) {
            return null;
        }
        PartidaDTO partidaDTO = new PartidaDTO();
        partidaDTO.setCantJugadores(partida.getCantJugadores());
        partidaDTO.setCantFichas(partida.getCantFichas());
        partidaDTO.setEstado(partida.getEstado());
        partidaDTO.setTablero(convertirTablero(partida.getTablero()));
        partidaDTO.setJugadores(convertirListaJugadores(partida.getJugadores()));
        partidaDTO.setFichas(convertirListaFichas(partida.getFichas()));
        partidaDTO.setPozo(convertirPozo(partida.getPozo()));
        return partidaDTO;
    }

    public SalaDTO convertirSala(Sala sala) {
        if (sala == null) {
            return null;
        }
        SalaDTO salaDTO = new SalaDTO();
        salaDTO.setCantJugadores(sala.getCantJugadores());
        salaDTO.setNumeroFichas(sala.getNumeroFichas());
        salaDTO.setJugador(convertirListaJugadores(sala.getJugador()));
        salaDTO.setEstado(sala.getEstado());
        salaDTO.setPartida(convertirPartida(sala.getPartida()));
        return salaDTO;
    }

    public List<SalaDTO> convertirListaSalas(List<Sala> salas) {
        if (salas == null) {
            return null;
        }
        List<SalaDTO> salasDTO = new ArrayList<>();
        for (Sala sala : salas) {
            salasDTO.add(convertirSala(sala));
        }
        return salasDTO;
    }
}
