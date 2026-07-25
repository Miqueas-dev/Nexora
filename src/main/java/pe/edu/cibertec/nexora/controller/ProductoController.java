package pe.edu.cibertec.nexora.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import pe.edu.cibertec.nexora.entity.Producto;
import pe.edu.cibertec.nexora.service.ProductoService;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    // GET: Lista todos los productos
    @GetMapping("/listar")
    public List<Producto> listar() {
        return productoService.listar();
    }

    // POST: Registra un nuevo producto
    @PostMapping("/registrar")
    public Producto registrar(@RequestBody Producto producto) {
        return productoService.guardar(producto);
    }

    // GET: Busca un producto por ID
    @GetMapping("/buscar/{id}")
    public Producto buscarPorId(@PathVariable Integer id) {
        return productoService.buscarPorId(id);
    }

    // PUT: Actualiza un producto existente
    @PutMapping("/actualizar")
    public Producto actualizar(@RequestBody Producto producto) {
        return productoService.guardar(producto);
    }

    // DELETE: Elimina un producto por ID
    @DeleteMapping("/eliminar/{id}")
    public void eliminar(@PathVariable Integer id) {
        productoService.eliminar(id);
    }

    // GET: Busca por descripción
    @GetMapping("/buscar/descripcion")
    public List<Producto> buscarPorDescripcion(
            @RequestParam String descripcion) {

        return productoService.buscarPorDescripcion(descripcion);
    }

    // GET: Busca productos con stock mayor al indicado
    @GetMapping("/buscar/stock")
    public List<Producto> buscarPorStock(
            @RequestParam int stock) {

        return productoService.buscarPorStockMayor(stock);
    }
}