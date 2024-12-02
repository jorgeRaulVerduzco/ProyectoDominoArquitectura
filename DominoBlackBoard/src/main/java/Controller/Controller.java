/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import BlackBoard.BlackBoard;
import Dominio.Jugador;
import Dominio.Sala;
import EventoJuego.Evento;
import KnowdledgeSource.KnowdledgeSource;
import KnowdledgeSource.SalaKnowledgeSource;
import Server.Server;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author INEGI
 */
public class Controller implements KnowdledgeSource {

    private BlackBoard blackboard;
    private List<KnowdledgeSource> knowledgeSources;
    private Server server;
    private Map<String, Jugador> jugadores;
    private Map<String, Sala> salas;

    /**
     * Constructor de la clase Controller. Inicializa la pizarra, las fuentes de
     * conocimiento y el servidor. También registra las fuentes de conocimiento
     * en el sistema.
     *
     * @param server El servidor que enviará los eventos.
     */
    public Controller(Server server) {
        this.jugadores = new HashMap<>();
        this.blackboard = new BlackBoard(server);
        this.knowledgeSources = new ArrayList<>();
        this.server = server;
        salas= new HashMap<>();
    }

    /**
     * Registra las fuentes de conocimiento que procesarán los eventos. En este
     * caso, se registra una fuente de conocimiento relacionada con las salas.
     */
    

    /**
     * Procesa la desconexión de un jugador. Se notifica a todas las salas donde
     * el jugador estaba presente, y se actualiza el estado de esas salas o se
     * elimina si es necesario. Finalmente, se elimina al jugador de la pizarra.
     *
     * @param jugador El jugador que se ha desconectado.
     */
    public void procesarDesconexion(Jugador jugador) {
        // Notificar a todas las salas donde el jugador estaba
        blackboard.getSalasDisponibles().stream()
                .filter(sala -> sala.getJugador().contains(jugador))
                .forEach(sala -> {
                    sala.getJugador().remove(jugador);
                    if (sala.getJugador().isEmpty()) {
                        blackboard.removerSala(sala.getId());
                    } else {
                        blackboard.actualizarEstadoSala(sala.getId(), sala);
                    }
                });

        // Remover el jugador del blackboard
        blackboard.removerJugador(jugador.getNombre());
    }

    /**
 * Notifica un cambio al servidor. Crea un evento con el tipo de cambio y lo
 * envía al servidor para que se notifique a los clientes.
 *
 * @param tipoEvento El tipo de evento que describe el cambio.
 */
public void notificarCambio(String tipoEvento) {
    System.out.println("Enviando mensaje a todos los clientes");
    System.out.println("Tipo de evento es: " + tipoEvento);

    // Convertir la lista de jugadores a un formato adecuado (por ejemplo, JSON o String)
    String mensaje = tipoEvento; // Comienza con el tipo de evento

    // Si el tipo de evento es 'REGISTRO_USUARIO', incluir la lista de jugadores
    if (tipoEvento.equals("REGISTRO_USUARIO")) {
        // Convertir la lista de jugadores a una representación de texto (por ejemplo, un String)
        String listaJugadores = convertirListaDeJugadoresAString();

        // Concatenar la lista de jugadores al mensaje
        mensaje += ";" + listaJugadores;

        // Agregar la lista de jugadores registrados al servidor
        server.registrarJugadores(jugadores);  // Pasar la lista de jugadores de Blackboard al Server
    }
    else if(tipoEvento.equals("CREAR_SALA")){
        //eSTE PUEDE QUE ESTE MAL
        server.registrarSalas(salas);
    }

    // Enviar el mensaje a todos los clientes
    server.enviarMensajeATodosLosClientes(mensaje);
}

private String convertirListaDeJugadoresAString() {
    StringBuilder sb = new StringBuilder();

    // Iterar sobre la lista de jugadores y agregar la información de cada uno al String
    for (Jugador jugador : jugadores.values()) {
        sb.append(jugador.getNombre()).append(":").append(jugador.getNombre()).append(",");
    }

    // Eliminar la última coma
    if (sb.length() > 0) {
        sb.deleteCharAt(sb.length() - 1);
    }

    return sb.toString();
}



    /**
     * Implementación del método de la interfaz `KnowledgeSource` para que el
     * controlador procese eventos finales generados en el `BlackBoard`.
     *
     * @param evento El evento que se debe procesar.
     * @return 
     */
    @Override
    public boolean puedeProcesar(Evento evento) {
        // Procesar solo eventos de tipo "RESULTADO"
        return evento.getTipo().startsWith("");
    }

    /**
     * Método que recibe eventos procesados desde el BlackBoard y los envía al servidor.
     * 
     * @param cliente El socket del cliente que ha generado el evento.
     * @param evento El evento que ha sido procesado.
     */
    @Override
    public void procesarEvento(Socket cliente, Evento evento) {
        // Notificar al servidor sobre el resultado
        System.out.println("Notificando resultado al servidor: " + evento.getTipo());
        server.enviarEvento(evento, cliente);
    }
}
