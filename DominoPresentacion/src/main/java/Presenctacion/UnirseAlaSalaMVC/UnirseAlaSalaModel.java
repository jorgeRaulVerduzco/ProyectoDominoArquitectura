/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Presenctacion.UnirseAlaSalaMVC;

import Dominio.Jugador;
import Dominio.Sala;
import EventoJuego.Evento;
import Presenctacion.Observer;
import Server.Server;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;

/**
 *
 * @author INEGI
 */
public class UnirseAlaSalaModel {

    private List<Sala> salasDisponibles;
    private List<Observer> observers;
    private Server server;

    public UnirseAlaSalaModel() {
        this.salasDisponibles = new ArrayList<>();
        this.observers = new ArrayList<>();
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    private void notifyObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }

    public List<Sala> getSalasDisponibles() {
        return salasDisponibles;
    }

    public void actualizarSalasDisponibles(List<Sala> salas) {
   System.out.println("Actualizando salas disponibles: " + (salas != null ? salas.size() : "null"));
    this.salasDisponibles = salas != null ? new ArrayList<>(salas) : new ArrayList<>();
    SwingUtilities.invokeLater(() -> {
        System.out.println("Notificando observadores. Salas: " + salasDisponibles.size());
        notifyObservers();
    });
    }

    public void solicitarSalasDisponibles() {
        if (server != null && server.isConnected()) {
            Evento evento = new Evento("SOLICITAR_SALAS");
            server.enviarEvento(evento);
        }
    }

    public void unirseASala(String salaId, Jugador jugador) {
        if (server != null && server.isConnected()) {
            Evento evento = new Evento("UNIR_SALA");
            evento.agregarDato("salaId", salaId);
            evento.agregarDato("jugador", jugador);
            server.enviarEvento(evento);
        }
    }
}
