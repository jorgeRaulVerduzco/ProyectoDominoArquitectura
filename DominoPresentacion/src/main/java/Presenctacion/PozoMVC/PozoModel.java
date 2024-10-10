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
    private PozoMediator pozoMediador;
    private Pozo pozo; // Agregamos una instancia de Pozo para manejar las fichas

    public PozoModel() {
        this.pozo = new Pozo(); // Inicializa el pozo
    }

    public PozoModel(PozoMediator pozoMediador) {
        this.pozoMediador = pozoMediador;
        this.pozo = new Pozo(); // Inicializa el pozo
    }

    public void guardarFichasPozo() {
        pozoMediador.guardarFichas();
    }

    public List<Ficha> getFichasPozo() {
        return pozo.getFichasPozo();
    }
}
