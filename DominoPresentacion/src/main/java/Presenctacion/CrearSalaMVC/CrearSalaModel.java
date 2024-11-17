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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author INEGI
 */
public class CrearSalaModel {
    private volatile boolean conexionConfirmada;
    private CountDownLatch latch;
    private int numeroJugadores;
    private int numeroFichas;
    private ServicioControlJuego servicioControlJuego;
    private List<Observer> observers;
    private Server server;
    private volatile boolean conexionExitosa;
    private final Object lockConexion = new Object();

    public CrearSalaModel() {
        this.servicioControlJuego = new ServicioControlJuego();
        this.observers = new ArrayList<>();
        this.latch = new CountDownLatch(1);
        this.conexionExitosa = false;
    }

    public void setServer(Server server) {
       this.server = server;
        if (server != null && server.isConnected()) {
            confirmarConexion();
        } else {
            System.err.println("No se pudo establecer conexión con el servidor");
        }
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
  
    public void confirmarConexion() {
        synchronized (lockConexion) {
            conexionExitosa = true;
            lockConexion.notifyAll();
        }
    }
    public void crearSala() {
      if (server == null || !server.isConnected()) {
            System.err.println("No hay conexión establecida con el servidor");
            return;
        }

        try {
            Sala nuevaSala = new Sala();
            nuevaSala.setCantJugadores(numeroJugadores);
            nuevaSala.setNumeroFichas(numeroFichas);
            nuevaSala.setEstado("ESPERANDO");
            
            Evento evento = new Evento("CREAR_SALA");
            evento.agregarDato("numJugadores", numeroJugadores);
            evento.agregarDato("numFichas", numeroFichas);
            
            System.out.println("Enviando evento de creación de sala al servidor...");
            server.enviarEvento(evento);
            
        } catch (Exception e) {
            System.err.println("Error al crear sala: " + e.getMessage());
            e.printStackTrace();
        }
    
    }

    public void esperarServidor() throws InterruptedException {
        latch.await();  // Espera hasta que el servidor esté inicializado
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
