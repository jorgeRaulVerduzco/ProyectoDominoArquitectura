/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Presenctacion.CrearSalaMVC;

import Dominio.Jugador;
import Dominio.Sala;
import EventoJuego.Evento;
import Negocio.ServicioControlJuego;
import Presenctacion.Observer;
import Server.Server;
import ServerLocal.ServerComunicacion;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
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
    private Jugador jugadorActual;

    public void setJugadorActual(Jugador jugador) {
        this.jugadorActual = jugador;
    }

    public CrearSalaModel() {
        this.servicioControlJuego = new ServicioControlJuego();
        this.observers = new ArrayList<>();
        this.latch = new CountDownLatch(1);
        this.conexionExitosa = false;
    }

    public void setServer(Server server) {
        this.server = server;
        if (server != null && server.isServidorActivo()) {
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
        System.out.println("[DEBUG] Iniciando creación de sala en el modelo");
        try {
            if (numeroJugadores <= 0 || numeroFichas <= 0) {
                System.err.println("[ERROR] Números inválidos");
                return;
            }

            if (jugadorActual == null) {
                System.err.println("[ERROR] No hay jugador actual");
                return;
            }

            if (server == null) {
                System.err.println("[ERROR] Servidor no configurado");
                return;
            }
Sala sala = new Sala();
sala.setCantJugadores(numeroJugadores);
sala.setNumeroFichas(numeroFichas);
            Evento evento = new Evento("CREAR_SALA");
            evento.agregarDato("numJugadores", numeroJugadores);
            evento.agregarDato("numFichas", numeroFichas);
            evento.agregarDato("jugador", jugadorActual);
            ServerComunicacion servercito = new ServerComunicacion(server);
            System.out.println("[DEBUG] Enviando evento CREAR_SALA al servidor");
            Socket cliente = new Socket("localhost", 51114);
            ObjectOutputStream out = new ObjectOutputStream(cliente.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(cliente.getInputStream());
           servercito.procesarEvento(cliente, evento);
 //  server.enviarEvento(evento);
        } catch (Exception e) {
            System.err.println("[ERROR] Error creando sala: " + e.getMessage());
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
