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

    /**
     * Constructor de la clase ServicioPozo. Inicializa el pozo utilizando la
     * instancia única de PozoSingleton. porque el pozo es lo primero que se
     * crea ya que de aqui se reparten las fichas
     */
    public ServicioPozo() {
        pozo = PozoSingleton.getInstancia(); // es  Singleton
    }

    /**
     * Inicia un nuevo juego repartiendo fichas a los jugadores. Guarda las
     * fichas generadas en el pozo y reparte 7 fichas aleatorias a cada jugador.
     *
     * @param jugadores Lista de jugadores que recibirán sus fichas al inicio
     * del juego.
     */
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

    /**
     * Genera las fichas del juego y las guarda en el pozo. Las fichas generadas
     * representan todas las combinaciones posibles de números del 0 al 6.
     */
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

    /**
     * Toma una ficha del pozo en un índice específico. La ficha se elimina del
     * pozo y se devuelve.
     *
     * @param pozo El pozo de fichas de donde se desea tomar la ficha.
     * @param indice El índice de la ficha que se desea tomar.
     * @return La ficha seleccionada, o null si el pozo está vacío.
     */
    public Ficha tomarFichaDelPozo(Pozo pozo, int indice) {
        if (pozo.getFichasPozo().isEmpty()) {
            return null;
        }
        return pozo.getFichasPozo().remove(indice);
    }

    /**
     * Elimina una ficha específica del pozo. Busca la ficha en el pozo y la
     * elimina si está presente.
     *
     * @param ficha La ficha que se desea eliminar del pozo.
     * @return true si la ficha fue eliminada exitosamente, false si no se
     * encontró.
     */
    public boolean eliminarFichaDelPozo(Ficha ficha) {
        return pozo.getFichasPozo().remove(ficha); // Elimina la ficha del pozo y devuelve true si se eliminó
    }

    /**
     * Devuelve la instancia del pozo actual. Permite acceder al pozo para
     * consultar su estado o modificarlo.
     *
     * @return El pozo de fichas actual.
     */
    public Pozo getPozo() {
        return pozo;
    }
}
