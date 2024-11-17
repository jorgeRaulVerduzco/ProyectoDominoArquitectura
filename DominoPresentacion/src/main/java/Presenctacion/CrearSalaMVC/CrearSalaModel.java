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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author INEGI
 */
public class CrearSalaModel {

    private CountDownLatch latch;

    private int numeroJugadores;
    private int numeroFichas;
    private ServicioControlJuego servicioControlJuego;
    private List<Observer> observers;
    private Server server;

    public CrearSalaModel() {
        this.servicioControlJuego = new ServicioControlJuego();
        this.observers = new ArrayList<>();
        this.latch = new CountDownLatch(1);

    }

    public void setServer(Server server) {
        this.server = server;
        if (server != null) {
            latch.countDown();  // Llama a countDown cuando el servidor esté listo
            System.out.println("Servidor asignado correctamente.");
        } else {
            System.out.println("Advertencia: El servidor no se ha asignado.");
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

    public void crearSala() {
       try {
            esperarServidor();
            
            Sala nuevaSala = new Sala();
            nuevaSala.setCantJugadores(numeroJugadores);
            nuevaSala.setNumeroFichas(numeroFichas);
            nuevaSala.setEstado("ESPERANDO");
            
            Evento evento = new Evento("CREAR_SALA");
            evento.agregarDato("numJugadores", numeroJugadores);
            evento.agregarDato("numFichas", numeroFichas);
            
            System.out.println("Enviando evento CREAR_SALA al servidor");
            server.enviarEvento(evento);
            
            notifyObservers();
        } catch (InterruptedException ex) {
            System.err.println("Error al esperar al servidor: " + ex.getMessage());
            Logger.getLogger(CrearSalaModel.class.getName()).log(Level.SEVERE, null, ex);
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
