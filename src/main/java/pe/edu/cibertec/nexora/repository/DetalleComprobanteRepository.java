package pe.edu.cibertec.nexora.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pe.edu.cibertec.nexora.entity.DetalleComprobante;

public interface DetalleComprobanteRepository
        extends JpaRepository<DetalleComprobante, Integer> {

    List<DetalleComprobante> findByComprobanteNumComprobante(
            Integer numComprobante);
}
