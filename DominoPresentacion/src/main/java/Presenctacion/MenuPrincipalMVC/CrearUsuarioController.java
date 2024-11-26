/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Presenctacion.MenuPrincipalMVC;

import Dominio.Avatar;
import Dominio.Jugador;
import EventoJuego.Evento;
import Presenctacion.Mediador;
import Server.Server;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
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
                try {
                    crearUsuario();
                } catch (IOException ex) {
                    Logger.getLogger(CrearUsuarioController.class.getName()).log(Level.SEVERE, null, ex);
                }
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

   // Método para conectar el Socket
    public void conectarSocket() {
        try {
            // Intentar establecer la conexión al servidor
            Socket socket = new Socket("localhost", 51114);  // Asegúrate de usar la IP y el puerto correctos
            if (socket.isConnected()) {
                System.out.println("Conexión establecida con el servidor.");
            } else {
                System.err.println("Error: No se pudo conectar al servidor.");
            }
        } catch (IOException e) {
            System.err.println("Error al conectar el socket: " + e.getMessage());
        }
    }

    public void crearUsuario() throws IOException {
        // Verificar si el socket está conectado antes de registrar el jugador
        conectarSocket(); // Asegúrate de que la conexión esté activa

        // Continuar con el registro del jugador
        String nombre = view.getNombre();
        String avatarSeleccionado = view.getSelectedAvatar();

        // Validaciones
        if (nombre == null || nombre.trim().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Por favor, ingrese un nombre válido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (avatarSeleccionado == null || avatarSeleccionado.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Por favor, seleccione un avatar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Crear jugador
        Avatar avatar = new Avatar(avatarSeleccionado);
        Jugador jugador = new Jugador(nombre, avatar);
        jugador.setEstado("ACTIVO");

        // Registrar jugador en el servidor
        if (server != null) {
             Socket socket = new Socket("localhost", 51114);
            server.registrarJugador(socket,jugador);  // Usar el nombre del jugador como clave
            System.out.println("[REGISTRO] Jugador registrado en el servidor: " + jugador);
        } else {
            JOptionPane.showMessageDialog(view, "Error: El servidor no está disponible.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Notificar al mediador con el jugador creado
        mediador.usuarioCreado(jugador);
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
