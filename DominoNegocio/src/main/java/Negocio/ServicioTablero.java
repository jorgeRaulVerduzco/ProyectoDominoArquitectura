/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;

import Dominio.Ficha;
import Dominio.Jugador;
import Dominio.Pozo;
import Dominio.Tablero;
import PresentacionTableroMVC.TableroModel;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author INEGI
 */
public class ServicioTablero {

    public ServicioTablero() {
    }

    public void repartirFichas(Pozo pozo, List<Jugador> jugadores) {
        // Obtén las fichas del pozo generadas previamente
        List<Ficha> fichasDelPozo = pozo.getFichasPozo();

        // Fijamos a 7 la cantidad de fichas a repartir por jugador
        int fichasPorJugador = 7;

        // Repartimos fichas a cada jugador
        for (Jugador jugador : jugadores) {
            List<Ficha> fichasJugador = new ArrayList<>();

            // Reparte 7 fichas a cada jugador
            for (int i = 0; i < fichasPorJugador; i++) {
                if (!fichasDelPozo.isEmpty()) {
                    // Remover una ficha del pozo y agregarla a la mano del jugador
                    Ficha fichaRepartida = fichasDelPozo.remove(0);
                    fichasJugador.add(fichaRepartida);
                } else {
                    System.out.println("No hay más fichas en el pozo para repartir.");
                    break;
                }
            }

            // Asignamos las fichas repartidas al jugador
            jugador.setFichasJugador(fichasJugador);
        }

        // Actualizamos el pozo con las fichas restantes
        pozo.setFichasPozo(fichasDelPozo);

        System.out.println("Se han repartido 7 fichas a cada jugador.");
    }

    // Método para agregar fichas al tablero en el lado izquierdo o derecho
    public void agregarFichaAlTablero(Tablero tablero, Ficha ficha, String lado) {
        if (tablero.getFichasTablero().isEmpty()) {
            // Agregar la primera ficha directamente sin comprobar el lado
            tablero.getFichasTablero().add(ficha);
        } else if (lado.equals("izquierdo")) {
            if (!puedeAgregarAlIzquierdo(tablero, ficha)) {
                throw new IllegalArgumentException("No se puede agregar la ficha al lado izquierdo.");
            }
            tablero.getFichasTablero().add(0, ficha);
        } else if (lado.equals("derecho")) {
            if (!puedeAgregarAlDerecho(tablero, ficha)) {
                throw new IllegalArgumentException("No se puede agregar la ficha al lado derecho.");
            }
            tablero.getFichasTablero().add(ficha);
        } else {
            throw new IllegalArgumentException("Lado inválido. Debe ser 'izquierdo' o 'derecho'.");
        }
    }
    // Método para mover una ficha de una posición a otra

    public void moverFicha(Tablero tablero, int indexOrigen, int indexDestino) {
        if (indexOrigen < 0 || indexOrigen >= tablero.getFichasTablero().size()
                || indexDestino < 0 || indexDestino >= tablero.getFichasTablero().size()) {
            throw new IndexOutOfBoundsException("Índice fuera de los límites del tablero.");
        }

        Ficha fichaAEncontrar = tablero.getFichasTablero().get(indexOrigen);
        tablero.getFichasTablero().remove(indexOrigen);
        tablero.getFichasTablero().add(indexDestino, fichaAEncontrar);
    }

    // Método para obtener el extremo izquierdo del tablero
    public Ficha obtenerExtremoIzquierdo(Tablero tablero) {
        return !tablero.getFichasTablero().isEmpty() ? tablero.getFichasTablero().get(0) : null;
    }

    // Método para obtener el extremo derecho del tablero
    public Ficha obtenerExtremoDerecho(Tablero tablero) {
        return !tablero.getFichasTablero().isEmpty() ? tablero.getFichasTablero().get(tablero.getFichasTablero().size() - 1) : null;
    }

    // Verificación para agregar ficha al lado izquierdo
    private boolean puedeAgregarAlIzquierdo(Tablero tablero, Ficha ficha) {
        return !tablero.getFichasTablero().isEmpty()
                && (ficha.getEspacio2() == tablero.getFichasTablero().get(0).getEspacio1());
    }

    private boolean puedeAgregarAlDerecho(Tablero tablero, Ficha ficha) {
        if (tablero.getFichasTablero().isEmpty()) {
            System.out.println("El tablero está vacío. Agregando la primera ficha.");
            return true;
        }
        Ficha fichaDerecha = obtenerExtremoDerecho(tablero);
        System.out.println("Ficha a agregar: " + ficha);
        System.out.println("Ficha en el extremo derecho: " + fichaDerecha);
        boolean resultado = (ficha.getEspacio1() == fichaDerecha.getEspacio2() || ficha.getEspacio2() == fichaDerecha.getEspacio2());
        System.out.println("Puede agregar al derecho: " + resultado);
        return resultado;
    }

      public void agregarFichaAlTableroRenovado(Tablero tablero, Ficha ficha, String lado) {
        List<Ficha> fichasTablero = tablero.getFichasTablero();
        
        System.out.println("Intentando agregar ficha " + ficha + " al lado " + lado);
        System.out.println("Estado actual del tablero: " + fichasTablero);

        if (fichasTablero.isEmpty()) {
            fichasTablero.add(ficha);
            System.out.println("Tablero vacío, agregando primera ficha");
            return;
        }

        if (lado.equals("izquierdo")) {
            Ficha primeraFicha = fichasTablero.get(0);
            System.out.println("Comparando con la primera ficha del tablero: " + primeraFicha);
            if (ficha.getEspacio2() == primeraFicha.getEspacio1()) {
                fichasTablero.add(0, ficha);
                System.out.println("Ficha agregada al lado izquierdo");
            } else if (ficha.getEspacio1() == primeraFicha.getEspacio1()) {
                fichasTablero.add(0, new Ficha(ficha.getEspacio2(), ficha.getEspacio1()));
                System.out.println("Ficha invertida y agregada al lado izquierdo");
            } else {
                System.out.println("La ficha no coincide con el extremo izquierdo del tablero");
                throw new IllegalArgumentException("La ficha no coincide con el extremo izquierdo del tablero.");
            }
        } else if (lado.equals("derecho")) {
            Ficha ultimaFicha = fichasTablero.get(fichasTablero.size() - 1);
            System.out.println("Comparando con la última ficha del tablero: " + ultimaFicha);
            if (ficha.getEspacio1() == ultimaFicha.getEspacio2()) {
                fichasTablero.add(ficha);
                System.out.println("Ficha agregada al lado derecho");
            } else if (ficha.getEspacio2() == ultimaFicha.getEspacio2()) {
                fichasTablero.add(new Ficha(ficha.getEspacio2(), ficha.getEspacio1()));
                System.out.println("Ficha invertida y agregada al lado derecho");
            } else {
                System.out.println("La ficha no coincide con el extremo derecho del tablero");
                throw new IllegalArgumentException("La ficha no coincide con el extremo derecho del tablero.");
            }
        } else {
            throw new IllegalArgumentException("Lado inválido. Debe ser 'izquierdo' o 'derecho'.");
        }
        
        System.out.println("Estado final del tablero: " + fichasTablero);
    }


    private boolean puedeAgregarAlIzquierdoRenovado(Tablero tablero, Ficha ficha) {
        if (tablero.getFichasTablero().isEmpty()) {
            return true;
        }
        Ficha primeraFicha = tablero.getFichasTablero().get(0);
        return ficha.getEspacio1() == primeraFicha.getEspacio1() || ficha.getEspacio2() == primeraFicha.getEspacio1();
    }

    private boolean puedeAgregarAlDerechoRenovado(Tablero tablero, Ficha ficha) {
        if (tablero.getFichasTablero().isEmpty()) {
            return true;
        }
        Ficha ultimaFicha = tablero.getFichasTablero().get(tablero.getFichasTablero().size() - 1);
        return ficha.getEspacio1() == ultimaFicha.getEspacio2() || ficha.getEspacio2() == ultimaFicha.getEspacio2();
    }
}
