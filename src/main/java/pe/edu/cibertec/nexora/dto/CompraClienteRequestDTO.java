package pe.edu.cibertec.nexora.dto;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CompraClienteRequestDTO {

    private List<DetalleVentaRequestDTO> detalles;
}