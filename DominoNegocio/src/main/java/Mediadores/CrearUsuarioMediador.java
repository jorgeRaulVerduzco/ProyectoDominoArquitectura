/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Mediadores;

import Server.Server;
import EventoJuego.Evento;
import Presenctacion.MenuPrincipalMVC.CrearUsuarioModel;


/**
 *
 * @author Serva
 */
public class CrearUsuarioMediador {

    private Server server;

    public CrearUsuarioMediador(Server server) {
        this.server = server;
    }

    public CrearUsuarioMediador() {
    }

    public void crearJugador(CrearUsuarioModel usuario) {
        // Crear el evento de registro
        Evento evento = new Evento("REGISTRO_USUARIO");
        evento.agregarDato("nombre", usuario.getNombre());
        evento.agregarDato("avatar", usuario.getAvatar());

        // Enviar el evento al server
        server.enviarEvento(evento);
    }

}
