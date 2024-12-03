/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Presenctacion.UnirseAlaSalaMVC;

import Dominio.Jugador;
import Dominio.Sala;
import EventoJuego.Evento;
import Negocio.ServicioControlJuego;
import Presenctacion.ConfiguracionSocket;
import Presenctacion.Observer;
import Server.Server;
import ServerLocal.ServerComunicacion;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

/**
 *
 * @author INEGI
 */
public class UnirseAlaSalaModel {

    private List<Observer> observers;
    private Server server;
    private ServicioControlJuego servicioControlJuego;
    private ServerComunicacion serverComunicacion;
    private Jugador jugadorActual;

    /**
     * Constructor por defecto que inicializa las listas de salas disponibles y
     * observadores.
     */
    public UnirseAlaSalaModel() {
        this.serverComunicacion = new ServerComunicacion(server);
        this.observers = new ArrayList<>();
        this.servicioControlJuego = ServicioControlJuego.getInstance();  // Usar la instancia única
    }
    
    
        public void setJugadorActual(Jugador jugador) {
        this.jugadorActual = jugador;
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
     * @throws java.io.IOException
     */
    public List<Sala> getSalasDisponibles() throws IOException {
        System.out.println("UnirseAlaSalaModel: Solicitando salas al servidor...");

        // Obtener las salas disponibles desde ServicioControlJuego
        List<Sala> salasDisponibles = server.cargarSalasMultijugador();
        int puertoSocket = ConfiguracionSocket.getInstance().getPuertoSocket();
        Socket cliente = new Socket("localhost", puertoSocket);
        Evento solicitudSalas = new Evento("RESPUESTA_SALAS");

        ServerComunicacion servercito = new ServerComunicacion(server);
        System.out.println("se ven al millon");
        
        if (salasDisponibles != null) {
            System.out.println("UnirseAlaSalaModel: Salas recibidas. Total: " + salasDisponibles.size());
            for (Sala sala : server.obtenerSalasActivas()) {
                System.out.println("Sala ID: " + sala.getId() + ", Estado: " + sala.getEstado());
            }
        } else {
            System.out.println("UnirseAlaSalaModel: No se recibieron salas del servidor.");
        }

        return salasDisponibles;
    }

    public void actualizarSalas(List<Sala> salas) {
        if (salas != null) {
            System.out.println("Modelo: Actualizando salas. Cantidad recibida: " + salas.size());
            // Las salas se gestionan directamente desde ServicioControlJuego, por lo que no necesitamos almacenar una lista local
        } else {
            System.out.println("Modelo: Salas recibidas es null. Inicializando lista vacía.");
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
        List<Sala> salasDisponibles = servicioControlJuego.getSalasDisponibles();
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
    
     public Jugador obtenerJugadorConectado() {
        Jugador jugador = server.getJugadorConectado(); // Obtiene un solo jugador
        if (jugador != null) {
            System.out.println("Jugador conectado: " + jugador.getNombre());
            // Puedes hacer lo que desees con el jugador aquí
        } else {
            System.out.println("No hay jugadores conectados.");
        }
        return jugador;
    }

    /**
     * Intenta unir a un jugador a una sala específica. Envia un evento
     * "UNIR_SALA" al servidor con los datos de la sala y el jugador.
     *
     * @param id
     * @param jugador
     */
    public void unirseASala(String id, Jugador jugador) {
    try {
        jugador = obtenerJugadorConectado();
        System.out.println("Jugador conectado es: " + jugador);
        
        Sala sala = servicioControlJuego.buscarSalaPorId(id); // Recupera la sala desde ServicioControlJuego
        if (sala == null) {
            System.out.println("Error: Sala con ID " + id + " no encontrada.");
            return;
        }
        
        int puertoSocket = ConfiguracionSocket.getInstance().getPuertoSocket();
        Socket socket = new Socket("localhost", puertoSocket);
        System.out.println("Socket del jugador actual: " + puertoSocket);
        
        Evento evento = new Evento("UNIR_SALA");
        evento.agregarDato("sala", sala); // Enviar el objeto completo de Sala
        evento.agregarDato("jugador", jugador); // Enviar el objeto Jugador
        serverComunicacion.procesarEvento(socket, evento); // Procesar el evento
    } catch (IOException ex) {
        Logger.getLogger(UnirseAlaSalaModel.class.getName()).log(Level.SEVERE, null, ex);
    }
}


}
