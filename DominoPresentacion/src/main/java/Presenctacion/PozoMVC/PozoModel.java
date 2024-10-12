/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Presenctacion.PozoMVC;

import Dominio.Ficha;
import Dominio.Jugador;
import Dominio.Pozo;
import Mediadores.PozoMediator;
import Negocio.ServicioPozo;
import Singleton.PozoSingleton;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author INEGI
 */
public class PozoModel {

   private Pozo pozo = PozoSingleton.getInstancia();
    private ServicioPozo servicioPozo;

    public PozoModel() {
        this.pozo.inicializarFichas();
        this.servicioPozo = new ServicioPozo();
    }

    public void iniciarNuevoJuego(List<Jugador> jugadores) {
        this.pozo.inicializarFichas();
        servicioPozo.iniciarNuevoJuego(jugadores);
        this.pozo = servicioPozo.getPozo();
    }

    public List<Ficha> getFichasPozo() {
        return servicioPozo.getPozo().getFichasPozo();
    }

    public void setFichasPozo(List<Ficha> fichas) {
        this.pozo.setFichasPozo(fichas);
    }

    public Pozo getPozo() {
        return this.pozo;
    }
}
