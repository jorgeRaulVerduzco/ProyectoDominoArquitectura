/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;

import Dominio.Ficha;
import Dominio.Jugador;
import Dominio.Pozo;
import Singleton.PozoSingleton;
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
        pozo = PozoSingleton.getInstancia(); // Usar la instancia Singleton
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

        // Genera combinaciones de fichas únicas
        for (int i = 0; i <= 6; i++) {
            for (int j = i; j <= 6; j++) { // Asegura que (i, j) y (j, i) sean la misma ficha
                Ficha nuevaFicha = new Ficha(i, j);
                fichas.add(nuevaFicha);
            }
        }

        pozo.setFichasPozo(fichas); // Asigna las fichas generadas al pozo
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
