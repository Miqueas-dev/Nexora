package pe.edu.cibertec.nexora.service;

import java.util.List;

import pe.edu.cibertec.nexora.entity.Producto;

public interface ProductoService {

    // Lista todos los productos
    List<Producto> listar();

    // Guarda un producto nuevo o actualiza uno existente
    Producto guardar(Producto producto);

    // Busca un producto por su código
    Producto buscarPorId(Integer id);

    // Elimina un producto por su código
    void eliminar(Integer id);

    // Busca productos por descripción
    List<Producto> buscarPorDescripcion(String descripcion);

    // Lista productos con stock mayor al valor indicado
    List<Producto> buscarPorStockMayor(int stock);

}