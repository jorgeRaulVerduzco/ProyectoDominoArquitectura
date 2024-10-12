/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PresentacionTableroMVC;

import Dominio.Ficha;
import java.awt.Point;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
/**
 *
 * @author Serva
 */
import Presenctacion.Mediador;
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

    public TableroController(TableroModel model, TableroView view) {
        this.tableroModel = model;
        this.tableroView = view;
    }

    public boolean isFichaColocada() {
        return fichaColocada;
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
}
