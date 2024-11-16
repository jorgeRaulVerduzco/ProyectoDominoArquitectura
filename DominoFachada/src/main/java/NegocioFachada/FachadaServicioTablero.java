/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package NegocioFachada;

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
     * @param pozoDTO El pozo de fichas en formato DTO
     * @param jugadoresDTO Lista de jugadores en formato DTO
     */
    public void repartirFichas(PozoDTO pozoDTO, List<JugadorDTO> jugadoresDTO) {
        Pozo pozo = conversor.convertirPozo(pozoDTO);
        List<Jugador> jugadores = conversor.convertirListaJugadores(jugadoresDTO);
        servicioTablero.repartirFichas(pozo, jugadores);
    }
    
    /**
     * Agrega una ficha al tablero en el lado especificado utilizando DTOs.
     *
     * @param tableroDTO El tablero en formato DTO
     * @param fichaDTO La ficha a agregar en formato DTO
     * @param lado El lado del tablero ("izquierdo" o "derecho")
     * @throws IllegalArgumentException Si no se puede agregar la ficha en el lado especificado
     */
    public void agregarFichaAlTablero(TableroDTO tableroDTO, FichaDTO fichaDTO, String lado) {
        Tablero tablero = conversor.convertirTablero(tableroDTO);
        Ficha ficha = conversor.convertirFicha(fichaDTO);
        servicioTablero.agregarFichaAlTablero(tablero, ficha, lado);
    }
    
    /**
     * Mueve una ficha de una posición a otra dentro del tablero utilizando DTO.
     *
     * @param tableroDTO El tablero en formato DTO
     * @param indexOrigen Índice de la posición de origen
     * @param indexDestino Índice de la posición de destino
     * @throws IndexOutOfBoundsException Si algún índice está fuera de los límites
     */
    public void moverFicha(TableroDTO tableroDTO, int indexOrigen, int indexDestino) {
        Tablero tablero = conversor.convertirTablero(tableroDTO);
        servicioTablero.moverFicha(tablero, indexOrigen, indexDestino);
    }
    
    /**
     * Obtiene la ficha del extremo izquierdo del tablero y la retorna como DTO.
     *
     * @param tableroDTO El tablero en formato DTO
     * @return La ficha del extremo izquierdo en formato DTO, o null si el tablero está vacío
     */
    public FichaDTO obtenerExtremoIzquierdo(TableroDTO tableroDTO) {
        Tablero tablero = conversor.convertirTablero(tableroDTO);
        Ficha ficha = servicioTablero.obtenerExtremoIzquierdo(tablero);
        // Aquí necesitaríamos un método para convertir de Ficha a FichaDTO
        return convertirFichaADTO(ficha);
    }
    
    /**
     * Obtiene la ficha del extremo derecho del tablero y la retorna como DTO.
     *
     * @param tableroDTO El tablero en formato DTO
     * @return La ficha del extremo derecho en formato DTO, o null si el tablero está vacío
     */
    public FichaDTO obtenerExtremoDerecho(TableroDTO tableroDTO) {
        Tablero tablero = conversor.convertirTablero(tableroDTO);
        Ficha ficha = servicioTablero.obtenerExtremoDerecho(tablero);
        // Aquí necesitaríamos un método para convertir de Ficha a FichaDTO
        return convertirFichaADTO(ficha);
    }
    
    /**
     * Agrega una ficha al tablero renovado utilizando DTOs.
     *
     * @param tableroDTO El tablero en formato DTO
     * @param fichaDTO La ficha a agregar en formato DTO
     * @param lado El lado del tablero ("izquierdo" o "derecho")
     * @throws IllegalArgumentException Si no se puede agregar la ficha en el lado especificado
     */
    public void agregarFichaAlTableroRenovado(TableroDTO tableroDTO, FichaDTO fichaDTO, String lado) {
        Tablero tablero = conversor.convertirTablero(tableroDTO);
        Ficha ficha = conversor.convertirFicha(fichaDTO);
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
