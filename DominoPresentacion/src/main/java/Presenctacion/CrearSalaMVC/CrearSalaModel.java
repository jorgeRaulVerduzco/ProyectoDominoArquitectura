/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Presenctacion.CrearSalaMVC;

import Dominio.Sala;
import EventoJuego.Evento;
import Negocio.ServicioControlJuego;
import Presenctacion.Observer;
import Server.Server;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author INEGI
 */
public class CrearSalaModel {

    private int numeroJugadores;
    private int numeroFichas;
    private ServicioControlJuego servicioControlJuego;
    private List<Observer> observers;
    private Server server;

    public CrearSalaModel() {
        this.servicioControlJuego = new ServicioControlJuego();
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

    public void crearSala() {
        Sala nuevaSala = new Sala();
        nuevaSala.setCantJugadores(numeroJugadores);
        nuevaSala.setNumeroFichas(numeroFichas);
        nuevaSala.setEstado("ESPERANDO");
        servicioControlJuego.getSalasDisponibles().add(nuevaSala);
        Evento evento = new Evento("CREAR_SALA");
        evento.agregarDato("numJugadores", numeroJugadores);
        evento.agregarDato("numFichas", numeroFichas);

        // Enviar evento al servidor
        server.enviarEvento(evento);
        notifyObservers();
    }

    // Getters y Setters
    public void setNumeroJugadores(int numeroJugadores) {
        this.numeroJugadores = numeroJugadores;
        notifyObservers();
    }

    public void setNumeroFichas(int numeroFichas) {
        this.numeroFichas = numeroFichas;
        notifyObservers();
    }

    public int getNumeroJugadores() {
        return numeroJugadores;
    }

    public int getNumeroFichas() {
        return numeroFichas;
    }
}
