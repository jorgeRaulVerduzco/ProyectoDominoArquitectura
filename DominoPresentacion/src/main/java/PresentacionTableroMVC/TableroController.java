/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PresentacionTableroMVC;

import Dominio.Ficha;
import Dominio.Jugador;
import Dominio.Tablero;
import EventoJuego.Evento;
import java.awt.Point;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
/**
 *
 * @author Serva
 */
import Presenctacion.Mediador;
import Server.Server;
import ServerLocal.ServerComunicacion;
import java.net.Socket;
import javax.swing.JOptionPane;

public class TableroController {

    private TableroModel tableroModel;
    private TableroView tableroView;
    private Mediador mediador;
    private boolean isDragging = false; // Estado de arrastre
    private Point dragStartPoint;
    private Ficha fichaSeleccionada; // Para guardar la ficha seleccionada
    private int indiceSeleccionado = -1; // Para guardar el índice de la ficha seleccionada
    private boolean fichaColocada = false;
    private Server server;
    private ServerComunicacion serverComunicacion;

    public TableroController(TableroModel model, TableroView view, ServerComunicacion serverComunicacion) {
        this.tableroModel = model;
        this.tableroView = view;
        this.serverComunicacion = serverComunicacion;
    }

    public boolean isFichaColocada() {
        return fichaColocada;
    }
    
    public void setServer(Server server) {
        this.server = server;
    }
    public void moverFichaArrastrada(MouseEvent e) {
        if (isDragging && fichaSeleccionada != null && !fichaColocada) { // Solo mover si no está colocada
            JPanel panel = (JPanel) e.getSource();
            int newX = panel.getX() + e.getX() - dragStartPoint.x;
            int newY = panel.getY() + e.getY() - dragStartPoint.y;
            panel.setLocation(newX, newY);
        }
    }

    public void iniciarArrastreFicha(int index, Point point) {
        Ficha ficha = tableroModel.getFichasTablero().get(index);
        if (!ficha.isColocada()) { // Verificar si la ficha ya está colocada
            fichaSeleccionada = ficha;
            indiceSeleccionado = index;
            isDragging = true;
            dragStartPoint = point;
        }
    }

    public void setMediator(Mediador mediador) {
        this.mediador = mediador;
    }
    
    

    public void detenerArrastreFicha() {
        if (isDragging && fichaSeleccionada != null) {
            // ... (lógica para colocar la ficha en el tablero)
            fichaSeleccionada.setColocada(true); // Marcar la ficha como colocada
            // ... (actualizar el modelo del tablero)
            isDragging = false;
            fichaSeleccionada = null;
            indiceSeleccionado = -1;
        }
    }

    public boolean colocarFichaEnTablero(Ficha ficha, String lado) {
          try {
        tableroModel.agregarFicha(ficha, lado);
        tableroView.actualizarVista(); // Asegúrate de que esta línea esté presente
        return true;
    } catch (IllegalArgumentException e) {
        JOptionPane.showMessageDialog(tableroView, "Jugada no válida: " + e.getMessage());
        return false;
    }    }
    
    

   /**
     * Procesa una jugada enviando el evento "JUGADA".
     * 
     * @param ficha La ficha seleccionada.
     * @param lado El lado donde colocar la ficha.
     */
    public void procesarJugada(Ficha ficha, String lado) {
        // Emitir evento "JUGADA"
        System.out.println("Evento JUGADA emitido: Ficha " + ficha + " Lado: " + lado);

        // Aquí enviamos el evento al servidor (simulado por ahora)
        simularEnvioEvento(ficha, lado);
    }

    /**
     * Simula el envío de un evento y la recepción del tablero actualizado.
     * 
     * @param ficha La ficha colocada.
     * @param lado El lado del tablero.
     */
    private void simularEnvioEvento(Ficha ficha, String lado) {
        // Actualizamos el modelo directamente (esto lo haría el servidor)
        tableroModel.agregarFicha(ficha, lado);

        // Simulamos la recepción del tablero actualizado
        Tablero tableroActualizado = tableroModel.getTablero();

        // Actualizamos la vista con el tablero actualizado
        actualizarTablero(tableroActualizado);
    }
    
    
   public void procesarJugadaArrastrada(Ficha ficha, String lado, Socket socketJugador) {
    // Validar si la jugada es correcta
    if (esJugadaValida(ficha, lado)) {
        // Actualizar modelo y vista localmente
        tableroModel.agregarFicha(ficha, lado);
        tableroView.actualizarVista();
        
        // Construir el evento
        Evento evento = new Evento("JUGADA");
        evento.agregarDato("ficha", ficha);  // Asegurémonos de que ficha sea del tipo correcto
        evento.agregarDato("lado", lado);    // Lado debe ser un String, asumiendo que "lado" es un String

        // Enviar el evento al servidor
        if (socketJugador == null || !socketJugador.isConnected()) {
            System.err.println("[ERROR] Socket no válido para el jugador actual");
            return;
        }

        // Enviar el evento al servidor
        serverComunicacion.procesarEvento(socketJugador, evento);
        
        
    } else {
        JOptionPane.showMessageDialog(tableroView, "Jugada no válida: el valor no coincide con el extremo.");
    }
}



    private boolean esJugadaValida(Ficha ficha, String lado) {
        Ficha extremo = lado.equals("izquierdo")
            ? tableroModel.obtenerExtremoIzquierdo()
            : tableroModel.obtenerExtremoDerecho();

        if (extremo == null) {
            return true; // Tablero vacío, cualquier ficha es válida
        }

        return lado.equals("izquierdo")
            ? ficha.getEspacio2() == extremo.getEspacio1()
            : ficha.getEspacio1() == extremo.getEspacio2();
    }


    private String construirEventoJugada(Ficha ficha, String lado) {
        return String.format("Ficha: [%d, %d], Lado: %s", ficha.getEspacio1(), ficha.getEspacio2(), lado);
    }




    

    /**
     * Sincroniza el modelo y la vista con un tablero actualizado.
     * 
     * @param tableroActualizado El estado actualizado del tablero.
     */
    private void actualizarTablero(Tablero tableroActualizado) {
    tableroModel.actualizarFichasTablero(tableroActualizado.getFichasTablero());
    tableroView.actualizarVista();
}

}

    
    

