/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package NegocioFachada;

import DTOaEntidad.ConversorDTOaEntidad;
import DTOs.FichaDTO;
import Dominio.Ficha;
import Negocio.ServicioFicha;

/**
 *
 * @author INEGI
 */
public class FachadaServicioFicha {
    private final ConversorDTOaEntidad conversor;
    private final ServicioFicha servicioFicha;

    public FachadaServicioFicha() {
        this.conversor = new ConversorDTOaEntidad();
        this.servicioFicha = new ServicioFicha();
    }

    /**
     * Verifica si una ficha DTO es una mula.
     *
     * @param fichaDTO la ficha DTO a verificar.
     * @return true si la ficha es una mula, false en caso contrario.
     */
    public boolean esMula(FichaDTO fichaDTO) {
  
        Ficha ficha = conversor.convertirFicha(fichaDTO);
        return servicioFicha.esMula(ficha);
    }

    /**
     * Suma los puntos de una ficha DTO.
     *
     * @param fichaDTO la ficha DTO cuyos puntos se desean sumar.
     * @return la suma de los puntos de la ficha, o 0 si el DTO es null.
     */
    public int sumarPuntosFicha(FichaDTO fichaDTO) {
      
        Ficha ficha = conversor.convertirFicha(fichaDTO);
        return servicioFicha.sumarPuntosFicha(ficha);
    }
}
