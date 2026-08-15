package pe.edu.cibertec.nexora.service;

import java.util.List;

import pe.edu.cibertec.nexora.dto.ComprobanteResponseDTO;
import pe.edu.cibertec.nexora.dto.VentaRequestDTO;

public interface ComprobanteService {

    // Vendedor registra venta
    ComprobanteResponseDTO registrar(
            VentaRequestDTO venta,
            String correoVendedor);

    // Administrador consulta todas
    List<ComprobanteResponseDTO> listar();

    ComprobanteResponseDTO buscarPorId(
            Integer id);

    // Cliente consulta sus compras
    List<ComprobanteResponseDTO>
            listarPorCliente(String correo);

    // Vendedor consulta sus ventas
    List<ComprobanteResponseDTO>
            listarPorVendedor(String correo);
}