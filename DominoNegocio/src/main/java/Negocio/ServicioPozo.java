/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;

import Dominio.Ficha;
import Dominio.Jugador;
import Dominio.Pozo;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author INEGI
 */
public class ServicioPozo {

    private Pozo pozo;
    private Random random = new Random();

    public ServicioPozo() {
        pozo = new Pozo();
    }

    // Método que inicializa un nuevo juego
    public void iniciarNuevoJuego(List<Jugador> jugadores) {
        // Guardar las fichas en el pozo
        guardarFichasPozo();

        // Repartir 7 fichas a cada jugador
        for (Jugador jugador : jugadores) {
            List<Ficha> fichasJugador = new ArrayList<>();
            for (int i = 0; i < 7; i++) {
                int indiceFicha = random.nextInt(pozo.getFichasPozo().size());
                Ficha ficha = tomarFichaDelPozo(pozo, indiceFicha);
                if (ficha != null) {
                    fichasJugador.add(ficha);
                }
            }
            jugador.setFichasJugador(fichasJugador);
        }
    }

    public void guardarFichasPozo() {
        List<Ficha> fichas = new ArrayList<>();
        for (int i = 0; i < 28; i++) {
            int ladoIzquierdo = random.nextInt(7);
            int ladoDerecho = random.nextInt(7);

            Ficha nuevaFicha = new Ficha(ladoIzquierdo, ladoDerecho);
            fichas.add(nuevaFicha);  // Agregar ficha generada
        }
        pozo.setFichasPozo(fichas);  // Asigna las fichas generadas al pozo
    }

    public Ficha tomarFichaDelPozo(Pozo pozo, int indice) {
        if (pozo.getFichasPozo().isEmpty()) {
            return null;
        }
        return pozo.getFichasPozo().remove(indice);
    }

    public boolean eliminarFichaDelPozo(Ficha ficha) {
        return pozo.getFichasPozo().remove(ficha); // Elimina la ficha del pozo y devuelve true si se eliminó
    }

    // Método para obtener el pozo
    public Pozo getPozo() {
        return pozo;
    }
}
