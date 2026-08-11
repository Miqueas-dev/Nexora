package pe.edu.cibertec.nexora.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.cibertec.nexora.entity.Estado;

public interface EstadoRepository extends JpaRepository<Estado, Integer> {
	
	/* BUSCA ESTADOS CUYA DESCRIPCIÓN CONTENGA EL TEXTO ENVIADO */
    List<Estado> findByDescripcionContainingIgnoreCase(String descripcion);
}
