/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PresentacionTableroMVC;

import Dominio.Ficha;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Serva
 */
public class TableroModel {

    private List<Ficha> fichasTablero;

    public TableroModel() {
        this.fichasTablero = new ArrayList<>();
    }

    public List<Ficha> getFichasTablero() {
        return fichasTablero;
    }

    public void agregarFicha(Ficha ficha, String lado) {
        if (lado.equals("izquierdo")) {
            fichasTablero.add(0, ficha); // Añadir al inicio
        } else if (lado.equals("derecho")) {
            fichasTablero.add(ficha); // Añadir al final
        } else {
            throw new IllegalArgumentException("Lado inválido. Debe ser 'izquierdo' o 'derecho'.");
        }
    }

    public void moverFicha(int indexOrigen, int indexDestino) {
        if (indexOrigen < 0 || indexOrigen >= fichasTablero.size()
                || indexDestino < 0 || indexDestino >= fichasTablero.size()) {
            throw new IndexOutOfBoundsException("Índice fuera de los límites del tablero.");
        }

        // Obtener la ficha a mover
        Ficha fichaAEncontrar = fichasTablero.get(indexOrigen);

        // Quitar la ficha del índice de origen
        fichasTablero.remove(indexOrigen);

        // Insertar la ficha en la nueva posición
        fichasTablero.add(indexDestino, fichaAEncontrar);
    }

    public Ficha obtenerExtremoIzquierdo() {
        return !fichasTablero.isEmpty() ? fichasTablero.get(0) : null;
    }

    public Ficha obtenerExtremoDerecho() {
        return !fichasTablero.isEmpty() ? fichasTablero.get(fichasTablero.size() - 1) : null;
    }
}
