/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FachadaProvicional;

import DTOaEntidad.ConversorDTOaEntidad;
import DTOs.FichaDTO;
import DTOs.JugadorDTO;
import DTOs.PozoDTO;
import DTOs.TableroDTO;
import Dominio.Ficha;
import Dominio.Jugador;
import Dominio.Pozo;
import Dominio.Tablero;
import Negocio.ServicioTablero;
import java.util.List;

/**
 *
 * @author INEGI
 */
public class FachadaServicioTablero {
     private final ServicioTablero servicioTablero;
    private final ConversorDTOaEntidad conversor;
    
    public FachadaServicioTablero() {
        this.servicioTablero = new ServicioTablero();
        this.conversor = new ConversorDTOaEntidad();
    }
    
    /**
     * Reparte fichas del pozo a una lista de jugadores utilizando DTOs.
     *
     * @param pozo
     * @param jugadores
     */
    public void repartirFichas(Pozo pozo, List<Jugador> jugadores) {
        
        servicioTablero.repartirFichas(pozo, jugadores);
    }
    
    /**
     * Agrega una ficha al tablero en el lado especificado utilizando DTOs.
     *
     * @param tablero
     * @param ficha
     * @param lado El lado del tablero ("izquierdo" o "derecho")
     * @throws IllegalArgumentException Si no se puede agregar la ficha en el lado especificado
     */
    public void agregarFichaAlTablero(Tablero tablero, Ficha ficha, String lado) {
 
        servicioTablero.agregarFichaAlTablero(tablero, ficha, lado);
    }
    
    /**
     * Mueve una ficha de una posición a otra dentro del tablero utilizando DTO.
     *
     * @param tablero
     * @param indexOrigen Índice de la posición de origen
     * @param indexDestino Índice de la posición de destino
     * @throws IndexOutOfBoundsException Si algún índice está fuera de los límites
     */
    public void moverFicha(Tablero tablero, int indexOrigen, int indexDestino) {
        servicioTablero.moverFicha(tablero, indexOrigen, indexDestino);
    }
    
    /**
     * Obtiene la ficha del extremo izquierdo del tablero y la retorna como DTO.
     *
     * @param tablero
     * @return La ficha del extremo izquierdo en formato DTO, o null si el tablero está vacío
     */
    public Ficha obtenerExtremoIzquierdo(Tablero tablero) {
        
        return servicioTablero.obtenerExtremoIzquierdo(tablero);
       
    }
    
    /**
     * Obtiene la ficha del extremo derecho del tablero y la retorna como DTO.
     *
     * @param tablero
     * @return La ficha del extremo derecho en formato DTO, o null si el tablero está vacío
     */
    public Ficha obtenerExtremoDerecho(Tablero tablero) {
      return servicioTablero.obtenerExtremoDerecho(tablero);
        
    }
    
    /**
     * Agrega una ficha al tablero renovado utilizando DTOs.
     *
     * @param tablero
     * @param ficha
     * @param lado El lado del tablero ("izquierdo" o "derecho")
     * @throws IllegalArgumentException Si no se puede agregar la ficha en el lado especificado
     */
    public void agregarFichaAlTableroRenovado(Tablero tablero, Ficha ficha, String lado) {
    
        servicioTablero.agregarFichaAlTableroRenovado(tablero, ficha, lado);
    }
    
    /**
     * Método auxiliar para convertir una Ficha a FichaDTO
     */
    private FichaDTO convertirFichaADTO(Ficha ficha) {
        if (ficha == null) {
            return null;
        }
        FichaDTO fichaDTO = new FichaDTO();
        fichaDTO.setEspacio1(ficha.getEspacio1());
        fichaDTO.setEspacio2(ficha.getEspacio2());
        fichaDTO.setColocada(ficha.isColocada());
        fichaDTO.setOrientacion(ficha.getOrientacion());
        return fichaDTO;
    }
}
