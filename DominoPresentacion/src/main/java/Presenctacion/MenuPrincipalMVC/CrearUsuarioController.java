/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Presenctacion.MenuPrincipalMVC;

import EventoJuego.Evento;
import Presenctacion.Mediador;
import Server.Server;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

/**
 *
 * @author Serva
 */
public class CrearUsuarioController {

    private CrearUsuarioView view;
    private Mediador mediador;
    private Server server;


    
    
    public CrearUsuarioController(CrearUsuarioView view) {
        this.view = view;
        // Vinculando el evento de creación de usuario a la vista
        this.view.setCreateUserListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                crearUsuario();
            }
        });
    }

    public void setMediator(Mediador mediador) {
        this.mediador = mediador;
    }

    public Mediador getMediator() {
        return mediador;
    }

    public void setServer(Server server) {
        if (server == null) {
            throw new IllegalArgumentException("El servidor no puede ser nulo.");
        }
        this.server = server;
        System.out.println("Servidor asignado correctamente.");
    }

    public void mostrarVista() {
        view.setVisible(true);
    }

    private void crearUsuario() {
        String nombre = view.getNombre();
        String avatar = view.getSelectedAvatar();

        // Validación de los campos
        if (nombre == null || nombre.trim().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Por favor, ingrese un nombre válido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (avatar == null || avatar.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Por favor, seleccione un avatar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Crear el usuario con los datos ingresados
        CrearUsuarioModel usuario = new CrearUsuarioModel(nombre, avatar);

        // Notificar al mediador que el usuario ha sido creado
        mediador.usuarioCreado(usuario);
        System.out.println("ENVIANDOEVENTO");
        // Enviar el evento de creación de usuario al servidor
        if (server != null) {
            Evento evento = new Evento("REGISTRO_USUARIO");
            evento.agregarDato("usuario", usuario);

            // Enviar el evento a todos los clientes conectados
            server.enviarNuevoCliente(evento);
            System.out.println("Evento enviado al servidor: " + evento.getTipo());
        } else {
            JOptionPane.showMessageDialog(view, "Error: El servidor no está disponible.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        System.out.println("Servidor en crearUsuario: " + (server != null ? "Inicializado" : "No Inicializado"));
    }
    
    public void crearUsuario2(CrearUsuarioModel usuario) {
    if (usuario == null) {
        System.out.println("[ERROR] El modelo de usuario es null.");
        JOptionPane.showMessageDialog(view, "Error: Datos de usuario inválidos.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    String nombre = usuario.getNombre();
    String avatar = usuario.getAvatar();

    // Validación de los campos
    if (nombre == null || nombre.trim().isEmpty()) {
        JOptionPane.showMessageDialog(view, "Por favor, ingrese un nombre válido.", "Error", JOptionPane.ERROR_MESSAGE);
        System.out.println("[ERROR] Nombre inválido: " + nombre);
        return;
    }

    if (avatar == null || avatar.trim().isEmpty()) {
        JOptionPane.showMessageDialog(view, "Por favor, seleccione un avatar.", "Error", JOptionPane.ERROR_MESSAGE);
        System.out.println("[ERROR] Avatar inválido: " + avatar);
        return;
    }

    System.out.println("[DEBUG] Usuario válido. Nombre: " + nombre + ", Avatar: " + avatar);

    // Notificar al mediador que el usuario ha sido creado
    
       
    
        System.out.println("");
    // Enviar el evento de creación de usuario al servidor
    if (server != null) {
        Evento evento = new Evento("REGISTRO_USUARIO");
        evento.agregarDato("usuario", usuario);

        server.enviarNuevoCliente(evento);
        System.out.println("[DEBUG] Evento enviado al servidor: " + evento.getTipo());
    } else {
        JOptionPane.showMessageDialog(view, "Error: El servidor no está disponible.", "Error", JOptionPane.ERROR_MESSAGE);
        System.out.println("[ERROR] Servidor no inicializado.");
    }
}


    public void ocultarVista() {
        view.setVisible(false);
    }
}
