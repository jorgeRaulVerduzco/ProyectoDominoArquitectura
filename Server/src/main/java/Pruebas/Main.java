/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Pruebas;

import Dominio.Jugador;
import Dominio.Sala;
import EventoJuego.Evento;
import Server.Server;
import ServerLocal.ServerComunicacion;
import java.io.IOException;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) {
        try {
            // Iniciar el servidor en un hilo separado
            Server server = new Server();
            ServerComunicacion servercito = new ServerComunicacion(server);
            
            
            Socket cliente = new Socket("localhost", 7680);
            
            // Registrar jugador
            Evento eventoRegistro = new Evento("REGISTRO_USUARIO");
            eventoRegistro.agregarDato("jugador", new Jugador("jorge"));
            
            servercito.registrarUsuario(cliente, eventoRegistro);
            
            // Crear sala
            Evento evento = new Evento("CREAR_SALA");
            Sala sala = new Sala();
            sala.setNumeroFichas(27);
            sala.setCantJugadores(4);
            evento.agregarDato("numJugadores", 4);
            evento.agregarDato("numFichas", 27);
            evento.agregarDato("jugador", new Jugador("jorge"));
            
            // Usar el método de ServerComunicacion para crear sala
            servercito.procesarEvento(cliente, evento);
            Evento solicitudSalas = new Evento("RESPUESTA_SALAS");
            servercito.procesarEvento(cliente, solicitudSalas);
            System.out.println("se ven");
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
      
        }
    }
