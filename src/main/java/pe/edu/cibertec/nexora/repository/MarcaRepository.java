package pe.edu.cibertec.nexora.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pe.edu.cibertec.nexora.entity.Marca;
import java.util.List;


public interface MarcaRepository extends JpaRepository<Marca, Integer>{
	Marca findByMarcaDesc(String marcaDesc);
	
	List<Marca> findByMarcaDescContaining(String textoIngresado);
	
}
