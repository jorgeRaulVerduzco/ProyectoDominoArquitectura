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
    public void solicitarSalasDisponibles() {
        System.out.println("Modelo: Solicitando salas disponibles al servidor...");
        
        if (server != null && server.isConnected()) {
            Evento evento = new Evento("SOLICITAR_SALAS");
            server.enviarEvento(evento);
            System.out.println("Modelo: Solicitud de salas enviada");
            
        }
    }
    // Método que recibe la respuesta del servidor
    public void actualizarSalas(List<Sala> salas) {
        System.out.println("Modelo: Recibiendo actualización de salas. Cantidad: " + 
            (salas != null ? salas.size() : "null"));
        // Tu código de actualización
        notifyObservers();
    }
    
    

    /**
     * Actualiza la lista de salas disponibles con una nueva lista y notifica a
     * los observadores. Si se ejecuta fuera del hilo de la interfaz gráfica,
     * utiliza el Event Dispatch Thread (EDT).
     *
     * @param salas nueva lista de salas disponibles.
     */
    public void actualizarSalasDisponibles(List<Sala> salas) {
        SwingUtilities.invokeLater(() -> {
            try {
                System.out.println("Modelo: Actualizando salas disponibles");
                
                if (salas == null) {
                    this.salasDisponibles = new ArrayList<>();
                } else {
                    this.salasDisponibles = new ArrayList<>(salas);
                }
                
                System.out.println("Modelo: Total de salas actualizadas: " + 
                    this.salasDisponibles.size());
                
                // Notificar a los observadores
                notifyObservers();
                
            } catch (Exception e) {
                System.err.println("Modelo: Error actualizando salas: " + e.getMessage());
                e.printStackTrace();
            }
        });
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
    public void unirseASala(String salaId, Jugador jugador) {
        if (server != null && server.isConnected()) {
            Evento evento = new Evento("UNIR_SALA");
            evento.agregarDato("salaId", salaId);
            evento.agregarDato("jugador", jugador);
            server.enviarEvento(evento);
        }
    }
}
