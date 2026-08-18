package pe.edu.cibertec.nexora.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import pe.edu.cibertec.nexora.entity.Estado;

public interface EstadoRepository
        extends JpaRepository<Estado, Integer> {

    /* BUSQUEDA PARCIAL */
    List<Estado> findByDescripcionContainingIgnoreCase(
            String descripcion);

    /* BUSQUEDA EXACTA PARA REGLAS DE NEGOCIO */
    Optional<Estado> findByDescripcionIgnoreCase(
            String descripcion);
}