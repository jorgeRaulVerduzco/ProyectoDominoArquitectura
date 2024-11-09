/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ServerLocal;

import Server.Server;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.Socket;

/**
 *
 * @author INEGI
 */
public class ServerComunicacion {
     private Server server;
    private ObjectInputStream in;

    public ServerComunicacion(Server server) {
        this.server = server;
    }

    // Determina qué tipo de evento es y llama al método correspondiente
    public void determinarEvento(String evento) {
        try {
            switch(evento) {
                case "TURNO":
                    avisarTurno(in.readObject());
                    break;
                case "POZO":
                    mandarPozo(in.readObject());
                    break;
                case "CREAR_JUGADOR":
                    crearJugador(in.readObject());
                    break;
                case "CAMBIAR_TURNO":
                    cambiarTurno(in.readObject());
                    break;
                case "GANADORES":
                    obtenerGanadores(in.readObject());
                    break;
                case "CREAR_SALA":
                    CrearSala();
                    break;
            }
        } catch (Exception e) {
            manejarErrorComunicacion();
        }
    }

    public void manejarErrorComunicacion() {
        System.out.println("Error en la comunicación");
        server.manejarErrorComunicacion();
    }

    
    public void avisarTurno(Object serializado) {
        try {
            Serializable turnoSerializado = (Serializable) serializado;
            server.enviarEvento("TURNO");
        } catch (Exception e) {
            manejarErrorComunicacion();
        }
    }

    public void mandarPozo(Object serializado) {
        try {
            Serializable pozoSerializado = (Serializable) serializado;
            server.enviarEvento("POZO");
        } catch (Exception e) {
            manejarErrorComunicacion();
        }
    }

    public void crearJugador(Object serializado) {
        try {
            Serializable jugadorSerializado = (Serializable) serializado;
            server.enviarEvento("JUGADOR_CREADO");
        } catch (Exception e) {
            manejarErrorComunicacion();
        }
    }

    public void cambiarTurno(Object serializado) {
        try {
            Serializable turnoSerializado = (Serializable) serializado;
            server.enviarEvento("CAMBIO_TURNO");
        } catch (Exception e) {
            manejarErrorComunicacion();
        }
    }

    public void obtenerGanadores(Object serializado) {
        try {
            Serializable ganadoresSerializado = (Serializable) serializado;
            server.enviarEvento("GANADORES");
        } catch (Exception e) {
            manejarErrorComunicacion();
        }
    }

    public void CrearSala() {
        try {
            server.enviarEvento("SALA_CREADA");
        } catch (Exception e) {
            manejarErrorComunicacion();
        }
    }
}
