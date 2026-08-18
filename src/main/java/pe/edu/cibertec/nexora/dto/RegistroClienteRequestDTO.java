package pe.edu.cibertec.nexora.dto;

import java.sql.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegistroClienteRequestDTO {

    private String dniUsuario;

    private String nombreUsuario;

    private String apepatUsuario;

    private String apematUsuario;

    private String correoUsuario;

    private String claveUsuario;

    private Date fecnacUsuario;
}