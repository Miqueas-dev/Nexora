package pe.edu.cibertec.nexora.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pe.edu.cibertec.nexora.entity.Comprobante;

public interface ComprobanteRepository
        extends JpaRepository<Comprobante, Integer> {

    // Compras realizadas por un cliente
    List<Comprobante> findByUsuarioIdUsuario(
            Integer idUsuario);

    // Ventas realizadas por un vendedor
    List<Comprobante> findByVendedorIdUsuario(
            Integer idVendedor);
}