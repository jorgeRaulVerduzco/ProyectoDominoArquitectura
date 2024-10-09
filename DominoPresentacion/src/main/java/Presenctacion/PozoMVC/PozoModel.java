/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Presenctacion.PozoMVC;

import Dominio.Ficha;
import Dominio.Pozo;
import Mediadores.PozoMediator;
import Negocio.ServicioPozo;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author INEGI
 */
public class PozoModel {

PozoMediator pozoMediador;
    public PozoModel() {
    }

    public PozoModel(PozoMediator pozoMediador) {
        this.pozoMediador = pozoMediador;
    }

    public void guardarFichasPozo() {
        pozoMediador.guardarFichas();
    }

}
