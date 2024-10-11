/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Mediadores;

import Dominio.Ficha;
import Dominio.Jugador;
import Negocio.ServicioPozo;
import Presenctacion.PozoMVC.PozoModel;
import java.util.List;

/**
 *
 * @author INEGI
 */
public class PozoMediator implements IPozoMediador{

    private PozoModel pozoModel;
    private ServicioPozo servicioPozo;

    public PozoMediator(PozoModel pozoModel, ServicioPozo servicioPozo) {
        this.pozoModel = pozoModel;
        this.servicioPozo = servicioPozo;
    }

    @Override
    public void iniciarNuevoJuego(List<Jugador> jugadores) {
        pozoModel.iniciarNuevoJuego(jugadores); // Delegar a PozoModel
    }

    @Override
    public void guardarFichasPozo() {
        servicioPozo.guardarFichasPozo(); // Delegar a ServicioPozo
    }

    @Override
    public List<Ficha> getFichasPozo() {
        return servicioPozo.getPozo().getFichasPozo(); // Delegar a ServicioPozo
    }

}
