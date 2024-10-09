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

    public AvatarDTO() {
    }

    public AvatarDTO(String urlAvatar) {
        this.urlAvatar = urlAvatar;
    }

    public String getUrlAvatar() {
        return urlAvatar;
    }

    public void setUrlAvatar(String urlAvatar) {
        this.urlAvatar = urlAvatar;
    }

    @Override
    public String toString() {
        return "Avatar{" + "urlAvatar=" + urlAvatar + '}';
    }

}
