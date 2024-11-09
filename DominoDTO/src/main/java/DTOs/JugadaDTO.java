/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTOs;

/**
 *
 * @author INEGI
 */
public class JugadaDTO {

    private FichaDTO fichaDTO;
    private JugadorDTO jugadorDTO;
    String lado;

    public JugadaDTO() {
    }

    public JugadaDTO(FichaDTO fichaDTO, JugadorDTO jugadorDTO, String lado) {
        this.fichaDTO = fichaDTO;
        this.jugadorDTO = jugadorDTO;
        this.lado = lado;
    }

    public FichaDTO getFichaDTO() {
        return fichaDTO;
    }

    public void setFichaDTO(FichaDTO fichaDTO) {
        this.fichaDTO = fichaDTO;
    }

    public JugadorDTO getJugadorDTO() {
        return jugadorDTO;
    }

    public void setJugadorDTO(JugadorDTO jugadorDTO) {
        this.jugadorDTO = jugadorDTO;
    }

    public String getLado() {
        return lado;
    }

    public void setLado(String lado) {
        this.lado = lado;
    }

    @Override
    public String toString() {
        return "JugadaDTO{" + "fichaDTO=" + fichaDTO + ", jugadorDTO=" + jugadorDTO + ", lado=" + lado + '}';
    }
}
