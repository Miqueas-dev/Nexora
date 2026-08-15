package pe.edu.cibertec.nexora.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pe.edu.cibertec.nexora.entity.Comprobante;

public interface ComprobanteRepository
        extends JpaRepository<Comprobante, Integer> {
}
