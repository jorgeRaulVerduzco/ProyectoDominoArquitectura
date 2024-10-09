/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTOs;


import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author INEGI
 */
public class PartidaDTO {
    private int cantJugadores;
    private int cantFichas;
    private String estado;
    private TableroDTO tablero;
    private List<JugadorDTO> jugadores;
    private List<FichaDTO> fichas;
    private PozoDTO pozo;

    public PartidaDTO() {
        jugadores = new ArrayList<>();
        fichas = new ArrayList<>();
    }

    public PartidaDTO(int cantJugadores, int cantFichas, String estado, TableroDTO tablero, List<JugadorDTO> jugadores, List<FichaDTO> fichas, PozoDTO pozo) {
        this.cantJugadores = cantJugadores;
        this.cantFichas = cantFichas;
        this.estado = estado;
        this.tablero = tablero;
        this.jugadores = jugadores;
        this.fichas = fichas;
        this.pozo = pozo;
    }

    public int getCantJugadores() {
        return cantJugadores;
    }

    public void setCantJugadores(int cantJugadores) {
        this.cantJugadores = cantJugadores;
    }

    public int getCantFichas() {
        return cantFichas;
    }

    public void setCantFichas(int cantFichas) {
        this.cantFichas = cantFichas;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public TableroDTO getTablero() {
        return tablero;
    }

    public void setTablero(TableroDTO tablero) {
        this.tablero = tablero;
    }

    public List<JugadorDTO> getJugadores() {
        return jugadores;
    }

    public void setJugadores(List<JugadorDTO> jugadores) {
        this.jugadores = jugadores;
    }

    public List<FichaDTO> getFichas() {
        return fichas;
    }

    public void setFichas(List<FichaDTO> fichas) {
        this.fichas = fichas;
    }

    public PozoDTO getPozo() {
        return pozo;
    }

    public void setPozo(PozoDTO pozo) {
        this.pozo = pozo;
    }

    @Override
    public String toString() {
        return "Partida{" + "cantJugadores=" + cantJugadores + ", cantFichas=" + cantFichas + ", estado=" + estado + ", tablero=" + tablero + ", jugadores=" + jugadores + ", fichas=" + fichas + ", pozo=" + pozo + '}';
    }

}
