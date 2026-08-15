package pe.edu.cibertec.nexora.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DetalleComprobanteResponseDTO {

    private Integer idDetalle;
    private Integer idProducto;
    private String descripcionProducto;
    private int cantidad;
    private BigDecimal precioVenta;
    private BigDecimal subtotal;
}
