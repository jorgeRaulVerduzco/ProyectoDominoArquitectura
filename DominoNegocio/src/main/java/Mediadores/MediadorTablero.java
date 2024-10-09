/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Mediadores;

import Dominio.Ficha;
import Dominio.Tablero;
import Negocio.ServicioTablero;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author INEGI
 */
public class MediadorTablero {

    private List<Ficha> fichasTablero;
    private ServicioTablero servicioTablero;
    private Tablero tablero; // Este objeto Tablero representa el estado actual del tablero

    public MediadorTablero() {
        this.fichasTablero = new ArrayList<>();
        this.servicioTablero = new ServicioTablero();
        this.tablero = new Tablero(); // Inicializamos el tablero
        tablero.setFichasTablero(fichasTablero); // Sincronizamos las fichas del modelo con el tablero
    }

    public List<Ficha> getFichasTablero() {
        return fichasTablero;
    }

    public void agregarFicha(Ficha ficha, String lado) {
        servicioTablero.agregarFichaAlTablero(tablero, ficha, lado);
    }

    public void moverFicha(int indexOrigen, int indexDestino) {
        servicioTablero.moverFicha(tablero, indexOrigen, indexDestino);
    }

    public Ficha obtenerExtremoIzquierdo() {
        return servicioTablero.obtenerExtremoIzquierdo(tablero);
    }

    public Ficha obtenerExtremoDerecho() {
        return servicioTablero.obtenerExtremoDerecho(tablero);
    }

    public boolean puedeAgregarAlDerecho(Ficha ficha) {
        return !tablero.getFichasTablero().isEmpty()
                && (ficha.getEspacio1() == tablero.getFichasTablero().get(tablero.getFichasTablero().size() - 1).getEspacio2());
    }
}
