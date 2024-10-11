/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Presenctacion.PozoMVC;

import Dominio.Ficha;
import Dominio.Jugador;
import Negocio.ServicioPozo;
import java.util.List;

/**
 *
 * @author INEGI
 */
public class PozoController {

     private PozoModel pozoModel;
    private PozoView pozoView;
    private ServicioPozo servicioPozo; // Referencia al servicio del pozo

    public PozoController(PozoModel pozoModel, PozoView pozoView, ServicioPozo servicioPozo) {
        this.pozoModel = pozoModel;
        this.pozoView = pozoView;
        this.servicioPozo = servicioPozo; // Inicializa el servicio del pozo

        // Asocia acciones en la vista con métodos en el controlador
        this.pozoView.setController(this);
        actualizarVista();
    }

    // Actualiza la vista con las fichas actuales del pozo
    public void actualizarVista() {
        pozoView.mostrarFichasEnPozo(); // Muestra las fichas en la vista
    }

    // Método para iniciar un nuevo juego
    public void iniciarNuevoJuego(List<Jugador> jugadores) {
        servicioPozo.iniciarNuevoJuego(jugadores); // Utiliza el servicio para iniciar un nuevo juego
        actualizarVista(); // Actualiza la vista después de iniciar el juego
    }

    // Toma una ficha del pozo y actualiza la vista
    public void tomarFichaDelPozo(int indice) {
        Ficha fichaTomada = servicioPozo.tomarFichaDelPozo(servicioPozo.getPozo(), indice);
        if (fichaTomada != null) {
            // Aquí puedes agregar la lógica para asignar la ficha al jugador actual
            // Ejemplo: jugadorActual.agregarFicha(fichaTomada);
            pozoView.mostrarFichasEnPozo(); // Actualiza la vista después de tomar la ficha
        } else {
            pozoView.mostrarMensaje("No se puede tomar ficha, el pozo está vacío."); // Muestra un mensaje si el pozo está vacío
        }
    }

    public void eliminarFicha(Ficha ficha) {
        if (servicioPozo.eliminarFichaDelPozo(ficha)) {
            pozoView.mostrarFichasEnPozo(); // Actualiza la vista después de eliminar la ficha
        } else {
            pozoView.mostrarMensaje("No se puede eliminar la ficha, no está en el pozo."); // Muestra un mensaje si la ficha no está en el pozo
        }
    }
}
