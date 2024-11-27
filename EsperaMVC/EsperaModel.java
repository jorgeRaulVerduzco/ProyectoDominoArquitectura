/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Presenctacion.EsperaMVC;

import Presenctacion.Observer;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Serva
 */
public class EsperaModel {

    private List<String> jugadoresConectados;
    private boolean partidaIniciada;
    private List<Observer> observers;

    public EsperaModel() {
        jugadoresConectados = new ArrayList<>();
        partidaIniciada = false;
        observers = new ArrayList<>();
    }

    public List<String> getJugadoresConectados() {
        return new ArrayList<>(jugadoresConectados); // Devuelve una copia para evitar modificaciones externas
    }

    public void agregarJugador(String jugador) {
        jugadoresConectados.add(jugador);
        notifyObservers();
    }

    public boolean isPartidaIniciada() {
        return partidaIniciada;
    }

    public void setPartidaIniciada(boolean partidaIniciada) {
        this.partidaIniciada = partidaIniciada;
        notifyObservers();
    }

    public void iniciarPartida() {
        this.partidaIniciada = true;
        notifyObservers();
    }

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    private void notifyObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }
}
