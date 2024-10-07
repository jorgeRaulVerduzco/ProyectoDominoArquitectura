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
public class TableroController {
       private TableroModel tableroModel;
    private TableroView tableroView;
    private boolean isDragging = false; // Estado de arrastre
    private Point dragStartPoint;
    private Ficha fichaSeleccionada; // Para guardar la ficha seleccionada
    private int indiceSeleccionado = -1; // Para guardar el índice de la ficha seleccionada

    public TableroController(TableroModel model, TableroView view) {
        this.tableroModel = model;
        this.tableroView = view;
    }

    public void iniciarArrastreFicha(int index, Point point) {
        fichaSeleccionada = tableroModel.getFichasTablero().get(index); // Guardar la ficha seleccionada
        indiceSeleccionado = index; // Guardar su índice
        isDragging = true; // Iniciar arrastre
        dragStartPoint = point; // Guardar el punto inicial
    }

    public void moverFichaArrastrada(MouseEvent e) {
        if (isDragging && fichaSeleccionada != null) {
            JPanel panel = (JPanel) e.getSource();
            int newX = panel.getX() + e.getX() - dragStartPoint.x;
            int newY = panel.getY() + e.getY() - dragStartPoint.y;
            panel.setLocation(newX, newY);
        }
    }

    public void detenerArrastreFicha() {
        if (isDragging) {
            isDragging = false; // Detener arrastre
            fichaSeleccionada = null; // Limpiar ficha seleccionada
            indiceSeleccionado = -1; // Limpiar índice
        }
    }
}
