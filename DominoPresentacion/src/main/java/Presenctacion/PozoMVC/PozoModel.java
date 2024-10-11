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

    private PozoMediator pozoMediador;
    private Pozo pozo; // Instancia de Pozo para manejar las fichas
    private ServicioPozo servicioPozo; // Instancia del servicio para gestionar el pozo

  public PozoModel() {
    this.pozo = PozoSingleton.getInstancia(); // Usar la instancia Singleton
    this.pozo.inicializarFichas(); // Inicializa las fichas en el pozo
    this.servicioPozo = new ServicioPozo(); // Inicializa el servicio
}

 public PozoModel(PozoMediator pozoMediador) {
    this.pozoMediador = pozoMediador;
    this.pozo = PozoSingleton.getInstancia(); // Usar la instancia Singleton
    this.pozo.inicializarFichas(); // Asegúrate de inicializar las fichas
    this.servicioPozo = new ServicioPozo(); // Inicializa el servicio
}

    public void iniciarNuevoJuego(List<Jugador> jugadores) {
        this.pozo.inicializarFichas(); // Asegúrate de inicializar las fichas al iniciar un nuevo juego
        servicioPozo.iniciarNuevoJuego(jugadores);
        this.pozo = servicioPozo.getPozo(); // Actualiza la instancia de pozo con las fichas generadas
    }

    public void guardarFichasPozo() {
        servicioPozo.guardarFichasPozo(); // Guarda las fichas en el pozo
    }

    public List<Ficha> getFichasPozo() {
        return servicioPozo.getPozo().getFichasPozo(); // Obtiene las fichas del pozo
    }

    public void setFichasPozo(List<Ficha> fichas) {
        this.pozo.setFichasPozo(fichas); // Llama al método set de la clase Pozo
    }

    public Pozo getPozo() {
        return this.pozo; // Devuelve la instancia de Pozo
    }
}
