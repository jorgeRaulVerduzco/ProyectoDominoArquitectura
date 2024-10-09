/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Mediadores;

import Dominio.Ficha;
import Negocio.ServicioPozo;
import Presenctacion.PozoMVC.PozoModel;

/**
 *
 * @author INEGI
 */
public class PozoMediator {

    private ServicioPozo servicioPozo;
    private PozoModel pozoModel;

    public PozoMediator(ServicioPozo servicioPozo, PozoModel pozoModel) {
        this.servicioPozo = servicioPozo;
        this.pozoModel = pozoModel;
    }

    public void guardarFichas() {
        servicioPozo.guardarFichasPozo();
    }

}
