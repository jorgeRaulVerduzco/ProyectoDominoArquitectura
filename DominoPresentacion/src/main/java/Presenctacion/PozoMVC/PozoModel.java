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
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author INEGI
 */
public class PozoModel {

 
    private PozoMediator pozoMediador;
    private Pozo pozo; // Agregamos una instancia de Pozo para manejar las fichas
    private ServicioPozo servicioPozo; // Instancia del servicio para gestionar el pozo

    public PozoModel() {
        this.pozo = new Pozo(); // Inicializa el pozo
        this.servicioPozo = new ServicioPozo(); // Inicializa el servicio
        
        this.servicioPozo.guardarFichasPozo();
    }

    public PozoModel(PozoMediator pozoMediador) {
        this.pozoMediador = pozoMediador;
        this.pozo = new Pozo(); // Inicializa el pozo
        this.servicioPozo = new ServicioPozo(); // Inicializa el servicio
    }

    // Método para iniciar un nuevo juego y repartir las fichas a los jugadores
    public void iniciarNuevoJuego(List<Jugador> jugadores) {
        servicioPozo.iniciarNuevoJuego(jugadores);
        this.pozo = servicioPozo.getPozo(); // Actualiza la instancia de pozo con las fichas generadas
    }

    // Método para guardar las fichas en el pozo usando el mediador
    public void guardarFichasPozo() {
        servicioPozo.guardarFichasPozo(); // Guarda las fichas en el pozo
    }

    public List<Ficha> getFichasPozo() {
        return servicioPozo.getPozo().getFichasPozo(); // Obtiene las fichas del pozo
    }
}