/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DTOs;

/**
 *
 * @author INEGI
 */
public class AvatarDTO {

    private String urlAvatar;

    /**
     * Constructor por defecto de la clase Avatar. Inicializa un avatar sin
     * ninguna URL definida.
     */
    public AvatarDTO() {
    }

    /**
     * Constructor sobrecargado de la clase Avatar. Permite crear un avatar con
     * una URL especificada.
     *
     * @param urlAvatar La URL de la imagen del avatar.
     */
    public AvatarDTO(String urlAvatar) {
        this.urlAvatar = urlAvatar;
    }

    /**
     * Método getter para obtener la URL del avatar.
     *
     * @return La URL de la imagen del avatar.
     */
    public String getUrlAvatar() {
        return urlAvatar;
    }

    /**
     * Método setter para modificar la URL del avatar.
     *
     * @param urlAvatar La nueva URL de la imagen del avatar.
     */
    public void setUrlAvatar(String urlAvatar) {
        this.urlAvatar = urlAvatar;
    }

    /**
     * Método sobreescrito para generar una representación en formato String del
     * avatar, que incluye la URL del mismo.
     *
     * @return Una cadena con el formato "Avatar{urlAvatar=URL}".
     */
    @Override
    public String toString() {
        return "Avatar{" + "urlAvatar=" + urlAvatar + '}';
    }

}
