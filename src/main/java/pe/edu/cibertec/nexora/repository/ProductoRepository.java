package pe.edu.cibertec.nexora.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pe.edu.cibertec.nexora.entity.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Integer>{
	
	 // Busca productos cuya descripción contenga un texto.
    // IgnoreCase permite buscar sin importar mayúsculas o minúsculas.
    List<Producto> findByDescripProductoContainingIgnoreCase(
            String descripcion);

    // Busca productos con stock mayor al valor enviado.
    List<Producto> findByStockProductoGreaterThan(
            int stock);

}
