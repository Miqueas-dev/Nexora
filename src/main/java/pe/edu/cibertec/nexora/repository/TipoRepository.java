package pe.edu.cibertec.nexora.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.cibertec.nexora.entity.Tipo;

public interface TipoRepository extends JpaRepository<Tipo, Integer> {

    /* BUSCA TIPOS CUYA DESCRIPCIÓN CONTENGA EL TEXTO ENVIADO */
    List<Tipo> findByDescripcionContainingIgnoreCase(String descripcion);
}
