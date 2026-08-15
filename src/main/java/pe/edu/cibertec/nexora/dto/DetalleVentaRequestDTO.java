package pe.edu.cibertec.nexora.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DetalleVentaRequestDTO {

    private Integer idProducto;
    private int cantidad;
}
