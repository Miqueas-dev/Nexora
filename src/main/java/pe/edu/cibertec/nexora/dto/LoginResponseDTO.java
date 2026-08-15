package pe.edu.cibertec.nexora.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginResponseDTO {

    private Integer idUsuario;
    private String nombre;
    private String correo;
    private String rol;

    public LoginResponseDTO(
            Integer idUsuario,
            String nombre,
            String correo,
            String rol) {

        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.correo = correo;
        this.rol = rol;
    }
}