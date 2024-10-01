/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Dominio;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author INEGI
 */
public class Partida {

    private int cantJugadores;
    private int cantFichas;
    private String estado;
    private Tablero tablero;
    private List<Jugador> jugadores;
    private List<Ficha> fichas;
    private Pozo pozo;

    public Partida() {
        jugadores = new ArrayList<>();
        fichas = new ArrayList<>();
    }

    public Partida(int cantJugadores, int cantFichas, String estado, Tablero tablero, List<Jugador> jugadores, List<Ficha> fichas, Pozo pozo) {
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

    public Tablero getTablero() {
        return tablero;
    }

    public void setTablero(Tablero tablero) {
        this.tablero = tablero;
    }

    public List<Jugador> getJugadores() {
        return jugadores;
    }

    public void setJugadores(List<Jugador> jugadores) {
        this.jugadores = jugadores;
    }

    public List<Ficha> getFichas() {
        return fichas;
    }

    public void setFichas(List<Ficha> fichas) {
        this.fichas = fichas;
    }

    public Pozo getPozo() {
        return pozo;
    }

    public void setPozo(Pozo pozo) {
        this.pozo = pozo;
    }

    @Override
    public String toString() {
        return "Partida{" + "cantJugadores=" + cantJugadores + ", cantFichas=" + cantFichas + ", estado=" + estado + ", tablero=" + tablero + ", jugadores=" + jugadores + ", fichas=" + fichas + ", pozo=" + pozo + '}';
    }

}
