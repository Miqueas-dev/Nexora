package pe.edu.cibertec.nexora.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import pe.edu.cibertec.nexora.entity.Tipo;

public interface TipoRepository
        extends JpaRepository<Tipo, Integer> {

    /* BUSQUEDA PARCIAL */
    List<Tipo> findByDescripcionContainingIgnoreCase(
            String descripcion);

    /* BUSQUEDA EXACTA PARA REGLAS DE NEGOCIO */
    Optional<Tipo> findByDescripcionIgnoreCase(
            String descripcion);
}