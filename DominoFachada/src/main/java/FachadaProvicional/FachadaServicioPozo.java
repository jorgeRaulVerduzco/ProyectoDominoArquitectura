/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package FachadaProvicional;

import DTOaEntidad.ConversorDTOaEntidad;
import DTOs.FichaDTO;
import DTOs.JugadorDTO;
import DTOs.PozoDTO;
import Dominio.Ficha;
import Dominio.Jugador;
import Dominio.Pozo;
import Negocio.ServicioPozo;
import java.util.List;

/**
 *
 * @author INEGI
 */
public class FachadaServicioPozo {

    private final ServicioPozo servicioPozo;
    private final ConversorDTOaEntidad conversor;

    public FachadaServicioPozo() {
        this.servicioPozo = new ServicioPozo();
        this.conversor = new ConversorDTOaEntidad();
    }

    /**
     * Inicia un nuevo juego repartiendo fichas a los jugadores. Convierte los
     * jugadores DTO a entidades y los procesa.
     *
     * @param jugadores
     */
    public void iniciarNuevoJuego(List<Jugador> jugadores) {
        servicioPozo.iniciarNuevoJuego(jugadores);
    }

    /**
     * Genera todas las fichas posibles y las guarda en el pozo.
     */
    public void guardarFichasPozo() {
        servicioPozo.guardarFichasPozo();
    }

    /**
     * Toma una ficha del pozo en un índice específico y la devuelve como DTO.
     *
     * @param indice Índice de la ficha que se desea tomar.
     * @return La ficha DTO seleccionada, o null si el pozo está vacío.
     */
    public Ficha tomarFichaDelPozo(int indice) {
       return servicioPozo.tomarFichaDelPozo(servicioPozo.getPozo(), indice);
            
    
    }

    /**
     * Elimina una ficha específica del pozo usando un DTO.
     *
     * @param ficha
     * @return true si la ficha fue eliminada exitosamente, false si no se
     * encontró.
     */
    public boolean eliminarFichaDelPozo(Ficha ficha) {
        return servicioPozo.eliminarFichaDelPozo(ficha);
    }

    /**
     * Devuelve el estado actual del pozo como un DTO.
     *
     * @return Un PozoDTO que representa el estado actual del pozo.
     */
    public Pozo obtenerEstadoPozo() {
       return servicioPozo.getPozo();
  
    }

    // Métodos de conversión de entidad a DTO
    private FichaDTO convertirEntidadADTO(Ficha ficha) {
        FichaDTO fichaDTO = new FichaDTO();
        fichaDTO.setEspacio1(ficha.getEspacio1());
        fichaDTO.setEspacio2(ficha.getEspacio2());
        fichaDTO.setColocada(ficha.isColocada());
        fichaDTO.setOrientacion(ficha.getOrientacion());
        return fichaDTO;
    }

    private PozoDTO convertirEntidadADTO(Pozo pozo) {
        PozoDTO pozoDTO = new PozoDTO();
        pozoDTO.setFichasPozo(
                pozo.getFichasPozo().stream()
                        .map(this::convertirEntidadADTO)
                        .toList()
        );
        return pozoDTO;
    }
}
