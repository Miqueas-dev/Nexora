package pe.edu.cibertec.nexora.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pe.edu.cibertec.nexora.entity.Producto;
import pe.edu.cibertec.nexora.service.ProductoService;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    // GET: lista todos los productos
    // http://localhost:8080/api/productos
    @GetMapping
    public ResponseEntity<List<Producto>> listar() {
        return ResponseEntity.ok(productoService.listar());
    }

    // GET: busca un producto por ID
    // http://localhost:8080/api/productos/1
    @GetMapping("/{id}")
    public ResponseEntity<Producto> buscarPorId(@PathVariable Integer id) {

        Producto producto = productoService.buscarPorId(id);

        if (producto == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(producto);
    }

    // POST: registra un producto nuevo
    // http://localhost:8080/api/productos
    @PostMapping
    public ResponseEntity<Producto> registrar(
            @RequestBody Producto producto) {

        Producto guardado = productoService.guardar(producto);

        return ResponseEntity.ok(guardado);
    }

    // PUT: actualiza un producto existente
    // http://localhost:8080/api/productos/1
    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizar(
            @PathVariable Integer id,
            @RequestBody Producto producto) {

        Producto existente = productoService.buscarPorId(id);

        if (existente == null) {
            return ResponseEntity.notFound().build();
        }

        producto.setIdProducto(id);

        Producto actualizado = productoService.guardar(producto);

        return ResponseEntity.ok(actualizado);
    }

    // DELETE: elimina un producto
    // http://localhost:8080/api/productos/1
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {

        Producto producto = productoService.buscarPorId(id);

        if (producto == null) {
            return ResponseEntity.notFound().build();
        }

        productoService.eliminar(id);

        return ResponseEntity.noContent().build();
    }

    // GET: busca productos por descripción
    // /api/productos/buscar/descripcion?descripcion=laptop
    @GetMapping("/buscar/descripcion")
    public ResponseEntity<List<Producto>> buscarPorDescripcion(
            @RequestParam String descripcion) {

        return ResponseEntity.ok(
                productoService.buscarPorDescripcion(descripcion)
        );
    }

    // GET: busca productos cuyo stock sea mayor al indicado
    // /api/productos/buscar/stock?stock=5
    @GetMapping("/buscar/stock")
    public ResponseEntity<List<Producto>> buscarPorStock(
            @RequestParam int stock) {

        return ResponseEntity.ok(
                productoService.buscarPorStockMayor(stock)
        );
    }
}