package pe.edu.cibertec.nexora.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.edu.cibertec.nexora.entity.Producto;
import pe.edu.cibertec.nexora.repository.ProductoRepository;
import pe.edu.cibertec.nexora.service.ProductoService;

@Service
public class ProductoServiceImpl implements ProductoService{
	
	//Inyecta el repositorio para acceder a la tabla producto
	@Autowired
	private ProductoRepository productoRepository;

	//Retorna todos los productos registrados
	@Override
	public List<Producto> listar() {
		return productoRepository.findAll();
	}

	// Registra un producto nuevo o actualiza uno existente
	@Override
	public Producto guardar(Producto producto) {
		return productoRepository.save(producto);
	}

	//Buscar un producto por su ID
	@Override
	public Producto buscarPorId(Integer id) {
		return productoRepository.findById(id).orElse(null);
	}

	//Eliminar un producto por su ID
	@Override
	public void eliminar(Integer id) {
		
		productoRepository.deleteById(id);
	}

	// Busca productos cuya descripción contenga el texto enviado
	@Override
	public List<Producto> buscarPorDescripcion(String descripcion) {
		return productoRepository.findByDescripProductoContainingIgnoreCase(descripcion);
	}

	// Busca productos cuyo stock sea mayor al valor enviado
	@Override
	public List<Producto> buscarPorStockMayor(int stock) {
		return productoRepository.findByStockProductoGreaterThan(stock);
	}
	
	

}
