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

    /**
     * Constructor por defecto de la clase Partida. Inicializa las listas de
     * jugadores y fichas vacías.
     */
    public Partida() {
        jugadores = new ArrayList<>();
        fichas = new ArrayList<>();
    }

    /**
     * Constructor sobrecargado de la clase Partida. Permite crear una partida
     * con todos sus atributos definidos.
     *
     * @param cantJugadores Cantidad de jugadores que participan en la partida.
     * @param cantFichas Cantidad de fichas que cada jugador recibe al inicio.
     * @param estado Estado actual de la partida.
     * @param tablero El tablero de juego donde se colocarán las fichas.
     * @param jugadores La lista de jugadores que participarán en la partida.
     * @param fichas Las fichas asignadas a la partida.
     * @param pozo El pozo de fichas no distribuidas.
     */
    public Partida(int cantJugadores, int cantFichas, String estado, Tablero tablero, List<Jugador> jugadores, List<Ficha> fichas, Pozo pozo) {
        this.cantJugadores = cantJugadores;
        this.cantFichas = cantFichas;
        this.estado = estado;
        this.tablero = tablero;
        this.jugadores = jugadores;
        this.fichas = fichas;
        this.pozo = pozo;
    }

    /**
     * Método getter para obtener la cantidad de jugadores.
     *
     * @return La cantidad de jugadores que participan en la partida.
     */
    public int getCantJugadores() {
        return cantJugadores;
    }

    /**
     * Método setter para modificar la cantidad de jugadores.
     *
     * @param cantJugadores La nueva cantidad de jugadores.
     */
    public void setCantJugadores(int cantJugadores) {
        this.cantJugadores = cantJugadores;
    }

    /**
     * Método getter para obtener la cantidad de fichas que cada jugador recibe
     * al inicio.
     *
     * @return La cantidad de fichas por jugador.
     */
    public int getCantFichas() {
        return cantFichas;
    }

    /**
     * Método setter para modificar la cantidad de fichas por jugador.
     *
     * @param cantFichas La nueva cantidad de fichas por jugador.
     */
    public void setCantFichas(int cantFichas) {
        this.cantFichas = cantFichas;
    }

    /**
     * Método getter para obtener el estado de la partida. El estado puede ser
     * algo como "en curso" o "finalizada".
     *
     * @return El estado actual de la partida.
     */
    public String getEstado() {
        return estado;
    }

    /**
     * Método setter para modificar el estado de la partida.
     *
     * @param estado El nuevo estado de la partida.
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * Método getter para obtener el tablero de juego.
     *
     * @return El tablero donde se colocan las fichas durante la partida.
     */
    public Tablero getTablero() {
        return tablero;
    }

    /**
     * Método setter para modificar el tablero de juego.
     *
     * @param tablero El nuevo tablero que se usará en la partida.
     */
    public void setTablero(Tablero tablero) {
        this.tablero = tablero;
    }

    /**
     * Método getter para obtener la lista de jugadores de la partida.
     *
     * @return La lista de jugadores que participan en la partida.
     */
    public List<Jugador> getJugadores() {
        return jugadores;
    }

    /**
     * Método setter para modificar la lista de jugadores.
     *
     * @param jugadores La nueva lista de jugadores que participarán en la
     * partida.
     */
    public void setJugadores(List<Jugador> jugadores) {
        this.jugadores = jugadores;
    }

    /**
     * Método getter para obtener la lista de fichas de la partida.
     *
     * @return La lista de fichas disponibles para la partida.
     */
    public List<Ficha> getFichas() {
        return fichas;
    }

    /**
     * Método setter para modificar la lista de fichas de la partida.
     *
     * @param fichas La nueva lista de fichas que se usarán en la partida.
     */
    public void setFichas(List<Ficha> fichas) {
        this.fichas = fichas;
    }

    /**
     * Método getter para obtener el pozo de fichas. El pozo contiene las fichas
     * que no han sido distribuidas a los jugadores.
     *
     * @return El pozo de fichas de la partida.
     */
    public Pozo getPozo() {
        return pozo;
    }

    /**
     * Método setter para modificar el pozo de fichas.
     *
     * @param pozo El nuevo pozo de fichas.
     */
    public void setPozo(Pozo pozo) {
        this.pozo = pozo;
    }

    /**
     * Método sobreescrito para generar una representación en formato String de
     * la partida, que incluye la cantidad de jugadores, cantidad de fichas,
     * estado, tablero, jugadores, fichas y pozo.
     *
     * @return Una cadena con el formato "Partida{cantJugadores=cantJugadores,
     * cantFichas=cantFichas, estado=estado, tablero=tablero,
     * jugadores=jugadores, fichas=fichas, pozo=pozo}".
     */
    @Override
    public String toString() {
        return "Partida{" + "cantJugadores=" + cantJugadores + ", cantFichas=" + cantFichas + ", estado=" + estado + ", tablero=" + tablero + ", jugadores=" + jugadores + ", fichas=" + fichas + ", pozo=" + pozo + '}';
    }

}
