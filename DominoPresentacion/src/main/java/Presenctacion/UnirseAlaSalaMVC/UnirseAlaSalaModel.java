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
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
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

    /**
     * Constructor por defecto que inicializa las listas de salas disponibles y
     * observadores.
     */
    public UnirseAlaSalaModel() {
        this.salasDisponibles = new ArrayList<>();
        this.observers = new ArrayList<>();
    }

    /**
     * Establece la conexión con el servidor.
     *
     * @param server instancia del servidor para comunicación.
     */
    public void setServer(Server server) {
        this.server = server;
    }

    /**
     * Añade un observador a la lista.
     *
     * @param observer el observador que desea ser notificado de cambios.
     */
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    /**
     * Elimina un observador de la lista.
     *
     * @param observer el observador que ya no desea ser notificado.
     */
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    /**
     * Devuelve la lista actual de salas disponibles.
     *
     * @return una lista de objetos {@code Sala}.
     */
    public List<Sala> getSalasDisponibles() {
        return salasDisponibles;
    }

    /**
     * Solicita al servidor la lista de salas disponibles. Envia un evento
     * "SOLICITAR_SALAS" si el servidor está conectado.
     */
    public void solicitarSalasDisponibles() throws IOException {
        System.out.println("Modelo: Solicitando salas disponibles al servidor...");
        Socket socket = new Socket("localhost", 51114);
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

        System.out.println("ENTRANDO AL IF DE SOLICITAR");

        if (server != null && server.isServidorActivo()) {
            Evento evento = new Evento("RESPUESTA_SALAS");
            server.enviarMensajeACliente(socket, evento);
            System.out.println("Modelo: Solicitud de salas enviada");

        }
    }

    public void actualizarSalas(List<Sala> salas) {
        if (salas != null) {
            System.out.println("Modelo: Actualizando salas. Cantidad recibida: " + salas.size());
            this.salasDisponibles = salas;
        } else {
            System.out.println("Modelo: Salas recibidas es null. Inicializando lista vacía.");
            this.salasDisponibles = new ArrayList<>();
        }

        // Notifica a la vista
        notifyObservers();
    }

    /**
     * Notifica a todos los observadores sobre un cambio en los datos.
     */
    private void notifyObservers() {
        System.out.println("Modelo: Notificando a " + observers.size() + " observadores");
        for (Observer observer : observers) {
            observer.update();
        }
    }

    /**
     * Imprime el estado actual del modelo, incluyendo la cantidad y detalles de
     * las salas disponibles. Útil para depuración.
     */
    public void imprimirEstadoActual() {
        System.out.println("Estado actual del modelo:");
        System.out.println("Número de salas disponibles: " + (salasDisponibles != null ? salasDisponibles.size() : "null"));
        if (salasDisponibles != null) {
            for (Sala sala : salasDisponibles) {
                System.out.println("Sala ID: " + sala.getId()
                        + ", Estado: " + sala.getEstado()
                        + ", Jugadores: " + sala.getJugador().size() + "/"
                        + sala.getCantJugadores());
            }
        }
    }

    /**
     * Intenta unir a un jugador a una sala específica. Envia un evento
     * "UNIR_SALA" al servidor con los datos de la sala y el jugador.
     *
     * @param salaId el identificador de la sala a la que se desea unir.
     * @param jugador el jugador que desea unirse.
     */
    public void unirseASala(Integer salaId, Jugador jugador) {
        if (server != null && server.isServidorActivo()) {
            Evento evento = new Evento("UNIR_SALA");
            evento.agregarDato("salaId", salaId);
            evento.agregarDato("jugador", jugador);
            server.enviarEvento(evento);
        }
    }
}