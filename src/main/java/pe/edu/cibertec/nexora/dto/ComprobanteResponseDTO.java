package pe.edu.cibertec.nexora.dto;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pe.edu.cibertec.nexora.entity.Usuario;

@Getter
@Setter
@NoArgsConstructor
public class ComprobanteResponseDTO {

    private Integer numComprobante;
    private Date fechaComprobante;
    private Usuario usuario;
    private BigDecimal total;
    private List<DetalleComprobanteResponseDTO> detalles;
}
