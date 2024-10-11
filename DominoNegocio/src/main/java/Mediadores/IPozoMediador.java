/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Mediadores;

import Dominio.Ficha;
import Dominio.Jugador;
import java.util.List;

/**
 *
 * @author INEGI
 */
public interface IPozoMediador {

    void iniciarNuevoJuego(List<Jugador> jugadores);

    void guardarFichasPozo();

    List<Ficha> getFichasPozo();
}
