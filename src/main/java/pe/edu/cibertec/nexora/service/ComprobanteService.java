package pe.edu.cibertec.nexora.service;

import java.util.List;

import pe.edu.cibertec.nexora.dto.ComprobanteResponseDTO;
import pe.edu.cibertec.nexora.dto.VentaRequestDTO;

public interface ComprobanteService {

    ComprobanteResponseDTO registrar(VentaRequestDTO venta);

    List<ComprobanteResponseDTO> listar();

    ComprobanteResponseDTO buscarPorId(Integer id);
}
