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
public class Sala {
    int cantJugadores;
    int numeroFichas;
    List<Jugador> jugador = new ArrayList<>();
    String estado;
    Partida partida;

    public Sala() {
    }

    public int getCantJugadores() {
        return cantJugadores;
    }

    public void setCantJugadores(int cantJugadores) {
        this.cantJugadores = cantJugadores;
    }

    public int getNumeroFichas() {
        return numeroFichas;
    }

    public void setNumeroFichas(int numeroFichas) {
        this.numeroFichas = numeroFichas;
    }

    public List<Jugador> getJugador() {
        return jugador;
    }

    public void setJugador(List<Jugador> jugador) {
        this.jugador = jugador;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Partida getPartida() {
        return partida;
    }

    public void setPartida(Partida partida) {
        this.partida = partida;
    }

    @Override
    public String toString() {
        return "Sala{" + "cantJugadores=" + cantJugadores + ", numeroFichas=" + numeroFichas + ", jugador=" + jugador + ", estado=" + estado + ", partida=" + partida + '}';
    }
    
    
}
