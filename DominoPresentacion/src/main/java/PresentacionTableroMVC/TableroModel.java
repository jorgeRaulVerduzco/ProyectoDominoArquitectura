/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package PresentacionTableroMVC;

import Negocio.ServicioTablero;
import Dominio.Ficha;
import java.util.ArrayList;
import java.util.List;
import Dominio.Tablero;

/**
 *
 * @author Serva
 */
public class TableroModel {
    private List<Ficha> fichasTablero;
    private List<Ficha> fichasPozo; // Nueva lista para las fichas del pozo
    private ServicioTablero servicioTablero;
    private Tablero tablero; // Este objeto Tablero representa el estado actual del tablero

    public TableroModel() {
        this.fichasTablero = new ArrayList<>();
        this.fichasPozo = new ArrayList<>(); // Inicializamos la lista de fichas del pozo
        this.servicioTablero = new ServicioTablero();
        this.tablero = new Tablero(); // Inicializamos el tablero
        tablero.setFichasTablero(fichasTablero); // Sincronizamos las fichas del modelo con el tablero
    }
  public void agregarFicha(Ficha ficha, String lado) {
        System.out.println("Intentando agregar ficha " + ficha + " al lado " + lado); // Agregar este mensaje para depuración
        servicioTablero.agregarFichaAlTableroRenovado(tablero, ficha, lado);
        System.out.println("Ficha agregada exitosamente"); // Agregar este mensaje para depuración
    }

    public List<Ficha> getFichasTablero() {
        return fichasTablero;
    }
    // Método para establecer las fichas del pozo
    public void setFichasPozo(List<Ficha> fichas) {
        this.fichasPozo = fichas;
    }

    // Método para obtener las fichas del pozo
    public List<Ficha> getFichasPozo() {
        return fichasPozo;
    }
    
    public Tablero getTablero() {
    return tablero;
}

public void setTablero(Tablero tablero) {
    this.tablero = tablero;
}

public Ficha obtenerExtremoIzquierdo() {
    return fichasTablero.isEmpty() ? null : fichasTablero.get(0);
}

public Ficha obtenerExtremoDerecho() {
    return fichasTablero.isEmpty() ? null : fichasTablero.get(fichasTablero.size() - 1);
}

public void setFichasTablero(List<Ficha> fichasTablero) {
    this.tablero.setFichasTablero(fichasTablero);
}

public void actualizarFichasTablero(List<Ficha> nuevasFichas) {
    this.tablero.setFichasTablero(nuevasFichas);
}


   

    public void moverFicha(int indexOrigen, int indexDestino) {
        servicioTablero.moverFicha(tablero, indexOrigen, indexDestino);
    }


    public boolean puedeAgregarAlDerecho(Ficha ficha) {
        return !tablero.getFichasTablero().isEmpty()
                && (ficha.getEspacio1() == tablero.getFichasTablero().get(tablero.getFichasTablero().size() - 1).getEspacio2());
    }
}