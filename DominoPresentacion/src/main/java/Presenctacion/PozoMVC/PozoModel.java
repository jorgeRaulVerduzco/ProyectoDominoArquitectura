/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Presenctacion.PozoMVC;

import Dominio.Ficha;
import Dominio.Pozo;
import Negocio.ServicioPozo;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author INEGI
 */
public class PozoModel {

    ServicioPozo servicioPozo;

    public PozoModel() {
    }

    public PozoModel(ServicioPozo servicioPozo) {
        this.servicioPozo = servicioPozo;
    }

    public void guardarFichasPozo() {
        servicioPozo.guardarFichasPozo();
    }

}
