/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Presenctacion.MenuPrincipalMVC;

import Dominio.Avatar;
import Dominio.Jugador;
import EventoJuego.Evento;
import Presenctacion.ConfiguracionSocket;
import Presenctacion.Mediador;
import Server.Server;
import ServerLocal.ServerComunicacion;
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

public void crearUsuario() throws IOException {
    // Verificar si el socket está conectado antes de registrar el jugador

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
    Jugador jugador = new Jugador(nombre);
    jugador.setNombre(nombre);
    jugador.setEstado("ACTIVO");
    String textoPuerto = view.getPuertoSocket().getText().trim();

    int puerto = Integer.parseInt(textoPuerto);
    ConfiguracionSocket.getInstance().setPuertoSocket(puerto);
    int puertoSocket = ConfiguracionSocket.getInstance().getPuertoSocket();

    // Verificar si el servidor está disponible
    if (server == null) {
        JOptionPane.showMessageDialog(view, "Error: El servidor no está disponible.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Crear socket
    Socket socket = new Socket("localhost", puertoSocket);
    System.out.println("Socket del jugador actual: " + puertoSocket);

    if (!socket.isConnected()) {
        JOptionPane.showMessageDialog(view, "Error: No se pudo conectar con el servidor.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Registrar el socket y el jugador en el servidor
    server.registrarJugador(socket, jugador);

    Evento eventoRegistro = new Evento("REGISTRO_USUARIO");
    eventoRegistro.agregarDato("jugador", jugador);

    System.out.println("Evento creado: " + eventoRegistro);
    System.out.println("Jugador a registrar: " + jugador);

    ServerComunicacion comunicacion = new ServerComunicacion(server);
    try {
        comunicacion.registrarUsuario(socket, eventoRegistro); // Registrar usuario en el servidor
        System.out.println("[REGISTRO] Jugador registrado en el servidor: " + jugador);
        
        server.persistirDatosMultijugador();
        server.cargarClientesPersistidos();

        int cantidadUsuariosConectados = server.getUsuariosConectados().size();
        System.out.println("Cantidad de usuarios conectados en el servidor: " + cantidadUsuariosConectados);

        // Notificar al mediador
        mediador.usuarioCreado(jugador);
    } catch (Exception e) {
        System.err.println("Error al registrar usuario: " + e.getMessage());
        JOptionPane.showMessageDialog(view, "Error al registrar usuario. Intente nuevamente.", "Error", JOptionPane.ERROR_MESSAGE);
    }
}



    public void crearUsuario2(CrearUsuarioModel usuario) {
        System.out.println("es el 2");
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
