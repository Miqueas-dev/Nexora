package pe.edu.cibertec.nexora.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;

import pe.edu.cibertec.nexora.entity.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Integer>{
	
	 // Busca productos cuya descripción contenga un texto.
    // IgnoreCase permite buscar sin importar mayúsculas o minúsculas.
    List<Producto> findByDescripProductoContainingIgnoreCase(
            String descripcion);

    // Busca productos con stock mayor al valor enviado.
    List<Producto> findByStockProductoGreaterThan(
            int stock);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Producto p WHERE p.idProducto = :idProducto")
    Optional<Producto> buscarPorIdParaVenta(
            @Param("idProducto") Integer idProducto);

}
